import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from './layouts/user/user.component';
import { AdminComponent } from './layouts/admin/admin.component';
import { AuthAdminComponent } from './layouts/auth-admin/auth-admin.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';

const routes: Routes = [
  {
    path:'',
    component:UserComponent,
    children: [
      {
        path: 'login',
        loadChildren: () =>
          import('./features/login/login.module').then((m) => m.LoginModule),
      },
      {
        path: 'register',
        loadChildren: () =>
          import('./features/register/register.module').then(
            (m) => m.RegisterModule
          ),
      },
      {
        path: 'reset-password',
        loadChildren: () =>
          import('./features/reset-password/reset-password.module').then(
            (m) => m.ResetPasswordModule
          ),
      },
    ]
  },
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      
    ],
  },
  {
    path:'admin/login',
    component: AuthAdminComponent,
    
  },
  {
    path: '**', component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
