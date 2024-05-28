import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-accepted-requests',
  templateUrl: './accepted-requests.component.html',
  styleUrls: ['./accepted-requests.component.css']
})
export class AcceptedRequestsComponent implements OnInit{
  acceptedConsultations: any;
  selectedConsultation: any;
  isDetailsModalOpen: boolean = false;
  constructor(
    private accountantService: AccountantServiceService,
    private sanitizer: DomSanitizer,
    private darkModeService: DarkModeService,
    private toastService:ToastService,
    private router:Router
  ) {}
  ngOnInit(): void {
    this.getAcceptedConsultationRequests();
  }
  getAcceptedConsultationRequests() {
    this.accountantService.getAcceptedConsultation().subscribe({
      next: (data: any) => {
        console.log(data);
        this.acceptedConsultations = data.map((item: any) => ({
          ...item,
          sentAt: this.parseSentDate(item.sentAt),
          proposedDate: this.parseProposedDate(item.proposedDate),
          finalConsultationDate: this.parseSentDate(item.finalConsultationDate),
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
}
