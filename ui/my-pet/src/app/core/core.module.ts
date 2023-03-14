import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ApiService, UserService} from './services';

@NgModule({
  imports: [
    CommonModule
  ],
  providers: [
    ApiService,
    UserService
  ],
  declarations: []
})
export class CoreModule { }
