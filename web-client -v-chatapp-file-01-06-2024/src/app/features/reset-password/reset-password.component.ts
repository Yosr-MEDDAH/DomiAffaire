import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime } from 'rxjs';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';
import { passwordStrengthValidator } from 'src/app/shared/validators/password-strength-validator';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent {
  url: any;
  password: string = '';
  confirmPassword: string = '';
  passwordsMismatch: boolean = false;
  emailError: boolean = false;
  passwordResetRequest!: FormGroup;
  passwordErrors = {
    required: false,
    remainingLength: false,
    lowercase: false,
    uppercase: false,
    number: false,
    symbol: false,
  };
  constructor(
    private darkModeService: DarkModeService,
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.passwordResetRequest = this.fb.group({
      newPassword: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          passwordStrengthValidator,
        ],
      ],
      confirmPassword: ['', 
      [Validators.required]
    ],
    });
    this.subscribeToPasswordChanges()
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  onSubmit() {
    this.authService.resetPassword(this.passwordResetRequest.value).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', data.message);
        this.authService.deleteUrlResetPassword();
        this.router.navigate(['login']);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
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
  subscribeToPasswordChanges() {
    this.passwordResetRequest
      .get('newPassword')
      ?.valueChanges.pipe(debounceTime(300))
      .subscribe(() => {
        this.updatePasswordMessage();
      });
  }
  updatePasswordMessage() {
    const passwordControl = this.passwordResetRequest.get('newPassword');
    if (!passwordControl) return;

    const errors: ValidationErrors | null = passwordControl.errors;

    if (errors) {
      this.passwordErrors.required = errors.hasOwnProperty('required');
      this.passwordErrors.remainingLength =
        errors.hasOwnProperty('remainingLength');
      this.passwordErrors.lowercase = errors.hasOwnProperty('lowercase');
      this.passwordErrors.uppercase = errors.hasOwnProperty('uppercase');
      this.passwordErrors.number = errors.hasOwnProperty('number');
      this.passwordErrors.symbol = errors.hasOwnProperty('symbol');
    } else {
      this.passwordErrors = {
        required: false,
        remainingLength: false,
        lowercase: false,
        uppercase: false,
        number: false,
        symbol: false,
      };
    }
  }
}
