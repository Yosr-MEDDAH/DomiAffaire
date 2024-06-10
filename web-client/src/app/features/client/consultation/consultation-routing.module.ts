import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsultationComponent } from './consultation.component';
import { ConsultationRequestComponent } from './consultation-request/consultation-request.component';

const routes: Routes = [
  {path:'consultations',component:ConsultationComponent},
  {path:'consultation-request',component:ConsultationRequestComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConsultationRoutingModule { }
