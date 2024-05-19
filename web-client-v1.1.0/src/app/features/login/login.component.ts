import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { debounceTime } from 'rxjs';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';
import { passwordStrengthValidator } from 'src/app/shared/validators/password-strength-validator';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  passwordHidden: boolean = true;
  receivedUrl: any;
  signinForm!: FormGroup;
  passsField: any;
  passwordErrors = {
    required: false,
    remainingLength: false,
    lowercase: false,
    uppercase: false,
    number: false,
    symbol: false
  };
  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private darkModeService: DarkModeService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.createForm();
    this.receivedUrl =
      this.activatedRoute.snapshot.queryParams['returnUrl'] || '/';
    this.subscribeToPasswordChanges();
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
      pwd: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          passwordStrengthValidator,
        ],
      ],
    });
  }
  subscribeToPasswordChanges() {
    this.signinForm
      .get('pwd')
      ?.valueChanges.pipe(debounceTime(300))
      .subscribe(() => {
        this.updatePasswordMessage();
      });
  }

  
  updatePasswordMessage() {
    const passwordControl = this.signinForm.get('pwd');
    if (!passwordControl) return;

    const errors: ValidationErrors | null = passwordControl.errors;

    if (errors) {
      this.passwordErrors.required = errors.hasOwnProperty('required');
      this.passwordErrors.remainingLength = errors.hasOwnProperty('remainingLength');
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
        symbol: false
      };
    }
  }
  onSubmit() {
    console.log(this.signinForm.value);
    this.authService.login(this.signinForm.value).subscribe({
      next: (data: any) => {
        // console.log(data);
        this.authService.saveTokenUser(data.jwt, data.userRole);
        this.router.navigate([this.receivedUrl]);
        this.toastService.showToast('success', 'Login successful');
      },
      error: (err: HttpErrorResponse) => {
        // console.log(err);
        if (err.error != null) {
          let errorMessage = 'An error occurred: ';
          for (const key in err.error) {
              if (err.error.hasOwnProperty(key)) {
                  errorMessage += `${err.error[key]} `;
              }
          }
          this.toastService.showToast('error', errorMessage);
      } else {
          if (err.status === 403) {
            this.toastService.showToast('error', 'Your account is disabled..');
          }
        }
      },
    });
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }

  togglePasswordVisibility() {
    this.passwordHidden = !this.passwordHidden;
    const pwdInput = document.getElementById(
      'passwordInput'
    ) as HTMLInputElement;
    if (pwdInput) {
      pwdInput.type = this.passwordHidden ? 'password' : 'text';
    }
  }
}
