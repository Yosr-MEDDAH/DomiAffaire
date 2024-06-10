import {
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit,
} from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AdminService } from 'src/app/core/services/admin.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  currentLanguage: string;
  showLanguages: boolean = false;
  isAdmin: boolean = false;
  userProfile: any;
  isClient: boolean = false;
  navbarOpen = false;
  constructor(
    private darkModeService: DarkModeService,
    private authService: AuthServiceService,
    private clientService: ClientService,
    private accountantService: AccountantServiceService,
    private adminService: AdminService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private translate: TranslateService
  ) {
    this.currentLanguage = sessionStorage.getItem('currentLang') || 'fr';
    this.translate.use(this.currentLanguage);
    this.authService.loginStatus.subscribe((status) => {
      if (status) {
        this.refreshUserProfile();
      }
    });
    this.authService.roleChanged.subscribe(() => {
      this.refreshUserProfile(); // Refresh user profile on role change
    });
  }
  ngOnInit(): void {
    if (this.isUserLoggedIn()) {
      this.refreshUserProfile();
    }
  }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }
  closeMenu() {
    this.navbarOpen = false;
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  isUserLoggedIn(): boolean {
    return this.authService.LoggedIn();
  }

  logout(): void {
    this.authService.logout().subscribe((success) => {
      if (success) {
        this.userProfile = null;
        this.isClient = false;
        this.isAdmin = false;
        this.router.navigate(['/login']);
      }
    });
  }

  refreshUserProfile(): void {
    const email = this.authService.getEmail();
    if (email) {
      if (this.authService.LoggedInUser()) {
        this.clientService.getClientData(email).subscribe({
          next: (data) => {
            this.userProfile = data;
            this.isClient = true;
            this.isAdmin = false;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Error getting client data', err);
          },
        });
      } else if (this.authService.LoggedInAccountant()) {
        this.accountantService.getAccountantData(email).subscribe({
          next: (data) => {
            this.userProfile = data;
            this.isClient = false;
            this.isAdmin = false;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Error getting accountant data', err);
          },
        });
      } else if (this.authService.LoggedInAdmin()) {
        this.adminService.getAdminProfile(email).subscribe({
          next: (data) => {
            this.userProfile = data;
            this.isClient = false;
            this.isAdmin = true;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Error getting admin data', err);
          },
        });
      }
    }
  }
  changeCurrentLang(lang: string) {
    this.translate.use(lang);
    sessionStorage.setItem('currentLang', lang);
    this.currentLanguage = lang;
    this.showLanguages = false;
  }
  toggleLanguageList() {
    this.showLanguages = !this.showLanguages;
  }
}
