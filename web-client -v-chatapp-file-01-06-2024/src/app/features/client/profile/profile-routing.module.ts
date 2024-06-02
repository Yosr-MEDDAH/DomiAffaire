import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { UpdateProfileComponent } from './update-profile/update-profile.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

const routes: Routes = [
  {path:"profile", component:ProfileComponent},
  {path:"update-profile", component:UpdateProfileComponent},
  {path:"change-password", component:ChangePasswordComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
