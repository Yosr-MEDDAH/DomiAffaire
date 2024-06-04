import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-rejected-requests',
  templateUrl: './rejected-requests.component.html',
  styleUrls: ['./rejected-requests.component.css']
})
export class RejectedRequestsComponent implements OnInit {
  rejectedConsultation: any;
  selectedConsultation: any;
  requestId:any;
  isDetailsModalOpen: boolean = false;
  isAcceptModalOpen: boolean = false;
  isConversationModalOpen:boolean=false;
  constructor(
    private accountantService: AccountantServiceService,
    private sanitizer: DomSanitizer,
    private darkModeService: DarkModeService,
    private toastService:ToastService,
    private router:Router
  ) {}
  ngOnInit(): void {
    this.getRejectedConsultationRequests();
  }
  getRejectedConsultationRequests() {
    this.accountantService.getRejectedConsultation().subscribe({
      next: (data: any) => {
        console.log(data);
        this.rejectedConsultation = data.map((item: any) => ({
          ...item,
          sentAt: this.parseSentDate(item.sentAt),
          proposedDate: item.proposedDate ? this.parseProposedDate(item.proposedDate) : null,
          finalConsultationDate:item.finalConsultationDate ? this.parseSentDate(item.finalConsultationDate) : null,
          // details: this.sanitizer.bypassSecurityTrustHtml(item.details),
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
  parseProposedDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute] = dateTimeArray;
    return new Date(year, month - 1, day, hour, minute);
  }
  truncateText(text: string): string {
    const maxLength: number = 150; // Set the maximum length before truncating
    if (text.length > maxLength) {
      return text.slice(0, maxLength) + '...';
    } else {
      return text;
    }
  }
  openDetailsModal(item: any) {
    this.selectedConsultation = item;
    // Sanitize the HTML content
    // this.sanitizedDetails = this.sanitizer.bypassSecurityTrustHtml(
    //   item.details
    // );
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  closeDetailsModal() {
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  openOrCloseAcceptModal(id?:any){
    this.isAcceptModalOpen=!this.isAcceptModalOpen;
    if(id) this.requestId=id;
    console.log(this.requestId)
  }
  accept() {
    // this.openAcceptModal();
console.log(this.requestId)
    this.accountantService.acceptConsultationRequest(this.requestId).subscribe({
      next: (data: any) => {
        console.log(data);
        this.toastService.showToast("success",data.message);
        this.openConversationModal();
        this.getRejectedConsultationRequests();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
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
    });
  }
  confirmAccept() {
    this.openOrCloseAcceptModal();
    this.accept();
  }
  startConversation() {
    // Add your logic here to navigate to "/"" or perform any other action
    // For example:
    this.router.navigate(['/chat']);
    // Close the conversation modal
    this.closeConversationModal();
  }
  openConversationModal() {
    this.isConversationModalOpen = !this.isConversationModalOpen;
  }

  // Function to close the conversation modal
  closeConversationModal() {
    this.isConversationModalOpen = !this.isConversationModalOpen;
  }
}
