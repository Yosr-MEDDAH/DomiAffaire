import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FAQsRoutingModule } from './faqs-routing.module';
import { FAQsComponent } from '../faqs/faqs.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    FAQsComponent
  ],
  imports: [
    CommonModule,
    FAQsRoutingModule,
    ReactiveFormsModule
  ]
})
export class FAQsModule { }
