import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BlogsRoutingModule } from './blogs-routing.module';
import { BlogsComponent } from './blogs.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { AddBlogComponent } from './add-blog/add-blog.component';


@NgModule({
  declarations: [
    BlogsComponent,
    AddBlogComponent
  ],
  imports: [
    CommonModule,
    BlogsRoutingModule,
    ReactiveFormsModule,
    AngularEditorModule
  ]
})
export class BlogsModule { }
