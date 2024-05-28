import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-handle-blog',
  templateUrl: './handle-blog.component.html',
  styleUrls: ['./handle-blog.component.css']
})
export class HandleBlogComponent implements OnInit {
  selectedFile!: File;
  selectedImageURL: string | ArrayBuffer | null = null; // Initialize selectedImageURL

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
  blogsCategories: any;
  blogForm!: FormGroup;
  isEdit: boolean = false;
  blogId!: any;
  AccountantProfile: any;
  constructor(
    private route: ActivatedRoute,
    private accountantService: AccountantServiceService,
    private toastService: ToastService,
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.blogId = this.route.snapshot.paramMap.get('id');
    if (this.blogId) {
      this.isEdit = true;
      this.loadBlogData(this.blogId);
    }
    this.blogForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      category: [null],
      image: ['', Validators.required],
    });
    this.getBlogsCategories();
    this.refreshAccountantProfile();
  }
  refreshAccountantProfile(): void {
    const email = this.authService.getEmail();
    if (email) {
      this.accountantService.getAccountantData(email).subscribe({
        next: (data) => {
          console.log(data);
          this.AccountantProfile = data;
        },
        error: (err) => {
          console.error('Error getting admin data', err);
        },
      });
    }
  }
  loadBlogData(id: any): void {
    this.accountantService.getBlogByID(id).subscribe((blog: any) => {
      this.blogForm.patchValue(blog);
      if (blog.category) {
        this.blogForm.get('category')?.setValue(blog.category.id);
      }
      if (blog.image) {
        this.selectedImageURL = 'data:image/png;base64,' + blog.image.fileData;
      }
    });
  }
  getBlogsCategories() {
    this.accountantService.getCategoriesBlogs().subscribe({
      next: (data: any) => {
        console.log(data);
        this.blogsCategories = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  onSubmit() {
    if (this.isEdit && this.blogId) {
      const formData = this.prepareFormData();

      this.accountantService.updateBlog(this.blogId, formData).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'Blog updated successfully.');
          this.blogForm.reset();
          this.selectedImageURL=null;
          this.router.navigate(['/accountant/blogs']);
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
    } else {
      const formData = this.prepareFormData();

      this.accountantService.addBlog(formData).subscribe({
        next: (data: any) => {
          this.toastService.showToast('success', 'Blog added successfully.');
          this.blogForm.reset();
          this.selectedImageURL=null;
        },
        error: (err: HttpErrorResponse) => {
          this.handleError(err);
        },
      });
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
  prepareFormData(): FormData {
    console.log(this.blogForm.value);
    const formData = new FormData();
    const blog = {
      title: this.blogForm.get('title')?.value,
      content: this.blogForm.get('content')?.value,
      category: this.blogForm.get('category')?.value,
    };
    formData.append(
      'blog',
      new Blob([JSON.stringify(blog)], { type: 'application/json' })
    );
    // Append the existing image if available
    if (!this.selectedFile && this.isEdit) {
      const fileData = this.blogForm.get('image')?.value;
      const blob = this.dataURItoBlob(
        'data:image/png;base64,' + fileData.fileData
      );
      formData.append('image', blob);
    } else if (this.selectedFile) {
      console.log(this.selectedFile);
      // Append the new image if selected
      formData.append('image', this.selectedFile);
    }

    return formData;
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    if (files.length > 0) {
      this.selectedFile = files[0];
      console.log(this.selectedFile);
      this.getBase64(files[0]);
    }
  }
  getBase64(file: File) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.selectedImageURL = reader.result;
    };
    reader.onerror = (error) => {
      console.log('Error: ', error);
    };
  }
  isCategorySelected(categoryId: any): boolean {
    if (this.isEdit) {
      // Check if the category matches the selected category in the edited blog
      return this.blogForm.value.category === categoryId;
    } else {
      // If it's not an edit, no category should be selected by default
      return false;
    }
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
}
