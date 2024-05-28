import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-packs',
  templateUrl: './packs.component.html',
  styleUrls: ['./packs.component.css'],
})
export class PacksComponent implements OnInit {
  packs: any;
  isSlideoverHidden: boolean = true;
  packForm!: FormGroup;
  isEdit: boolean = false;
  currentPackId: string | null = null;
  isDeleteModalOpen: boolean = false;
  toDelete:any;
  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private toastService: ToastService
  ) {}
  ngOnInit(): void {
    this.packForm = this.fb.group({
      designation: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
    });
    this.getPacks();
  }
  confirmModal(id?: any) {
    if(id) this.toDelete=id;
    this.isDeleteModalOpen=!this.isDeleteModalOpen;
  }
  getPacks() {
    this.adminService.getAllPacks().subscribe({
      next: (data: any) => {
        console.log(data);
        this.packs = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  editPack(pack: any) {
    this.isEdit = true;
    this.currentPackId = pack.id;
    this.packForm.patchValue(pack);
    this.toggleSlideover();
  }
  removePack() {
      this.adminService.deletePack(this.toDelete).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'Pack deleted successfully.');
          this.confirmModal()
          this.getPacks();
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
    
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
  onSubmit() {
    if (this.isEdit && this.currentPackId) {
      this.adminService
        .putPack(this.packForm.value, this.currentPackId)
        .subscribe({
          next: (data: any) => {
            this.toastService.showToast(
              'success',
              'Pack updated successfully.'
            );
            this.getPacks();
          },
          error: (err: HttpErrorResponse) => {
            this.handleError(err);
          },
        });
    } else {
      this.adminService.postPack(this.packForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'Pack added successfully.');
          this.getPacks();
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
    }
    this.toggleSlideover();
  }
  cancel() {
    this.packForm.reset();
    this.toggleSlideover();
  }
  handleSlideOverClick(event: Event) {
    if (event.target instanceof HTMLDivElement) {
      if (!(event.target.tagName.toLowerCase() === 'input')) {
        this.toggleSlideover();
      }
    }
  }
  toggleSlideover() {
    this.isSlideoverHidden = !this.isSlideoverHidden;
  }
  getChunks(arr: any[], size: number): any[][] {
    const result = [];
    for (let i = 0; i < arr.length; i += size) {
      result.push(arr.slice(i, i + size));
    }
    return result;
  }
}
