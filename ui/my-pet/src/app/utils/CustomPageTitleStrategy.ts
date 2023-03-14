import {Injectable} from '@angular/core';
import {RouterStateSnapshot, TitleStrategy} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Title} from '@angular/platform-browser';


@Injectable()
export class CustomPageTitleStrategy extends TitleStrategy {
  constructor(private translateService: TranslateService,
              private readonly title: Title) {
    super();
  }

  override updateTitle(snapshot: RouterStateSnapshot): void {
    const titleSide = this.buildTitle(snapshot);
    if (titleSide) {
      this.translateService.get(titleSide).subscribe((translatedTitle) => {
        this.title.setTitle(translatedTitle);
      })
    } else {
      this.translateService.get("title.default").subscribe((translatedTitle) => {
        this.title.setTitle(translatedTitle);
      })
    }
  }
}
