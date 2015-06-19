'use strict';

angular.module('myFinance').controller('VirtualAccountCtrl', ['$scope', '$log', 'RestService',
function($scope, $log, RestService) {
	$scope.virtualAccountList = [];
	$scope.reloadAccountList = function() {
		$scope.virtualAccountList.slice(0,$scope.virtualAccountList.length);
		var result = {
			setData : function(data, headers) {
				$scope.virtualAccountList = data;
			},
			setError : function(status, errorData) {
				$log.error('failed to retrieve virtual account list, the reason is : \n' + errorData);
			}
		};
		RestService.doGet('virtualAccounts', result);
	};
	$scope.addNewAccount = function() {
		var result = {
			setData : function(data, headers) {
				$scope.virtualAccountList.push(jQuery.extend(true, {}, data));
			},
			setError : function(status, errorData) {
				$log.error('failed to add this virtual account, the reason is : \n' + errorData);
				alert("failed to add account!");
			}
		};
		RestService.doPost('virtualAccounts', result, {
			data : $scope.newAccount
		});
	};
	$scope.saveAccount = function(account) {
		delete account.isInEditMode;
		var result = {
			setData : function(data, headers) {
			},
			setError : function(status, errorData) {
				$log.error('failed to update this virtual account, the reason is : \n' + errorData);
				alert('failed to update this virtual account, the reason is : \n' + errorData);
			}
		};
		RestService.doPut('virtualAccount', result, {
			pathParametersMap : {
				id : account.id
			},
			data : account
		});
	};
	$scope.deleteAccount = function(idx) {
		
		var account = $scope.virtualAccountList[idx];

		var result = {
			setData : function(data, headers) {
				$scope.virtualAccountList.splice(idx, 1);
			},
			setError : function(status, errorData) {
				$log.error('failed to update this virtual account, the reason is : \n' + errorData);
				alert('删除失败 : \n' + errorData);
			}
		};
		RestService.doDelete('virtualAccount', result, {
			pathParametersMap : {
				id : account.id
			}
		});
	};

	//initialize account list
	
	$scope.reloadAccountList();
}]);
