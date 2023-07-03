import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { RandomData } from './models';
import { RandomService } from './services/random.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  form! : FormGroup
  data!: RandomData

  nums$!: Observable<number[]>

  constructor(
    private fb : FormBuilder,
    private randSvc : RandomService
  ) {}

  createForm() : FormGroup {
    return this.fb.group({
      min: this.fb.control<number>(0),
      max: this.fb.control<number>(0),
      count: this.fb.control<number>(5)
    })
  }
  
  getNumbers() {
    this.nums$ = this.randSvc.getRandomNumbers()
  }

  postWithJson() {
    const data: RandomData = this.form.value
    console.info(">> data: ", data)
    this.nums$ = this.randSvc.postRandomNumbersAsJson(data)
  }

  postWithForm() {
    const data: RandomData = this.form.value
    console.info(">> data: ", data)
    this.nums$ = this.randSvc.postRandomNumbersAsForm(data)
  }

  ngOnInit(): void {
      this.form = this.createForm()
  }
}