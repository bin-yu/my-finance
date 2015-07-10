'use strict';

angular.module('myFinance').factory('AccountTransactionService', ['RestService', 'MemoryCacheService', '$q',
function(RestService, MemoryCacheService, $q) {
	var KEY_TRANSACTION_LIST = 'TransactionList';
	return {
		asyncGetTransactionListOfThisMonth : function() {
			var deferred = $q.defer();
			var cachedValue = MemoryCacheService.get(KEY_TRANSACTION_LIST);
			if (cachedValue) {
				deferred.resolve(cachedValue);
			} else {
				RestService.doGet('transactions').then(function(data, headers) {
					MemoryCacheService.set(KEY_TRANSACTION_LIST, data);
					deferred.resolve(angular.copy(data));
				}, function(status, errorData) {
					$log.error('failed to retrieve transaction list, the reason is : \n' + errorData);
					deferred.reject('failed to retrieve transaction list, the reason is : \n' + errorData);
				});
			}
			return deferred.promise;
		},
		getTransactionListOfThisMonth : function() {
			var promise= this.asyncGetTransactionListOfThisMonth();
			promise.then(function(list){
				return list;
			},function(error){
				return [];
			});
			return promise;
		},
		flushTransactionListOfThisMonth : function(){
			MemoryCacheService.remove(KEY_TRANSACTION_LIST);
		}
	};
}]);
