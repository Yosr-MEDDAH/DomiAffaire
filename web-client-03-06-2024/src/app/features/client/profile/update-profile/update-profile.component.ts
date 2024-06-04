import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Client } from 'src/app/core/models/client.model';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastService } from 'src/app/core/services/toast.service';
import { invalidDateValidator } from 'src/app/shared/validators/date-validator';
 
@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css'],
})
export class UpdateProfileComponent {
  imageNotChanged: boolean = false;
  oldClient: any;
  profileForm!: FormGroup;
  private clientId: any;
  selectedFile: File | undefined;
  client: any;
  constructor(
    private clientService: ClientService,
    private authService: AuthServiceService,
    private formBuilder: FormBuilder,
    public toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.getClientProfile();
  }
  initForm() {
    this.profileForm = this.formBuilder.group({
      firstName: [
        this.oldClient.firstName,
        [
          Validators.required,
          Validators.minLength(3),
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/),
        ],
      ],
      lastName: [
        this.oldClient.lastName,
        [
          Validators.required,
          Validators.minLength(3),
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/),
        ],
      ],
      phoneNumber: [
        this.oldClient.phoneNumber,
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(8),
          Validators.pattern(/^[24579][0-9]*$/),
        ],
      ],
      birthDate: [
        this.parseDateString(this.oldClient.birthDate),
        [Validators.required, invalidDateValidator],
      ],
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

  updateClientProfile() {
    // console.log(this.profileForm.value)
    // console.log(this.oldClient.image)
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
          'data:image/png;base64,' + this.oldClient.image
        );
        formData.append('image', blob);
        formData.append(
          'updateRequest',
          new Blob([JSON.stringify(updatedData)], { type: 'application/json' })
        );
      }

      this.clientService
        .updateClientProfile(this.clientId, formData)
        .subscribe({
          next: (response: any) => {
            this.toastService.showToast('success', 'Data updated successfully');
            this.getClientProfile();
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

  logout() {
    this.authService.logout();
  }
  parseDateString(dateString: any): any {
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
  }
}
