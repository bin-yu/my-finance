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
		controller : 'VirtualAccountCtrl'
	}).when('/divide-money', {
		templateUrl : 'views/account/divide-money.html',
		controller : 'DivideMoneyCtrl'
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
	virtualAccounts : '/virtual_accounts',
	virtualAccount : '/virtual_accounts/{id}',
});

//use this directive to allow jquery mobile to render subviews
myApp.directive('jqm', function($timeout) {
  return {
    link: function(scope, elm, attr) {
        //$timeout(function(){
            elm.trigger('create');
        //});
    }
  };
});

myApp.directive('jslider', function () {
    return {
        scope: {
            id: '@sliderid',
            label: '@',
            allocated: '=allocated',
            min: '@',
            max: '@',
            step: '@',
            disabled: '@',
            mini: '@',
            highlight: '@',
            start: '&',
            stop: '&'
        },
        restrict: 'E',
        replace: false,
        templateUrl: 'templates/jslider.html',
        compile: function (e) {
            e.trigger('create');
            return {
                post: function (s, e, a) {
                    e.find('a').click(function (e) {
                        return e.preventDefault();
                    });

                    e.find('div.ui-slider').find('input[type="range"]').on('slidestop', function () {
                        return s.stop();
                    });

                    e.find('div.ui-slider').find('input[type="range"]').on('slidestart', function () {
                        return s.start();
                    });
                    e.find('div.ui-slider').find('input[type="number"]').change(function () {
                        return s.stop();
                    });

                    s.$watch('allocated', function () {
                        return e.find('#' + a.id).slider('refresh');
                    });
                }
            };
        }
    };
});