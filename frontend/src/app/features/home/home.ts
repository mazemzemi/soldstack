import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthState } from '../../core/services/auth-state';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class Home {
  isLoggedIn$: Observable<boolean>;

  constructor(private authStateService: AuthState, private router: Router) {
    this.isLoggedIn$ = this.authStateService.isLoggedIn$;
  }

  logout() {
    this.authStateService.logout();
    this.router.navigate(['/login']);
  }
}
