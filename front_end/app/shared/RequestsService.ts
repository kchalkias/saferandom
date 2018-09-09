/// <reference path="./ts.definitions/jquery.d.ts"/>
import {Injectable} from 'angular2/core';
import {Subject, BehaviorSubject, Observable} from "rxjs/Rx";
import {Contest} from "./models/Contest";
import { HTTP_PROVIDERS } from 'angular2/http';
import {Headers, RequestOptions} from 'angular2/http';
import {Http, Response} from 'angular2/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';

@Injectable()
export class RequestsService {
	private static ip: string = "http://127.0.0.1:8081";
	private static APIKEY: string = "32e17e3b85e2f5db814d583e201c6f4f";

	constructor(private http: Http){}

	
	public getContests(keyword?: string): Observable<Contest[]>{
		let body;

		if (!keyword) {
			body = JSON.stringify({
				api_key: RequestsService.APIKEY,
				mine: true
			});
		}else{
			body = JSON.stringify({
				api_key: RequestsService.APIKEY,
				keyword: keyword,
				mine: true
			});
		}

    	let headers = new Headers({ 'Content-Type': 'application/json' });
  		let options = new RequestOptions({ headers: headers });

			return this.
				http.
				post(RequestsService.ip + "/api/getContests", body, options).
				map(this.extractData).
				map((jsonArray: Array<Object>) => {
					return jsonArray.map((data: Object) => {
						return new Contest(data.id, data.title, data.state, data.type, data.endtime, data.details, data.participations, data.winner, data.wintoken);
					});
				})
				.catch(this.handleError);

	}


	public getContestDetails(id: number): Observable<Contest>{
		let body = JSON.stringify({ contest_id : id});

    	let headers = new Headers({ 'Content-Type': 'application/json' });
	 		let options = new RequestOptions({ headers: headers }); 

		return this.
				http.
				post(RequestsService.ip + "/api/getContestDetails", body, options).
				map(this.extractData).
				map((data: Object) => {
				return new Contest(data.id, data.title, data.state, data.type, data.endtime, data.details, data.participations, data.winner, data.wintoken);
			})
				.catch(this.handleError);
	}


	public saveContestParticipants(id: number, arrayOfParticipants: Array<Object>): Observable<Object>{
		let body = JSON.stringify({ api_key: RequestsService.APIKEY,
									cid : id,
									participations: arrayOfParticipants});

	   	let headers = new Headers({ 'Content-Type': 'application/json' });
	 	let options = new RequestOptions({ headers: headers }); 
		
		return this.
				http.
				post(RequestsService.ip + "/api/addParticipations", body, options).
				map(this.extractData)
				.catch(this.handleError);

	}


	public addNewContest(title: string, type: number, endTime: number): Observable<Object> {
		let body = JSON.stringify({ api_key: RequestsService.APIKEY,
									title : title,
									type: type,
									endTime: endTime});

		let headers = new Headers({ 'Content-Type': 'application/json' });
		let options = new RequestOptions({ headers: headers }); 
		
		  console.log(body);

		return this.
				http.
				post(RequestsService.ip + "/api/addContest", body, options).
				map(this.extractData)
				.catch(this.handleError);	
	}


	 private handleError (error: any) {
	    // In a real world app, we might send the error to remote logging infrastructure
		let errMsg = error.message || 'Server error';
		console.error(errMsg); // log to console instead
		return Observable.throw(errMsg);
	  }

	private extractData(res: Response): any {
	    if (res.status < 200 || (res).status >= 300) {
	      throw new Error('Bad response status: ' + res.status);
	    }

		console.log(res.json());

	    return res.json() || { };
	}

}