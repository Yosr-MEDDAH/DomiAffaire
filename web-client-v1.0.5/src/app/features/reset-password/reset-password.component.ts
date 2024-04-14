import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent {
  // emailEntered: boolean = false;
  url: any;
  // enteredEmail: string = '';
  password: string = '';
  confirmPassword: string = '';
  passwordsMismatch: boolean = false;
  emailError: boolean = false;
  passwordResetRequest!: FormGroup;
  constructor(
    private darkModeService: DarkModeService,
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthServiceService
  ) {}
  ngOnInit(): void {
    this.passwordResetRequest = this.fb.group({
      newPassword: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
    });
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  onSubmit() {
    this.authService.resetPassword(this.passwordResetRequest.value).subscribe({
      next: (data: any) => {
        console.log(data);
        this.authService.deleteUrlResetPassword();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }

  // passwordMatch() {
  //   if (this.password === this.confirmPassword) {
  //     this.router.navigate(['/login']);
  //   } else {
  //     this.passwordsMismatch = true;
  //   }
  // }

  // isPasswordValid() {
  //   return this.password === this.confirmPassword;
  // }
}
