import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConsultationValidationRoutingModule } from './consultation-validation-routing.module';
import { InProgressConsultationsComponent } from './in-progress-consultations/in-progress-consultations.component';
import { ValidatedConsultationsComponent } from './validated-consultations/validated-consultations.component';

import {MultiLineTruncateDirective} from 'src/app/shared/directives/multi-line-truncate.directive';
import { AcceptedRequestsComponent } from './accepted-requests/accepted-requests.component';
import { RejectedRequestsComponent } from './rejected-requests/rejected-requests.component'
@NgModule({
  declarations: [
    InProgressConsultationsComponent,
    ValidatedConsultationsComponent,
    MultiLineTruncateDirective,
    AcceptedRequestsComponent,
    RejectedRequestsComponent
  ],
  imports: [
    CommonModule,
    ConsultationValidationRoutingModule
  ]
})
export class ConsultationValidationModule { }
