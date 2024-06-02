import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  toastVisible: boolean = false;
  toastMessage: string = '';
  toastType: string = '';

  constructor() {}
  showToast(type: string, message: string): void {
    this.toastType = type;
    this.toastMessage = message;
    this.toastVisible = true;

    setTimeout(() => {
      this.hideToast();
    }, 5000);
  }

  hideToast(): void {
    this.toastVisible = false;
  }
}
