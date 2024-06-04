import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {}
  private getHeaders() {
    let token = sessionStorage.getItem('token');
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }
  getAllBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/users/blogs`,
      {
        headers: this.getHeaders(),
      }
    )
  }
}
