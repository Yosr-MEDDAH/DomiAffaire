import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-faqs',
  templateUrl: './faqs.component.html',
  styleUrls: ['./faqs.component.css']
})
export class FAQsComponent implements OnInit{
  isDropdownOpen: { [key: string]: boolean } = {};  faq: any;
  isEdit: boolean = false;
  isAdd: boolean = false;
  currentFAQid: any;
  faqForm!: FormGroup;
  toEdit:any
  isDeleteModalOpen: boolean = false;

  
  constructor(
    private adminService: AdminService,
    private toastService: ToastService,
    private fb: FormBuilder
  ) {}
  
  ngOnInit(): void {
    this.faqForm = this.fb.group({
      question: ['', Validators.required],
      answer: ['', Validators.required],
    });
    this.getAllfaq();
  }
  
  getAllfaq() {
    this.adminService.getFAQs().subscribe({
      next: (data: any) => {
        console.log(data);
        this.faq = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }


  removeFAQ() {
    this.adminService.deleteFAQ(this.currentFAQid).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'FAQ deleted successfully.');
        this.confirmModal();
        this.getAllfaq();
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
  }
  
  cancel() {
    this.faqForm.reset();
    this.isEdit = false;
    this.isAdd = false;
    this.currentFAQid = null;
  }
  
  confirmModal(id?: any) {
    if (id) this.currentFAQid = id;
    this.isDeleteModalOpen = !this.isDeleteModalOpen;
  }
  
  private handleError(err: HttpErrorResponse) {
    let errorMessage = 'An error occurred: ';
    for (const key in err.error) {
      if (err.error.hasOwnProperty(key)) {
        errorMessage += `${err.error[key]} `;
      }
    }
    this.toastService.showToast('error', errorMessage);
  }
  editFAQ() {
    this.faqForm.patchValue(this.toEdit);
  }
  
  onSubmit() {
    if (this.isEdit && this.currentFAQid) {
      this.adminService.updateFAQ(this.currentFAQid,this.faqForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'FAQ updated successfully.');
          this.getAllfaq();
          this.closeModal();
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
    } else {
      this.adminService.addFAQ(this.faqForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'FAQ added successfully.');
          this.getAllfaq();
          this.closeModal();
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
    }
  }
  
  openModal(action: 'add' | 'edit', item?: any) {
    if (action === 'add') {
      this.isEdit = false;
      this.isAdd = true;
      this.faqForm.reset();
    } else if (action === 'edit') {
      this.isEdit = true;
      this.isAdd = false;
      this.toEdit = item;
      this.currentFAQid = item.id;
      this.editFAQ();
    }
  }
  
  closeModal() {
    this.isEdit = false;
    this.isAdd = false;
    this.currentFAQid = null;
    this.faqForm.reset();
  }
  toggleAccordion(id: any): void {
    const element = document.getElementById('collapse' + id);
    element?.parentElement?.classList.toggle('active');
  }
  toggleDropdown(id: any) {
    this.isDropdownOpen[id] = !this.isDropdownOpen[id];
  }

}



