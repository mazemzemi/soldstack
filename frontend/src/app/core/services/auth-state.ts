import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'  
})
export class AuthState {
  private _isLoggedIn = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this._isLoggedIn.asObservable();
  private isBrowser: boolean;

  constructor() {
    const platformId = inject(PLATFORM_ID);
    this.isBrowser = isPlatformBrowser(platformId);

    if (this.isBrowser && this.hasToken()) {
      this._isLoggedIn.next(true);
    }
  }

  private hasToken(): boolean {
    if (!this.isBrowser) return false;
    return !!localStorage.getItem('token');
  }

  login(token: string) {
    if (this.isBrowser) {
      localStorage.setItem('token', token);
      this._isLoggedIn.next(true);
    }
  }

  logout() {
    if (this.isBrowser) {
      localStorage.removeItem('token');
      this._isLoggedIn.next(false);
    }
  }

  getToken(): string | null {
    return this.isBrowser ? localStorage.getItem('token') : null;
  }
}

// Factory provider standalone
export const provideAuthState = {
  provide: AuthState,
  useFactory: () => new AuthState(),
};
