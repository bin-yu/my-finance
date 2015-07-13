'use strict';

angular.module('myFinance').controller('VirtualAccountCtrl', ['$scope', '$log', 'RestService','AccountService','virtualAccountList',
function($scope, $log, RestService,AccountService,virtualAccountList) {
	//load virtualAccountList
	$scope.virtualAccountList = virtualAccountList;
	
	$scope.addNewAccount = function() {
		AccountService.addVirtualAccount($scope.newAccount).then(function(data){
			$scope.virtualAccountList.push(jQuery.extend(true, {}, data));
		});
	};
	$scope.saveAccount = function(account) {
		delete account.isInEditMode;
		AccountService.updateVirtualAccount(account);
	};
	$scope.deleteAccount = function(idx) {
		AccountService.deleteVirtualAccount($scope.virtualAccountList[idx]).then(function(){
			$scope.virtualAccountList.splice(idx, 1);
		});
	};
	$scope.prepareEdit = function(account){
		account.isInEditMode=true;
	};
}]);
