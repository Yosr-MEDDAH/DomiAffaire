import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DomiciliationRequestsComponent } from './domiciliation-requests/domiciliation-requests.component';
import { DomiciliationRequestDetailsComponent } from './domiciliation-request-details/domiciliation-request-details.component';
import { AcceptedByAdminComponent } from './accepted-by-admin/accepted-by-admin.component';
import { AcceptedByUserComponent } from './accepted-by-user/accepted-by-user.component';
import { RejectedComponent } from './rejected/rejected.component';
import { DomiciliarionHistoryComponent } from './domiciliarion-history/domiciliarion-history.component';

const routes: Routes = [
  { path: 'domiciliation-requests', component: DomiciliationRequestsComponent },
  {
    path: 'domiciliation-requests/details/:id',
    component: DomiciliationRequestDetailsComponent,
  },
  {
    path: 'accepted-domiciliation-requests/details/:id',
    component: DomiciliationRequestDetailsComponent,
  },
  {
    path: 'accepted-domiciliation-requests',
    component: AcceptedByAdminComponent,
  },
  {
    path: 'rejected-domiciliation-requests',
    component: RejectedComponent,
  },
  { path: 'domiciliation', component: AcceptedByUserComponent },
  {
    path: 'domiciliation-history',
    component: DomiciliarionHistoryComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DomiciliationManagementRoutingModule {}
