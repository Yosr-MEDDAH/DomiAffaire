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
  isToArchive = false;
  isLoading = false;
  clientToArchive: any;

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
  openArchiveModal(id?:any){
    if(id) {this.clientToArchive=id}
    this.isToArchive=!this.isToArchive;
  }
  archiveClient(){
    this.openArchiveModal()
    this.isLoading=true;
    this.adminService.archiveClient(this.clientToArchive).subscribe({
      next: (data: any) => {
        
        console.log(data);
        this.toastService.showToast("success","Client archived successfully!")
        this.getAllClients();
        this.isLoading=false;
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err)
        this.isLoading=false;

      },
    })
  }
  private handleError(err: HttpErrorResponse) {
    let errorMessage = 'An error occurred: ';
    for (const key in err.error) {
      if (err.error.hasOwnProperty(key)) {
        errorMessage += `${err.error[key]} `;
      }
    }
    this.toastService.showToast('error', errorMessage);
  }
 
}
