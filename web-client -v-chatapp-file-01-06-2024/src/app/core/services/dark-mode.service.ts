import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DarkModeService {
  private isDarkMode!: boolean;
  constructor() {
    this.isDarkMode = JSON.parse(sessionStorage.getItem('darkMode')!) || false;
    this.updateTheme();
  }

  switchMode() {
    this.isDarkMode = !this.isDarkMode;
    sessionStorage.setItem('darkMode', JSON.stringify(this.isDarkMode));
    this.updateTheme();
  }

  private updateTheme() {
    const htmlElement = document.querySelector('html');
    if (this.isDarkMode) {
      htmlElement!.classList.add('dark');
    } else {
      htmlElement!.classList.remove('dark');
    }
  }

  isDarkModeEnabled() {
    return this.isDarkMode;
  }
}
