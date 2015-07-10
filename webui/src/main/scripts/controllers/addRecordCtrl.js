angular.module('myFinance').controller('AddRecordCtrl', ['$scope', '$log', 'RestService', 'AccountService','AccountTransactionService',
function($scope, $log, RestService, AccountService,AccountTransactionService) {
	//initialize physicalAccountList
	$scope.physicalAccountList = [];
	AccountService.asyncGetPhysicalAccountList().then(function(list) {
		$scope.physicalAccountList = list;
		if ($scope.physicalAccountList.length > 0) {
			$scope.trans.fromPhysicalAccount = $scope.physicalAccountList[0];
			$scope.trans.toPhysicalAccount = $scope.physicalAccountList[0];
		}
	});

	//load virtualAccountList
	$scope.virtualAccountList = [];
	AccountService.asyncGetVirtualAccountList().then(function(list) {
		$scope.virtualAccountList = list;
		if ($scope.virtualAccountList.length > 0) {
			$scope.trans.fromVirtualAccount = $scope.trans.toVirtualAccount = $scope.virtualAccountList[0];
		}
	});
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
			RestService.doPost('transactions', {
				data : transData
			}).then(function(data, headers) {
				AccountTransactionService.flushTransactionListOfThisMonth();
			}, function(status, errorData) {
				$log.error('failed to add the record, the reason is : \n' + errorData);
				alert('failed:status=' + status);
			});
		}
	};
}]);
