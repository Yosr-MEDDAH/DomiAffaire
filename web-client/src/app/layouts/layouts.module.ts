import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserComponent } from './user/user.component';
import { AdminComponent } from './admin/admin.component';
import { AuthAdminComponent } from './auth-admin/auth-admin.component';
import { NavbarComponent } from '../shared/components/navbar/navbar.component';
import { ClickOutsideDirective } from '../shared/directives/click-outside.directive';



@NgModule({
  declarations: [
    UserComponent,
    AdminComponent,
    AuthAdminComponent,
    NavbarComponent,
    ClickOutsideDirective
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [ClickOutsideDirective]
})
export class LayoutsModule { }
