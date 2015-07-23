angular.module('myFinance').controller('HistoryReportsCtrl', ['$scope',
function($scope) {
	$scope.dateOptions = {
		formatYear : 'yy',
		startingDay : 1
	};

}]);
