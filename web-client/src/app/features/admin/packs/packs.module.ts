import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PacksRoutingModule } from './packs-routing.module';
import { PacksComponent } from './packs.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    PacksComponent
  ],
  imports: [
    CommonModule,
    PacksRoutingModule,
    ReactiveFormsModule
  ]
})
export class PacksModule { }
