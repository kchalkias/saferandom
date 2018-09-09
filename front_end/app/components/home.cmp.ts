/// <reference path="../shared/ts.definitions/jquery.d.ts"/>
import {Component, OnInit} from 'angular2/core';
import {Router} from 'angular2/router';


@Component({
    selector: 'sr-home-cmp',
    template: `<div class="wrapper">
                  <div class="top">
                      <div id="logo">
                          <img class="logo" src="/assets/img/logo.png"/><br/>
                          <span>The world’s first public verifiable random engine</span>
                      </div>
                  </div>
    			  <div class="left">
    			  	<div class="middle">
    			  		<span>FOR CONTESTANTS</span>
    			  		<br/>
    			  		<br/>
    			  		<div class="clickbutton green" (click)="searchForContest($event)">SEARCH A CONTEST</div>
    			  	</div>
    			  	<div class="bottom">
    			  		<img class="search" src="/assets/img/search_ic.png"/>
    			  	</div>
    			  </div>
    			  <div class="right">
                      <div class="middle">
                          <span>FOR CONTEST ORGANIZERS</span>
                      <br/>
                           <br/>
                              <div class="clickbutton blue" (click)="organizeContest($event)">ORGANIZE A CONTEST</div>
                      </div>
                      <div class="bottom">
                          <img class="search" src="/assets/img/cup_ic.png"/>
                      </div>
    			  </div>
    		   </div>`,    
    host:{
	   class: 'home-cmp'
    }
})

export class HomeCmp implements OnInit{ 

	constructor(private mRouter: Router){

	}

    ngOnInit() {
        let windowHeight = $(window).outerHeight();
        let logoHeight = $(".wrapper .top").outerHeight();

        $(window).scroll(function(event){
            let scrollpos = $(this).scrollTop();

            //console.log(`${scrollpos + logoHeight/2}`);

            if (scrollpos + logoHeight / 2 > windowHeight){
                $(".wrapper .top").addClass("back");
            }else{
                $(".wrapper .top").removeClass("back");
            }
                //console.log(`${scrollpos}`);
        });

    }

    private showView(viewName: string, parameters?: Object) {
        this.mRouter.navigate([ viewName, parameters]);
    }

    private searchForContest(): void {
      this.showView("Search");
    }

    private organizeContest(): void {
		  this.showView("Register");

    }
}