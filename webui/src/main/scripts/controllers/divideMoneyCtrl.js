'use strict';

angular.module('myFinance').controller('DivideMoneyCtrl', ['$scope', '$log', 'AccountTransactionService', 'physicalAccountList', 'virtualAccountList',
function($scope, $log, AccountTransactionService, physicalAccountList, virtualAccountList) {

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
			this.onTargetAccountChange();
		},
		initVirtualAccountList : function(list) {
			this.virtualAccountList = [];
			//remove unallocated account from the list
			for (var i = 0; i < list.length; i++) {
				if (list[i].id != -1) {
					this.virtualAccountList.push(list[i]);
				}
			}
			this.refreshAllocated();
		},
		execute : function() {
			$("body").addClass('ui-disabled');
			$.mobile.loading('show');
			if (this.targetVa && this.virtualAccountList) {
				var transactionList = [];
				var now = new Date().toUTCString();
				for (var i = 0; i < this.virtualAccountList.length; i++) {
					if(this.virtualAccountList[i].allocated>0){
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
				}
				AccountTransactionService.doBatchTransaction(transactionList).finally(function(data){
					$("body").removeClass('ui-disabled');
					$.mobile.loading('hide');
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

	//initialize physicalAccountList
	$scope.physicalAccountList = physicalAccountList;
	if ($scope.physicalAccountList.length > 0) {
		$scope.allocating.setTargetPhysicalAccount($scope.physicalAccountList[0]);
	}

	//initialize virtualAccountList
	$scope.allocating.initVirtualAccountList(virtualAccountList);

	$scope.doDivide = function() {
		$scope.allocating.execute();
	};
}]);
