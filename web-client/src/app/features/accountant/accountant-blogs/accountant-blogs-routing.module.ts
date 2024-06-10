import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountantBlogsComponent } from './accountant-blogs.component';
import { HandleBlogComponent } from './handle-blog/handle-blog.component';

const routes: Routes = [
  {path:'blogs',component:AccountantBlogsComponent},
  {path:'blog/add',component:HandleBlogComponent},
  {path:'blog/edit/:id',component:HandleBlogComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountantBlogsRoutingModule { }
