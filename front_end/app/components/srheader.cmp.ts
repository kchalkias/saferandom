/// <reference path="../shared/ts.definitions/jquery.d.ts"/>
import {Component, OnInit} from 'angular2/core';
import {Router} from 'angular2/router';


@Component({
  selector: 'sr-header-cmp',
  template: `<div>
                <img (click)="showHomeView($event)" class="logo" src="/assets/img/logo.png"/><br/>
                <span>The world’s first public verifiable random engine</span>
            </div>`,
  host: {
	   class: 'sr-header-cmp'
  }
})

export class HeaderCmp implements OnInit {
  private showRegisterPopup: boolean = true;
  private mViewState: number = 1;

  constructor(private mRouter: Router) {

  }

  ngOnInit() {
    let windowHeight = $(window).outerHeight();
    let logoHeight = $(".wrapper .top").outerHeight();

    $(window).scroll(function(event) {
      let scrollpos = $(this).scrollTop();

      //console.log(`${scrollpos + logoHeight/2}`);

      if (scrollpos + logoHeight / 2 > windowHeight) {
        $(".wrapper .top").addClass("back");
      } else {
        $(".wrapper .top").removeClass("back");
      }
      //console.log(`${scrollpos}`);
    });

  }

  private showHomeView():void{
    this.showView("Home");
  }

  private showView(viewName: string, parameters?: Object) {
    this.mRouter.navigate([viewName, parameters]);
  }

  private showSignup(): void {
    //this.showView("Signup");
    this.showRegisterPopup = false;

  }
}