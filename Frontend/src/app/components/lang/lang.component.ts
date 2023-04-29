import { Component, ElementRef, ViewChild } from '@angular/core';
import { faGithub, faGithubAlt, faGithubSquare, faSquareGithub } from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-lang',
  templateUrl: './lang.component.html',
  styleUrls: ['./lang.component.css']
})
export class LangComponent {

  public githubIcon = faGithub;

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
