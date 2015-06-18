'use strict';

angular.module('myFinance').controller('PhysicalAccountCtrl', ['$scope', '$log', 'RestService',
function($scope, $log, RestService) {
	$scope.physicalAccountList = [];
	$scope.reloadAccountList = function() {
		$scope.physicalAccountList.slice(0,$scope.physicalAccountList.length);
		var result = {
			setData : function(data, headers) {
				$scope.physicalAccountList = data;
			},
			setError : function(status, errorData) {
				$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
			}
		};
		RestService.doGet('physicalAccounts', result);
	};
	$scope.addNewAccount = function() {
		var result = {
			setData : function(data, headers) {
				$scope.physicalAccountList.push(jQuery.extend(true, {}, data));
			},
			setError : function(status, errorData) {
				$log.error('failed to add this physical account, the reason is : \n' + errorData);
				alert("failed to add account!");
			}
		};
		RestService.doPost('physicalAccounts', result, {
			data : $scope.newAccount
		});
	};
	$scope.saveAccount = function(account) {
		delete account.isInEditMode;
		var result = {
			setData : function(data, headers) {
			},
			setError : function(status, errorData) {
				$log.error('failed to update this physical account, the reason is : \n' + errorData);
				alert('failed to update this physical account, the reason is : \n' + errorData);
			}
		};
		RestService.doPut('physicalAccount', result, {
			pathParametersMap : {
				id : account.id
			},
			data : account
		});
	};
	$scope.deleteAccount = function(idx) {
		
		var account = $scope.physicalAccountList[idx];

		var result = {
			setData : function(data, headers) {
				$scope.physicalAccountList.splice(idx, 1);
			},
			setError : function(status, errorData) {
				$log.error('failed to update this physical account, the reason is : \n' + errorData);
				alert('删除失败 : \n' + errorData);
			}
		};
		RestService.doDelete('physicalAccount', result, {
			pathParametersMap : {
				id : account.id
			}
		});
	};

	//initialize account list
	
	$scope.reloadAccountList();
}]);
