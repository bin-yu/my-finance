'use strict';

angular.module('myFinance').controller('DivideMoneyCtrl', ['$scope', '$log', 'RestService',
function($scope, $log, RestService) {

	$scope.allocating = {
		//refresh target virtual account
		refreshFromVirtualAccount : function() {
			if (this.targetPhysicalAccount && this.targetPhysicalAccount.mappedVirtualAccounts) {
				var vaList = this.targetPhysicalAccount.mappedVirtualAccounts;
				var i;
				for ( i = 0; i < vaList.length; i++) {
					if (vaList[i].virtualAccountId === -1) {
						this.targetVa = vaList[i];
						this.totalUnallocate = this.targetVa.amount;
						return;
					}
				}
			}
			this.targetVa = null;
			this.totalUnallocate = 0;
		},
		onTargetAccountChange : function() {
			this.refreshFromVirtualAccount();
			this.refreshAllocated();
		},
		//refresh allocated for all virtual accounts;
		refreshAllocated : function() {
			if (this.virtualAccountList) {
				var total = this.totalUnallocate;
				for (var i = 0; i < this.virtualAccountList.length; i++) {
					this.virtualAccountList[i].allocated = this.virtualAccountList[i].budget > total ? total : this.virtualAccountList[i].budget;
				}
			}
		},
		remain : function() {
			var remaining = this.totalUnallocate;
			if (this.virtualAccountList) {
				for (var i = 0; i < this.virtualAccountList.length; i++) {
					remaining -= this.virtualAccountList[i].allocated;
				}
			}
			return remaining;
		},
		setTargetPhysicalAccount : function(account) {
			this.targetPhysicalAccount = account;
		},
		initVirtualAccountList : function(list) {
			this.virtualAccountList = [];
			//remove unallocated account from the list
			for (var i = 0; i < list.length; i++) {
				if(list[i].id!=-1){
					this.virtualAccountList.push(list[i]);
				}
			}
			this.refreshAllocated();
		},
		execute : function() {
			if (this.targetVa && this.virtualAccountList) {
				var transactionList = [];
				var now=new Date().toUTCString();
				for (var i = 0; i < this.virtualAccountList.length; i++) {
					transactionList.push({
						date : now,
						type : 'TRANSFER',
						fromPhysicalAccountId : this.targetPhysicalAccount.id,
						fromVirtualAccountId : this.targetVa.virtualAccountId,
						toPhysicalAccountId : this.targetPhysicalAccount.id,
						toVirtualAccountId : this.virtualAccountList[i].id,
						amount : this.virtualAccountList[i].allocated,
						description : '分赃'
					});
				}
				var result = {
					setData : function(data, headers) {
						alert('success');
					},
					setError : function(status, errorData) {
						$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
						alert('failed:status=' + status);
					}
				};
				RestService.doPost('doBatchTransaction', result, {
					data : transactionList
				});
			}
		}
	};
	//refresh from-virtual-account whenever the target physical account changed
	$scope.$watch('allocating.targetPhysicalAccount', function(newValue, oldValue) {
		if (oldValue != newValue) {
			$scope.allocating.onTargetAccountChange();
		}
	});

	//load physicalAccountList
	var result = {
		setData : function(data, headers) {
			$scope.physicalAccountList = data;
			if ($scope.physicalAccountList.length > 0) {
				$scope.allocating.setTargetPhysicalAccount($scope.physicalAccountList[0]);
			}
		},
		setError : function(status, errorData) {
			$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
		}
	};
	RestService.doGet('physicalAccounts', result);

	//load virtualAccountList
	var result = {
		setData : function(data, headers) {
			$scope.allocating.initVirtualAccountList(data);
		},
		setError : function(status, errorData) {
			$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
		}
	};
	RestService.doGet('virtualAccounts', result);

	$scope.doDivide = function() {
		$scope.allocating.execute();
	};
}]);
