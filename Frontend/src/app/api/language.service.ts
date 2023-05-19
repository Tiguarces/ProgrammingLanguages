import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Language } from './model/Language';
import { Observable } from 'rxjs';
import { LanguageDetails } from './model/LanguageDetails';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private static readonly API: string = "http://localhost:8080/api/language/";

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<Language[]> {
    return this.httpClient.get<Language[]>(LanguageService.API + "all");
  }

  public getDetails(languageName: string): Observable<LanguageDetails> {
    return this.httpClient.get<LanguageDetails>(LanguageService.API + "get", {
      params: { "language": languageName }
    });
  }
}
