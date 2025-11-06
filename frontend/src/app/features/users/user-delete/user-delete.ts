import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../../core/services/user';

@Component({
  selector: 'app-user-delete',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-delete.html',
  styleUrls: ['./user-delete.scss']
})
export class UserDelete implements OnInit {
  userId!: number;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: User
  ) { }

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
  }

  deleteUser(): void {
    this.userService.deleteUser(this.userId).subscribe({
      next: () => this.router.navigate(['/users']),
      error: () => this.errorMessage = 'Erreur lors de la suppression.'
    });
  }

  cancel(): void {
    this.router.navigate(['/users']);
  }
}
