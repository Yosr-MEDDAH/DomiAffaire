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
  // ======================================================================
  // ============================= Clients ================================
  // ======================================================================
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
  unarchiveClient(id: any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/clients/unarchive/${id}`,
      {},
      {
        headers: this.headerAll,
      }
    );
  }
  getArchivedClients() {
    return this.http.get(
      `${environment.urlBackend}api/admin/clients/archived`,
      {
        headers: this.headerAll,
      }
    );
  }
  // ======================================================================
  // ============================= Accountants ============================
  // ======================================================================
  getAllAccountants() {
    return this.http.get(`${environment.urlBackend}api/admin/accountants`, {
      headers: this.headerAll,
    });
  }
  // ======================================================================
  // ============================= Admin ==================================
  // ======================================================================
  getAdminProfile(email: any) {
    return this.http.get(`${environment.urlBackend}api/admin/${email}`, {
      headers: this.headerAll,
    });
  }
  // ======================================================================
  // ============================= Company creation =======================
  // ======================================================================
  getAllFiles() {
    return this.http.get(
      `${environment.urlBackend}api/admin/company-creation/documents`,

      {
        headers: this.headerAll,
      }
    );
  }
  uploadFile(formData: FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/admin/company-creation/documents`,
      formData,
      {
        headers: this.headerAll,
      }
    );
  }
  updateFile(formData: FormData, id: any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/company-creation/documents/update-file/${id}`,
      formData,
      {
        headers: this.headerAll,
      }
    );
  }
  renameFile(name: any, id: any) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'application/json');
    return this.http.put(
      `${environment.urlBackend}api/admin/company-creation/documents/rename-file/${id}`,
      name,
      {
        headers: this.headerAll,
      }
    );
  }
  deleteFile(id: any) {
    return this.http.delete(
      `${environment.urlBackend}api/admin/company-creation/documents/${id}`,
      {
        headers: this.headerAll,
      }
    );
  }
  getFileById(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/company-creation/documents/${id}`,
      {
        headers: this.headerAll,
      }
    );
  }
  // ======================================================================
  // =============================  Domiciliation   =======================
  // ======================================================================
  getAllDomiciliationRequests() {
    return this.http.get(
      `${environment.urlBackend}api/admin/domiciliation-requests/in-progress`,
      {
        headers: this.headerAll,
      }
    );
  }
  getDomiciliationRequestDetails(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/domiciliation-requests/${id}`,
      {
        headers: this.headerAll,
      }
    );
  }
  acceptDomiciliationRequest(id:any, formData:FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/admin/domiciliation-requests/response-admin/accept/${id}`,
      formData,
      {
        headers: this.headerAll,
      }
    );
  }
  rejectDomiciliationRequest(id:any) {
    return this.http.post(
      `${environment.urlBackend}api/admin/domiciliation-requests/response-admin/reject/${id}`,
      {},
      {
        headers: this.headerAll,
      }
    );
  }
}
