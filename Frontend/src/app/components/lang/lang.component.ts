import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faGithub, faGithubAlt, faGithubSquare, faLine, faSquareGithub } from '@fortawesome/free-brands-svg-icons';
import { faArrowTurnDown, faArrowTurnUp } from '@fortawesome/free-solid-svg-icons';
import { map } from 'rxjs';
import { LanguageService } from 'src/app/api/language.service';
import { LanguageDetails } from 'src/app/api/model/LanguageDetails';
import { TiobeStatus } from 'src/app/api/model/TiobeStatus';

@Component({
  selector: 'app-lang',
  templateUrl: './lang.component.html',
  styleUrls: ['./lang.component.css']
})
export class LangComponent {

  public githubIcon = faGithub;
  public upIcon = faArrowTurnUp;
  public downIcon = faArrowTurnDown;

  public details: LanguageDetails = {} as LanguageDetails;

  public globalTiobeStatus = TiobeStatus;

  constructor(languageService: LanguageService, route: ActivatedRoute) {
    let languageName = route.snapshot.params['name'];

    languageService.getDetails(languageName)
                   .subscribe(details => this.details = details);
  }

  @ViewChild("description")
  private descriptionElement: ElementRef = {} as ElementRef;

  @ViewChild("trends")
  private trendsElement: ElementRef = {} as ElementRef;

  @ViewChild("descriptionButton")
  private descriptionButtonElement: ElementRef = {} as ElementRef;

  @ViewChild("trendsButton")
  private trendsButtonElement: ElementRef = {} as ElementRef;

  public showDescription(): void {
    this.descriptionButtonElement.nativeElement.style.backgroundColor = "#329e5c";
    this.descriptionButtonElement.nativeElement.style.color = "white";

    this.trendsButtonElement.nativeElement.style.backgroundColor = "white";
    this.trendsButtonElement.nativeElement.style.color = "black";

    this.descriptionElement.nativeElement.style.display = "flex";
    this.trendsElement.nativeElement.style.display = "none";
  }

  public showTrends(): void {
    this.trendsButtonElement.nativeElement.style.backgroundColor = "#329e5c";
    this.trendsButtonElement.nativeElement.style.color = "white";

    this.descriptionButtonElement.nativeElement.style.backgroundColor = "white";
    this.descriptionButtonElement.nativeElement.style.color = "black";

    this.descriptionElement.nativeElement.style.display = "none";
    this.trendsElement.nativeElement.style.display = "flex";
  }
}
