angular.module('myFinance').controller('SearchRecordsCtrl', ['$scope','AccountTransactionService',
function($scope,AccountTransactionService) {
	
	
	//init data pickers
	$scope.dateOptions = {
		formatYear : 'yyyy',
		startingDay : 1
	};
	$scope.open = function($event,model) {
		$event.preventDefault();
		$event.stopPropagation();
		model.opened = true;
	};
	$scope.maxDate = new Date();
	$scope.from={
		date : new Date(),
		opened :false
	};
	$scope.to={
		date : new Date(),
		opened :false
	};
	//search function
	$scope.search=function(){
		removeTime($scope.from.date);
		removeTime($scope.to.date);
		AccountTransactionService.asyncGetTransactionCount($scope.from.date,$scope.to.date).then(function(count){
			$scope.totalRecords=count;
			$scope.currentPage =1;
			$scope.pageChanged();
		});
	};
	//page transition function
	$scope.pageChanged= function(){
		loadPage($scope.currentPage-1);
	};
	function loadPage(pageNo){
		removeTime($scope.from.date);
		removeTime($scope.to.date);
		AccountTransactionService.asyncSearchTransaction($scope.from.date,$scope.to.date,pageNo).then(function(list){
			$scope.transList = [];
			for (var i = 0; i < list.length; i++) {
				$scope.transList.push(wrapTransaction(list[i]));
			}
		});
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
	
	function removeTime(dateTime){
		dateTime.setHours(0);
		dateTime.setMinutes(0);
		dateTime.setSeconds(0);
		dateTime.setMilliseconds(0);
	}
}]); 