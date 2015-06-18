'use strict';

var myApp = angular.module('myFinance', ['ngRoute','ngAnimate','mobile-angular-ui']);

myApp.config(['$routeProvider',
function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'views/home-view.html',
		controller : 'HomeCtrl'
	}).when('/manage-records', {
		templateUrl : 'views/record/manage-records.html',
		controller : 'AddRecordCtrl'
	}).when('/month-records', {
		templateUrl : 'views/record/month-records.html',
		controller : 'AddRecordCtrl'
	}).when('/add-record', {
		templateUrl : 'views/record/add-record.html',
		controller : 'AddRecordCtrl'
	}).when('/search-records', {
		templateUrl : 'views/record/search-records.html',
		controller : 'AddRecordCtrl'
	}).when('/manage-accounts', {
		templateUrl : 'views/account/manage-accounts.html',
		controller : 'AddRecordCtrl'
	}).when('/pysical-accounts', {
		templateUrl : 'views/account/pysical-accounts.html',
		controller : 'PhysicalAccountCtrl'
	}).when('/virtual-accounts', {
		templateUrl : 'views/account/virtual-accounts.html',
		controller : 'AddRecordCtrl'
	}).when('/divide-money', {
		templateUrl : 'views/account/divide-money.html',
		controller : 'AddRecordCtrl'
	}).when('/manage-reports', {
		templateUrl : 'views/report/manage-reports.html',
		controller : 'AddRecordCtrl'
	}).when('/month-reports', {
		templateUrl : 'views/report/month-reports.html',
		controller : 'AddRecordCtrl'
	}).when('/history-reports', {
		templateUrl : 'views/report/history-reports.html',
		controller : 'AddRecordCtrl'
	}).otherwise({
		redirectTo : '/'
	});
}]); 
myApp.constant('HostConfig', {
	isCors : true,
	protocol : 'http',
	host : 'localhost',
	port : 8080,
	contextPath : 'my-finance-backend'
}).constant('RestResources', {
	physicalAccounts : '/physical_accounts',
	physicalAccount : '/physical_accounts/{id}',
});