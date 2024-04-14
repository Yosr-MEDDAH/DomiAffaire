import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit{
  client:any;
  constructor(
    private clientService: ClientService,
    private authService: AuthServiceService
  ) {}
  ngOnInit(): void {
this.getClient()  }

  getClient() {
    let email=this.authService.getEmail();
    this.clientService.getClientData(email).subscribe({
      next:(data:any)=>{
        console.log(data);
        this.client=data;
        
      },
      error:(err:HttpErrorResponse)=>{console.log(err);}
    });
  }
}
