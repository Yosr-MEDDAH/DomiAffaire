import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountantBlogsRoutingModule } from './accountant-blogs-routing.module';
import { AccountantBlogsComponent } from './accountant-blogs.component';
import { HandleBlogComponent } from './handle-blog/handle-blog.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AngularEditorModule } from '@kolkov/angular-editor';


@NgModule({
  declarations: [
    AccountantBlogsComponent,
    HandleBlogComponent
  ],
  imports: [
    CommonModule,
    AccountantBlogsRoutingModule,
    ReactiveFormsModule,
    AngularEditorModule
  ]
})
export class AccountantBlogsModule { }
