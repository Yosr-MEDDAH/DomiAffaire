import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AllBlogsComponent } from './all-blogs/all-blogs.component';
import { FavoriteBlogsComponent } from './favorite-blogs.component';
import { BlogDetailsComponent } from './blog-details/blog-details.component';

const routes: Routes = [
  {path:'all-blogs',component:AllBlogsComponent},
  {path:'favorite-blogs',component:FavoriteBlogsComponent},
  {path:'blog/:id',component:BlogDetailsComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FavoriteBlogsRoutingModule { }
