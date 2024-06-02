import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
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
export class ClientGuard implements CanActivateChild {
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
    // return new Promise ((resolve, reject)=>{
    //   const loggedIn = this.authService.LoggedInUser();
    //   if(!loggedIn){
    //     const loggedInAccountant = this.authService.LoggedInAccountant();
    //     if(!loggedInAccountant){
    //       this.router.navigate(['/login'], {
    //         queryParams: { returnUrl: state.url },
    //       });
    //       resolve(false);
    //     }
        
    //   }
    //   resolve(true);
    // });
    return new Promise((resolve, reject) => {
      const loggedIn = this.authService.LoggedInUser();
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
