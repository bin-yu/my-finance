'use strict';

angular.module('myFinance').controller('DivideMoneyCtrl', ['$scope', '$log', 'RestService',
function($scope, $log, RestService) {
	$scope.physicalAccountList = [{
		id : 1,
		name : 'cash',
		mappedVirtualAccounts : [
			{
				virtualAccountId : -1,
				virtualAccountName : 'unallocated',
				amount: 999
			},
			{
				virtualAccountId : 1,
				virtualAccountName : 'normal',
				amount: 20
			}
		]
	},
	{
		id : 2,
		name : 'other'
	}];
	$scope.virtualAccountList=[
		{
			id : 2,
			name : '日常开销账户',
			budget : 10,
		},
		{
			id : 3,
			name : '养猪账户',
			budget : 100,
		},
	];
	for(var i=0;i<$scope.virtualAccountList.length;i++){
		$scope.virtualAccountList[i].allocated=$scope.virtualAccountList[i].budget;
	}

	$scope.reloadAccountList = function() {
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
	$scope.reloadAccountList();
	$scope.allocating={
		targetPhysicalAccount : $scope.physicalAccountList[0],
		fromVirtualAccount : function(){
			if(this.targetPhysicalAccount && this.targetPhysicalAccount.mappedVirtualAccounts){
				var vaList=this.targetPhysicalAccount.mappedVirtualAccounts;
				var i;
				for(i=0;i<vaList.length;i++){
					if(vaList[i].virtualAccountId === -1){
						return vaList[i];
					}
				}
			}
			return null;
		},
		remain : function(){
			var fromVa=this.fromVirtualAccount();
			if(fromVa===null){
				return 0;
			}
			return fromVa.amount;
		}
	};

	$scope.value=20;
	$scope.doDivide = function() {
		alert("target=" + $scope.targetPhysicalAccount.name);
	};
}]);
