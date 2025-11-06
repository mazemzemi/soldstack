import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { User, UserDto } from '../../../core/services/user';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-detail.html',
  styleUrls: ['./user-detail.scss']
})
export class UserDetail implements OnInit {
  user?: UserDto;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private userService: User
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.userService.getUserById(id).subscribe({
        next: (data) => this.user = data,
        error: () => this.errorMessage = 'Utilisateur introuvable.'
      });
    }
  }
}
