import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DomiciliationRoutingModule } from './domiciliation-routing.module';
import { DomiciliationRequestComponent } from './domiciliation-request/domiciliation-request.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MyDomiciliationRequestsComponent } from './my-domiciliation-requests/my-domiciliation-requests.component';


@NgModule({
  declarations: [
    DomiciliationRequestComponent,
    MyDomiciliationRequestsComponent
  ],
  imports: [
    CommonModule,
    DomiciliationRoutingModule,
    ReactiveFormsModule 
  ]
})
export class DomiciliationModule { }
