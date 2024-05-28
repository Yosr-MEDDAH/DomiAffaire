import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import {
  BehaviorSubject,
  Observable,
  fromEvent,
  merge,
  throwError,
  timer,
} from 'rxjs';
import { map, catchError, debounceTime, distinctUntilChanged } from 'rxjs/operators';
@Injectable()
export class OfflineInterceptor implements HttpInterceptor {
  private online$!: BehaviorSubject<boolean>;
  constructor() {
    this.online$ = new BehaviorSubject<boolean>(navigator.onLine);
    const online$ = fromEvent(window, 'online').pipe(map(() => true));
    const offline$ = fromEvent(window, 'offline').pipe(map(() => false));
    merge(online$, offline$).subscribe((isOnline: any) => {
      this.online$.next(isOnline);
    });
    timer(0, 3000).subscribe(() => {
      this.online$.next(navigator.onLine);
    });
  }
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (!this.online$.getValue()) {
          return throwError('Device is offline');
        }
        return throwError(error);
      })
    );
  }
}
