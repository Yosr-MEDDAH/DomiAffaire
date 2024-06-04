import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from 'src/app/core/services/client.service';
import { ToastService } from 'src/app/core/services/toast.service';
import { UsersService } from 'src/app/core/services/users.service';

@Component({
  selector: 'app-domiciliation-request',
  templateUrl: './domiciliation-request.component.html',
  styleUrls: ['./domiciliation-request.component.css'],
})
export class DomiciliationRequestComponent implements OnInit {
  packs: any;
  selectedCINFile!: File;
  selectedDenominationFile!: File;
  selectedExtractRneFile!: File;
  selectedPvChangeAddressFile!: File;
  selectedOldBusinessLicenceFile!: File;
  selectedOldExDeclaFile!: File;
  blogs: any;
  domiciliationForm!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private toastService: ToastService,
    private userServicce:UsersService
  ) {}
  ngOnInit(): void {
    this.initForm();
    this.getDomiciliationPacks();
    this.getAllBlogs();
  }
  initForm() {
    this.domiciliationForm = this.fb.group({
      legalForm: ['', Validators.required],
      companyStatus: ['', Validators.required],
      cin: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(8),
          Validators.pattern(/^[0-9]*$/),
        ],
      ],
      cinFile: ['', Validators.required],
      denomination: ['', Validators.required],
      denominationFile: ['', Validators.required],
      pack: ['', Validators.required],
      paymentMode: ['', Validators.required],
      draftStatus: [''],
      shareCapital: [''],
      management: [''],
      extractRNE: [''],
      oldDraftStatus: [''],
      oldLegalForm: [''],
      oldShareCapital: [''],
      oldManagement: [''],
      pvChangeAddress: [''],
      oldBusinessLicence: [''],
      oldExistenceDeclaration: [''],
    });
  }
  submitForm() {
    console.log(this.domiciliationForm.value);
    const formData = new FormData();
    formData.append('cin', this.selectedCINFile);
    formData.append('denomination', this.selectedDenominationFile);
    if (this.domiciliationForm.get('legalForm')?.value === 'Natural person') {
      const domiciliation = {
        cin: this.domiciliationForm.get('cin')?.value,
        denomination: this.domiciliationForm.get('denomination')?.value,
        companyStatus: this.domiciliationForm.get('companyStatus')?.value,
        pack: this.domiciliationForm.get('pack')?.value,
        paymentMode: this.domiciliationForm.get('paymentMode')?.value,
      };
      formData.append(
        'domiciliation',
        new Blob([JSON.stringify(domiciliation)], { type: 'application/json' })
      );
      this.clientService.sendDomiciliationPP(formData).subscribe({
        next: (data: any) => {
          // console.log(data);
          this.domiciliationForm.reset();
          this.domiciliationForm.controls['draftStatus'].setValue('');
          this.domiciliationForm.controls['oldDraftStatus'].setValue('');
          this.toastService.showToast(
            'success',
            'Domiciliation Request Sent Successfully'
          );
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
    } else if (
      this.domiciliationForm.get('legalForm')?.value === 'Corporation' &&
      this.domiciliationForm.get('companyStatus')?.value === 'In Process'
    ) {
      formData.append('extractRNE', this.selectedExtractRneFile);
      const domiciliation = {
        cin: this.domiciliationForm.get('cin')?.value,
        denomination: this.domiciliationForm.get('denomination')?.value,
        companyStatus: this.domiciliationForm.get('companyStatus')?.value,
        draftStatus: this.domiciliationForm.get('draftStatus')?.value,
        shareCapital: this.domiciliationForm.get('shareCapital')?.value,
        management: this.domiciliationForm.get('management')?.value,
        pack: this.domiciliationForm.get('pack')?.value,
        paymentMode: this.domiciliationForm.get('paymentMode')?.value,
      };
      formData.append(
        'domiciliation',
        new Blob([JSON.stringify(domiciliation)], { type: 'application/json' })
      );
      this.clientService.sendDomiciliationPMInProcess(formData).subscribe({
        next: (data: any) => {
          this.domiciliationForm.reset();
          this.domiciliationForm.controls['draftStatus'].setValue('');
          this.domiciliationForm.controls['oldDraftStatus'].setValue('');
          this.toastService.showToast(
            'success',
            'Domiciliation Request Sent Successfully'
          );
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
    } else if (
      this.domiciliationForm.get('legalForm')?.value === 'Corporation' &&
      this.domiciliationForm.get('companyStatus')?.value === 'Transfer'
    ) {
      formData.append('extractRNE', this.selectedExtractRneFile);
      formData.append('pvChangeAddress', this.selectedPvChangeAddressFile);
      formData.append(
        'oldBusinessLicence',
        this.selectedOldBusinessLicenceFile
      );
      formData.append('oldExistenceDeclaration', this.selectedOldExDeclaFile);
      const domiciliation = {
        cin: this.domiciliationForm.get('cin')?.value,
        denomination: this.domiciliationForm.get('denomination')?.value,
        companyStatus: this.domiciliationForm.get('companyStatus')?.value,
        draftStatus: this.domiciliationForm.get('draftStatus')?.value,
        shareCapital: this.domiciliationForm.get('shareCapital')?.value,
        management: this.domiciliationForm.get('management')?.value,
        pack: this.domiciliationForm.get('pack')?.value,
        paymentMode: this.domiciliationForm.get('paymentMode')?.value,
        oldDraftStatus: this.domiciliationForm.get('oldDraftStatus')?.value,
        oldLegalForm: this.domiciliationForm.get('oldLegalForm')?.value,
        oldShareCapital: this.domiciliationForm.get('oldShareCapital')?.value,
        oldManagement: this.domiciliationForm.get('oldManagement')?.value,
      };
      formData.append(
        'domiciliation',
        new Blob([JSON.stringify(domiciliation)], { type: 'application/json' })
      );
      this.clientService.sendDomiciliationPMTransfer(formData).subscribe({
        next: (data: any) => {
          this.domiciliationForm.reset();
          this.domiciliationForm.controls['draftStatus'].setValue('');
          this.domiciliationForm.controls['oldDraftStatus'].setValue('');
          this.toastService.showToast(
            'success',
            'Domiciliation Request Sent Successfully'
          );
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
  onFileSelected(event: any, fileName: any) {
    const files = event.target.files;
    if (files.length > 0) {
      if (fileName === 'cin') {
        this.selectedCINFile = files[0];
        console.log(this.selectedCINFile);
      }
      if (fileName === 'denomination') {
        this.selectedDenominationFile = files[0];
        console.log(this.selectedDenominationFile);
      }
      if (fileName === 'extractRne') {
        this.selectedExtractRneFile = files[0];
        console.log(this.selectedExtractRneFile);
      }
      if (fileName === 'pvChangeAddress') {
        this.selectedPvChangeAddressFile = files[0];
        console.log(this.selectedPvChangeAddressFile);
      }
      if (fileName === 'oldBussinessLicence') {
        this.selectedOldBusinessLicenceFile = files[0];
        console.log(this.selectedOldBusinessLicenceFile);
      }
      if (fileName === 'oldExistenceDeclaration') {
        this.selectedOldExDeclaFile = files[0];
        console.log(this.selectedOldExDeclaFile);
      }
    }
    // console.log(this.selectedFile);
  }
  getDomiciliationPacks() {
    this.clientService.getPacks().subscribe({
      next: (data: any) => {
        console.log(data);
        this.packs = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  onLegalFormChange() {
    this.updateValidators();
  }

  onCompanyStatusChange() {
    this.updateValidators();
  }

  updateValidators() {
    const legalForm = this.domiciliationForm.get('legalForm')?.value;
    const companyStatus = this.domiciliationForm.get('companyStatus')?.value;

    if (legalForm === 'Corporation') {
      if (companyStatus === 'In Process') {
        this.setRequiredValidatorsForInProcess();
      } else if (companyStatus === 'Transfer') {
        this.setRequiredValidatorsForTransfer();
      }
    } else {
      this.clearValidators();
    }

    this.updateFormValidity();
  }

  private setRequiredValidatorsForInProcess() {
    this.domiciliationForm
      .get('draftStatus')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('shareCapital')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('management')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('extractRNE')
      ?.setValidators([Validators.required]);
    this.clearValidatorsForTransferFields();
  }

  private setRequiredValidatorsForTransfer() {
    this.domiciliationForm
      .get('draftStatus')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('shareCapital')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('management')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('extractRNE')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('oldDraftStatus')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('oldLegalForm')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('oldShareCapital')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('oldManagement')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('pvChangeAddress')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('oldBusinessLicence')
      ?.setValidators([Validators.required]);
    this.domiciliationForm
      .get('oldExistenceDeclaration')
      ?.setValidators([Validators.required]);
  }

  private clearValidators() {
    const fields = [
      'draftStatus',
      'shareCapital',
      'management',
      'extractRNE',
      'oldDraftStatus',
      'oldLegalForm',
      'oldShareCapital',
      'oldManagement',
      'pvChangeAddress',
      'oldBusinessLicence',
      'oldExistenceDeclaration',
    ];

    fields.forEach((fieldName) => {
      this.domiciliationForm.get(fieldName)?.clearValidators();
    });
  }

  private clearValidatorsForTransferFields() {
    const fields = [
      'oldDraftStatus',
      'oldLegalForm',
      'oldShareCapital',
      'oldManagement',
      'pvChangeAddress',
      'oldBusinessLicence',
      'oldExistenceDeclaration',
    ];

    fields.forEach((fieldName) => {
      this.domiciliationForm.get(fieldName)?.clearValidators();
    });
  }

  private updateFormValidity() {
    const fields = [
      'draftStatus',
      'shareCapital',
      'management',
      'extractRNE',
      'oldDraftStatus',
      'oldLegalForm',
      'oldShareCapital',
      'oldManagement',
      'pvChangeAddress',
      'oldBusinessLicence',
      'oldExistenceDeclaration',
    ];

    fields.forEach((fieldName) => {
      this.domiciliationForm.get(fieldName)?.updateValueAndValidity();
    });
  }

  getAllBlogs() {
    this.userServicce.getAllBlogs().subscribe({
      next: (data: any) => {
        console.log(data)
        this.blogs = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
