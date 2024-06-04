import { Component, OnInit } from '@angular/core';
import * as lottie from 'lottie-web';


@Component({
  selector: 'app-reset-password-success',
  templateUrl: './reset-password-success.component.html',
  styleUrls: ['./reset-password-success.component.css']
})
export class ResetPasswordSuccessComponent implements OnInit{
  ngOnInit(): void {
    (lottie as any).loadAnimation({
      container: document.getElementById('lottie-container'), 
      renderer: 'svg', 
      loop: false,
      autoplay: true,
      path: '/assets/dist/images/success-animation.json'    });  }

}
