angular.module('myFinance').controller('MonthRecordCtrl', ['$scope', '$log', 'RestService', 'transactionsOfThisMonth',
function($scope, $log, RestService, transactionsOfThisMonth) {
	$scope.transList = [];
	for (var i = 0; i < transactionsOfThisMonth.length; i++) {
		$scope.transList.push(wrapTransaction(transactionsOfThisMonth[i]));
	}

	//wrap transaction data with ui getter methods.
	function wrapTransaction(transData) {
		transData.getDateStr = function() {
			return new Date(this.date).toLocaleDateString();
		};
		transData.getFromAccount = function() {
			if (this.fromVirtualAccountName) {
				return this.fromVirtualAccountName;
			} else {
				return 'N/A';
			}
		};
		transData.getToAccount = function() {
			if (this.toVirtualAccountName) {
				return this.toVirtualAccountName;
			} else {
				return 'N/A';
			}
		};
		transData.getTypeStr = function() {
			switch (this.type) {
			case 'TRANSFER' :
				return '转账';
			case 'INCOME' :
				return '收入';
			case 'EXPENSE' :
				return '支出';
			default :
				return '未知';
			}
		};
		return transData;
	}

}]);
