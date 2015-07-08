angular.module('myFinance').controller('MonthRecordCtrl', ['$scope', '$log', 'RestService',
function($scope, $log, RestService) {
	$scope.transList = [];
	//load transList
	var result = {
		setData : function(data, headers) {
			for (var i = 0; i < data.length; i++) {
				$scope.transList.push(wrapTransaction(data[i]));
			}
		},
		setError : function(status, errorData) {
			$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
		}
	};
	RestService.doGet('transactions', result);

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
