import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { LayoutsModule } from './layouts/layouts.module';
import { DarkModeSwitcherComponent } from './shared/components/dark-mode-switcher/dark-mode-switcher.component';

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    DarkModeSwitcherComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AppRoutingModule,
    HttpClientModule,
    LayoutsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
