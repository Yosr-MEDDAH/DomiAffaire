import { HttpErrorResponse } from '@angular/common/http';
import { Component,  OnInit } from '@angular/core';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  constructor(
    private authService: AuthServiceService,
    private darkModeService : DarkModeService
    ) {}
  ngOnInit(): void {
    // this.getUrl();
  }

  isDarkModeEnabled(){
    return this.darkModeService.isDarkModeEnabled();
  }
  
  // getUrl() {
  //   let data = { email: 'yosr.meddah92@gmail.com' };
  //   this.authService.requestResetPassword(data).subscribe({
  //     next: (data: any) => {
  //       console.log(data);
  //     },
  //     error: (err: HttpErrorResponse) => {
  //       console.log(err);
  //     },
  //   });
  // }
}
