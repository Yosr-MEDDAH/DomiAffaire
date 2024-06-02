import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-accountant-change-password',
  templateUrl: './accountant-change-password.component.html',
  styleUrls: ['./accountant-change-password.component.css'],
})
export class AccountantChangePasswordComponent {
  passwordHidden: { [inputId: string]: boolean } = {
    oldPassword: false,
    newPassword: false,
    confirmPassword: false,
  };
  chnagePswdForm!: FormGroup;
  oldAccountant: any;
  accountantId: any;
  constructor(
    private clientService: AccountantServiceService,
    private authService: AuthServiceService,
    private fb: FormBuilder,
    public toastService: ToastService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.getAccountantProfile();
    this.chnagePswdForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }
  getAccountantProfile() {
    let email = this.authService.getEmail();
    this.clientService.getAccountantData(email).subscribe({
      next: (data: any) => {
        // console.log(data);
        this.accountantId = data.id;
        this.oldAccountant = data;
        // console.log(this.oldAccountant);
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
  changePswd() {
    if (this.chnagePswdForm.valid) {
      this.clientService
        .changeAccountantPassword(this.accountantId, this.chnagePswdForm.value)
        .subscribe({
          next: (data: any) => {
            // console.log(data);
            this.toastService.showToast(
              'success',
              'Password has been changed successfully'
            );
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
            }
          },
        });
    }
  }
  logout(): void {
    this.authService.logout().subscribe((success) => {
      if (success) {
        this.router.navigate(['/login']);
      }
    });
  }

  togglePasswordVisibility(inputId: string) {
    console.log(this.passwordHidden[inputId]);
    this.passwordHidden[inputId] = !this.passwordHidden[inputId];
    const input = document.getElementById(inputId) as HTMLInputElement;
    if (input) {
      input.type = this.passwordHidden[inputId] ? 'text' : 'password';
    }
  }
}
