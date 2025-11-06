import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { UsersList } from './features/users/users-list/users-list';
import { UserAdd } from './features/users/user-add/user-add';
import { UserDetail } from './features/users/user-detail/user-detail';
import { UserDelete } from './features/users/user-delete/user-delete';
import { AuthGuard } from './core/guards/auth.guard';
export const routes: Routes = [
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  { path: 'welcome', loadComponent: () => import('./features/home/home').then(m => m.Home) },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'users', component: UsersList, canActivate: [AuthGuard] },
  { path: 'users/add', component: UserAdd, canActivate: [AuthGuard] },
  { path: 'users/:id', component: UserDetail, canActivate: [AuthGuard] },
  { path: 'users/:id/delete', component: UserDelete, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];
