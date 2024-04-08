import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent {
  emailEntered: boolean = false;
  enteredEmail: string = '';
  password: string = '';
  confirmPassword: string = '';
  passwordsMismatch: boolean = false;
  emailError: boolean = false;
  constructor(private darkModeService: DarkModeService, private router: Router) {}
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  checkEmail() {
    console.log(this.emailEntered)
    if (this.enteredEmail === 'yosr.meddah92@gmail.com') {
      this.emailEntered = true;
    } else {
      this.emailError = true;
    }
  }

  paswordMatch() {
    if (this.password === this.confirmPassword) {
      this.router.navigate(['/login']);
    } else {
      this.passwordsMismatch = true;
    }
  }

  isPasswordValid() {
    return this.password === this.confirmPassword;
  }
}