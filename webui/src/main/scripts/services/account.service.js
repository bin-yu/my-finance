'use strict';

angular.module('myFinance').factory('AccountService', ['RestService', 'MemoryCacheService', '$q',
function(RestService, MemoryCacheService, $q) {
	var KEY_PHYSICAL_ACCOUNT_LIST = 'PhysicalAccountList';
	var KEY_VIRTUAL_ACCOUNT_LIST = 'VirtualAccountList';
	return {
		//functions for physical account
		asyncGetPhysicalAccountList : function() {
			var deferred = $q.defer();
			var cachedValue = MemoryCacheService.get(KEY_PHYSICAL_ACCOUNT_LIST);
			if (cachedValue) {
				deferred.resolve(cachedValue);
			} else {
				RestService.doGet('physicalAccounts').then(function(data, headers) {
					MemoryCacheService.set(KEY_PHYSICAL_ACCOUNT_LIST, data);
					deferred.resolve(angular.copy(data));
				}, function(status, errorData) {
					$log.error('failed to retrieve physical account list, the reason is : \n' + errorData);
					deferred.reject('failed to retrieve physical account list, the reason is : \n' + errorData);
				});
			}
			return deferred.promise;
		},
		flushPhysicalAccountList : function(){
			MemoryCacheService.remove(KEY_PHYSICAL_ACCOUNT_LIST);
		},
		//functions for virtual account
		asyncGetVirtualAccountList : function() {
			var deferred = $q.defer();
			var cachedValue = MemoryCacheService.get(KEY_VIRTUAL_ACCOUNT_LIST);
			if (cachedValue) {
				deferred.resolve(cachedValue);
			} else {
				RestService.doGet('virtualAccounts').then(function(data, headers) {
					MemoryCacheService.set(KEY_VIRTUAL_ACCOUNT_LIST, data);
					deferred.resolve(angular.copy(data));
				}, function(status, errorData) {
					$log.error('failed to retrieve virtual account list, the reason is : \n' + errorData);
					deferred.reject('failed to retrieve virtual account list, the reason is : \n' + errorData);
				});
			}
			return deferred.promise;
		},
		flushVirtualAccountList : function(){
			MemoryCacheService.remove(KEY_VIRTUAL_ACCOUNT_LIST);
		}
	};
}]);
