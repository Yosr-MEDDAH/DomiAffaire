import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConsultationRoutingModule } from './consultation-routing.module';
import { ConsultationComponent } from './consultation.component';
import { ConsultationRequestComponent } from './consultation-request/consultation-request.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AngularEditorModule } from '@kolkov/angular-editor';
@NgModule({
  declarations: [ConsultationComponent, ConsultationRequestComponent],
  imports: [
    CommonModule,
    ConsultationRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AngularEditorModule,
  ],
})
export class ConsultationModule {}
