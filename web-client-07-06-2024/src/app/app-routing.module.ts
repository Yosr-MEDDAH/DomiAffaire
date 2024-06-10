import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from './layouts/user/user.component';
import { AdminComponent } from './layouts/admin/admin.component';
import { AuthAdminComponent } from './layouts/auth-admin/auth-admin.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { ClientGuard } from './guards/client.guard';
import { NoGuardGuard } from './guards/no-guard.guard';
import { AdminGuard } from './guards/admin.guard';
import { AccountantGuard } from './guards/accountant.guard';

const routes: Routes = [
  {
    path: '',
    component: UserComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./features/home/home.module').then((m) => m.HomeModule),
      },
      {
        path: 'faq',
        loadChildren: () =>
          import('./features/faq/faq.module').then((m) => m.FaqModule),
      },
      {
        path: 'login',
        loadChildren: () =>
          import('./features/login/login.module').then((m) => m.LoginModule),
        canActivateChild: [NoGuardGuard],
      },
      {
        path: 'register',
        loadChildren: () =>
          import('./features/register/register.module').then(
            (m) => m.RegisterModule
          ),
        canActivateChild: [NoGuardGuard],
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/reset-password/reset-password.module').then(
            (m) => m.ResetPasswordModule
          ),
          canActivateChild:[NoGuardGuard]
      },
      {
        path: 'client',
        loadChildren: () =>
          import('./features/client/profile/profile.module').then(
            (m) => m.ProfileModule
          ),
        canActivateChild: [ClientGuard],
      },
      {
        path: 'client',
        loadChildren: () =>
          import('./features/client/consultation/consultation.module').then(
            (m) => m.ConsultationModule
          ),
        canActivateChild: [ClientGuard],
      },
      {
        path: 'client',
        loadChildren: () =>
          import('./features/client/domiciliation/domiciliation.module').then(
            (m) => m.DomiciliationModule
          ),
          canActivateChild: [ClientGuard],
        },
      {
        path: 'client',
        loadChildren: () =>
          import('./features/client/favorite-blogs/favorite-blogs.module').then(
            (m) => m.FavoriteBlogsModule
          ),
          canActivateChild: [ClientGuard],
        },
      {
        path: 'accountant',
        loadChildren: () =>
          import('./features/accountant/accountant-profile/accountant-profile.module').then(
            (m) => m.AccountantProfileModule
          ),
        canActivateChild: [AccountantGuard],
      },
      {
        path: 'accountant',
        loadChildren: () =>
          import('./features/accountant/favorite-blogs/favorite-blogs.module').then(
            (m) => m.FavoriteBlogsModule
          ),
        canActivateChild: [AccountantGuard],
      },
      {
        path: 'accountant',
        loadChildren: () =>
          import('./features/accountant/accountant-blogs/accountant-blogs.module').then(
            (m) => m.AccountantBlogsModule
          ),
        canActivateChild: [AccountantGuard],
      },
      {
        path: 'accountant',
        loadChildren: () =>
          import('./features/accountant/consultation-validation/consultation-validation.module').then(
            (m) => m.ConsultationValidationModule
          ),
        canActivateChild: [AccountantGuard],
      },
      {
        path: 'company-creation',
        loadChildren: () =>
          import('./features/company-creation/company-creation.module').then(
            (m) => m.CompanyCreationModule
          )
      },
      {
        path: 'chat/:id',
        loadChildren: () =>
          import('./features/chat/chat.module').then(
            (m) => m.ChatModule
          ),
        // canActivateChild: [ClientGuard],
      },
    ],
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivateChild:[AdminGuard],
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/dashboard/dashboard.module').then(
            (m) => m.DashboardModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/clients/clients.module').then(
            (m) => m.ClientsModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/rooms/rooms.module').then(
            (m) => m.RoomsModule
          ),
      },
      {
        path: 'archived-clients',
        loadChildren: () =>
          import('./features/admin/archived-clients/archived-clients.module').then(
            (m) => m.ArchivedClientsModule
          ),
      },
      {
        path: 'accountants',
        loadChildren: () =>
          import('./features/admin/accountant/accountant.module').then(
            (m) => m.AccountantModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/admin-profile/admin-profile.module').then(
            (m) => m.AdminProfileModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/reservation/reservation.module').then(
            (m) => m.ReservationModule
          ),
      },
      {
        path: 'company-creation-documents',
        loadChildren: () =>
          import('./features/admin/company-creation-documents/company-creation-documents.module').then(
            (m) => m.CompanyCreationDocumentsModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/domiciliation-management/domiciliation-management.module').then(
            (m) => m.DomiciliationManagementModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/packs/packs.module').then(
            (m) => m.PacksModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/blogs/blogs.module').then(
            (m) => m.BlogsModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/categories/categories.module').then(
            (m) => m.CategoriesModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/faqs/faqs.module').then(
            (m) => m.FAQsModule
          ),
      },
      {
        path: '',
        loadChildren: () =>
          import('./features/admin/clients-documents/clients-documents.module').then(
            (m) => m.ClientsDocumentsModule
          ),
      },
    ],
  },
  {
    path: 'admin/login',
    component: AuthAdminComponent,
  },
  {
    path: '**',
    component: PageNotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
