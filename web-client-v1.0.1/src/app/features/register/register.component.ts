import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthServiceService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  signupForm!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService
  ) {}
  ngOnInit(): void {
    this.createForm();
  }
  createForm() {
    this.signupForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email, // Angular's built-in email validator
          Validators.pattern(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/), // Custom email pattern
        ],
      ],
      pwd: [
        '',
        [
          Validators.required,
          Validators.minLength(8), // Minimum length of 8 characters
        ],
      ],
      image: ['', [Validators.required]],
      firstName: [
        '',
        [
          Validators.required,
          Validators.minLength(3), // Minimum length of 3 characters
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/), // Only allow alphanumeric characters and underscore
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.minLength(3), // Minimum length of 3 characters
          Validators.pattern(/^[a-zA-Z0-9_ ]*$/), // Only allow alphanumeric characters and underscore
        ],
      ],
      phoneNumber: [
        '',
        [
          Validators.required,
          Validators.minLength(8), // Minimum length of 3 characters
          Validators.pattern(/^[0-9_ ]*$/), // Only allow alphanumeric characters and underscore
        ],
      ],
      birthDate: ['', [Validators.required]],
    });
  }
  onSubmit() {
    //console.log(this.signupForm.value)
    this.authService.register(this.signupForm.value).subscribe({
      next: (data: any) => {
        console.log(data);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
