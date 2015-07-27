'use strict';

angular.module('myFinance').factory('AccountTransactionService', ['RestService', 'MemoryCacheService', 'AccountService', '$q', '$log',
function(RestService, MemoryCacheService, AccountService, $q, $log) {
	var KEY_TRANSACTION_CNT_OF_THIS_MONTH = 'TransCntOfThisMonth';
	var PAGE_SIZE = 10;
	return {
		asyncSearchTransaction : function(from,to,pageNo) {
			var deferred = $q.defer();
			RestService.doGet('transactions', {
				requestParameters : {
					fromDate : from.getTime(),
					toDate : to.getTime(),
					offset : pageNo * PAGE_SIZE,
					limit : PAGE_SIZE
				}
			}).then(function(data, headers) {
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to retrieve transaction list, the reason is : \n' + errorData);
				deferred.reject('failed to retrieve transaction list, the reason is : \n' + errorData);
			});
			return deferred.promise;
		},
		asyncGetTransactionCount : function(from,to) {
			var deferred = $q.defer();
			RestService.doGet('countTransactions', {
				requestParameters : {
					fromDate : from.getTime(),
					toDate : to.getTime()
				}
			}).then(function(data, headers) {
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to retrieve transaction list, the reason is : \n' + errorData);
				deferred.reject('failed to retrieve transaction list, the reason is : \n' + errorData);
			});
			return deferred.promise;
		},
		asyncGetTransactionListOfThisMonth : function(pageNo) {
			var deferred = $q.defer();
			RestService.doGet('transactions', {
				requestParameters : {
					offset : pageNo * PAGE_SIZE,
					limit : PAGE_SIZE
				}
			}).then(function(data, headers) {
				deferred.resolve(angular.copy(data));
			}, function(status, errorData) {
				$log.error('failed to retrieve transaction list, the reason is : \n' + errorData);
				deferred.reject('failed to retrieve transaction list, the reason is : \n' + errorData);
			});
			return deferred.promise;
		},
		asyncGetTransactionCountOfThisMonth : function() {
			var deferred = $q.defer();
			var cachedValue = MemoryCacheService.get(KEY_TRANSACTION_CNT_OF_THIS_MONTH);
			if (cachedValue) {
				deferred.resolve(cachedValue);
			} else {
				RestService.doGet('countTransactions').then(function(data, headers) {
					MemoryCacheService.set(KEY_TRANSACTION_CNT_OF_THIS_MONTH, data);
					deferred.resolve(angular.copy(data));
				}, function(status, errorData) {
					$log.error('failed to retrieve transaction count, the reason is : \n' + errorData);
					deferred.reject('failed to retrieve transaction count, the reason is : \n' + errorData);
				});
			}
			return deferred.promise;
		},
		getTransactionCountOfThisMonth : function() {
			var promise = this.asyncGetTransactionCountOfThisMonth();
			promise.then(function(result) {
				return result;
			}, function(error) {
				return [];
			});
			return promise;
		},

		flushTransactionListOfThisMonth : function() {
			MemoryCacheService.remove(KEY_TRANSACTION_CNT_OF_THIS_MONTH);
		},
		addTransaction : function(transData) {
			var thisSrv = this;
			RestService.doPost('transactions', {
				data : transData
			}).then(function(data, headers) {
				thisSrv.flushTransactionListOfThisMonth();
				AccountService.flushPhysicalAccountList();
				AccountService.flushVirtualAccountList();
			}, function(status, errorData) {
				$log.error('failed to add the record, the reason is : \n' + errorData);
				alert('failed:status=' + status);
			});
		},
		doBatchTransaction : function(transactionList) {
			var deferred = $q.defer();
			var thisSrv = this;
			RestService.doPost('doBatchTransaction', {
				data : transactionList
			}).then(function(data, headers) {

				thisSrv.flushTransactionListOfThisMonth();
				AccountService.flushPhysicalAccountList();
				AccountService.flushVirtualAccountList();
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to submit batch transaction, the reason is : \n' + errorData);
				alert('failed:status=' + status);
				deferred.reject(errorData);
			});
			return deferred.promise;
		}
	};
}]);
