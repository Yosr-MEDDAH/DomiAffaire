import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountantProfileComponent } from './accountant-profile.component';
import { UpdateAccountantProfileComponent } from './update-accountant-profile/update-accountant-profile.component';
import { AccountantChangePasswordComponent } from './accountant-change-password/accountant-change-password.component';

const routes: Routes = [
  {path:'profile', component: AccountantProfileComponent},
  {path:'update-profile', component: UpdateAccountantProfileComponent},
  {path:'change-password', component: AccountantChangePasswordComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountantProfileRoutingModule { }
