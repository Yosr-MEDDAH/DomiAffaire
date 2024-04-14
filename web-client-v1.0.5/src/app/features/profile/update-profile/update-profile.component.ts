import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Client } from 'src/app/core/models/client.model';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css'],
})
export class UpdateProfileComponent {
  imageNotChanged:boolean=false;
  // oldClient: Client = new Client();
  oldClient: any;
  profileForm!: FormGroup;
  private clientId: any;
  selectedFile: File | undefined;
  client: any;
  constructor(
    private clientService: ClientService,
    private authService: AuthServiceService,
    private formBuilder: FormBuilder
  ) {}
  ngOnInit(): void {
    this.getClientProfile();
  }
  initForm() {
    this.profileForm = this.formBuilder.group({
      firstName: [this.oldClient.firstName],
      lastName: [this.oldClient.lastName],
      phoneNumber: [this.oldClient.phoneNumber],
      birthDate: [new Date(this.oldClient.birthDate)],
    });
  }
  getClientProfile() {
    let email = this.authService.getEmail();
    this.clientService.getClientData(email).subscribe({
      next: (data: any) => {
        console.log(data);
        this.clientId = data.id;
        this.oldClient = data;
        console.log(this.oldClient);
        this.initForm();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.imageNotChanged=true;
  }


    updateClientProfile() {
      // console.log(this.profileForm.value)
      // console.log(this.oldClient.image)
      if (this.profileForm.valid) {
        const updatedData = this.profileForm.value;
        console.log(updatedData)
        const formData = new FormData();
        if(this.imageNotChanged){
          formData.append('image', this.selectedFile!);
          formData.append(
            'updateRequest',
            new Blob([JSON.stringify(updatedData)], { type: 'application/json' })
          );
        }else{
          const blob = this.dataURItoBlob('data:image/png;base64,' + this.oldClient.image);
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
              console.log('Data updated successfully:', response);
              // Optionally, handle success message or navigation
            },
            error: (error: any) => {
              console.error('Error updating data:', error);
              // Optionally, handle error message
            },
          });
      } else {
        // Optionally, handle form validation errors
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
}
