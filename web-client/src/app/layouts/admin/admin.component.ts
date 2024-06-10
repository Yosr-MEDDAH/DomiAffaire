import {
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit,
} from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AdminService } from 'src/app/core/services/admin.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ClientService } from 'src/app/core/services/client.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent {
  private cssLinkElement!: HTMLLinkElement;
  private jsScriptElement!: HTMLScriptElement;

  AdminProfile: any;
  navbarOpen = false;
  constructor(
    private darkModeService: DarkModeService,
    private authService: AuthServiceService,
    private adminService: AdminService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.authService.loginStatus.subscribe((status) => {
      if (status) {
        this.refreshAdminProfile();
      }
    });
    this.authService.roleChanged.subscribe(() => {
      this.refreshAdminProfile();
    });
  }
  ngOnInit(): void {
    if (this.isUserLoggedIn()) {
      this.refreshAdminProfile();
    }

    this.cssLinkElement = document.createElement('link');
    this.cssLinkElement.rel = 'stylesheet';
    this.cssLinkElement.href =
      'https://cdnjs.cloudflare.com/ajax/libs/flowbite/2.3.0/flowbite.min.css';
    document.head.appendChild(this.cssLinkElement);

    this.jsScriptElement = document.createElement('script');
    this.jsScriptElement.src =
      'https://cdnjs.cloudflare.com/ajax/libs/flowbite/2.3.0/flowbite.min.js';
    document.body.appendChild(this.jsScriptElement);
  }
  ngOnDestroy(): void {
    if (this.cssLinkElement) {
      document.head.removeChild(this.cssLinkElement);
    }

    if (this.jsScriptElement) {
      document.body.removeChild(this.jsScriptElement);
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
    return this.authService.LoggedInAdmin();
  }

  logout(): void {
    this.authService.logout().subscribe((success) => {
      if (success) {
        this.AdminProfile = null;
        this.router.navigate(['/admin/login']);
      }
    });
  }

  refreshAdminProfile(): void {
    const email = this.authService.getEmail();
    if (email) {
      if (this.authService.LoggedInAdmin()) {
        this.adminService.getAdminProfile(email).subscribe({
          next: (data) => {
            this.AdminProfile = data;

            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Error getting admin data', err);
          },
        });
      }
    }
  }
}
