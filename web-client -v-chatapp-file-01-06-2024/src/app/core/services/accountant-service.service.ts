import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root',
})
export class AccountantServiceService {
  helper = new JwtHelperService();
  constructor(private http: HttpClient) {}
  private getHeaders() {
    let token = sessionStorage.getItem('token');
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }
  // accountant profile ================================================
  getAccountantData(email: any) {
    return this.http.get(`${environment.urlBackend}api/accountants/${email}`, {
      headers: this.getHeaders(),
    });
  }
  updateAccountantProfile(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/update-profile/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  changeAccountantPassword(id: any, data: any) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/change-password/${id}`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  // dealing with consultation requests ================================================
  getAllConsultation() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/consultation-requests`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAcceptedOrRejectedConsultation() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/consultation-requests/accepted-or-rejected`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAcceptedConsultation() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/consultation-requests/accepted`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getRejectedConsultation() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/consultation-requests/rejected`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getInProgressConsultation() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/consultation-requests/in-progress`,
      {
        headers: this.getHeaders(),
      }
    );
  }

  acceptConsultationRequest(id: any) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/consultation-requests/accept/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }
  rejectConsultationRequest(id: any) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/consultation-requests/reject/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }
  // handle blogs =====================================================================
  getCategoriesBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/blog-categories`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAccountantBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/blogs-accountant`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  deleteBlog(id:any) {
    return this.http.delete(
      `${environment.urlBackend}api/accountants/blogs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  updateBlog(id:any, formData:FormData) {
    return this.http.put(
      `${environment.urlBackend}api/accountants/blogs/${id}`,
      formData,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAllBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/blogs`,
      {
        headers: this.getHeaders(),
      }
    )
  }
  addBlog(data:FormData) {
    return this.http.post(
      `${environment.urlBackend}api/accountants/blogs`,
      data,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getBlogByID(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/accountants/blogs/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  saveBlog(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/accountants/blogs/save/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  unSaveBlog(id:any) {
    return this.http.get(
      `${environment.urlBackend}api/accountants/blogs/unsave/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getSavedBlogs() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/saved-blogs`,
      {
        headers: this.getHeaders(),
      }
    );
  }
}
