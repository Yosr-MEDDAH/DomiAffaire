import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyCreationDocumentsRoutingModule } from './company-creation-documents-routing.module';
import { CompanyCreationDocumentsComponent } from './company-creation-documents.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    CompanyCreationDocumentsComponent
  ],
  imports: [
    CommonModule,
    CompanyCreationDocumentsRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class CompanyCreationDocumentsModule { }
