import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../../../core/services/user';
@Component({
  selector: 'app-user-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-add.html',
  styleUrls: ['./user-add.scss']
})
export class UserAdd {
  userForm: FormGroup;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private userService: User,
    private router: Router
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) return;
    this.isSubmitting = true;

    this.userService.addUser(this.userForm.value).subscribe({
      next: () => {
        this.successMessage = 'Utilisateur ajouté avec succès !';
        this.isSubmitting = false;
        this.router.navigate(['/users']);
      },
      error: () => {
        this.errorMessage = 'Erreur lors de l’ajout de l’utilisateur.';
        this.isSubmitting = false;
      }
    });
  }
}
