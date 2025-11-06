
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User, UserDto } from '../../../core/services/user';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './users-list.html',
  styleUrls: ['./users-list.scss']
})
export class UsersList implements OnInit {
  users: UserDto[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private userService: User) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors du chargement des utilisateurs.';
        this.isLoading = false;
      }
    });
  }
}
