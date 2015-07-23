angular.module('myFinance').controller('SearchRecordsCtrl', ['$scope',
function($scope) {
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
}]); 