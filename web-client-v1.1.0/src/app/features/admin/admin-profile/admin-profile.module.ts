import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminProfileRoutingModule } from './admin-profile-routing.module';
import { ChangePasswordAdminComponent } from './change-password-admin/change-password-admin.component';
import { UpdateProfileAdminComponent } from './update-profile-admin/update-profile-admin.component';
import { AdminProfileComponent } from './admin-profile.component';



@NgModule({
  declarations: [
    ChangePasswordAdminComponent,
    UpdateProfileAdminComponent,
    AdminProfileComponent 
  ],
  imports: [
    CommonModule,
    AdminProfileRoutingModule
  ]
})
export class AdminProfileModule { }
