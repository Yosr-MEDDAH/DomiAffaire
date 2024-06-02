import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  ChangeDetectionStrategy,
  HostListener,
} from '@angular/core';
import { ClientService } from 'src/app/core/services/client.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-my-domiciliation-requests',
  templateUrl: './my-domiciliation-requests.component.html',
  styleUrls: ['./my-domiciliation-requests.component.css'],
})
export class MyDomiciliationRequestsComponent {
  selectedDomiciliation: any;
  isCheckboxChecked: boolean = false;
  isDetailsModalOpen: boolean = false;
  isCancelModalOpen: boolean = false;
  domiciliations: any;
  isAcceptModalOpen: boolean = false;
  requestId: any;
  pdfSrc: string = '';
  obscured: boolean = false;

  constructor(
    private clientService: ClientService,
    private darkModeService: DarkModeService,
    private toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.getClientDomiciliationRequests();
  }
  getClientDomiciliationRequests() {
    this.clientService.getClientDomiciliationRequests().subscribe({
      next: (data: any) => {
        this.domiciliations = data.map((item: any) => ({
          ...item,
          createdAt: item.createdAt ? this.parseSentDate(item.createdAt) : null,
        }));
        console.log(this.domiciliations);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  openCancelOrCloseModal(id?: any) {
    if (id) this.requestId = id;
    this.isCancelModalOpen = !this.isCancelModalOpen;
  }

  confirmCancelRequest() {
    this.clientService.cancelDomiciliationRequest(this.requestId).subscribe({
      next: (data: any) => {
        // console.log(data);
        this.openCancelOrCloseModal();
        this.toastService.showToast('success', data.message);
        this.getClientDomiciliationRequests();
      },
      error: (err: HttpErrorResponse) => {
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
    console.log(item);
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  closeDetailsModal() {
    this.isDetailsModalOpen = !this.isDetailsModalOpen;
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }

  protest(id: any) {}
  openAcceptOrCloseModal() {
    this.isAcceptModalOpen = !this.isAcceptModalOpen;
  }
  viewContract(item: any) {
    this.openAcceptOrCloseModal();
    this.selectedDomiciliation = item;
    this.pdfSrc = item.responseDomiAdmin.draftContract.fileData;
  }

  confirmAccept() {
    if (this.isCheckboxChecked) {
      this.clientService
        .acceptDomiciliationRequest(this.selectedDomiciliation.id)
        .subscribe({
          next: (data: any) => {
            // console.log(data);
            this.getClientDomiciliationRequests();
            this.toastService.showToast('success',data.message);
            this.openAcceptOrCloseModal();
          },
          error: (err: HttpErrorResponse) => {
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
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent) {
    if (
      event.keyCode == 91 ||
      (event.keyCode == 91 && event.shiftKey && event.key === 'S')
    ) {
      this.obscureContent();
    }
  }
  @HostListener('window:keyup', ['$event'])
  handleKeyUp(event: KeyboardEvent) {
    if (event.keyCode == 91 || event.keyCode === 44) {
      this.obscureContent();
    }
  }

  obscureContent() {
    this.obscured = true;
    setTimeout(() => {
      this.obscured = false;
    }, 3000); // Obscure content for 2 seconds
  }
}
