import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountantProfileRoutingModule } from './accountant-profile-routing.module';
import { AccountantProfileComponent } from './accountant-profile.component';
import { UpdateAccountantProfileComponent } from './update-accountant-profile/update-accountant-profile.component';
import { AccountantChangePasswordComponent } from './accountant-change-password/accountant-change-password.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    AccountantProfileComponent,
    UpdateAccountantProfileComponent,
    AccountantChangePasswordComponent
  ],
  imports: [
    CommonModule,
    AccountantProfileRoutingModule,
    ReactiveFormsModule
  ]
})
export class AccountantProfileModule { }
