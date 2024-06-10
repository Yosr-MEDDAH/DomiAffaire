import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { ToastService } from './toast.service';

@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  helper = new JwtHelperService();
  public loginStatus = new EventEmitter<boolean>();
  public roleChanged = new EventEmitter<void>();

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router,
    private toastService:ToastService
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

  verifyEmail(token: string): Observable<any> {
    return this.http.get<any>(
      `${environment.urlBackend}api/auth/verifyEmail?token=${token}`
    );
  }

  getEmail(): string | null {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken = this.helper.decodeToken(token);
      return decodedToken.sub;
    }
    return null;
  }

  login(data: any): Observable<boolean> {
    return this.http
      .post<any>(`${environment.urlBackend}api/auth/login`, data)
      .pipe(
        tap((response) => {
          // console.log(response)
          if (response.jwt && response.userRole) {
            this.saveTokenUser(response.jwt, response.userRole);
            this.loginStatus.emit(true);
            this.roleChanged.emit();
          }
        }),
        map((response) => !!response.jwt && !!response.userRole),
        catchError((error: HttpErrorResponse) => {
          this.handleError(error);
          return of(false);
        })
      );
  }

  requestResetPassword(email: any): Observable<any> {
    return this.http.post(
      `${environment.urlBackend}api/auth/password-reset-request`,
      email
    );
  }

  saveTokenUser(token: string, userRole: string): void {
    sessionStorage.setItem('token', token);
    sessionStorage.setItem('userRole', userRole);
  }

  setUrlResetPassword(url: string): void {
    this.cookieService.set('url', url);
  }

  getUrlResetPassword(): string {
    return this.cookieService.get('url');
  }

  deleteUrlResetPassword(): void {
    this.cookieService.delete('url');
  }

  resetPassword(data: any): Observable<any> {
    return this.http.post(this.getUrlResetPassword(), data);
  }

  LoggedInAdmin(): boolean {
    return this.LoggedIn() && sessionStorage.getItem('userRole') === 'ADMIN';
  }

  LoggedInUser(): boolean {
    return this.LoggedIn() && sessionStorage.getItem('userRole') === 'CLIENT';
  }

  LoggedInAccountant(): boolean {
    return  this.LoggedIn() && sessionStorage.getItem('userRole') === 'ACCOUNTANT';
  }

  LoggedIn(): boolean {
    const token = sessionStorage.getItem('token');
    if (token && !this.helper.isTokenExpired(token)) {
      return true;
    }
    return false;
  }

  // logout(): Observable<boolean> {
  //   return new Observable<boolean>((observer) => {
  //     if (this.LoggedIn()) {
  //       sessionStorage.removeItem('token');
  //       sessionStorage.removeItem('userRole');
  //       if (this.LoggedInAdmin()) {
  //         this.router.navigate(['/admin/login']);
  //       } else {
  //         this.router.navigate(['/login']);
  //       }
  //     }
  //     sessionStorage.removeItem('token');
  //     sessionStorage.removeItem('userRole');
  //     this.router.navigate(['/login']);
  //     observer.next(true);
  //     observer.complete();
  //   });
  // }
  logout(): Observable<boolean> {
    return new Observable<boolean>(observer => {
      sessionStorage.clear();
      this.router.navigate(['/login']).then(() => {
        observer.next(true);
        observer.complete();
      });
    });
  }

  private handleError(err: HttpErrorResponse): void {
    let errorMessage = 'An error occurred: ';
    if(err.status===403){
      errorMessage="Your account is disabled!";
    }else{
      if (err.error instanceof ErrorEvent) {
        errorMessage += `Error: ${err.error.message}`;
      } else {
        
        if (err.error) {
          for (const key in err.error) {
            if (err.error.hasOwnProperty(key)) {
              errorMessage += `${err.error[key]} `;
            }
          }
        }
      }
    }
    
    this.toastService.showToast('error', errorMessage);
  }
}
