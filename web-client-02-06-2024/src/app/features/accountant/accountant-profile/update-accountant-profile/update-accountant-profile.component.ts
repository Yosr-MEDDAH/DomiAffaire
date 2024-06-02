import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-update-accountant-profile',
  templateUrl: './update-accountant-profile.component.html',
  styleUrls: ['./update-accountant-profile.component.css'],
})
export class UpdateAccountantProfileComponent {
  imageNotChanged: boolean = false;
  oldAccountant: any;
  profileForm!: FormGroup;
  private accountantId: any;
  selectedFile: File | undefined;
  accountant: any;
  constructor(
    private accountantService: AccountantServiceService,
    private authService: AuthServiceService,
    private formBuilder: FormBuilder,
    public toastService: ToastService,
    private router:Router
  ) {}
  ngOnInit(): void {
    this.getAccountantProfile();
  }
  initForm() {
    this.profileForm = this.formBuilder.group({
      firstName: [
        this.oldAccountant.firstName,
        [
          Validators.required,
          Validators.minLength(3),
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/),
        ],
      ],
      lastName: [
        this.oldAccountant.lastName,
        [
          Validators.required,
          Validators.minLength(3),
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/),
        ],
      ],
      phoneNumber: [
        this.oldAccountant.phoneNumber,
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(8),
          Validators.pattern(/^[24579][0-9]*$/),
        ],
      ],
    });
  }
  getAccountantProfile() {
    let email = this.authService.getEmail();
    this.accountantService.getAccountantData(email).subscribe({
      next: (data: any) => {
        // console.log(data);
        this.accountantId = data.id;
        this.oldAccountant = data;
        // console.log(this.oldAccountant);
        this.initForm();
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

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.imageNotChanged = true;
  }

  updateAccountantProfile() {
    if (this.profileForm.valid) {
      const updatedData = this.profileForm.value;
      // console.log(updatedData);
      const formData = new FormData();
      if (this.imageNotChanged) {
        formData.append('image', this.selectedFile!);
        formData.append(
          'updateRequest',
          new Blob([JSON.stringify(updatedData)], { type: 'application/json' })
        );
      } else {
        const blob = this.dataURItoBlob(
          'data:image/png;base64,' + this.oldAccountant.image
        );
        formData.append('image', blob);
        formData.append(
          'updateRequest',
          new Blob([JSON.stringify(updatedData)], { type: 'application/json' })
        );
      }

      this.accountantService
        .updateAccountantProfile(this.accountantId, formData)
        .subscribe({
          next: (response: any) => {
            this.toastService.showToast('success', 'Data updated successfully');
            this.getAccountantProfile();
          },
          error: (err: any) => {
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

  dataURItoBlob(dataURI: string) {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ab], { type: mimeString });
    return blob;
  }

  logout(): void {
    this.authService.logout().subscribe((success) => {
      if (success) {
        this.router.navigate(['/login']);
      }
    });
  }
}
