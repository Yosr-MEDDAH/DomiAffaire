import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InProgressConsultationsComponent } from './in-progress-consultations/in-progress-consultations.component';
import { ValidatedConsultationsComponent } from './validated-consultations/validated-consultations.component';
import { AcceptedRequestsComponent } from './accepted-requests/accepted-requests.component';
import { RejectedRequestsComponent } from './rejected-requests/rejected-requests.component';

const routes: Routes = [
  {
    path: 'in-progress-consultations',
    component: InProgressConsultationsComponent,
  },
  {
    path: 'validated-consultations',
    component: ValidatedConsultationsComponent,
  },
  { path: 'accepted-consultations', component: AcceptedRequestsComponent },
  { path: 'rejected-consultations', component: RejectedRequestsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ConsultationValidationRoutingModule {}
