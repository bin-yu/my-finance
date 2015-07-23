angular.module('myFinance').controller('MonthReportsCtrl', ['$scope', '$log', 'AccountTransactionService','monthlyUsageSummary',
function($scope, $log, AccountTransactionService,monthlyUsageSummary) {
	$scope.monthlyUsageData = [
	{
		"key" : "预算",
		"values" : []
	},
	{
		"key" : "花费",
		"values" : []
	}, ];
	for (var i = 0; i < monthlyUsageSummary.length; i++) {
		if(monthlyUsageSummary[i].id>=0){
		$scope.monthlyUsageData[0].values.push([monthlyUsageSummary[i].name,monthlyUsageSummary[i].budget]);
		$scope.monthlyUsageData[1].values.push([monthlyUsageSummary[i].name,monthlyUsageSummary[i].expense]);
		}
	}
	$scope.toolTipContentFunction = function(){
		return function(key, x, y, e, graph) {
	    	return  key+ ' : ' + y;
		};
	};
	$scope.colorFunction = function() {
	    return function(d, i) {
	    	if(d.series === 0){
	    		return d3.rgb(0,255,0);
	    	}else if(d.series === 1){
	    		return d3.rgb(255,0,0);
	    	}else{
	    		return d3.rgb(0,0,255);
	    	}
	        //return colorCategory(i);
	    };
	};
	$scope.valueFormatFunction = function(){
		return function(d){
	    	return d;
	   };
	};
}]);
