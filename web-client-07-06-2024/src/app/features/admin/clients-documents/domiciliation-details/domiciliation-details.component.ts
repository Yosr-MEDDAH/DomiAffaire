import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from 'src/app/core/services/admin.service';
import * as saveAs from 'file-saver';
@Component({
  selector: 'app-domiciliation-details',
  templateUrl: './domiciliation-details.component.html',
  styleUrls: ['./domiciliation-details.component.css'],
})
export class DomiciliationDetailsComponent implements OnInit {
  domiId: any;
  domiciliation:any
  constructor(
    private adminService: AdminService,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.domiId = this.route.snapshot.paramMap.get('id');
    if (this.domiId) {
      this.getDomiDetails(this.domiId);
    }
  }
  getDomiDetails(id: any) {
    this.adminService.getDomiciliationRequestDetails(id).subscribe({
      next: (data: any) => {
        console.log(data);
        this.domiciliation=data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  openFile(file: any) {
    const byteCharacters = atob(file.fileData);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: file.type });
    const blobUrl = URL.createObjectURL(blob);

    // Open the file in a new tab
    window.open(blobUrl, '_blank');
  }
}
