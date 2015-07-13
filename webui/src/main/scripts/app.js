'use strict';

//disable jquery mobile page transition, instead we use angular-route
$.mobile.linkBindingEnabled = false;
$.mobile.hashListeningEnabled = false;
//close left side bar when click any menu of it on mobile screen.
var closeLeftBar = function (){
	$('#leftbar').panel( 'close' );
};
$('#leftbar,a').click(closeLeftBar);

//Below is the definition of the main module 
var myApp = angular.module('myFinance', ['ngRoute','ngAnimate',/*'mobile-angular-ui'*/]);

myApp.config(['$routeProvider',
function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'views/home-view.html',
		controller : 'HomeCtrl'
	}).when('/manage-records', {
		templateUrl : 'views/record/manage-records.html',
		//controller : 'AddRecordCtrl'
	}).when('/month-records', {
		templateUrl : 'views/record/month-records.html',
		controller : 'MonthRecordCtrl',
		resolve : {
			transactionsOfThisMonth : function(AccountTransactionService){
				return AccountTransactionService.getTransactionListOfThisMonth();
			}
		}
	}).when('/add-record', {
		templateUrl : 'views/record/add-record.html',
		controller : 'AddRecordCtrl',
		resolve : {
			physicalAccountList : function(AccountService){
				return AccountService.getPhysicalAccountList();
			},
			virtualAccountList : function(AccountService){
				return AccountService.getVirtualAccountList();
			}
		}
	}).when('/search-records', {
		templateUrl : 'views/record/search-records.html',
		controller : 'AddRecordCtrl'
	}).when('/manage-accounts', {
		templateUrl : 'views/account/manage-accounts.html',
		//controller : 'AddRecordCtrl'
	}).when('/pysical-accounts', {
		templateUrl : 'views/account/pysical-accounts.html',
		controller : 'PhysicalAccountCtrl',
		resolve : {
			physicalAccountList : function(AccountService){
				return AccountService.getPhysicalAccountList();
			}
		}
	}).when('/virtual-accounts', {
		templateUrl : 'views/account/virtual-accounts.html',
		controller : 'VirtualAccountCtrl',
		resolve : {
			virtualAccountList : function(AccountService){
				return AccountService.getVirtualAccountList();
			}
		}
	}).when('/divide-money', {
		templateUrl : 'views/account/divide-money.html',
		controller : 'DivideMoneyCtrl',
		resolve : {
			physicalAccountList : function(AccountService){
				return AccountService.getPhysicalAccountList();
			},
			virtualAccountList : function(AccountService){
				return AccountService.getVirtualAccountList();
			}
		}
	}).when('/manage-reports', {
		templateUrl : 'views/report/manage-reports.html',
		controller : 'AddRecordCtrl'
	}).when('/month-reports', {
		templateUrl : 'views/report/month-reports.html',
		//controller : 'AddRecordCtrl'
	}).when('/history-reports', {
		templateUrl : 'views/report/history-reports.html',
		//controller : 'AddRecordCtrl'
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
	virtualAccounts : '/virtual_accounts',
	virtualAccount : '/virtual_accounts/{id}',
	transactions : '/account_transactions',
	doBatchTransaction : '/account_transactions/batch'
});

//use this directive to allow jquery mobile to render subviews
myApp.directive('jqm', function($timeout) {
  return {
    link : function postLink(scope, element, attrs){
        $timeout(function(){
            element.trigger('create');
        });
    }
  };
});

//define loading page
myApp.run(['$rootScope', function($root) {
  $root.$on('$routeChangeStart', function(e, curr, prev) { 
    if (curr.$$route && curr.$$route.resolve) {
      // Show a loading icon until promises aren't resolved
      $root.loadingView = true;
    }
  });
  $root.$on('$routeChangeSuccess', function(e, curr, prev) { 
    // Hide loading icon
    $root.loadingView = false;
  });
}]);
