import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthServiceService } from '../core/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class NoGuardGuard implements CanActivate, CanActivateChild {
  constructor(
    private authService: AuthServiceService, 
    private router: Router
  ) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return true;
  }
  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return new Promise((resolve, reject) => {
        const loggedIn= this.authService.LoggedInUser()
        if ( !loggedIn) {
          resolve(true);
        } else {
          resolve(false);
          this.router.navigate(['/'], {
            queryParams: { returnUrl: state.url },
          });
        }
      });
  }
  
}
