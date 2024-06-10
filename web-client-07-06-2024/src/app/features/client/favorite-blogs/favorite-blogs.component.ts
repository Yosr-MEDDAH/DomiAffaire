import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/core/services/client.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-favorite-blogs',
  templateUrl: './favorite-blogs.component.html',
  styleUrls: ['./favorite-blogs.component.css']
})
export class FavoriteBlogsComponent implements OnInit {
  savedBlogs: any[] = [];
  constructor(
    private clientService: ClientService,
    private cdr: ChangeDetectorRef,
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.getSavedBlogs();
  }

  getSavedBlogs() {
    this.clientService.getSavedBlogs().subscribe({
      next: (data: any) => {
        this.savedBlogs = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }

  saveBlog(id: any) {
    this.clientService.saveBlog(id).subscribe({
      next: (data: any) => {
        this.savedBlogs.push({ id }); 
        this.cdr.detectChanges();
        this.toastService.showToast("success",'Blog saved successfully')
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
  }

  unsaveBlog(id: any) {
    this.clientService.unSaveBlog(id).subscribe({
      next: (data: any) => {
        this.savedBlogs = this.savedBlogs.filter((blog) => blog.id !== id); 
        this.toastService.showToast("warning",'Blog unsaved successfully')

      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
  }
  isBlogSaved(id: any): boolean {
    return this.savedBlogs.some((blog) => blog.id === id); 
  }

  toggleSave(blog: any) {
    if (this.isBlogSaved(blog.id)) {
      this.unsaveBlog(blog.id);
    } else {
      this.saveBlog(blog.id);
    }
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
}
