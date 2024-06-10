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
export class AccountantGuard implements CanActivateChild {
  constructor(
    private authService: AuthServiceService,
    private router: Router
  ) {}
  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return new Promise((resolve, reject) => {
      const loggedIn = this.authService.LoggedInAccountant();
      if (!loggedIn) {
        this.router.navigate(['/login'], {
          queryParams: { returnUrl: state.url },
        });
        resolve(false);
      } else {
        resolve(true);
      }
    });
  }
}
