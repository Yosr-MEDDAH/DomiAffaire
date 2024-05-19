import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountantServiceService {
  headerAll: any;
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {
    let token = sessionStorage.getItem('token');
    if (token !== null) {
      this.headerAll = new HttpHeaders({ Authorization: `Bearer ${token}` });
    }
  }
  // accountant profile ================================================
  getAccountantData(email: any) {
    return this.http.get(`${environment.urlBackend}api/accountants/${email}`, {
      headers: this.headerAll,
    });
  }
  updateAccountantProfile(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/update-profile/${id}`,
      data,
      {
        headers: this.headerAll,
      }
    );
  }
  changeAccountantPassword(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/change-password/${id}`,
      data,
      {
        headers: this.headerAll,
      }
    );
  }
  // dealing with consultation requests ================================================
  getAllConsultation() {
    return this.http.get(`${environment.urlBackend}api/accountants/consultation-requests`, {
      headers: this.headerAll,
    });
  }
  getAcceptedOrRejectedConsultation() {
    return this.http.get(`${environment.urlBackend}api/accountants/consultation-requests/accepted-or-rejected`, {
      headers: this.headerAll,
    });
  }
  getAcceptedConsultation() {
    return this.http.get(`${environment.urlBackend}api/accountants/consultation-requests/accepted`, {
      headers: this.headerAll,
    });
  }
  getRejectedConsultation() {
    return this.http.get(`${environment.urlBackend}api/accountants/consultation-requests/rejected`, {
      headers: this.headerAll,
    });
  }
  getInProgressConsultation() {
    return this.http.get(`${environment.urlBackend}api/accountants/consultation-requests/in-progress`, {
      headers: this.headerAll,
    });
  }
  
  acceptConsultationRequest(id:any) {
    return this.http.put(`${environment.urlBackend}api/accountants/consultation-requests/accept/${id}`,
    {}, {
      headers: this.headerAll,
    });
  }
  rejectConsultationRequest(id:any) {
    return this.http.put(`${environment.urlBackend}api/accountants/consultation-requests/reject/${id}`,
    {}, {
      headers: this.headerAll,
    });
  }
}
