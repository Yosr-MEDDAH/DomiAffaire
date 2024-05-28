import { Component } from '@angular/core';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.css']
})
export class PageNotFoundComponent {
  constructor(private darkModeService:DarkModeService ){}

  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
