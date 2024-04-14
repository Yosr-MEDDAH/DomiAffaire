import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  chnagePswdForm!:FormGroup;
  oldClient: any;
  clientId:any;
  constructor(
    private clientService: ClientService,
    private authService: AuthServiceService,
    private fb:FormBuilder
  ) {}
  ngOnInit(): void {
    this.getClientProfile();
    this.chnagePswdForm=this.fb.group({
      oldPassword:['', Validators.required],
      newPassword:['', Validators.required],
      confirmPassword:['', Validators.required],
    })
  }
  getClientProfile() {
    let email = this.authService.getEmail();
    this.clientService.getClientData(email).subscribe({
      next: (data: any) => {
        console.log(data);
        this.clientId = data.id;
        this.oldClient = data;
        console.log(this.oldClient);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  changePswd(){
    if(this.chnagePswdForm.valid){
      this.clientService.changeClientPassword(this.clientId,this.chnagePswdForm.value).subscribe({
        next:(data:any)=>{console.log(data);},
        error:(err:HttpErrorResponse)=>{console.log(err);},
      })
    }
   
  }
}
