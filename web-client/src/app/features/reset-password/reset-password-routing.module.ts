import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResetPasswordComponent } from './reset-password.component';
import { ResetPasswordRequestComponent } from './reset-password-request/reset-password-request.component';
import { ResetPasswordSuccessComponent } from './reset-password-success/reset-password-success.component';

const routes: Routes = [
  {path:'reset-password-request',component:ResetPasswordRequestComponent},
  {path:'reset-password',component:ResetPasswordComponent},
  {path:'reset-password-success',component:ResetPasswordSuccessComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResetPasswordRoutingModule { }
