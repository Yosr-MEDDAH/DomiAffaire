import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { ClientService } from 'src/app/core/services/client.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-my-domiciliation-requests',
  templateUrl: './my-domiciliation-requests.component.html',
  styleUrls: ['./my-domiciliation-requests.component.css']
})
export class MyDomiciliationRequestsComponent {
  selectedDomiciliation: any;
  isDetailsModalOpen: boolean = false;
  isCancelModalOpen: boolean = false;
  domiciliations: any;

  requestId:any
  constructor(
    private clientService: ClientService,
    private darkModeService: DarkModeService,
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.getClientDomiciliationRequests();
  }
  getClientDomiciliationRequests() {
    this.clientService.getClientDomiciliationRequests().subscribe({
      next: (data: any) => {
        this.domiciliations = data.map((item: any) => ({
          ...item,
          createdAt:item.createdAt ? this.parseSentDate(item.createdAt) : null,
        }));
        console.log(this.domiciliations)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  openCancelOrCloseModal(id?:any){
    if(id) this.requestId=id;
    this.isCancelModalOpen=!this.isCancelModalOpen;
  }
  
  confirmCancelRequest(){
    this.clientService.cancelDomiciliationRequest(this.requestId)
    .subscribe({
      next:(data:any)=>{
        // console.log(data);
        this.openCancelOrCloseModal();
        this.toastService.showToast("success", data.message);
        this.getClientDomiciliationRequests();
      },
      error:(err:HttpErrorResponse)=>{
        if (err.error != null) {
          let errorMessage = 'An error occurred: ';
          for (const key in err.error) {
            if (err.error.hasOwnProperty(key)) {
              errorMessage += `${err.error[key]} `;
            }
          }
          this.toastService.showToast('error', errorMessage);
        }
      },
    })
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
  parseProposedDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute] = dateTimeArray;
    return new Date(year, month - 1, day, hour, minute);
  }

  openDetailsModal(item: any) {
    this.selectedDomiciliation = item;

    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  closeDetailsModal() {
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
