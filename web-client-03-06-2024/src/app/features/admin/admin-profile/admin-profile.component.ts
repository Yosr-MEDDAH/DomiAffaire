import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-admin-profile',
  templateUrl: './admin-profile.component.html',
  styleUrls: ['./admin-profile.component.css'],
})
export class AdminProfileComponent implements OnInit {
  email: any;
  constructor(
    private adminService: AdminService,
    private authService: AuthServiceService
  ) {}
  ngOnInit(): void {
    this.getAdminEmail();
    this.getAdminProfile();
  }
  getAdminEmail() {
    this.email=this.authService.getEmail();
  }
  getAdminProfile() {
    if(this.email){
      this.adminService.getAdminProfile(this.email).subscribe({
        next: (data: any) => {
          console.log(data);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
    }
  }
}
