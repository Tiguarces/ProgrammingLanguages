import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main/main.component';
import { LangComponent } from './components/lang/lang.component';

const routes: Routes = [
  { path: "", component: MainComponent },
  { path: "lang/:name", component: LangComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
