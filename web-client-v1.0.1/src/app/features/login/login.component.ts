import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
constructor(private authService:AuthServiceService){}
  ngOnInit(): void {
    this.getUrl();
  }
getUrl(){
  let data={email:"yosr.meddah92@gmail.com"}
  this.authService.requestResetPassword(data).subscribe({
    next:(data:any)=>{
      console.log(data);
    },
    error:(err:HttpErrorResponse)=>{
      console.log(err);
    }
  })
}
}
