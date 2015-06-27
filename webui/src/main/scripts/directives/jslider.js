'use strict';

angular.module('myFinance').directive('jslider', ['$compile', '$http', '$templateCache',
function($compile, $http, $templateCache, $interpolate) {
	return {
		restrict : 'E',
		priority : 100,
		terminal : true,
		link : function postLink(scope, element, attrs) {

			loadTemplate('templates/jslider.html').then(function(templateElement) {

				//set input element
				var inputElement = templateElement.find('input');
				//set label element
				var labelElement = templateElement.find('label');
				angular.forEach(attrs.$attr, function(original, normalized) {
					var value = element.attr(original);
					if ('label' === original) {
						labelElement.html(value);
					} else {
						inputElement.attr(original, value);
					}
					if ('id' === original) {
						labelElement.attr('for', value);
					}
				});

				//append the template element
				//templateElement.find('input').attr('value',scope.va.allocated);
				element.append(templateElement).trigger('create');
				$compile(templateElement)(scope);
				inputElement = templateElement.find('input');
				var ngModelCtrl = inputElement.controller('ngModel');
				ngModelCtrl.$render = function() {
					inputElement.val(ngModelCtrl.$viewValue).slider('refresh');
				};
				//prevent <a> link click event.
				templateElement.find('a').click(function(e) {
					return e.preventDefault();
				});
			});

			function loadTemplate(template) {
				return $http.get(template, {
					cache : $templateCache
				}).then(function(response) {
					return angular.element(response.data);
				}, function(response) {
					throw new Error('Template not found: ' + template);
				});
			}

		}
	};
}]);

angular.module('myFinance').directive('select', function() {
	return {
		restrict : 'E',
		//require: '?ngModel',
		link : function (scope, element, attrs ) {
			var unbindInitializationWatch = scope.$watch(attrs.ngModel, function(newValue, oldValue) {
							if (newValue && oldValue === undefined) {
								element.selectmenu('refresh');
								unbindInitializationWatch();
							} else if (newValue && oldValue) {
								element.selectmenu();
								unbindInitializationWatch();
							}
						});
			

			

		}
	};
});
