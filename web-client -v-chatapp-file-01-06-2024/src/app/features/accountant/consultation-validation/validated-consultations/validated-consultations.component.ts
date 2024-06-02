import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-validated-consultations',
  templateUrl: './validated-consultations.component.html',
  styleUrls: ['./validated-consultations.component.css']
})
export class ValidatedConsultationsComponent implements OnInit {
  validatedConsultations: any;
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
    this.getInProgressConsultationRequests();
  }
  getInProgressConsultationRequests() {
    this.accountantService.getAcceptedOrRejectedConsultation().subscribe({
      next: (data: any) => {
        console.log(data);
        this.validatedConsultations = data.map((item: any) => ({
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
}
