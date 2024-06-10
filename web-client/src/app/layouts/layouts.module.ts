import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserComponent } from './user/user.component';
import { AdminComponent } from './admin/admin.component';
import { AuthAdminComponent } from './auth-admin/auth-admin.component';
import { NavbarComponent } from '../shared/components/navbar/navbar.component';
import { ClickOutsideDirective } from '../shared/directives/click-outside.directive';
import { FooterComponent } from '../shared/components/footer/footer.component';
import { HttpClient } from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    UserComponent,
    AdminComponent,
    AuthAdminComponent,
    NavbarComponent,
    ClickOutsideDirective,
    FooterComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    
    TranslateModule.forRoot({
      defaultLanguage:'fr',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  exports: [ClickOutsideDirective,  FooterComponent]
})
export class LayoutsModule { }
