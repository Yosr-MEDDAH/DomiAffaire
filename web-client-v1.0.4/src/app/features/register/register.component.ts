import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Renderer2 } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { invalidDateValidator } from 'src/app/shared/validators/date-validator';
import { strongPasswordValidator } from 'src/app/shared/validators/password-strength-validator';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  thefirstNameField:any
  previousButtonDisabled: boolean = false;
  storedFormData: any;
  verifyingCode: boolean = false;
  codeVerificationError: boolean = false;
  uploadedImage: boolean = false;
  selectedFile!: File;
  signupForm!: FormGroup;
  blurFiles: boolean = false;
  fileList: File[] = [];
  showErrorMessage: boolean = false;
  fileErrorMessage: String = '';
  currentStep: number = 1;
  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private darkModeService: DarkModeService,
    private renderer: Renderer2
  ) {}
  ngOnInit(): void {
    this.createForm();
    this.renderer.setStyle(document.body, 'overflow', 'hidden');
    this.disablePreviousButton();
  }

  createForm() {
    this.signupForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/),
        ],
      ],
      pwd: [
        '',
        [
          Validators.required,
          // strongPasswordValidator(this.thefirstNameField)
        ],
      ],
      confirmPassword: [
        '',
        [
          Validators.required,
          // this.confirmPasswordValidator(), 
        ],
      ],
      // image: ['', [Validators.required]],
      firstName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/),
        ],
      ],
      phoneNumber: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(8),
          Validators.pattern(/^[0-9]*$/),
        ],
      ],

      birthDate: ['', [Validators.required, invalidDateValidator]],
      smsCode: [''],
    });
    this.signupForm.get('password')!.setValidators([
      Validators.required,
    ]);
  
    this.signupForm.get('confirmPassword')!.setValidators([
      Validators.required,
    ]);
  }

  
  onSubmit() {
    if (this.uploadedImage) {
      console.log('Submitting form with image...');
      this.goToStep(6);
    } else {
      console.log('Submitting form without image...');
      this.goToStep(6);
    }
    // const signupRequest = {
    //   email: this.signupForm.get('email')?.value,
    //   pwd: this.signupForm.get('pwd')?.value,
    //   firstName: this.signupForm.get('firstName')?.value,
    //   lastName: this.signupForm.get('lastName')?.value,
    //   phoneNumber: this.signupForm.get('phoneNumber')?.value,
    //   birthDate: this.signupForm.get('birthDate')?.value,
    // };

    // const formData = new FormData();
    // formData.append('image', this.selectedFile);
    // formData.append(
    //   'signupRequest',
    //   new Blob([JSON.stringify(signupRequest)], { type: 'application/json' })
    // );
    // this.authService.register(formData).subscribe({
    //   next: (data: any) => {
    //     console.log(data);
    //   },
    //   error: (err: HttpErrorResponse) => {
    //     console.log(err);
    //   },
    // });
  }
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();

    const files = event.dataTransfer!.files;
    this.handleFiles(files);
  }

  handleFileInput(event: any) {
    const files = event.target.files;
    this.handleFiles(files);
    this.uploadedImage = true;
  }

  handleFiles(files: FileList) {
    this.blurFiles = true;
    let errorMessage = '';
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const fileType = this.getFileType(file.name);
      const fileErrorMessage = this.getFileTypeErrorMessage(
        fileType,
        file.name
      );
      if (fileErrorMessage) {
        errorMessage += fileErrorMessage + '<br>';
      } else {
        const fileObject = new File([file], file.name, { type: file.type });
        this.fileList.push(fileObject);
      }
    }
    this.fileErrorMessage = errorMessage;
    if (errorMessage) {
      this.showErrorMessage = true;
      setTimeout(() => {
        this.showErrorMessage = false;
      }, 5000);
    }
    setTimeout(() => {
      this.blurFiles = false;
    }, 1000);
  }

  getFileTypeErrorMessage(fileType: string, fileName: string): string {
    if (fileType === 'other') {
      return `The file type is not acceptable for : ${fileName}`;
    } else {
      return '';
    }
  }

  getFileType(fileName: string): string {
    const extension = fileName.split('.').pop()!.toLowerCase();
    if (this.isImage(extension)) {
      return 'image';
    } else if (extension === 'pdf') {
      return 'pdf';
    } else {
      return 'other';
    }
  }

  isImage(extension: string): boolean {
    return ['jpg', 'jpeg', 'png'].includes(extension);
  }

  formatBytes(bytes: number) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
  getImgSrc(file: File): string {
    return URL.createObjectURL(file);
  }

  isImageType(fileType: string): boolean {
    return fileType.startsWith('image/');
  }

  isOtherType(fileType: string): boolean {
    console.log(
      !this.isImageType(fileType) &&
        fileType !== 'application/pdf' &&
        fileType !== 'application/msword' &&
        fileType !== 'application/zip'
    );
    return (
      !this.isImageType(fileType) &&
      fileType !== 'application/pdf' &&
      fileType !== 'application/msword' &&
      fileType !== 'application/zip'
    );
  }
  removeFile(index: number) {
    this.fileList.splice(index, 1);
  }
  goToStep(step: number) {
    if (this.currentStep === 1 || this.currentStep === 2) {
      // if (this.signupForm) {
      this.storedFormData = this.signupForm.value;
      // }
    }
    this.currentStep = step;
    if (this.storedFormData) {
      this.signupForm.patchValue(this.storedFormData);
    }
  }
  skipUpload() {
    this.uploadedImage = false;
    this.goToStep(6);
  }

  verifyCode() {
    this.verifyingCode = true;
    this.codeVerificationError = false;

    setTimeout(() => {
      const enteredCode = this.signupForm.get('smsCode')?.value;
      const expectedCode = '1234';

      if (enteredCode === expectedCode) {
        this.goToStep(4);
      } else {
        this.codeVerificationError = true;
        this.previousButtonDisabled = false;
      }
      this.verifyingCode = false;
    }, 2000);
  }
  

  disablePreviousButton() {
    const codeControl = this.signupForm.get('smsCode');

    if (!codeControl || !codeControl.value || !codeControl.value.length) {
      this.previousButtonDisabled = false;
      return;
    }

    this.previousButtonDisabled = true;
    this.codeVerificationError = false;
  }

  onSmsCodeChange() {
    this.disablePreviousButton();
  }
  onPreviousButtonClick() {
    this.codeVerificationError = false;
  }
}
