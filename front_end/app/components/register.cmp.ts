/// <reference path="../shared/ts.definitions/jquery.d.ts"/>
import {Component, OnInit} from 'angular2/core';
import {Router} from 'angular2/router';
import {HeaderCmp} from './srheader.cmp';

@Component({
  selector: 'sr-register-cmp',
  template: `<div [ngSwitch]="mViewState">
               <template [ngSwitchWhen]="1">
                 <div class="wrapper">
                   <h2>Registration</h2>
                   <form>
                     <input type="text" name="username" placeholder="Enter your username"/><br/>
                     <input type="text" name="email" placeholder="Enter your email"/><br/>
                     <input type="text" name="password" placeholder="Enter a password"/><br/>
                     <div class="clickbutton green" (click)="showSignup($event)">Register</div>
                   </form>
                   <br/>
                   <div style="text-align:center">Already have an account? <span style="cursor:pointer; font-weight: bold" (click)="showDashboard($event)">Login</span>
                   </div>
                 </div>
               </template>
               <template [ngSwitchWhen]="2">
                 <sr-header-cmp></sr-header-cmp>
                 <div class="view2" style="padding: 1em;">
                   <h2>Create your account</h2>
                   <div class="state"></div>
                   <div class="line"></div>
                   <div class="plans">
                     <div class="plan basic">
                       <h2>Basic Plan</h2>
                       <div class="clickbutton green" (click)="showPaymentView('Basic Plan', 20)">SELECT PLAN</div>
                     </div>
                     <div class="plan ultimate">
                       <h2>Ultimate Plan</h2>
                       <div class="clickbutton green" (click)="showPaymentView('Ultimate Plan', 50)">SELECT PLAN</div>
                     </div>
                     <div style="clear:both"></div>
                   </div>
                 </div>
               </template>
               <template [ngSwitchWhen]="3">
                 <sr-header-cmp></sr-header-cmp>
                 <div class="view3" style="padding: 1em;">
                   <h2>Create your account</h2>
                   <div class="state position2"></div>
                   <div class="line"></div>
                   <div style="width:100%; text-align:center;">
                     <br/>
                     <h2>You will buy: {{selectedPlan}}</h2><br/>
                     <div class="paymentform">
                       <h3>Amount</h3>
                       <input value="\${{selectedPrice}}" disabled>
                       <h3>Credit card number</h3>
                       <input value="">
                       <h3>Name on card</h3>
                       <input value="">
                       <h3>Exp Date</h3>
                       <input value="">
                       <h3>CVV/CVV2</h3>
                       <input value="">
                       <br/>   
                       <div class="clickbutton green" (click)="showFinalizeView($event)">Pay {{selectedPrice}}</div>
                    
                     </div>
                   </div>
                 </div>
               </template>
               <template [ngSwitchWhen]="4">
                  <sr-header-cmp></sr-header-cmp>
                   <div class="view4" style="padding: 1em;">
                     <h2>Create your account</h2>
                     <div class="state position3"></div>
                     <div class="line"></div>
                     <div class="ordercompleted">
                       <h1>Your order has been completed<br/>
                           Now let’s create some <span style="color:#679239">safe</span> contests :-)</h1>
                       <br/>
                       <div class="clickbutton green" (click)="createAContest($event)">CREATE A CONTEST</div>
                      </div>
                  </div>
               </template>
               <div class="footer" style="width:100%; position: absolute; bottom: 0;background-color:#cacaca; padding: 1.2em 0em; color: #949494;text-align:center;">Powered by BigDataNauts</div>
             </div>`,
  directives: [HeaderCmp],
  host: {
	   class: 'register-cmp'
  }
})

export class RegisterCmp implements OnInit {
  private showRegisterPopup: boolean = true;
  private mViewState: number = 1;
  private selectedPlan: string;
  private selectedPrice: number;

  constructor(private mRouter: Router) {

  }

  ngOnInit() {

  }

  private showView(viewName: string, parameters?: Object) {
    this.mRouter.navigate([viewName, parameters]);
  }

  private showPaymentView(selectedPlan: string, selectedPrice: number):void{
    this.selectedPlan = selectedPlan;
    this.selectedPrice = selectedPrice;
    this.mViewState = 3;
  }

  private showFinalizeView(){
    this.mViewState = 4;
  }

  private showSignup(): void {
    this.mViewState = 2; //show the 1st step of the sign up
  }

  private createAContest(): void {
    this.showView("Dashboard");
  }

  private showDashboard():void{
    this.showView("Dashboard");
  
  }
}