import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NgxPrintModule} from 'ngx-print';
import { DomiciliationManagementRoutingModule } from './domiciliation-management-routing.module';
import { DomiciliationRequestsComponent } from './domiciliation-requests/domiciliation-requests.component';
import { DomiciliationRequestDetailsComponent } from './domiciliation-request-details/domiciliation-request-details.component';
import { FormsModule } from '@angular/forms';
import { AcceptedByAdminComponent } from './accepted-by-admin/accepted-by-admin.component';
import { AcceptedByUserComponent } from './accepted-by-user/accepted-by-user.component';
import { RejectedComponent } from './rejected/rejected.component';
import { DomiciliarionHistoryComponent } from './domiciliarion-history/domiciliarion-history.component';
import { Ng2SearchPipeModule} from 'ng2-search-filter';


@NgModule({
  declarations: [
    DomiciliationRequestsComponent,
    DomiciliationRequestDetailsComponent,
    AcceptedByAdminComponent,
    AcceptedByUserComponent,
    RejectedComponent,
    DomiciliarionHistoryComponent
  ],
  imports: [
    CommonModule,
    DomiciliationManagementRoutingModule,
    FormsModule,
    NgxPrintModule,
    Ng2SearchPipeModule
  ]
})
export class DomiciliationManagementModule { }
