import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-add-files',
  templateUrl: './add-files.component.html',
  styleUrls: ['./add-files.component.css'],
})
export class AddFilesComponent implements OnInit {
  domiId: any;
  selectedFile: File | undefined;
  addForm!: FormGroup;
  isModalOpen: boolean = false;
  modalTitle: string = '';
  actionType: 'patente' | 'ed' | 'contract' | undefined;

  constructor(
    private adminService: AdminService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.domiId = this.route.snapshot.paramMap.get('id');
    this.addForm = this.fb.group({
      file: [null]
    });
  }

  openModal(action: 'patente' | 'ed' | 'contract') {
    this.actionType = action;
    this.isModalOpen = true;
    switch (action) {
      case 'contract':
        this.modalTitle = 'Ajouter le Contrat';
        break;
      case 'ed':
        this.modalTitle = "Ajouter la Déclaration d'Existence";
        break;
      case 'patente':
        this.modalTitle = 'Ajouter la Patente';
        break;
    }
  }

  closeModal() {
    this.isModalOpen = false;
    this.addForm.reset();
  }

  onSubmit() {
    if (!this.selectedFile) {
      this.toastService.showToast('warning', 'No file selected.');
      return;
    }
    const formData = new FormData();
    formData.append(this.actionType as string, this.selectedFile);

    let serviceCall:any;
    switch (this.actionType) {
      case 'contract':
        serviceCall = this.adminService.addContract(this.domiId, formData);
        break;
      case 'ed':
        serviceCall = this.adminService.addExistenceDec(this.domiId, formData);
        break;
      case 'patente':
        serviceCall = this.adminService.addPatente(this.domiId, formData);
        break;
    }

    serviceCall.subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'File added successfully.');
        this.closeModal();
      },
      error: (err: HttpErrorResponse) => {
        this.toastService.showToast('error', 'An error occurred.');
        console.log(err);
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
}
