import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  receivedUrl: any;
  signinForm!: FormGroup;
  passsField: any;
  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private darkModeService: DarkModeService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.createForm();
    this.receivedUrl =
      this.activatedRoute.snapshot.queryParams['returnUrl'] || '/';
  }
  createForm() {
    this.signinForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/),
        ],
      ],
      pwd: ['', [Validators.required, Validators.minLength(8)]],
    });
  }
  onSubmit() {
    console.log(this.signinForm.value);
    this.authService.login(this.signinForm.value).subscribe({
      next: (data: any) => {
        console.log(data);
        this.authService.saveTokenUser(data.jwt, data.userRole);
        this.router.navigate([this.receivedUrl]);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
