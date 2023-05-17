import { Component } from '@angular/core';
import { LanguageService } from 'src/app/api/language.service';
import { Language } from 'src/app/api/model/Language';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  public languages: Language[] | undefined;

  constructor(private languageService: LanguageService) {
    this.languageService.getAll()
                        .subscribe(languages => this.languages = languages);
  }
}
