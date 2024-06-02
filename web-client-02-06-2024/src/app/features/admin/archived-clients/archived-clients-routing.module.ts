import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArchivedClientsComponent } from './archived-clients.component';

const routes: Routes = [
  {path:'',component:ArchivedClientsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ArchivedClientsRoutingModule { }
