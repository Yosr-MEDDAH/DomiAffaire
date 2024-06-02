import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResetPasswordRoutingModule } from './reset-password-routing.module';
import { ResetPasswordComponent } from './reset-password.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ResetPasswordRequestComponent } from './reset-password-request/reset-password-request.component';
import { ResetPasswordSuccessComponent } from './reset-password-success/reset-password-success.component';
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  declarations: [
    ResetPasswordComponent,
    ResetPasswordRequestComponent,
    ResetPasswordSuccessComponent
  ],
  imports: [
    CommonModule,
    ResetPasswordRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers:[CookieService]
})
export class ResetPasswordModule { }
