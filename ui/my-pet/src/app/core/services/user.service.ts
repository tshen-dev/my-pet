import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, ReplaySubject} from 'rxjs';

import {ApiService} from './api.service';
import {ApiResponse, User} from '../models';
import {distinctUntilChanged, map} from 'rxjs/operators';


@Injectable()
export class UserService {
  private currentUserSubject = new BehaviorSubject<User>({} as User);
  public currentUser = this.currentUserSubject.asObservable().pipe(distinctUntilChanged());

  private isAuthenticatedSubject = new ReplaySubject<boolean>(1);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  constructor (
    private apiService: ApiService
  ) {}

  getInfo(): Observable<User> {
    console.log("UserService getInfo");
    return this.apiService.get("/api/user-service/users/info").pipe(map((response: ApiResponse<User>) => {
      this.currentUserSubject.next(response.data);
      console.log(response.data);
      return response.data;
    }));;
  }

  getCurrentUser(): User {
    return this.currentUserSubject.value;
  }
}
