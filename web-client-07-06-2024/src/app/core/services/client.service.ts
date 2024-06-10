import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root',
})
export class ClientService {
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {}
  private getHeaders() {
    let token = sessionStorage.getItem('token');
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }
  //profile
  getClientData(email: any) {
    return this.http.get(`${environment.urlBackend}api/clients/${email}`, {
      headers: this.getHeaders(),
    });
  }
  updateClientProfile(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/clients/update-profile/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  changeClientPassword(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/clients/change-password/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  //company creation
  getAllFiles() {
    return this.http.get(
      `${environment.urlBackend}api/visitors/company-creation/documents`
    );
  }
  //FAQs
  getAllFAQ() {
    return this.http.get(
      `${environment.urlBackend}api/visitors/faqs`
    );
  }
  //consultation + accountant
  makeConsultationRequest(data: any) {
    return this.http.post(
      `${environment.urlBackend}api/clients/consultion-request`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  cancelConsultationRequest(id: any) {
    return this.http.delete(
      `${environment.urlBackend}api/clients/consultion-request/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getClientConsultationRequest() {
    return this.http.get(
      `${environment.urlBackend}api/clients/all-client-consultation-request`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  //Domiciliation
  getPacks() {
    return this.http.get(`${environment.urlBackend}api/clients/packs`, {
      headers: this.getHeaders(),
    });
  }
  sendDomiciliationPP(formData: FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/clients/domiciliations/domiciliation-request/pp`,
      formData,
      {
        headers: this.getHeaders(),
      }
    );
  }
  sendDomiciliationPMInProcess(formData: FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/clients/domiciliations/domiciliation-request/pm/in-process`,
      formData,
      {
        headers: this.getHeaders(),
      }
    );
  }
  sendDomiciliationPMTransfer(formData: FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/clients/domiciliations/domiciliation-request/pm/transfer`,
      formData,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getClientDomiciliationRequests() {
    return this.http.get(
      `${environment.urlBackend}api/clients/all-client-domiciliation-request`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getDomiciliationRequest(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/clients/domiciliation-requests/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  cancelDomiciliationRequest(id: any) {
    return this.http.delete(
      `${environment.urlBackend}api/clients/domiciliation-request/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  acceptDomiciliationRequest(id: any) {
    return this.http.post(
      `${environment.urlBackend}api/clients/domiciliation-requests/response-client/accept/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }
  rejectDomiciliationRequest(id: any, body:any) {
    return this.http.post(
      `${environment.urlBackend}api/clients/domiciliation-requests/response-client/reject/${id}`,
      body,
      {
        headers: this.getHeaders(),
      }
    );
  }
  //blogs
  getAllBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/clients/blogs`,
      {
        headers: this.getHeaders(),
      }
    )
  }
  getBlogByID(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/clients/blogs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  saveBlog(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/clients/blogs/save/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  unSaveBlog(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/clients/blogs/unsave/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getSavedBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/clients/saved-blogs`,
      {
        headers: this.getHeaders(),
      }
    );
  }
}


