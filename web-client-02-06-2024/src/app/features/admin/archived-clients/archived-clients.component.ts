import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-archived-clients',
  templateUrl: './archived-clients.component.html',
  styleUrls: ['./archived-clients.component.css']
})
export class ArchivedClientsComponent implements OnInit {
  archivedClients: any;
  constructor(private adminService: AdminService) {}
  ngOnInit(): void {
    this.getArchivedClients();
  }
  getArchivedClients() {
    this.adminService.getArchivedClients().subscribe({
      next: (data: any) => {
        console.log(data);
        this.archivedClients=data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  unarchiveClient(id:any){
    this.adminService.unarchiveClient(id).subscribe({
      next: (data: any) => {
        console.log(data);
        this.getArchivedClients();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    })
  }
}
