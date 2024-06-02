import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { AdminService } from 'src/app/core/services/admin.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-blogs',
  templateUrl: './blogs.component.html',
  styleUrls: ['./blogs.component.css'],
})
export class BlogsComponent implements OnInit {
  Blogs: any;
  currentBlogID: any;
  isDeleteModalOpen: boolean = false;
  editorConfig: AngularEditorConfig = {
    editable: true,
    // spellcheck: true,
    height: 'auto',
    minHeight: '0',
    maxHeight: 'auto',
    width: 'auto',
    minWidth: '0',
    translate: 'yes',
    // enableToolbar: true,
    // showToolbar: true,
    // placeholder: 'Enter text here...',
    defaultParagraphSeparator: '',
    defaultFontName: '',
    defaultFontSize: '',
    fonts: [
      { class: 'arial', name: 'Arial' },
      { class: 'times-new-roman', name: 'Times New Roman' },
      { class: 'calibri', name: 'Calibri' },
      { class: 'comic-sans-ms', name: 'Comic Sans MS' },
    ],
    sanitize: true,
    toolbarPosition: 'top',
    toolbarHiddenButtons: [
      [
        'strikeThrough',
        'subscript',
        'superscript',
        'justifyLeft',
        'justifyCenter',
        'justifyRight',
        'justifyFull',
        'indent',
        'outdent',
        'insertUnorderedList',
        'insertOrderedList',
        'heading',
        // 'fontName'
      ],
      [
        // 'fontSize',
        // 'textColor',
        'backgroundColor',
        'customClasses',
        'link',
        'unlink',
        'insertImage',
        'insertVideo',
        // 'insertHorizontalRule',
        // 'removeFormat',
        'toggleEditorMode',
      ],
    ],
  };
  constructor(
    private adminService: AdminService,
    private toastService: ToastService,
  ) {}

  ngOnInit(): void {
   
    this.getAllBlogs();
  }
  
  getAllBlogs() {
    this.adminService.getAdminBlogs().subscribe({
      next: (data: any) => {
        console.log(data);
        // this.Blogs = data;
        this.Blogs = data.map((item: any) => ({
          ...item,
          createdAt: item.createdAt ? this.parseSentDate(item.createdAt) : null,
        }));
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

  removeBlogs() {
    this.adminService.deleteBlog(this.currentBlogID).subscribe({
      next: (data: any) => {
        this.toastService.showToast('success', 'Blog deleted successfully.');
        this.confirmModal();
        this.getAllBlogs();
      },
      error: (err: HttpErrorResponse) => {
        this.handleError(err);
      },
    });
  }



  confirmModal(id?: any) {
    if (id) this.currentBlogID = id;
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
 


}
