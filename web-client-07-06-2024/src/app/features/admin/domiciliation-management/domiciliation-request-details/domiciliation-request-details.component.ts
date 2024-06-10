import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from 'src/app/core/services/admin.service';
import * as saveAs from 'file-saver';
@Component({    
  selector: 'app-domiciliation-request-details',
  templateUrl: './domiciliation-request-details.component.html',
  styleUrls: ['./domiciliation-request-details.component.css']
})
export class DomiciliationRequestDetailsComponent implements OnInit {
  id: any;
  domiciliationRequestDetails:any
  constructor(
    private adminService:AdminService,
    private activatedRoute: ActivatedRoute,
    private router: Router 
  ) {}
  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id'];
    this.activatedRoute.params.subscribe((params) => {
      console.log(params['id']);
      this.adminService.getDomiciliationRequestDetails(params['id']).subscribe({
        next:(data:any)=>{
          const createdAt=this.parseSentDate(data.createdAt)
          const maskedCIN = this.maskCin(data.cin)
          this.domiciliationRequestDetails = 
          {
            ...data,
            cin:maskedCIN,
            createdAt:createdAt
          };
console.log(this.domiciliationRequestDetails)
        },
        error:(err:HttpErrorResponse)=>{console.log(err);},
      });
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
  downloadFile(item: any) {
    const blob = this.base64toBlob(item.fileData, item.type); // Convert base64 string to Blob
    saveAs(blob, item.name); // Save the Blob as a file using FileSaver.js
  }

  // Method to convert base64 string to Blob
  base64toBlob(base64Data: string, contentType: string) {
    const byteCharacters = atob(base64Data); // Decode base64 string
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
      const slice = byteCharacters.slice(offset, offset + 512);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, { type: contentType }); // Create Blob from byte arrays
  }
  maskCin(cinNumber:any) {
    const cin = cinNumber;
    if (cin && cin.length === 8) {
      return '*'.repeat(5) + cin.slice(-3);
    }
    return cin;
  }
}
