import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyCreationComponent } from './company-creation.component';

const routes: Routes = [
  {path:'',component:CompanyCreationComponent}
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyCreationRoutingModule { }
