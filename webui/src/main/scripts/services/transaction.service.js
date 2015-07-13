'use strict';

angular.module('myFinance').factory('AccountTransactionService', ['RestService', 'MemoryCacheService', '$q','$log',
function(RestService, MemoryCacheService, $q, $log) {
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
		},
		addTransaction : function(transData){
			var thisSrv=this;
			RestService.doPost('transactions', {
				data : transData
			}).then(function(data, headers) {
				thisSrv.flushTransactionListOfThisMonth();
			}, function(status, errorData) {
				$log.error('failed to add the record, the reason is : \n' + errorData);
				alert('failed:status=' + status);
			});
		},
		doBatchTransaction : function(transactionList){
			var thisSrv=this;
			RestService.doPost('doBatchTransaction', {
					data : transactionList
				}).then(function(data, headers) {
					alert('success');
					thisSrv.flushTransactionListOfThisMonth();
				}, function(status, errorData) {
					$log.error('failed to submit batch transaction, the reason is : \n' + errorData);
					alert('failed:status=' + status);
				});
		}
	};
}]);
