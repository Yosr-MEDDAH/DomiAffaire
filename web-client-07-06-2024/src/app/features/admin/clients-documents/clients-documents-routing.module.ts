import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientsDocumentsComponent } from './clients-documents.component';
import { DomiciliationDetailsComponent } from './domiciliation-details/domiciliation-details.component';
import { AddFilesComponent } from './add-files/add-files.component';

const routes: Routes = [
  {path:'clients-documents', component:ClientsDocumentsComponent},
  {path:'clients-documents/:id', component:DomiciliationDetailsComponent},
  {path:'clients-documents/add-file/:id', component:AddFilesComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsDocumentsRoutingModule { }
