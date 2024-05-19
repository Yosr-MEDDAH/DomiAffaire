import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyCreationDocumentsComponent } from './company-creation-documents.component';


const routes: Routes = [
  {path:'',component:CompanyCreationDocumentsComponent}
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyCreationDocumentsRoutingModule { }
