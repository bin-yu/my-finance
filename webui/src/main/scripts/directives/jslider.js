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
				//trigger jqm to compile the element
				element.append(templateElement).trigger('create');
				//ask angular to compile the element.
				$compile(templateElement)(scope);
				//tell ngModel how to render the value
				inputElement = templateElement.find('input');
				var ngModelCtrl = inputElement.controller('ngModel');
				ngModelCtrl.$render = function() {
					inputElement.val(ngModelCtrl.$viewValue).slider('refresh');
				};
				//refresh slider when 'maxVar' changes
				scope.$watch(attrs.maxvar,function(newValue,oldValue){
					if(newValue !== oldValue){
						inputElement.attr('max',newValue).slider('refresh');
					}
				});
				//refresh slider when 'minVar' changes
				scope.$watch(attrs.minvar,function(newValue,oldValue){
					if(newValue !== oldValue){
						inputElement.attr('min',newValue).slider('refresh');
					}
				});
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
		priority: 1,
		require: '?ngModel',
		link : function (scope, element, attrs ,ngModel) {
			var ngModelCtrl=ngModel;
			var defaultFn=ngModel.$render;
			ngModel.$render = function (){
				if(defaultFn)defaultFn();
				element.attr('value',ngModelCtrl.$viewValue);
				element.selectmenu('refresh');
			};
			element.selectmenu();
			/*
			var unbindInitializationWatch = scope.$watch(attrs.ngModel, function(newValue, oldValue) {
										if (newValue=== undefined && oldValue === undefined) {
											element.selectmenu();
											//unbindInitializationWatch();
										} else {
											element.selectmenu('refresh');
											//unbindInitializationWatch();
										}
									});*/
			
			

			

		}
	};
});

