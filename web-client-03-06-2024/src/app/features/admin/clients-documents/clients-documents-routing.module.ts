import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientsDocumentsComponent } from './clients-documents.component';

const routes: Routes = [
  {path:'clients-documents', component:ClientsDocumentsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsDocumentsRoutingModule { }
