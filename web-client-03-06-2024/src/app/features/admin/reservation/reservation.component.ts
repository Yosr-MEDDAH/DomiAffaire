import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css'],
})
export class ReservationComponent implements OnInit {
  reservations:any
  currentRequestID:any
  isRejectModalopen: boolean = false;
  isAcceptModalopen: boolean = false;
  constructor(private adminService: AdminService, private toastService:ToastService) {}
  ngOnInit(): void {
    this.getReservations();
  }
  getReservations() {
    this.adminService.getReservations().subscribe({
      next: (data: any) => {
        console.log(data)
        this.reservations = data.map((item: any) => ({
          ...item,
          createdAt: item.createdAt ? this.parseSentDate(item.createdAt) : null,
          dateBegining: item.dateBegining ? this.parseOtherDate(item.dateBegining) : null,
          dateEnding: item.dateEnding ? this.parseOtherDate(item.dateEnding) : null,
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
  parseOtherDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute] =
      dateTimeArray;
    return new Date(
      year,
      month - 1,
      day,
      hour,
      minute
    );
  }

  confirmModal(id?: any) {
    if (id) this.currentRequestID = id;
    this.isRejectModalopen = !this.isRejectModalopen;
  }
  confirmAcceptModal(id?: any) {
    if (id) this.currentRequestID = id;
    this.isAcceptModalopen = !this.isAcceptModalopen;
  }
  rejectRequest() {
    this.adminService.rejectReservation(this.currentRequestID).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'Request rejected successfully.');
        this.confirmModal();
        this.getReservations();
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
  }
  acceptRequest() {
    this.adminService.acceptReservation(this.currentRequestID).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'Request accepted successfully.');
        this.confirmAcceptModal();
        this.getReservations();
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
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
