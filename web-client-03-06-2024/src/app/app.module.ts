import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { LayoutsModule } from './layouts/layouts.module';
import { DarkModeSwitcherComponent } from './shared/components/dark-mode-switcher/dark-mode-switcher.component';
import { ToastComponent } from './shared/components/toast/toast.component';
import { PaginationComponent } from './shared/components/pagination/pagination.component';


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    DarkModeSwitcherComponent,
    ToastComponent,
    PaginationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    LayoutsModule,
    
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
