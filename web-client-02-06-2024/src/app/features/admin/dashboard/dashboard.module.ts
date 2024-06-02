import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsModule } from 'ng2-charts';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    DashboardComponent,
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    FormsModule,
    ChartsModule
  ]
})
export class DashboardModule { }
