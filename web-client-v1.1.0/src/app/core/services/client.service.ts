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
  //profile
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
  //company creation
  getAllFiles() {
    return this.http.get(
      `${environment.urlBackend}api/visitors/company-creation/documents`
    );
  }
  //consultation + accountant
  makeConsultationRequest(data: any) {
    return this.http.post(`${environment.urlBackend}api/clients/consultion-request`,
    data,
    {
      headers: this.headerAll,
    });
  }
  cancelConsultationRequest(id: any) {
    return this.http.delete(`${environment.urlBackend}api/clients/consultion-request/${id}`,
    {
      headers: this.headerAll,
    });
  }
  getClientConsultationRequest() {
    return this.http.get(`${environment.urlBackend}api/clients/all-client-consultation-request`,
    {
      headers: this.headerAll,
    });
  }
  //Domiciliation
  getPacks() {
    return this.http.get(`${environment.urlBackend}api/clients/packs`,
    {
      headers: this.headerAll,
    });
  }
  sendDomiciliationPP(formData:FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(`${environment.urlBackend}api/clients/domiciliations/domiciliation-request/pp`,
    formData,
    {
      headers: this.headerAll,
    });
  }
  sendDomiciliationPMInProcess(formData:FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(`${environment.urlBackend}api/clients/domiciliations/domiciliation-request/pm/in-process`,
    formData,
    {
      headers: this.headerAll,
    });
  }
  sendDomiciliationPMTransfer(formData:FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(`${environment.urlBackend}api/clients/domiciliations/domiciliation-request/pm/transfer`,
    formData,
    {
      headers: this.headerAll,
    });
  }
  getClientDomiciliationRequests() {
    return this.http.get(`${environment.urlBackend}api/clients/all-client-domiciliation-request`,
    {
      headers: this.headerAll,
    });
  }
  getDomiciliationRequest(id:any) {
    return this.http.get(`${environment.urlBackend}api/clients/domiciliation-requests/${id}`,
    {
      headers: this.headerAll,
    });
  }
  cancelDomiciliationRequest(id:any) {
    return this.http.delete(`${environment.urlBackend}api/clients/domiciliation-request/${id}`,
    {
      headers: this.headerAll,
    });
  }

}
