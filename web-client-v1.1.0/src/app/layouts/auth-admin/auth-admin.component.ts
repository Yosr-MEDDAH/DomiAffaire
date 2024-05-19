import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-auth-admin',
  templateUrl: './auth-admin.component.html',
  styleUrls: ['./auth-admin.component.css']
})
export class AuthAdminComponent {
  receivedUrl: any;
  signinForm!: FormGroup;
  passsField: any;
  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private darkModeService: DarkModeService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.createForm();
    this.receivedUrl =
      this.activatedRoute.snapshot.queryParams['returnUrl'] || '/admin';
  }
  createForm() {
    this.signinForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/),
        ],
      ],
      pwd: ['', [Validators.required, Validators.minLength(8)]],
    });
  }
  onSubmit() {
    console.log(this.signinForm.value);
    this.authService.login(this.signinForm.value).subscribe({
      next: (data: any) => {
        this.authService.saveTokenUser(data.jwt, data.userRole);
        this.router.navigate([this.receivedUrl]);
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
        }
      },
    });
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
