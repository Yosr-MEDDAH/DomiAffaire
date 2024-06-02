import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BlogsComponent } from './blogs.component';
import { AddBlogComponent } from './add-blog/add-blog.component';

const routes: Routes = [
  {path:'blogs',component:BlogsComponent},
  {path:'blog/add',component:AddBlogComponent},
  {path:'blog/edit/:id',component:AddBlogComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BlogsRoutingModule { }
