import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ArchivedClientsRoutingModule } from './archived-clients-routing.module';
import { ArchivedClientsComponent } from './archived-clients.component';


@NgModule({
  declarations: [
    ArchivedClientsComponent
  ],
  imports: [
    CommonModule,
    ArchivedClientsRoutingModule
  ]
})
export class ArchivedClientsModule { }
