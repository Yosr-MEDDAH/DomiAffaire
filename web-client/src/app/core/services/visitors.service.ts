import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VisitorsService {
  constructor(private http: HttpClient) {}

  getPacks() {
    return this.http.get(`${environment.urlBackend}api/visitors/packs`, {

    });
  }
}
