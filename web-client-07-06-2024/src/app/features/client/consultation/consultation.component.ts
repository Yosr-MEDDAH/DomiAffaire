import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/core/services/client.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ToastService } from 'src/app/core/services/toast.service';
 
@Component({
  selector: 'app-consultation',
  templateUrl: './consultation.component.html',
  styleUrls: ['./consultation.component.css'],
})
export class ConsultationComponent implements OnInit {
  selectedConsultation: any;
  isDetailsModalOpen: boolean = false;
  isCancelModalOpen: boolean = false;
  consultations: any;
  sanitizedDetails: SafeHtml | undefined;
  requestId:any
  constructor(
    private clientService: ClientService,
    private darkModeService: DarkModeService,
    private sanitizer: DomSanitizer,
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.getClientConsultationRequests();
  }
  getClientConsultationRequests() {
    this.clientService.getClientConsultationRequest().subscribe({
      next: (data: any) => {
        // console.log(data);
        // Convert LocalDateTime to Date object for each item
        this.consultations = data.map((item: any) => ({
          ...item,
          sentAt: this.parseSentDate(item.sentAt),
          proposedDate: item.proposedDate ? this.parseProposedDate(item.proposedDate) : null,
          finalConsultationDate:item.finalConsultationDate ? this.parseSentDate(item.finalConsultationDate) : null,
        }));
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  openCancelOrCloseModal(id?:any){
    if(id) this.requestId=id;
    this.isCancelModalOpen=!this.isCancelModalOpen;
    console.log(this.isCancelModalOpen)
  }
  
  confirmCancelRequest(){
    this.clientService.cancelConsultationRequest(this.requestId)
    .subscribe({
      next:(data:any)=>{
        // console.log(data);
        this.openCancelOrCloseModal();
        this.toastService.showToast("success", data.message);
        this.getClientConsultationRequests();
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
    this.selectedConsultation = item;
    // Sanitize the HTML content
    this.sanitizedDetails = this.sanitizer.bypassSecurityTrustHtml(
      item.details
    );
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  closeDetailsModal() {
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
