import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientsDocumentsRoutingModule } from './clients-documents-routing.module';
import { ClientsDocumentsComponent } from './clients-documents.component';
import { DomiciliationDetailsComponent } from './domiciliation-details/domiciliation-details.component';
import { AddFilesComponent } from './add-files/add-files.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    ClientsDocumentsComponent,
    DomiciliationDetailsComponent,
    AddFilesComponent
  ],
  imports: [
    CommonModule,
    ClientsDocumentsRoutingModule,
    ReactiveFormsModule
  ]
})
export class ClientsDocumentsModule { }
