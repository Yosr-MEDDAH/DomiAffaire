import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-rejected',
  templateUrl: './rejected.component.html',
  styleUrls: ['./rejected.component.css']
})
export class RejectedComponent implements OnInit{
  isRejectModalOpen:boolean=false;
  requestId:any;
  isAddModalOpen:boolean=false;
  selectedFile!:File;
  key:string="id";
  searchFirstName:any;
  domiciliationRequests:any;
  constructor(private adminService: AdminService, private darkModeService:DarkModeService, private toastService:ToastService) {}
  ngOnInit(): void {
    this.getAllDomiRequests();
  }
  getAllDomiRequests() {
    this.adminService.getRejectedDomiciliationRequests().subscribe({
      next: (data: any) => {
        console.log(data);
        this.domiciliationRequests = data.map((item: any) => ({
          ...item,
          createdAt: item.createdAt ? this.parseSentDate(item.createdAt) : null,
        }));
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  parseSentDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute, second, milliseconds] =
      dateTimeArray;
    return new Date(
      year,
      month - 1,
      day,
      hour,
      minute,
      second,
      milliseconds / 1000000
    );
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    if (files.length > 0) {
      this.selectedFile=files[0];
      console.log(this.selectedFile);
    }
  }

  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  search() {
    if (this.searchFirstName == "") {
      this.ngOnInit(); // Reinitialize the data
    } else {
      this.domiciliationRequests = this.domiciliationRequests.filter((res: { client: { firstName: string } }) => {
        return res.client.firstName.toLocaleLowerCase().includes(this.searchFirstName.toLocaleLowerCase());
      });
    }
  }
}


