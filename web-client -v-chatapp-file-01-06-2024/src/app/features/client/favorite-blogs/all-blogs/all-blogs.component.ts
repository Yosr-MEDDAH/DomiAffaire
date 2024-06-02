import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/core/services/client.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-all-blogs',
  templateUrl: './all-blogs.component.html',
  styleUrls: ['./all-blogs.component.css'],
})
export class AllBlogsComponent implements OnInit {
  blogs: any[] = [];
  savedBlogs: any[] = [];
  constructor(
    private clientService: ClientService,
    private cdr: ChangeDetectorRef,
    private toastService:ToastService
  ) {}
  ngOnInit(): void {
    this.getAllBlogs();
    this.getSavedBlogs();
  }
  getAllBlogs() {
    this.clientService.getAllBlogs().subscribe({
      next: (data: any) => {
        console.log(data);
        this.blogs = data;
        this.blogs = data.map((item: any) => ({
          ...item,
          createdAt: this.parseSentDate(item.createdAt),
        }));
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
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
  parseSentDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute, second, milliseconds] =
      dateTimeArray;
    return new Date(
      year,
      month - 1,
      day,
      hour,
      minute,
      second,
      milliseconds / 1000000
    );
  }
  saveBlog(id: any) {
    this.clientService.saveBlog(id).subscribe({
      next: (data: any) => {
        this.savedBlogs.push({ id }); // Ensure you push the correct structure of data
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
    return this.savedBlogs.some((blog) => blog.id === id); // Check if the blog is in the saved blogs list
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
