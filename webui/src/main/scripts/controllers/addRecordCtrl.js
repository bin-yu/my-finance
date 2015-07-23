angular.module('myFinance').controller('AddRecordCtrl', ['$scope', '$log', 'AccountTransactionService', 'physicalAccountList', 'virtualAccountList',
function($scope, $log, AccountTransactionService, physicalAccountList, virtualAccountList) {
	
	//define the form data and actions
	$scope.trans = {
		date : new Date(),
		type : 'EXPENSE',
		amount : 0,
		hasFrom : function() {
			return 'INCOME' !== this.type;
		},
		hasTo : function() {
			return 'EXPENSE' !== this.type;
		},
		submit : function() {
			var transData = {
				date : this.date.toUTCString(),
				type : this.type,
				fromPhysicalAccountId : this.hasFrom() ? this.fromPhysicalAccount.id : -99,
				fromVirtualAccountId : this.hasFrom() ? this.fromVirtualAccount.id : -99,
				toPhysicalAccountId : this.hasTo() ? this.toPhysicalAccount.id : -99,
				toVirtualAccountId : this.hasTo() ? this.toVirtualAccount.id : -99,
				amount : this.amount,
				description : this.desc
			};
			AccountTransactionService.addTransaction(transData);
		}
	};
	//initialize from/to PhysicalAccount
	$scope.physicalAccountList=physicalAccountList;
	if ($scope.physicalAccountList.length > 0) {
		$scope.trans.fromPhysicalAccount = $scope.physicalAccountList[0];
		$scope.trans.toPhysicalAccount = $scope.physicalAccountList[0];
	}

	//initial from/to virtualAccount
	$scope.virtualAccountList=virtualAccountList;
	if ($scope.virtualAccountList.length > 0) {
		$scope.trans.fromVirtualAccount = $scope.trans.toVirtualAccount = $scope.virtualAccountList[0];
	}
	
	//for date picker
	$scope.dateOptions = {
		formatYear : 'yyyy',
		startingDay : 1
	};
	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.trans.pickerOpened = true;
	};
}]);
