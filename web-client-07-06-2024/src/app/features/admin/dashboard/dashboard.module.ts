import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { DeadlinesComponent } from './deadlines/deadlines.component';
import { HttpClientModule } from '@angular/common/http';
import { ChartsModule } from 'ng2-charts';
import { PacksChartComponent } from './packs-chart/packs-chart.component';
import { CapitauxChartComponent } from './capitaux-chart/capitaux-chart.component';
import { DeadlineChartComponent } from './deadline-chart/deadline-chart.component';
import { ReservationChartComponent } from './reservation-chart/reservation-chart.component';

@NgModule({
  declarations: [DashboardComponent, DeadlinesComponent, PacksChartComponent, CapitauxChartComponent, DeadlineChartComponent, ReservationChartComponent],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    HttpClientModule,
    ChartsModule
  ],
})
export class DashboardModule {}
