angular.module('myFinance')
	.controller('PhysicalAccountCtrl', ['$scope','$log','RestService',function ($scope,$log,RestService) {
		var result={
			setData:function(data,headers){
				$scope.physicalAccountList=data;
			},
			setError:function(status,errorData){
				$log.error('failed to retrieve physical account list, the reason is : \n'+errorData);
			}
		};
		RestService.doGet('physicalAccounts',result);
	}]);