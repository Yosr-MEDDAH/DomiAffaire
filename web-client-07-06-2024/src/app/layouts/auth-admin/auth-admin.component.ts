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
  onSubmit(): void {
    console.log(this.signinForm.value);
    this.authService.login(this.signinForm.value).subscribe({
      next: (success: boolean) => {
        console.log(success)
        if (success) {
          this.router.navigate([this.receivedUrl]);
          this.toastService.showToast('success', 'Login successful');
        }
      },
      error: (err: any) => {
        let errorMessage = 'An error occurred: ';
        if (err.error) {
          for (const key in err.error) {
            if (err.error.hasOwnProperty(key)) {
              errorMessage += `${err.error[key]} `;
            }
          }
        } else {
          if (err.status === 403) {
            errorMessage = 'Your account is disabled.';
          } else {
            errorMessage = 'Login failed. Please try again.';
          }
        }
        this.toastService.showToast('error', errorMessage);
      }
    });
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
