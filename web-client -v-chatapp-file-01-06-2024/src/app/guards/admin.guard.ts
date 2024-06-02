import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateChild,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthServiceService } from '../core/services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivateChild {
  constructor(
    private authService: AuthServiceService,
    private router: Router
  ) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return true;
  }
  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return new Promise((resolve, reject) => {
      const LoggedInAdmin = this.authService.LoggedInAdmin();
      if (LoggedInAdmin) {
        resolve(true);
      } else {
        this.router.navigate(['/admin/login'], {
          queryParams: { returnUrl: state.url },
        });
        resolve(false);
      }
    });
  }
}
