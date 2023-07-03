import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, Subject, map, tap } from "rxjs";
import { ApiResponse, RandomData } from "../models";

const url = 'http://localhost:8080/api/random'

@Injectable()
export class RandomService {

    onRequest = new Subject<ApiResponse>()

    constructor (
        private http : HttpClient
    ) {}

    getRandomNumbers(count = 1): Observable<number[]> {

        const qs = new HttpParams().set("count", count)
        
        return this.http.get<ApiResponse>(url, {params : qs})
                .pipe(
                    tap(resp => this.onRequest.next(resp)),
                    map(resp => resp.numbers)
                )
    }

    postRandomNumbersAsJson(data: RandomData): Observable<number[]> {
        // Content-Type: application/json
        // Accept: application/json

        return this.http.post<ApiResponse>(url, data)
                .pipe(
                    tap(resp => this.onRequest.next(resp)),
                    map(resp => resp.numbers)
                )
    }

    postRandomNumbersAsForm(data: RandomData): Observable<number[]> {
        // Content-Type: application/x-www-form-urlencoded
        // Accept: application/json

        // toString() => min=-100&max=10&count=10
        const form = new HttpParams()
            .set("min", data.min)
            .set("max", data.max)
            .set("count", data.count)

        const headers = new HttpHeaders()
            .set("Content-Type", "application/x-www-form-urlencoded")

        return this.http.post<ApiResponse>(url, form.toString(), {headers})
                .pipe(
                    tap(resp => this.onRequest.next(resp)),
                    map(resp => resp.numbers)
                )
    }
}