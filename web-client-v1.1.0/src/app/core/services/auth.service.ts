import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  helper = new JwtHelperService();
  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router
  ) {}

  register(formData: FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/auth/signup`,
      formData,
      { headers: headers }
    );
  }
  verifyEmail(token: string) {
    return this.http.get<any>(
      `${environment.urlBackend}api/auth/verifyEmail?token=${token}`
    );
  }
  getEmail() {
    let email;
    let token = sessionStorage.getItem('token');
    let decodedToken;
    if (token !== null) {
      decodedToken = this.helper.decodeToken(token);
      email = decodedToken.sub;
    }
    return email;
  }

  login(data: any) {
    return this.http.post(`${environment.urlBackend}api/auth/login`, data);
  }
  requestResetPassword(email: any) {
    return this.http.post(
      `${environment.urlBackend}api/auth/password-reset-request`,
      email
    );
  }
  saveTokenUser(token: any, userRole: any) {
    sessionStorage.setItem('token', token);
    sessionStorage.setItem('userRole', userRole);
  }
  setUrlResetPassword(url: string) {
    this.cookieService.set('url', url);
  }

  getUrlResetPassword(): string {
    return this.cookieService.get('url');
  }
  deleteUrlResetPassword() {
    this.cookieService.delete('url');
  }

  resetPassword(data: any) {
    return this.http.post(this.getUrlResetPassword(), data);
  }

  LoggedInAdmin() {
    if (!this.LoggedIn()) {
      return false;
    }

    if (sessionStorage.getItem('userRole') !== 'ADMIN') {
      //toast you are not an admin
      return false;
    }

    return true;
  }
  LoggedInUser() {
    if (!this.LoggedIn()) {
      return false;
    }

    if (sessionStorage.getItem('userRole') !== 'CLIENT') {
      return false;
    }

    return true;
  }
  LoggedInAccountant() {
    if (!this.LoggedIn()) {
      return false;
    }

    if (sessionStorage.getItem('userRole') !== 'ACCOUNTANT') {
      return false;
    }

    return true;
  }
  LoggedIn() {
    let token = sessionStorage.getItem('token');

    if (!token) {
      return false;
    }
    return !this.helper.isTokenExpired(token); // to check if token is expired
  }
  logout() {
    if (this.LoggedIn()) {
      if (this.LoggedInAdmin()) {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('userRole');
        this.router.navigate(['/admin/login']);
      } else {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('userRole');
        this.router.navigate(['/login']);
      }
    } else {
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('userRole');
      this.router.navigate(['/login']);
    }
  }
}
