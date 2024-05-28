import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DomiciliationRequestComponent } from './domiciliation-request/domiciliation-request.component';
import { MyDomiciliationRequestsComponent } from './my-domiciliation-requests/my-domiciliation-requests.component';

const routes: Routes = [
  {path:'domiciliation-request', component:DomiciliationRequestComponent},
  {path:'my-domiciliation-requests', component:MyDomiciliationRequestsComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DomiciliationRoutingModule { }
