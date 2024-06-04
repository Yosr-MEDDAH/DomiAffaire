import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent implements OnInit {
  passwordHidden: { [inputId: string]: boolean } = {
    oldPassword: false,
    newPassword: false,
    confirmPassword: false,
  };
  chnagePswdForm!: FormGroup;
  oldClient: any;
  clientId: any;
  constructor(
    private clientService: ClientService,
    private authService: AuthServiceService,
    private fb: FormBuilder,
    public toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.getClientProfile();
    this.chnagePswdForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }
  getClientProfile() {
    let email = this.authService.getEmail();
    this.clientService.getClientData(email).subscribe({
      next: (data: any) => {
        // console.log(data);
        this.clientId = data.id;
        this.oldClient = data;
        // console.log(this.oldClient);
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
        .changeClientPassword(this.clientId, this.chnagePswdForm.value)
        .subscribe({
          next: (data: any) => {
            console.log(data);
            this.toastService.showToast(
              'success',
              'Password has been changed successfully'
            );
          },
          error: (err: HttpErrorResponse) => {
            console.log(err);
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
  logout() {
    this.authService.logout();
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
