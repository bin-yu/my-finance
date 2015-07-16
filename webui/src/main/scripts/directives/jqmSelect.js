
angular.module('myFinance').directive('select', function() {
	return {
		restrict : 'E',
		priority : 1,
		require : '?ngModel',
		link : function(scope, element, attrs, ngModel) {
			if (ngModel) {
				var ngModelCtrl = ngModel;
				var defaultFn = ngModel.$render;
				ngModel.$render = function() {
					if (defaultFn)
						defaultFn();
					element.attr('value', ngModelCtrl.$viewValue);
					element.selectmenu('refresh');
				};
			}
			element.selectmenu();

		}
	};
});
