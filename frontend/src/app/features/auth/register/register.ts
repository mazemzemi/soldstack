import { Component, ChangeDetectorRef, effect } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../../core/services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
})
export class Register {
  form: FormGroup;
  errorMessage: string | null = null;
  message: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: Auth,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    // 🧱 Création du formulaire réactif
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    // ⚡ Effet pour forcer Angular à rafraîchir la vue (nécessaire sans Zone.js)
    effect(() => {
      this.cdr.markForCheck();
    });
  }

  onRegister(): void {
    if (this.form.valid) {
      const formData = this.form.value;

      this.authService.register(formData).subscribe({
        next: () => {
          this.message = '✅ Inscription réussie ! Vous pouvez vous connecter.';
          // On force le rafraîchissement car Angular ne le fait pas automatiquement sans Zone.js
          this.cdr.markForCheck();
          setTimeout(() => this.router.navigate(['/login']), 1500);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || '❌ Erreur lors de l’inscription';
          this.cdr.markForCheck();
        },
      });
    } else {
      this.form.markAllAsTouched();
      this.cdr.markForCheck();
    }
  }
}
