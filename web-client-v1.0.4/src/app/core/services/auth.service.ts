import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  constructor(private http: HttpClient) {}

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
      `http://localhost:8080/api/auth/verifyEmail?token=${token}`
    );
  }
  requestResetPassword(email: any) {
    return this.http.post(
      `http://localhost:8080/api/auth/password-reset-request`,
      email
    );
  }
}
