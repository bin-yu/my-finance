'use strict';

angular.module('myFinance').factory('AccountService', ['RestService', 'MemoryCacheService', '$q', '$log',
function(RestService, MemoryCacheService, $q, $log) {
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
		getPhysicalAccountList : function() {
			var promise = this.asyncGetPhysicalAccountList();
			promise.then(function(list) {
				return list;
			}, function(error) {
				return [];
			});
			return promise;
		},
		flushPhysicalAccountList : function() {
			MemoryCacheService.remove(KEY_PHYSICAL_ACCOUNT_LIST);
		},
		addPhysicalAccount : function(newAccount) {
			var thisSrv = this;
			var deferred = $q.defer();
			RestService.doPost('physicalAccounts', {
				data : newAccount
			}).then(function(data, headers) {
				thisSrv.flushPhysicalAccountList();
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to add this physical account, the reason is : \n' + errorData);
				alert("failed to add account!");
				deferred.reject(errorData);
			});
			return deferred.promise;
		},
		updatePhysicalAccount : function(account) {
			var thisSrv = this;
			RestService.doPut('physicalAccount', {
				pathParametersMap : {
					id : account.id
				},
				data : account
			}).then(function(data, headers) {
				thisSrv.flushPhysicalAccountList();
			}, function(status, errorData) {
				$log.error('failed to update this physical account, the reason is : \n' + errorData);
				alert('failed to update this physical account, the reason is : \n' + errorData);
			});
		},
		deletePhysicalAccount : function(account) {
			var thisSrv = this;
			var deferred = $q.defer();
			RestService.doDelete('physicalAccount', {
				pathParametersMap : {
					id : account.id
				}
			}).then(function(data, headers) {
				thisSrv.flushPhysicalAccountList();
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to delete this physical account, the reason is : \n' + errorData);
				deferred.reject(errorData);
				alert('删除失败 : \n' + errorData);
			});
			return deferred.promise;
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
		getVirtualAccountList : function() {
			var promise = this.asyncGetVirtualAccountList();
			promise.then(function(list) {
				return list;
			}, function(error) {
				return [];
			});
			return promise;
		},
		flushVirtualAccountList : function() {
			MemoryCacheService.remove(KEY_VIRTUAL_ACCOUNT_LIST);
		},
		addVirtualAccount : function(newAccount) {
			var thisSrv = this;
			var deferred = $q.defer();
			RestService.doPost('virtualAccounts', {
				data : newAccount
			}).then(function(data, headers) {
				thisSrv.flushVirtualAccountList();
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to add this virtual account, the reason is : \n' + errorData);
				alert("failed to add account!");
				deferred.reject(errorData);
			});
			return deferred.promise;
		},
		updateVirtualAccount : function(account) {
			var thisSrv = this;
			RestService.doPut('virtualAccount', {
				pathParametersMap : {
					id : account.id
				},
				data : account
			}).then(function(data, headers) {
				thisSrv.flushVirtualAccountList();
			}, function(status, errorData) {
				$log.error('failed to update this virtual account, the reason is : \n' + errorData);
				alert('failed to update this virtual account, the reason is : \n' + errorData);
			});
		},
		deleteVirtualAccount : function(account) {
			var thisSrv = this;
			var deferred = $q.defer();
			RestService.doDelete('virtualAccount', {
				pathParametersMap : {
					id : account.id
				}
			}).then(function(data, headers) {
				thisSrv.flushVirtualAccountList();
				deferred.resolve(data);
			}, function(status, errorData) {
				$log.error('failed to delete this virtual account, the reason is : \n' + errorData);
				deferred.reject(errorData);
				alert('删除失败 : \n' + errorData);
			});
			return deferred.promise;
		}
	};
}]);
