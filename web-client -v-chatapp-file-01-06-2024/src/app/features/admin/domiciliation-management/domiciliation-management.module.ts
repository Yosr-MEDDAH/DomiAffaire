import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DomiciliationManagementRoutingModule } from './domiciliation-management-routing.module';
import { DomiciliationRequestsComponent } from './domiciliation-requests/domiciliation-requests.component';
import { DomiciliationRequestDetailsComponent } from './domiciliation-request-details/domiciliation-request-details.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    DomiciliationRequestsComponent,
    DomiciliationRequestDetailsComponent
  ],
  imports: [
    CommonModule,
    DomiciliationManagementRoutingModule,
    FormsModule
  ]
})
export class DomiciliationManagementModule { }
