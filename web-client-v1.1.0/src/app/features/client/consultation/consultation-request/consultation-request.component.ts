import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { ClientService } from 'src/app/core/services/client.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-consultation-request',
  templateUrl: './consultation-request.component.html',
  styleUrls: ['./consultation-request.component.css'],
})
export class ConsultationRequestComponent implements OnInit {
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
  consultationForm!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.consultationForm = this.fb.group({
      details: ['', [Validators.required]],
      proposedDate: ['', [Validators.required]],
      subject: ['', [Validators.required]],
      draftStatus: ['', [Validators.required]],
      budget: ['', [Validators.required]],
    });
  }

  onSubmit() {
    // console.log(this.consultationForm.value);
    this.clientService
      .makeConsultationRequest(this.consultationForm.value)
      .subscribe({
        next: (data: any) => {
          // console.log(data);
          this.toastService.showToast("success","Consultation request sent successfully");
          this.consultationForm.reset();
          this.consultationForm.controls['draftStatus'].setValue('');
        },
        error: (err: HttpErrorResponse) => {
          // console.log(err);
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
  cancelForm(){
    this.consultationForm.reset();
    this.consultationForm.controls['draftStatus'].setValue('');
  }
}
