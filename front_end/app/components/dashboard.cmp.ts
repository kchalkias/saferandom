/// <reference path="../shared/ts.definitions/jquery.d.ts"/>
import {Component,ChangeDetectorRef, AfterViewInit, OnInit} from 'angular2/core';
import {Router} from 'angular2/router';
import {HeaderCmp} from './srheader.cmp';
import {Contest} from '../shared/models/Contest';
import {RequestsService} from '../shared/RequestsService';
import {Subject, BehaviorSubject, Observable} from "rxjs/Rx";

@Component({
  selector: 'sr-dashboard-cmp',
  template: `<sr-header-cmp></sr-header-cmp>
              <div class="view2" style="padding: 1em;">
                 <h2>Dashboard</h2>
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
                           <div [ngClass]="{disabled: contest.state >= 2 }" class="contesttab" (click)="editContest(contest)" *ngFor="#contest of contestsArray">
                             <h2>#{{contest.id}} - {{contest.title}}</h2>
                             <h3>State: </h3>{{contest.getStateTitle()}}
                             <br/>
                             <h3>Method: </h3>{{contest.getMethodTitle()}}
                             <br/>
                             <h3>Contest date: </h3>{{contest.getDate()}}
                           </div>
                          </div>
                          <div *ngIf="contestsArray.length==0">
                            <h2 class="nocontestsmsg">You don't have any contests yet</h2>
                          </div>
                       </div>
                     </div>
                     <div class="controls">
                       <!--<div class="circlecontrol edit"></div>-->
                       <div class="circlecontrol" (click)="showCreateContestPopup($event)">+</div>
                     </div>
                   </div>
               </template>
               <template [ngSwitchWhen]="2">
                   <div class="outerwrapper">
                     <div class="editcontestwrapper">
                       <h2>#{{contestForEdit.id}} - {{contestForEdit.title}}</h2>
                        <h3 style="display: inline-block">State: </h3>{{contestForEdit.getStateTitle()}}
                        <br/>
                        <h3 style="display: inline-block">Method: </h3>{{contestForEdit.getMethodTitle()}}
                        <br/>
                        <h3 style="display: inline-block">Contest date: </h3>{{contestForEdit.getDate()}}
                        <br/><br/>
                        <div class="line"></div><br/>
                        <h3>Participants:
                          <span *ngIf="!contestForEdit.participantsArray">There are no participants yet</span>
                        </h3>
                        <div *ngIf="contestForEdit.participantsArray">
                          <div class="participant" *ngFor="#participant of contestForEdit.participantsArray">
                            <span>id: {{participant.identifier}}, name: {{participant.title}}</span>
                          </div>
                        </div>   
                        <br/>
                        <div (click)="showParticipantsPopup(contestForEdit)" class="clickbutton green" style="padding: 0.5em 2em">Add Participants</div>                  
                        <textarea id="participantsTextArea" style="width: 100%; min-height:15em; margin-top: 0.5em;" *ngIf="showParticipantsTextArea">{{participantsText}}</textarea>
                        <br/><br/>
                        <div class="line"></div>
                        <br/>
                        <div style="width: 100%; text-align: right">
                          <div class="editbtns clickbutton green" (click)="saveChanges(contestForEdit)">SAVE</div>
                          <div class="editbtns clickbutton green" (click)="closeEditPopup($event)">CANCEL</div>
                        </div>
                     </div>
                   </div>
               </template>
               <template [ngSwitchWhen]="3">
                   <div class="outerwrapper">
                     <div class="editcontestwrapper">
                        <h2>Enter title: <input style="font-size: 0.8em;" id="contesttitle" type="text" placeholder="contest title"/></h2>
                        <br/>
                        <h2 style="display:inline-block;">Method: </h2>
                        <select id="contesttype" style="font-size: 1em;margin-left: 0.2em;">
                          <option value="1">Bitcoin block</option>
                          <option value="2">S&P 500 stocks</option>
                          <option value="3">Weather Underground data</option>
                          <option value="4">Official Flight Landing Timestamps</option>
                        </select>
                        <br/>
                        <br/>
                        <h2>Contest date: 
                          <input style="font-size: 0.8em;" id="contestendtime" type="datetime-local"/>
                          </h2>
                        <br/>
                        <div class="line"></div><br/>
                        <br/>
                        <div style="width: 100%; text-align: right">
                          <div class="editbtns clickbutton green" (click)="createAContest($event)">SAVE</div>
                          <div class="editbtns clickbutton green" (click)="closeEditPopup($event)">CANCEL</div>
                        </div>
                     </div>
                   </div>
               </template>
               <div class="footer" style="width:100%; position:fixed; bottom: 0;background-color:#cacaca; padding: 1.2em 0em; color: #949494;text-align:center;">Powered by BigDataNauts</div>
             </div>`,
  directives: [HeaderCmp],
  host: {
	   class: 'dashboard-cmp'
  }
})

export class DashboardCmp implements OnInit, AfterViewInit {
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

  constructor(private mRouter: Router,
              private ref: ChangeDetectorRef,
              private mReqService: RequestsService) {

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
    //.do( () => loadingCallback(true) )
    .switchMap((text: string) => this.mReqService.getContests(text)) //the actual search
    .subscribe(
      (response) => { this.showSearchResults(response) }, //success
      (error) => { console.log(error); });  //error
     // () => { completeCallback(false) }); //on complete

    this.inputObservable.debounceTime(250);
  

    //now fetch the contests
    let contestsObs: Observable<Contest[]> = this.mReqService.getContests();
    
    contestsObs.subscribe((contests: Contest[]) => {
        //  console.log(contests);
        this.contestsArray = contests;

        this.ref.detectChanges();
    } );
  }


  private showSearchResults(contests: Contest[]):void{
      console.log(contests);
        this.contestsArray = contests;

        this.ref.detectChanges();
  }

  private showCreateContestPopup():void{
    this.mViewState = 3;
  }

  private editContest(contest: Contest): void{
    if (contest.state < 1) {
      this.contestForEdit = contest;
      this.mViewState = 2;

      this
        .mReqService
        .getContestDetails(contest.id)
        .subscribe((contest: Contest) => {
          this.contestForEdit = contest;
          this.mViewState = 2;
        });
    }
  }

  private closeEditPopup(): void{
    this.contestForEdit = null;
    this.mViewState = 1;

    setTimeout(() => {
      let left: number = $(".outerwrapper .wrapper").offset().left;
      let bottom: number = $(".footer").outerHeight();

      $(".controls").css("left", `${left}px`).css("bottom", `${bottom + 10}px`);
    

    }, 50);
  }

  private showParticipantsPopup(contest: Contest):void{
    this.showParticipantsTextArea = true;

    let participants: string = "";

    if (contest.participantsArray){
      contest.participantsArray.forEach( (participant: Object) => {
        if(participant != "undefined")
          participants += `{id: ${participant.id}, title: "${participant.title}"}\n`;
      } );
    }

    // this.participantsText = participants;
    // $("#participantsTextArea").val(participants);
  }


  private saveChanges(contest: Contest): void{
    //take the 
    //if(  )
    let participantsText: string = $("#participantsTextArea").val();

    if( participantsText ){
      //create the participants array
      let participantsArray: Array<Object> = new Array<Object>(),
          participantsPerLine = participantsText.split("\n");

      participantsArray = participantsPerLine.map((partTxt) => {
        var pSplit = partTxt.split(",");
        return {id:pSplit[0], title:pSplit[1]};
      });

      console.log(participantsArray);

      this
      .mReqService
      .saveContestParticipants(this.contestForEdit.id, participantsArray)
      .subscribe((result: Object) => {
        if (!result.error) {
          alert("Changes successfully made!");
          this.closeEditPopup();
        }
      });
    }

    //  console.log($("#participantsTextArea").val());
  }

  private showView(viewName: string, parameters?: Object) {
    this.mRouter.navigate([viewName, parameters]);
  }

  private showPaymentView(selectedPlan: string, selectedPrice: number): void {
    this.selectedPlan = selectedPlan;
    this.selectedPrice = selectedPrice;
    this.mViewState = 3;
  }

  private showFinalizeView() {
    this.mViewState = 4;
  }

  private showSignup(): void {
    this.mViewState = 2; //show the 1st step of the sign up
  }

  private createAContest(): void {
    let title: string = $("#contesttitle").val();
    let ctype: number = $("#contesttype").val();
    let endTime: number = new Date($("#contestendtime").val() ).getTime();

    console.log(title,ctype,endTime);

    this
      .mReqService
      .addNewContest(title, ctype, endTime)
      .subscribe( (result: Object) =>{
          if(!result.error){
              this.closeEditPopup();

              let contestsObs: Observable<Contest[]> = this.mReqService.getContests();
              contestsObs.subscribe((contests: Contest[]) => {
               //  console.log(contests);
                  this.contestsArray = contests;

                  this.ref.detectChanges();
              });      
          }else{
            alert('There was an error while saving the changes');
          }
      } );
  }
}