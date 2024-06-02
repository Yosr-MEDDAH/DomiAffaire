import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import * as saveAs from 'file-saver';
import { ClientService } from 'src/app/core/services/client.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-company-creation',
  templateUrl: './company-creation.component.html',
  styleUrls: ['./company-creation.component.css'],
})
export class CompanyCreationComponent implements OnInit {
  files:any
  constructor(
    private darkModeService: DarkModeService,
    private clientService: ClientService
  ) {}
  ngOnInit(): void {
    this.getCompanyCreationDocuments();
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  getCompanyCreationDocuments() {
    this.clientService.getAllFiles().subscribe({
      next: (data: any) => {
        console.log(data);
        this.files=data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  getFileIcon(fileType: string): string {
    const iconMappings: { [key: string]: string } = {
      'application/pdf': 'fas fa-file-pdf',
      'image/jpeg': 'fas fa-file-image',
      'application/msword': 'fas fa-file-word',
    };
    return iconMappings[fileType] || 'fas fa-file';
  }
  downloadFile(item: any) {
    const blob = this.base64toBlob(item.fileData, item.type); // Convert base64 string to Blob
    saveAs(blob, item.name); // Save the Blob as a file using FileSaver.js
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
}
