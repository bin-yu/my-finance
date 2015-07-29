/**
 * 
 * This is the api for access restful service
 * 
 */

(function(angular){
	'use strict';
	
	angular
	    .module('myFinance')
	    .factory('UrlService',['HostConfig','RestResources',function(HostConfig,RestResources){
			return {
				_getUrl : function(){
					var serverUrl = '';
					if(HostConfig.isCors){
						serverUrl = HostConfig.protocol + '://' + HostConfig.host + ':' + HostConfig.port ;
					}
					if(HostConfig.contextPath){
						serverUrl = serverUrl + '/' +HostConfig.contextPath;
					}
					return serverUrl;
				},
				
				getUrl: function(resourceKey,pathParametersMap){
					var url = RestResources[resourceKey];
					if(pathParametersMap){
						var key;
						for(key in pathParametersMap){
							url = url.replace('{'+key+'}',pathParametersMap[key]);
						}
					}
					url = this._getUrl() + url;
					return url;
				}
			};
	    }])
	
		/**
		 * 
		 * @param resourceKey , key for $http url which comes from AppConstant 
		 * @param result , callback or data model for $http
		 * @param config, a config object. 
		 *        e.g. 
		 *        {
		 *            pathParametersMap // path params
		 *            data              // data to post/put/delete
		 *            requestParameters // request params which will be attached in url
		 *            additionalHeaders // headers to add for the request
		 *            ignoreAuthModule  // if true, the retry mechanism will be igore
		 *        }
		 */
		.factory('RestService',['$http', '$log','UrlService','$q',function($http, $log,UrlService,$q){
			var baseOp = function(options){
				return function(resourceKey, config){
					var _config = config || {};
					var url = UrlService.getUrl(resourceKey,_config.pathParametersMap);
					$log.debug(url);
					
					var headers = { 
							'Accept': 'application/json' ,
							'Content-Type': 'application/json' 
					};
					
					//add additional headers
					if(_config.additionalHeaders){
						for(var key in _config.additionalHeaders){
							headers[key] = _config.additionalHeaders[key];
						}
					}
					var deferred=$q.defer();
					$http(
							{
								method : options.method,
								url  : url,
								data : _config.data,
								headers : headers,
								withCredentials : false,
								params : _config.requestParameters,
								ignoreAuthModule : _config.ignoreAuthModule
							}
					)
					.success(function(data, status, headers){
						$log.debug(options.func + '>> response content = ' + JSON.stringify(data));
						deferred.resolve(data,headers);
					}).error(function(errorData, status, headers){
						$log.error(options.func + '>> Failed, resp code = ' + status);
						deferred.reject(status,errorData);
					});
					return deferred.promise;
				};
			};
			
			return {
				doGet : baseOp({method : 'GET', func : 'doGet'}),
				doPost : baseOp({method : 'POST', func : 'doPost'}),
				doPut : baseOp({method : 'PUT', func : 'doPut'}),
				doDelete : baseOp({method : 'DELETE', func : 'doDelete'})
			};
		}]);
})(angular);

