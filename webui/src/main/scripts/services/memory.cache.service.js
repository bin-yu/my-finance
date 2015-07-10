'use strict';

angular.module('myFinance').factory('MemoryCacheService', ['$q',
function($q) {
	var MAX_LIFE=20000;//20 seconds
	return {
		set : function(key, value) {
			this[key] = new CacheItem(value);
		},
		get : function(key) {
			var item = this[key];
			if (item) {
				if(item.isExpired()){
					this.remove(key);
					return null;
				}
				return angular.copy(item.value);
			}
			return null;
		},
		remove : function(key){
			delete this[key];
		}
	};
	function CacheItem(value) {
		this.value = value;
		this.createTime = new Date();
		this.isExpired = function(){
			return new Date().getTime()-this.createTime.getTime()>MAX_LIFE;
		};
	}

}]); 