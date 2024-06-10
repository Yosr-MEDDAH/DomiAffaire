import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css'],
})
export class CategoriesComponent implements OnInit {
  categories: any;
  isEdit: boolean = false;
  isAdd: boolean = false;
  currentCategoryID: any;
  categoryForm!: FormGroup;
  toEdit:any
  isDeleteModalOpen: boolean = false;
  images: string[] = [
    'assets/dist/images/random1.jpg',
    'assets/dist/images/random2.jpg',
    'assets/dist/images/random3.jpg',
    'assets/dist/images/random4.jpg',
    'assets/dist/images/random5.jpg',
    'assets/dist/images/random6.avif',
    'assets/dist/images/random7.jpg',
    'assets/dist/images/random8.jpg',
    'assets/dist/images/random9.jpg',
    'assets/dist/images/random10.jpg',
    'assets/dist/images/random11.jpg',
    'assets/dist/images/random12.jpeg',
    'assets/dist/images/random13.jpg',
  ];
  
  constructor(
    private adminService: AdminService,
    private toastService: ToastService,
    private fb: FormBuilder
  ) {}
  
  ngOnInit(): void {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required],
    });
    this.getAllCategories();
  }
  
  getAllCategories() {
    this.adminService.getCategories().subscribe({
      next: (data: any) => {
        console.log(data);
        this.categories = data.map((category: any) => {
          category.randomImages = this.getRandomImages();
          return category;
        });
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  
  getRandomImages(): string[] {
    const shuffled = this.images.sort(() => 0.5 - Math.random());
    return shuffled.slice(0, 3);
  }
  

  removeCategory() {
    this.adminService.deleteCategory(this.currentCategoryID).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'Category deleted successfully.');
        this.confirmModal();
        this.getAllCategories();
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
  }
  
  cancel() {
    this.categoryForm.reset();
    this.isEdit = false;
    this.isAdd = false;
    this.currentCategoryID = null;
  }
  
  confirmModal(id?: any) {
    if (id) this.currentCategoryID = id;
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
  editCategory() {
    this.categoryForm.patchValue(this.toEdit);
  }
  
  onSubmit() {
    if (this.isEdit && this.currentCategoryID) {
      this.adminService.updateCategory(this.currentCategoryID,this.categoryForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'Category updated successfully.');
          this.getAllCategories();
          this.closeModal();
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
    } else {
      this.adminService.addCategory(this.categoryForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'Category added successfully.');
          this.getAllCategories();
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
      this.categoryForm.reset();
    } else if (action === 'edit') {
      this.isEdit = true;
      this.isAdd = false;
      this.toEdit = item;
      this.currentCategoryID = item.id;
      this.editCategory();
    }
  }
  
  closeModal() {
    this.isEdit = false;
    this.isAdd = false;
    this.currentCategoryID = null;
    this.categoryForm.reset();
  }
}
