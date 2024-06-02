import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountantRoutingModule } from './accountant-routing.module';
import { AccountantComponent } from './accountant.component';


@NgModule({
  declarations: [
    AccountantComponent
  ],
  imports: [
    CommonModule,
    AccountantRoutingModule
  ]
})
export class AccountantModule { }
