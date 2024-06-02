import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-accountant-profile',
  templateUrl: './accountant-profile.component.html',
  styleUrls: ['./accountant-profile.component.css'],
})
export class AccountantProfileComponent implements OnInit {
  accountant: any;
  constructor(
    private accountantService: AccountantServiceService,
    private authService: AuthServiceService, 
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.getAccountant();
  }

  getAccountant() {
    let email = this.authService.getEmail();
    this.accountantService.getAccountantData(email).subscribe({
      next: (data: any) => {
        this.accountant = data;
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
}
