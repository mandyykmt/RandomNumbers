import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { RandomService } from '../services/random.service';
import { ApiResponse } from '../models';

@Component({
  selector: 'app-logger',
  templateUrl: './logger.component.html',
  styleUrls: ['./logger.component.css']
})
export class LoggerComponent implements OnInit {

  nums$!: Observable<ApiResponse>

  constructor(
    private randSvc : RandomService
  ) {}

  ngOnInit(): void {
      this.nums$ = this.randSvc.onRequest
  }

}
