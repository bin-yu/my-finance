angular.module('myFinance').controller('AddRecordCtrl', ['$scope', '$log', 'RestService',
function($scope, $log, RestService) {
	//load physicalAccountList
	var result = {
		setData : function(data, headers) {
			$scope.physicalAccountList = data;
			if ($scope.physicalAccountList.length > 0) {
				$scope.trans.fromPhysicalAccount = $scope.physicalAccountList[0];
				$scope.trans.toPhysicalAccount = $scope.physicalAccountList[0];
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
			$scope.virtualAccountList = data;
			if ($scope.virtualAccountList.length > 0) {
				$scope.trans.fromVirtualAccount = $scope.trans.toVirtualAccount = $scope.virtualAccountList[0];
			}
		},
		setError : function(status, errorData) {
			$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
		}
	};
	RestService.doGet('virtualAccounts', result);
	//define the form data and actions
	$scope.trans = {
		date : new Date(),
		type : 'EXPENSE',
		amount : 0,
		hasFrom : function(){
			return 'INCOME' !== this.type;
		},
		hasTo : function(){
			return 'EXPENSE' !== this.type;
		},
		submit : function() {
			var transData = {
				date : this.date.toUTCString(),
				type : this.type,
				fromPhysicalAccountId : this.hasFrom()? this.fromPhysicalAccount.id:-99,
				fromVirtualAccountId : this.hasFrom()? this.fromVirtualAccount.id:-99,
				toPhysicalAccountId : this.hasTo()?this.toPhysicalAccount.id:-99,
				toVirtualAccountId : this.hasTo()?this.toVirtualAccount.id:-99,
				amount : this.amount,
				description : this.desc
			};
			var result = {
				setData : function(data, headers) {
					alert('success');
				},
				setError : function(status, errorData) {
					$log.error('failed to add the record, the reason is : \n' + errorData);
					alert('failed:status=' + status);
				}
			};
			RestService.doPost('transactions', result, {
				data : transData
			});
		}
	};
}]);
