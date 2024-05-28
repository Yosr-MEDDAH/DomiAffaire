import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-reset-password-request',
  templateUrl: './reset-password-request.component.html',
  styleUrls: ['./reset-password-request.component.css'],
})
export class ResetPasswordRequestComponent implements OnInit {
  loading: boolean = false;
  enteredEmail: string = '';
  emailEntered: boolean = false;
  passwordResetRequest!: FormGroup;
  emailError: boolean = false;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthServiceService,
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.passwordResetRequest = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/),
        ],
      ],
    });
  }

  checkEmail() {
    this.loading = true;
    this.authService
      .requestResetPassword(this.passwordResetRequest.value)
      .subscribe({
        next: (data: any) => {
          this.authService.setUrlResetPassword(data.url);
          this.router.navigate(['/reset-password-success']);
        },
        error: (err: HttpErrorResponse) => {
          if (err.error != null) {
            let errorMessage = 'An error occurred: ';
            for (const key in err.error) {
              if (err.error.hasOwnProperty(key)) {
                errorMessage += `${err.error[key]} `;
              }
            }
            this.toastService.showToast('error', errorMessage);
            this.emailError=true
            this.loading = false; 
          }
        },
        complete: () => {
          this.loading = false; 
        }
      });
  }
}
