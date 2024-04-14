import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from '@auth0/angular-jwt';
@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  helper = new JwtHelperService();
  constructor(private http: HttpClient, private cookieService: CookieService) {}

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

  LoggedInUser() {
    let token = sessionStorage.getItem('token');

    if (!token) {
      return false;
    }

    if (sessionStorage.getItem('userRole') !== 'CLIENT') {
      return false;
    }

    return true;
  }
}
