import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit {
  clients: any;
  constructor(private adminService: AdminService) {}
  ngOnInit(): void {
    this.getAllClients();
  }
  getAllClients() {
    this.adminService.getAllClients().subscribe({
      next: (data: any) => {
        console.log(data);
        this.clients=data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  archiveClient(id:any){
    this.adminService.archiveClient(id).subscribe({
      next: (data: any) => {
        console.log(data);
        this.getAllClients();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    })
  }
}
