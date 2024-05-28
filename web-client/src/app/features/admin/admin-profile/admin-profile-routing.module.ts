import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminProfileComponent } from './admin-profile.component';
import { UpdateProfileAdminComponent } from './update-profile-admin/update-profile-admin.component';
import { ChangePasswordAdminComponent } from './change-password-admin/change-password-admin.component';

const routes: Routes = [
  {path:'profile-admin',component:AdminProfileComponent},
  {path:'update-profile-admin',component:UpdateProfileAdminComponent},
  {path:'change-password-admin',component:ChangePasswordAdminComponent},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminProfileRoutingModule { }
