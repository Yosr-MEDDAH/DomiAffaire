import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DomiciliationRequestsComponent } from './domiciliation-requests/domiciliation-requests.component';
import { DomiciliationRequestDetailsComponent } from './domiciliation-request-details/domiciliation-request-details.component';

const routes: Routes = [
  {path :'domiciliation-requests', component:DomiciliationRequestsComponent},
  {path :'domiciliation-requests/details/:id', component:DomiciliationRequestDetailsComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DomiciliationManagementRoutingModule { }
