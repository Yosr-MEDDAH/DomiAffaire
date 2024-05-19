import { Component, HostListener, OnInit } from '@angular/core';
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
export class NavbarComponent implements OnInit{
  isAdmin:boolean=false;
  userProfile:any;
  isClient: boolean=false;
  navbarOpen = false;
  constructor(
    private darkModeService: DarkModeService,
    private authService: AuthServiceService,
    private clientService:ClientService,
    private accountantService:AccountantServiceService,
    private adminService:AdminService
  ) {}
  ngOnInit(): void {
    if (this.isUserLoggedIn()) {
      if(this.authService.LoggedInUser()){
        const email = this.authService.getEmail();
      this.clientService.getClientData(email).subscribe((data) => {
        console.log(data)
        this.userProfile = data;
        this.isClient=true;
        this.isAdmin=false;
      });
    }else if(this.authService.LoggedInAccountant()){
      const email = this.authService.getEmail();
      this.accountantService.getAccountantData(email).subscribe((data) => {
        console.log(data)
        this.userProfile = data;
        this.isClient=false;
        this.isAdmin=false;
      });
    }else if(this.authService.LoggedInAdmin()){
      const email = this.authService.getEmail();
      this.adminService.getAdminProfile(email).subscribe((data) => {
        console.log(data)
        this.userProfile = data;
        this.isClient=false;
        this.isAdmin=true;
        });
      }
      
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
    this.authService.logout();
  }
}
