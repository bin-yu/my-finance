'use strict';

angular.module('myFinance').controller('PhysicalAccountCtrl', ['$scope', '$log', 'RestService', 'AccountService','physicalAccountList',
function($scope, $log, RestService, AccountService,physicalAccountList) {
	//initialize account list
	$scope.physicalAccountList = physicalAccountList;
	
	$scope.addNewAccount = function() {
		AccountService.addPhysicalAccount($scope.newAccount).then(function(data){
			$scope.physicalAccountList.push(jQuery.extend(true, {}, data));
		});
	};
	$scope.saveAccount = function(account) {
		delete account.isInEditMode;
		AccountService.updatePhysicalAccount(account);
	};
	$scope.deleteAccount = function(idx) {
		AccountService.deletePhysicalAccount($scope.physicalAccountList[idx]).then(function(){
			$scope.physicalAccountList.splice(idx, 1);
		});
	};

}]);
