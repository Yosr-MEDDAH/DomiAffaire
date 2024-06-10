import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FavoriteBlogsRoutingModule } from './favorite-blogs-routing.module';
import { FavoriteBlogsComponent } from './favorite-blogs.component';
import { AllBlogsComponent } from './all-blogs/all-blogs.component';
import { BlogDetailsComponent } from './blog-details/blog-details.component';


@NgModule({
  declarations: [
    FavoriteBlogsComponent,
    AllBlogsComponent,
    BlogDetailsComponent
  ],
  imports: [
    CommonModule,
    FavoriteBlogsRoutingModule
  ]
})
export class FavoriteBlogsModule { }
