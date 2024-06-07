import { HttpErrorResponse } from '@angular/common/http';
import { Component, HostListener, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-clients-documents',
  templateUrl: './clients-documents.component.html',
  styleUrls: ['./clients-documents.component.css'],
})
export class ClientsDocumentsComponent implements OnInit {
  isDropdownOpen: { [key: string]: boolean } = {};
  domiciliationDocs: any;
  constructor(private adminService: AdminService) {}
  ngOnInit(): void {
    this.getDomiciliationDocuments();
  }
  getDomiciliationDocuments() {
    this.adminService.domiciliationDocuments().subscribe({
      next: (data: any) => {
        console.log(data);
        this.domiciliationDocs = data;
        this.domiciliationDocs.forEach((doc: any) => {
          this.isDropdownOpen[doc.id] = false;
        });
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }

  toggleDropdown(id: any) {
    this.isDropdownOpen[id] = !this.isDropdownOpen[id];
  }
  closeMenu() {
    Object.keys(this.isDropdownOpen).forEach((key) => {
      this.isDropdownOpen[key] = false;
    });
  }

}
