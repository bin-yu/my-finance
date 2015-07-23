angular.module('myFinance').controller('HomeCtrl', ['$scope', 'physicalAccountList', 'virtualAccountList', 'monthlyUsageSummary',
function($scope, physicalAccountList, virtualAccountList, monthlyUsageSummary) {
	$scope.totalMoney = 0;
	for (var i = 0; i < physicalAccountList.length; i++) {
		$scope.totalMoney += physicalAccountList[i].amount;
	}
	$scope.outOfMoneyVirtualAccounts = [];
	for (var i = 0; i < virtualAccountList.length; i++) {
		if (virtualAccountList[i].id >= 0 && virtualAccountList[i].amount <= 0) {
			$scope.outOfMoneyVirtualAccounts.push(virtualAccountList[i]);
		}
	}

	$scope.monthlyExpenseOverflowVirtualAccounts = [];
	$scope.totalExpenseOfThisMonth = 0;
	for (var i = 0; i < monthlyUsageSummary.length; i++) {
		if (monthlyUsageSummary[i].id >= 0) {
			$scope.totalExpenseOfThisMonth += monthlyUsageSummary[i].expense;
			if (monthlyUsageSummary[i].expense > monthlyUsageSummary[i].budget) {
				$scope.monthlyExpenseOverflowVirtualAccounts.push(monthlyUsageSummary[i]);
			}
		}
	}


}]); 