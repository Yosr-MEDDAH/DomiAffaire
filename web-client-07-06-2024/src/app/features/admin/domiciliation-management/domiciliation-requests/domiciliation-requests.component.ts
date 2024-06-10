import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-domiciliation-requests',
  templateUrl: './domiciliation-requests.component.html',
  styleUrls: ['./domiciliation-requests.component.css'],
})
export class DomiciliationRequestsComponent implements OnInit {
  isLoading: boolean = false;
  isRejectModalOpen: boolean = false;
  requestId: any;
  isAddModalOpen: boolean = false;
  selectedFile!: File;
  key: string = 'id';
  domiciliationRequests: any;
  searchFirstName:any;
  constructor(
    private adminService: AdminService,
    private darkModeService: DarkModeService,
    private toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.getAllDomiRequests();
  }
  getAllDomiRequests() {
    this.adminService.getAllDomiciliationRequests().subscribe({
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
  acceptRequest() {
    this.isLoading = true;
    const formData = new FormData();
    formData.append('draftContract', this.selectedFile);
    this.adminService
      .acceptDomiciliationRequest(this.requestId, formData)
      .subscribe({
        next: (data: any) => {
          this.getAllDomiRequests();
          this.openAddModal();
          this.toastService.showToast('success', data.message);
          this.isLoading = false;
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
          this.isLoading = false;
        },
      });
  }
  rejectRequest() {
    this.isLoading=true;
    this.adminService.rejectDomiciliationRequest(this.requestId).subscribe({
      next: (data: any) => {
        this.getAllDomiRequests();
        this.openOrCloseRejectModal();
        this.toastService.showToast('success', data.message);
        this.isLoading=false;
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
        this.isLoading=false;
      },
    });
  }
  onFileSelected(event: any) {
    const files = event.target.files;
    if (files.length > 0) {
      this.selectedFile = files[0];
      console.log(this.selectedFile);
    }
  }
  openAddModal(id?: any) {
    if (id) this.requestId = id;
    this.isAddModalOpen = !this.isAddModalOpen;
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  openOrCloseRejectModal(id?: any) {
    if (id) this.requestId = id;
    this.isRejectModalOpen = !this.isRejectModalOpen;
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
