import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  headerAll: any;
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {
    let token = sessionStorage.getItem('token');
    if (token !== null) {
      this.headerAll = new HttpHeaders({ Authorization: `Bearer ${token}` });
    }
  }
  getClientData(email: any) {
    return this.http.get(`${environment.urlBackend}api/clients/${email}`, {
      headers: this.headerAll,
    });
  }
  updateClientProfile(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/clients/update-profile/${id}`,
      data,
      {
        headers: this.headerAll,
      }
    );
  }
  changeClientPassword(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/clients/change-password/${id}`,
      data,
      {
        headers: this.headerAll,
      }
    );
  }
}
