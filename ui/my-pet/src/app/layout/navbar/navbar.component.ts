import {Component, OnInit} from '@angular/core';
import {KeycloakService} from 'keycloak-angular';
import {User, UserService} from 'src/app/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  user: User = {} as User;

  constructor(private keycloakService: KeycloakService,
    private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getInfo().subscribe(data => {
      Object.assign(this.user, this.userService.getCurrentUser())
    });
  }

  logout(): void {
    this.keycloakService.logout();
  }
}
