import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-company-creation-documents',
  templateUrl: './company-creation-documents.component.html',
  styleUrls: ['./company-creation-documents.component.css'],
})
export class CompanyCreationDocumentsComponent implements OnInit {
  isDeleteModalOpen: boolean = false;
  fileToDeleteId: any;
  oldFileName!: string;
  fileToUpdateId: any;
  fileToUpdate: any;
  isAddModalOpen: boolean = false;
  isUpdateModalOpen: boolean = false;
  file!: File;
  selectedFile: File | undefined;
  files: any;
  constructor(
    private adminService: AdminService,
    private darkModeService: DarkModeService,
    private toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.getAllFiles();
  }
  getAllFiles() {
    this.adminService.getAllFiles().subscribe({
      next: (data: any) => {
        console.log(data);
        this.files = data;
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
  openAddModal() {
    this.isAddModalOpen = !this.isAddModalOpen;
  }
  closeUpdateModal() {
    this.isUpdateModalOpen = !this.isUpdateModalOpen;
  }
  async openUpdateModal() {
    try {
      await this.getOneFile(this.fileToUpdateId);
      console.log(this.fileToUpdate);
      if (this.fileToUpdate) {
        this.oldFileName = this.fileToUpdate.name;
        this.selectedFile = this.fileToUpdate;
      }
      this.isUpdateModalOpen = !this.isUpdateModalOpen;
    } catch (error) {
      console.error('Error fetching file:', error);
    }
  }

  getOneFile(id: any): Promise<void> {
    return new Promise((resolve, reject) => {
      this.adminService.getFileById(id).subscribe({
        next: (data: any) => {
          console.log(data);
          this.fileToUpdate = data;
          resolve();
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
          reject(err);
        },
      });
    });
  }

  getUpdateId(fileId: any) {
    this.fileToUpdateId = fileId;
    this.openUpdateModal();
  }
  submitAddForm() {
    if (!this.selectedFile) {
      this.openAddModal();
      this.toastService.showToast('warning', 'No file selected.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    console.log(this.selectedFile);
    this.adminService.uploadFile(formData).subscribe({
      next: (response: any) => {
        // console.log('File uploaded successfully:', response);
        this.toastService.showToast('success', response.message);
        this.openAddModal();
        this.getAllFiles();
        this.selectedFile = undefined;
      },
      error: (err: HttpErrorResponse) => {
        this.openAddModal();
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

  onFileSelected(event: any) {
    const files = event.target.files;
    if (files.length > 0) {
      this.selectedFile = files[0];
    }
    console.log(this.selectedFile);
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }

  renameFile(name: string, id: any) {
    let data = { name: name };
    this.adminService.renameFile(data, id).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'Name updated successfully.');
        this.getAllFiles();
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
  submitUpdateForm() {
    if (!this.selectedFile) {
      this.closeUpdateModal();
      this.toastService.showToast('warning', 'No file selected.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    console.log(this.selectedFile);
    this.adminService.updateFile(formData, this.fileToUpdateId).subscribe({
      next: (response: any) => {
        this.toastService.showToast('success', 'File updated successfully.');
        this.closeUpdateModal();
        this.getAllFiles();
        this.selectedFile = undefined;
      },
      error: (err: HttpErrorResponse) => {
        this.closeUpdateModal();
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
  confirmDeleteFile(fileId: any) {
    this.fileToDeleteId = fileId;
    this.isDeleteModalOpen = true;
  }

  deleteFile() {
    this.adminService.deleteFile(this.fileToDeleteId).subscribe({
      next: (data: any) => {
        console.log(data);
        this.toastService.showToast('success', data.message);
  
        // Remove the deleted file from the files array
        this.files = this.files.filter((file: { id: any; }) => file.id !== this.fileToDeleteId);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
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
      complete: () => {
        // After deletion is complete, close the modal
        this.isDeleteModalOpen = false;
      }
    });
  }
  

  cancelDeleteFile() {
    this.isDeleteModalOpen = false;
  }
}
