import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-users',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css'],
})
export class ClientsComponent implements OnInit {
  clients: any;
  constructor(private adminService: AdminService,
    private toastService:ToastService
  ) {}
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
