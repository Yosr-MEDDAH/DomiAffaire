import { Component } from '@angular/core';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-dark-mode-switcher',
  templateUrl: './dark-mode-switcher.component.html',
  styleUrls: ['./dark-mode-switcher.component.css']
})
export class DarkModeSwitcherComponent {
  constructor(private darkModeService: DarkModeService) { }

  switchMode(){
    this.darkModeService.switchMode();
  }
  isDarkModeEnabled(){
    return this.darkModeService.isDarkModeEnabled();
  }
}
