import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  headerAll: any;
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {
    let token = sessionStorage.getItem('token');
    if (token !== null) {
      this.headerAll = new HttpHeaders({ Authorization: `Bearer ${token}` });
    }
  }
  getAllClients() {
    return this.http.get(`${environment.urlBackend}api/admin/clients`, {
      headers: this.headerAll,
    });
  }
  archiveClient(id: any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/clients/archive/${id}`,
      {},
      {
        headers: this.headerAll,
      }
    );
  }
}
