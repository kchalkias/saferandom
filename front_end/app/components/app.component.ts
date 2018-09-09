/**
 * Created by pavlos on 23/4/2016.
 */
import {Component, OnInit} from 'angular2/core';
import {Router, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import {Type} from "angular2/core";
import {HomeCmp} from './home.cmp';
import {RegisterCmp} from './register.cmp';
import {DashboardCmp} from './dashboard.cmp';
import {SearchCmp} from './searchforcontest.cmp';

@Component({
    selector: 'sr-app',
    template: `<router-outlet></router-outlet>`,
    directives: [ROUTER_DIRECTIVES],
    host:{
	   class: 'app-cmp'
    }
})

@RouteConfig([
    { path: "/home", name: "Home", component: <Type>HomeCmp, useAsDefault: true },
    { path: "/register", name: "Register", component: <Type>RegisterCmp },
    { path: "/dashboard", name: "Dashboard", component: <Type>DashboardCmp},
    { path: "/search", name: "Search", component: <Type>SearchCmp},
    { path: "/**", redirectTo: ['/Home']}
])
export class AppComponent implements OnInit{ 

    public constructor(){

    }


    ngOnInit() {

    }
}