import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyCreationRoutingModule } from './company-creation-routing.module';
import { CompanyCreationComponent } from './company-creation.component';


@NgModule({
  declarations: [
    CompanyCreationComponent
  ],
  imports: [
    CommonModule,
    CompanyCreationRoutingModule
  ]
})
export class CompanyCreationModule { }
