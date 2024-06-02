import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Renderer2 } from '@angular/core';
import * as lottie from 'lottie-web';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
  ValidatorFn, 
  ValidationErrors,
} from '@angular/forms';
import { debounceTime } from 'rxjs';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { ToastService } from 'src/app/core/services/toast.service';
import { invalidDateValidator } from 'src/app/shared/validators/date-validator';
import { passwordStrengthValidator } from 'src/app/shared/validators/password-strength-validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  hideBirthDate:boolean=false;
  passwordHidden: boolean = true;
  passwordErrors = {
    required: false,
    remainingLength: false,
    lowercase: false,
    uppercase: false,
    number: false,
    symbol: false,
  };
  data = {
    email: '',
    pwd: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    birthDate: '',
    role: '',
  };
  passsField: any;
  thefirstNameField: any;
  previousButtonDisabled: boolean = false;
  storedFormData: any;
  verifyingCode: boolean = false;
  codeVerificationError: boolean = false;
  uploadedImage: boolean = false;
  selectedFile: File | undefined;
  signupForm!: FormGroup;
  blurFiles: boolean = false;
  fileList: File[] = [];
  showErrorMessage: boolean = false;
  fileErrorMessage: String = '';
  currentStep: number = 0;
  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private darkModeService: DarkModeService,
    // private renderer: Renderer2,
    private toastService: ToastService

  ) {}
  ngOnInit(): void {
    this.createForm();
    // this.renderer.setStyle(document.body, 'overflow', 'hidden');
    this.disablePreviousButton();
    this.subscribeToPasswordChanges();
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
          Validators.minLength(8),
          passwordStrengthValidator,
        ],
      ],
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
          Validators.pattern(/^[24579][0-9]*$/),
        ],
      ],

      birthDate: this.hideBirthDate ? [''] : ['', [Validators.required, invalidDateValidator]],
      role: ['', Validators.required],
      code: [''],
    });
  }
  selectRole(role: string) {
    this.data.role = role;
  }
  dataURItoBlob(dataURI: string) {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ab], { type: mimeString });
    return blob;
  }

  onSubmit() {
    const formData = new FormData();
    const imageBlob = this.selectedFile
      ? this.selectedFile
      : this.dataURItoBlob('data:image/png;base64,' + this.selectedFile);
    formData.append('image', imageBlob);
    const signupRequest = {
      email: this.signupForm.get('email')?.value,
      pwd: this.signupForm.get('pwd')?.value,
      firstName: this.signupForm.get('firstName')?.value,
      lastName: this.signupForm.get('lastName')?.value,
      phoneNumber: this.signupForm.get('phoneNumber')?.value,
      birthDate: this.signupForm.get('birthDate')?.value,
      role: this.data.role.toUpperCase(),
      code: this.signupForm.get('code')?.value

    };
    formData.append(
      'signupRequest',
      new Blob([JSON.stringify(signupRequest)], { type: 'application/json' })
    );

    console.log(formData);
    this.authService.register(formData).subscribe({
      next: (data: any) => {
        console.log(data)
        this.goToStep(5);
        this.toastService.showToast('success', 'Success! Please check your email for a confirmation message and click on the confirmation link provided.');

      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
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
      this.handleFiles(this.selectedFile!);
    }
    console.log(this.selectedFile);
    this.handleFiles(this.selectedFile!);
    this.uploadedImage = true;
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
    console.log(files);
    if (files.length > 0) {
      this.uploadedImage = true;
      this.selectedFile = files[0];
      this.handleFiles(this.selectedFile);
    }
  }

  handleFiles(file: File) {
    this.blurFiles = true;
    let errorMessage = '';

    const fileType = this.getFileType(file.name);
    const fileErrorMessage = this.getFileTypeErrorMessage(fileType, file.name);
    if (fileErrorMessage) {
      this.selectedFile = undefined;
      errorMessage += fileErrorMessage + '<br>';
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
  removeFile() {
    this.selectedFile = undefined;
    this.uploadedImage = false;
  }
  goToStep(step: number) {
    if (this.currentStep === 0 && this.data.role === 'accountant') {
      this.currentStep = 10;
      this.hideBirthDate=true;
      return;
    }
    if (this.currentStep === 0 && this.data.role === 'accountant') {
      this.hideBirthDate=false;
    }
    if (
      this.currentStep === 0 ||
      this.currentStep === 1 ||
      this.currentStep === 2 ||
      this.currentStep === 3 
    ) {
      this.storedFormData = this.signupForm.value;
    }
    this.currentStep = step;
    if (this.storedFormData) {
      this.signupForm.patchValue(this.storedFormData);
    }
    if(this.currentStep === 5){ this.loadLottieAnimation(); }
  }
  skipUpload() {
    console.log('skip clicked');
    this.uploadedImage = false;
    const defaultFilePath = 'assets/dist/images/user-default.jpeg';
    fetch(defaultFilePath)
      .then((response) => response.blob())
      .then((blob) => {
        const defaultFile = new File([blob], 'default.png', {
          type: 'image/png',
        });
        this.selectedFile = defaultFile;
        this.onSubmit();
      })
      .catch((error) => {
        console.error('Error loading default file:', error);
      });
  }

  verifyCode() {
    this.verifyingCode = true;
    this.codeVerificationError = false;

    setTimeout(() => {
      const enteredCode = this.signupForm.get('code')?.value;
      const expectedCode = 'abcd';

      if (enteredCode === expectedCode) {
        this.goToStep(1);
      } else {
        this.codeVerificationError = true;
        this.previousButtonDisabled = false;
      }
      this.verifyingCode = false;
    }, 2000);
  }

  disablePreviousButton() {
    const codeControl = this.signupForm.get('code');

    if (!codeControl || !codeControl.value || !codeControl.value.length) {
      this.previousButtonDisabled = false;
      return;
    }

    this.previousButtonDisabled = true;
    this.codeVerificationError = false;
  }

  onSecretCodeChange() {
    this.disablePreviousButton();
  }
  onPreviousButtonClick() {
    this.codeVerificationError = false;
  }
  togglePasswordVisibility() {
    this.passwordHidden = !this.passwordHidden;
    const pwdInput = document.getElementById(
      'passwordInput'
    ) as HTMLInputElement;
    if (pwdInput) {
      pwdInput.type = this.passwordHidden ? 'password' : 'text';
    }
  }
  subscribeToPasswordChanges() {
    this.signupForm
      .get('pwd')
      ?.valueChanges.pipe(debounceTime(300))
      .subscribe(() => {
        this.updatePasswordMessage();
      });
  }
  updatePasswordMessage() {
    const passwordControl = this.signupForm.get('pwd');
    if (!passwordControl) return;

    const errors: ValidationErrors | null = passwordControl.errors;

    if (errors) {
      this.passwordErrors.required = errors.hasOwnProperty('required');
      this.passwordErrors.remainingLength =
        errors.hasOwnProperty('remainingLength');
      this.passwordErrors.lowercase = errors.hasOwnProperty('lowercase');
      this.passwordErrors.uppercase = errors.hasOwnProperty('uppercase');
      this.passwordErrors.number = errors.hasOwnProperty('number');
      this.passwordErrors.symbol = errors.hasOwnProperty('symbol');
    } else {
      this.passwordErrors = {
        required: false,
        remainingLength: false,
        lowercase: false,
        uppercase: false,
        number: false,
        symbol: false,
      };
    }
  }
  loadLottieAnimation(): void {
    const animationContainer = document.getElementById('mail-container');
    if (animationContainer) {
      (lottie as any).loadAnimation({
        container: animationContainer,
        renderer: 'svg',
        loop: false,
        autoplay: true,
        path: '/assets/dist/images/mail-animation.json'
      });
    } else {
      console.error("Animation container element not found.");
    }
  }
}
