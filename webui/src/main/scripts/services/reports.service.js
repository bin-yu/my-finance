angular.module('myFinance').factory('ReportsService', ['RestService', '$q', '$log',
function(RestService, $q, $log) {
	return {
		getMonthlyUsageSummary : function() {
			var deferred = $q.defer();
			RestService.doGet('monthlyUsageSummary').then(function(data, headers) {
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to retrieve transaction list, the reason is : \n' + errorData);
				deferred.reject('failed to retrieve transaction list, the reason is : \n' + errorData);
			});
			deferred.promise.then(function(result) {
				return result;
			}, function(error) {
				return [];
			});
			return deferred.promise;
		}
	};
}]);