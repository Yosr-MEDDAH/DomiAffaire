import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-accountant',
  templateUrl: './accountant.component.html',
  styleUrls: ['./accountant.component.css']
})
export class AccountantComponent {
  accountants: any;
  constructor(private adminService: AdminService) {}
  
  ngOnInit(): void {
    this.getAllClients();
  }
  getAllClients() {
    this.adminService.getAllAccountants().subscribe({
      next: (data: any) => {
        console.log(data);
        this.accountants=data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }

}
