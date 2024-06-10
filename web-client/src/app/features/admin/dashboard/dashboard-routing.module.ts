import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { DeadlinesComponent } from './deadlines/deadlines.component';

const routes: Routes = [
  {path:'', component:DashboardComponent},
  {path:'deadlines', component:DeadlinesComponent},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
