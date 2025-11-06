import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Auth } from '../../../core/services/auth';
import { AuthState } from '../../../core/services/auth-state';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  errorMessage = '';
  form!: FormGroup;
  isLoggedIn$: Observable<boolean>;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: Auth,
    private authStateService: AuthState
  ) {
    this.form = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });

    this.isLoggedIn$ = this.authStateService.isLoggedIn$;
  }

  onLogin() {
    if (this.form.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs';
      return;
    }

    const loginRequest = {
      email: this.form.value.email!,
      password: this.form.value.password!
    };

    this.authService.login(loginRequest).subscribe({
      next: (res) => {
        console.log('✅ Login réussi', res);
        this.authStateService.login(res.token);
        this.router.navigate(['/users']);
      },
      error: (err) => {
        console.error('❌ Erreur de connexion', err);
        this.errorMessage = err.error?.message || 'Erreur de connexion. Vérifiez vos identifiants.';
        this.form.reset();
      }
    });
  }

  logout() {
    this.authStateService.logout();
    this.router.navigate(['/login']);
  }
}
