/// <reference path="../shared/ts.definitions/jquery.d.ts"/>
import {Component, ChangeDetectorRef, AfterViewInit, OnInit} from 'angular2/core';
import {Router} from 'angular2/router';
import {HeaderCmp} from './srheader.cmp';
import {Contest} from '../shared/models/Contest';
import {RequestsService} from '../shared/RequestsService';
import {Subject, BehaviorSubject, Observable} from "rxjs/Rx";

@Component({
  selector: 'sr-search-cmp',
  template: `<sr-header-cmp></sr-header-cmp>
              <div class="view2" style="padding: 1em;">
                 <h2>Search for a contest</h2>
                 <div class="line"></div>
              <div [ngSwitch]="mViewState">
                <template [ngSwitchWhen]="1">
                   <div class="outerwrapper">
                     <div class="wrapper">
                       <div class="searchbarwrapper">
                         <input id="contestsearchinput" type="text" placeholder="Search for a contest"/>
                       </div>
                       <div class="contestslist">
                         <div *ngIf="contestsArray.length>0">
                           <div class="contesttab" *ngFor="#contest of contestsArray">
                             <h2>#{{contest.id}} - {{contest.title}}</h2>
                             <h3>State: </h3>{{contest.getStateTitle()}}
                             <br/>
                             <div *ngIf="contest.state==2">
                               <h3>Winning hash: </h3>{{contest.wintoken}}
                             </div>
                             <h3>Contest date: </h3>{{contest.getDate()}}
                             <br/>
                             <div style="padding: 0.5em 0em; margin-top: 1em; width: 100%;" class="clickbutton green" (click)="showContestResults(contest)">
                               Show Results
                             </div>
                             <br/>
                             <div class="table" style="margin-top: 1em;" *ngIf="contest.contestResultsArray.length > 0">
                               <div class="row">
                                 <div class="cell" *ngIf="contest.state == 2">Order</div>
                                 <div class="cell">Id</div>
                                 <div class="cell">Name</div>
                               </div>
                               <div class="row" [ngClass]="{ win: participant.myorder == 1}" *ngFor="#participant of contest.contestResultsArray">
                                 <div class="cell" *ngIf="contest.state == 2">{{participant.myorder}}</div>
                                 <div class="cell">{{participant.identifier}}</div>
                                 <div class="cell">
                                   {{participant.title}}
                                   <span *ngIf="participant.myorder == 1"> - WINNER!</span>
                                 </div>
                               </div>
                             </div>
                           </div>
                          </div>
                          <div *ngIf="contestsArray.length==0">
                            <h2 class="nocontestsmsg">No contests found yet</h2>
                          </div>
                       </div>
                     </div>
                   </div>
               </template>
               <div class="footer" style="width:100%; position:fixed; bottom: 0;background-color:#cacaca; padding: 1.2em 0em; color: #949494;text-align:center;">Powered by BigDataNauts</div>
             </div>`,
  directives: [HeaderCmp],
  host: {
	   class: 'search-cmp'
  }
})

export class SearchCmp implements OnInit, AfterViewInit {
  private CONTESTTYPES: Array<Object> = Contest.types;
  private showRegisterPopup: boolean = true;
  private mViewState: number = 1;
  private selectedPlan: string;
  private selectedPrice: number;
  private contestsArray: Array<Contest> = new Array<Contest>();
  private inputObservable: BehaviorSubject<Contest>;
  private contestForEdit: Contest;
  private showParticipantsTextArea: boolean = false;
  private participantsText: string;

  //private contestResultsArray: Array<Object>;

  constructor(private mRouter: Router,
    private ref: ChangeDetectorRef,
    private mReqService: RequestsService) {

    this.contestResultsArray = new Array<Object>();
  }

  ngOnInit() {


    this.contestsArray = new Array<Contest>();
    //this.contestsArray.push(new Contest("1", "Contest1", 0, 1, " 24/05/2016, 14:00:00"));

  }

  ngAfterViewInit() {
    let left: number = $(".outerwrapper .wrapper").offset().left;
    let bottom: number = $(".footer").outerHeight();

    $(".controls").css("left", `${left}px`).css("bottom", `${bottom + 10}px`);



    this.inputObservable = <BehaviorSubject<Contest>>Observable.fromEvent($("#contestsearchinput"), "keyup");

    this.inputObservable
      .map((element: any) => { return element.target.value }) //map the input value
      .filter((text: string) => { return text.length >= 0 }) //search only for values more than 1 characters
      //.do(() => { this.contestResultsArray = new Array<Object>() } )
      .switchMap((text: string) => this.mReqService.getContests(text)) //the actual search
      .subscribe(
      (response) => { this.showSearchResults(response) }, //success
      (error) => { console.log(error); });  //error
    // () => { completeCallback(false) }); //on complete

    this.inputObservable.debounceTime(250);


    //now fetch the contests
    /*
    let contestsObs: Observable<Contest[]> = this.mReqService.getContests();

    contestsObs.subscribe((contests: Contest[]) => {
      //  console.log(contests);
      this.contestsArray = contests;

      this.ref.detectChanges();
    });
    */
  }

  public showContestResults(contest:Contest):void{

    this
    .mReqService
    .getContestDetails(contest.id)
    .subscribe((resultcontest: Contest) => {
        let results: Array<Object> = resultcontest.participantsArray;

        results.sort(( a, b )=>{
          return a.myorder - b.myorder;
        });

        //results = results.filter( (participant: Object) => {
        //  return participant.myorder > 0;
        //});

        contest.contestResultsArray = results;
      });

  }


  private showSearchResults(contests: Contest[]): void {
    console.log(contests);
    this.contestsArray = contests;

    this.ref.detectChanges();
  }


  private showView(viewName: string, parameters?: Object) {
    this.mRouter.navigate([viewName, parameters]);
  }

}