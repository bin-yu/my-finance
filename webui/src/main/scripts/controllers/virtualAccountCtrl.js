'use strict';

angular.module('myFinance').controller('VirtualAccountCtrl', ['$scope', '$log', 'RestService','AccountService',
function($scope, $log, RestService,AccountService) {
	//load virtualAccountList
	$scope.virtualAccountList = [];
	AccountService.asyncGetVirtualAccountList().then(function(list) {
		$scope.virtualAccountList = list;
	});
	
	$scope.addNewAccount = function() {
		RestService.doPost('virtualAccounts', {
			data : $scope.newAccount
		}).then(
			function(data, headers) {
				$scope.virtualAccountList.push(jQuery.extend(true, {}, data));
				AccountService.flushVirtualAccountList();
			},
			function(status, errorData) {
				$log.error('failed to add this virtual account, the reason is : \n' + errorData);
				alert("failed to add account!");
			});
	};
	$scope.saveAccount = function(account) {
		delete account.isInEditMode;
		RestService.doPut('virtualAccount', {
			pathParametersMap : {
				id : account.id
			},
			data : account
		}).then(function(data, headers) {
				AccountService.flushVirtualAccountList();
			},
			function(status, errorData) {
				$log.error('failed to update this virtual account, the reason is : \n' + errorData);
				alert('failed to update this virtual account, the reason is : \n' + errorData);
			});
	};
	$scope.deleteAccount = function(idx) {
		RestService.doDelete('virtualAccount', {
			pathParametersMap : {
				id : $scope.virtualAccountList[idx].id
			}
		}).then(function(data, headers) {
				$scope.virtualAccountList.splice(idx, 1);
				AccountService.flushVirtualAccountList();
			},
			function(status, errorData) {
				$log.error('failed to update this virtual account, the reason is : \n' + errorData);
				alert('删除失败 : \n' + errorData);
			});
	};
	/*
	$scope.getFormName= function(account){
			return 'form_'+(account.id>=0?account.id:'_'+account.id);
		};*/
	
}]);
