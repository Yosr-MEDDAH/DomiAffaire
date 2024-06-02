import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DomiciliationRoutingModule } from './domiciliation-routing.module';
import { DomiciliationRequestComponent } from './domiciliation-request/domiciliation-request.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MyDomiciliationRequestsComponent } from './my-domiciliation-requests/my-domiciliation-requests.component';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';
@NgModule({
  declarations: [
    DomiciliationRequestComponent,
    MyDomiciliationRequestsComponent
  ],
  imports: [
    CommonModule,
    DomiciliationRoutingModule,
    ReactiveFormsModule ,
    FormsModule,
    NgxExtendedPdfViewerModule
  ]
})
export class DomiciliationModule { }
