'use strict';

angular.module('myFinance').controller('PhysicalAccountCtrl', ['$scope', '$log', 'RestService', 'AccountService',
function($scope, $log, RestService, AccountService) {
	//initialize account list
	$scope.physicalAccountList = [];
	AccountService.asyncGetPhysicalAccountList().then(function(list) {
		$scope.physicalAccountList = list;
	});
	$scope.addNewAccount = function() {
		RestService.doPost('physicalAccounts', {
			data : $scope.newAccount
		}).then(function(data, headers) {
			$scope.physicalAccountList.push(jQuery.extend(true, {}, data));
			AccountService.flushPhysicalAccountList();
		}, function(status, errorData) {
			$log.error('failed to add this physical account, the reason is : \n' + errorData);
			alert("failed to add account!");
		});
	};
	$scope.saveAccount = function(account) {
		delete account.isInEditMode;
		RestService.doPut('physicalAccount', {
			pathParametersMap : {
				id : account.id
			},
			data : account
		}).then(function(data, headers) {
			AccountService.flushPhysicalAccountList();
		}, function(status, errorData) {
			$log.error('failed to update this physical account, the reason is : \n' + errorData);
			alert('failed to update this physical account, the reason is : \n' + errorData);
		});
	};
	$scope.deleteAccount = function(idx) {
		RestService.doDelete('physicalAccount', {
			pathParametersMap : {
				id : $scope.physicalAccountList[idx]
			}
		}).then(function(data, headers) {
			$scope.physicalAccountList.splice(idx, 1);
			AccountService.flushPhysicalAccountList();
		}, function(status, errorData) {
			$log.error('failed to update this physical account, the reason is : \n' + errorData);
			alert('删除失败 : \n' + errorData);
		});
	};

}]);
