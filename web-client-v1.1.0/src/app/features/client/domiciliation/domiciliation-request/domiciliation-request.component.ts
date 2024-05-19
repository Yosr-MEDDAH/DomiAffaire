import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from 'src/app/core/services/client.service';

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

  domiciliationForm!: FormGroup;
  constructor(private fb: FormBuilder, private clientService: ClientService) {}
  ngOnInit(): void {
    this.initForm();
    this.getDomiciliationPacks();
  }
  initForm() {
    this.domiciliationForm = this.fb.group({
      legalForm: ['', Validators.required],
      companyStatus: ['', Validators.required],
      cin: [
        '',
        [Validators.required, Validators.minLength(8), Validators.maxLength(8)],
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
          console.log(data);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
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
          console.log(data);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
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
          console.log(data);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
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
}
