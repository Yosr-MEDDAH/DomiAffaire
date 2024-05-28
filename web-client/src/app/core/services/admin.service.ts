import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {}
  private getHeaders() {
    let token = sessionStorage.getItem('token');
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }
  // ======================================================================
  // ============================= Clients ================================
  // ======================================================================
  getAllClients() {
    return this.http.get(`${environment.urlBackend}api/admin/clients`, {
      headers: this.getHeaders(),
    });
  }
  archiveClient(id: any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/clients/archive/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }
  unarchiveClient(id: any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/clients/unarchive/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }
  getArchivedClients() {
    return this.http.get(
      `${environment.urlBackend}api/admin/clients/archived`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  // ======================================================================
  // ============================= Accountants ============================
  // ======================================================================
  getAllAccountants() {
    return this.http.get(`${environment.urlBackend}api/admin/accountants`, {
      headers: this.getHeaders(),
    });
  }
  // ======================================================================
  // ============================= Admin ==================================
  // ======================================================================
  getAdminProfile(email: any) {
    return this.http.get(`${environment.urlBackend}api/admin/${email}`, {
      headers: this.getHeaders(),
    });
  }
  // ======================================================================
  // ============================= Company creation =======================
  // ======================================================================
  getAllFiles() {
    return this.http.get(
      `${environment.urlBackend}api/admin/company-creation/documents`,

      {
        headers: this.getHeaders(),
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
        headers: this.getHeaders(),
      }
    );
  }
  updateFile(formData: FormData, id: any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/company-creation/documents/update-file/${id}`,
      formData,
      {
        headers: this.getHeaders(),
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
        headers: this.getHeaders(),
      }
    );
  }
  deleteFile(id: any) {
    return this.http.delete(
      `${environment.urlBackend}api/admin/company-creation/documents/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getFileById(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/company-creation/documents/${id}`,
      {
        headers: this.getHeaders(),
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
        headers: this.getHeaders(),
      }
    );
  }
  getDomiciliationRequestDetails(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/domiciliation-requests/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  acceptDomiciliationRequest(id: any, formData: FormData) {
    const headers = new HttpHeaders();
    headers.set('Content-type', 'multipart/form-data');
    return this.http.post(
      `${environment.urlBackend}api/admin/domiciliation-requests/response-admin/accept/${id}`,
      formData,
      {
        headers: this.getHeaders(),
      }
    );
  }
  rejectDomiciliationRequest(id: any) {
    return this.http.post(
      `${environment.urlBackend}api/admin/domiciliation-requests/response-admin/reject/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }
  // ======================================================================
  // =============================  Packs   =======================
  // ======================================================================
  getAllPacks() {
    return this.http.get(
      `${environment.urlBackend}api/admin/packs`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  postPack(data:any) {
    return this.http.post(
      `${environment.urlBackend}api/admin/packs`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getPack(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/packs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  putPack(data:any,id:any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/packs/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  deletePack(id:any) {
    return this.http.delete(
      `${environment.urlBackend}api/admin/packs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  // ======================================================================
  // =============================  FAQ   =======================
  // ======================================================================
  addFAQ(data:any) {
    return this.http.post(
      `${environment.urlBackend}api/admin/faqs`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getFAQ(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/faqs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getFAQs() {
    return this.http.get(
      `${environment.urlBackend}api/admin/faqs`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  updateFAQ(id:any, data:any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/faqs/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  deleteFAQ(id:any) {
    return this.http.delete(
      `${environment.urlBackend}api/admin/faqs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  // ======================================================================
  // =============================  Blogs   =======================
  // ======================================================================
  addBlog(data:FormData) {
    return this.http.post(
      `${environment.urlBackend}api/admin/blogs`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getBlog(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/blogs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/admin/blogs-admin`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAdminBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/admin/blogs`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  updateBlog(id:any, data:FormData) {
    return this.http.put(
      `${environment.urlBackend}api/admin/blogs/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  deleteBlog(id:any) {
    return this.http.delete(
      `${environment.urlBackend}api/admin/blogs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  // ======================================================================
  // =============================  Blog Categories   =====================
  // ======================================================================
  addCategory(data:any) {
    return this.http.post(
      `${environment.urlBackend}api/admin/blog-categories`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getCategory(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/admin/blog-categories/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getCategories() {
    return this.http.get(
      `${environment.urlBackend}api/admin/blog-categories`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  updateCategory(id:any, data:any) {
    return this.http.put(
      `${environment.urlBackend}api/admin/blog-categories/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  deleteCategory(id:any) {
    return this.http.delete(
      `${environment.urlBackend}api/admin/blog-categories/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
}
