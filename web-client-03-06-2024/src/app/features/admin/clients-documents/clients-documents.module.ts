import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientsDocumentsRoutingModule } from './clients-documents-routing.module';
import { ClientsDocumentsComponent } from './clients-documents.component';


@NgModule({
  declarations: [
    ClientsDocumentsComponent
  ],
  imports: [
    CommonModule,
    ClientsDocumentsRoutingModule
  ]
})
export class ClientsDocumentsModule { }
