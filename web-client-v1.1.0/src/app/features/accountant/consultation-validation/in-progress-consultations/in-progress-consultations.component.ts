import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-in-progress-consultations',
  templateUrl: './in-progress-consultations.component.html',
  styleUrls: ['./in-progress-consultations.component.css'],
})
export class InProgressConsultationsComponent implements OnInit {
  requestID:any;
  isAcceptModalOpen: boolean = false;
  isRejectModalOpen: boolean = false;
  isConversationModalOpen: boolean = false;
  selectedConsultation: any;
  isDetailsModalOpen: boolean = false;
  consultations: any;
  sanitizedDetails: SafeHtml | undefined;
  inProgressConsultations: any;
  constructor(
    private accountantService: AccountantServiceService,
    private sanitizer: DomSanitizer,
    private darkModeService: DarkModeService,
    private toastService:ToastService,
    private router:Router
  ) {}
  ngOnInit(): void {
    this.getInProgressConsultationRequests();
  }

  getInProgressConsultationRequests() {
    this.accountantService.getInProgressConsultation().subscribe({
      next: (data: any) => {
        console.log(data);
        this.inProgressConsultations = data.map((item: any) => ({
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
  sanitizeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
  truncateText(text: string): string {
    const maxLength: number = 150; // Set the maximum length before truncating
    if (text.length > maxLength) {
      return text.slice(0, maxLength) + '...';
    } else {
      return text;
    }
  }

  accept() {
    // this.openAcceptModal();

    this.accountantService.acceptConsultationRequest(this.requestID).subscribe({
      next: (data: any) => {
        console.log(data);
        this.toastService.showToast("success",data.message);
        this.openConversationModal();
        this.getInProgressConsultationRequests();
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
  reject() {
    // this.openAcceptModal();

    this.accountantService.rejectConsultationRequest(this.requestID).subscribe({
      next: (data: any) => {
        console.log(data);
        this.toastService.showToast("success",data.message);
        // this.openConversationModal();
        this.closeRejectModal();
        this.getInProgressConsultationRequests();
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
  // Function to open the Accept modal
  openAcceptModal(id:any) {
    this.requestID=id;
    this.isAcceptModalOpen =  !this.isAcceptModalOpen ;
  }

  // Function to close the Accept modal
  closeAcceptModal() {
    this.isAcceptModalOpen =  !this.isAcceptModalOpen ;
  }
  // Function to open the Accept modal
  openRejectModal(id:any) {
    this.requestID=id;
    this.isRejectModalOpen =  !this.isRejectModalOpen ;
  }

  // Function to close the Reject modal
  closeRejectModal() {
    this.isRejectModalOpen =  !this.isRejectModalOpen ;
  }

  // Function to open the conversation modal
  openConversationModal() {
    this.isConversationModalOpen = !this.isConversationModalOpen;
  }

  // Function to close the conversation modal
  closeConversationModal() {
    this.isConversationModalOpen = !this.isConversationModalOpen;
  }
  // Function to handle Accept
  confirmAccept() {
    this.closeAcceptModal();
    this.accept();
  }

  // Function to handle cancellation
  cancelAccept() {
    this.closeAcceptModal();
  }
  confirmReject() {
    // this.closeRejectModal();
    this.reject();
  }

  // Function to handle cancellation
  cancelReject() {
    this.closeRejectModal();
  }

  // Function to start conversation
  startConversation() {
    // Add your logic here to navigate to "/"" or perform any other action
    // For example:
    this.router.navigate(['/chat']);
    // Close the conversation modal
    this.closeConversationModal();
  }

}
