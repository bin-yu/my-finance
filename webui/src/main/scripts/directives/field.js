'use strict';

angular.module('myFinance').directive('field', ['$compile', '$http', '$templateCache', '$interpolate',
function($compile, $http, $templateCache, $interpolate) {
	return {
		restrict : 'E',
		priority : 100,
		terminal : true,
		compile : function(element, attrs) {
			//...
			var validationMsgs = getValidationMessageMap(element);
			var labelContent = getLabelContent(element);
			element.html('');
			return function postLink(scope, element, attrs) {
				var template = 'templates/field.html';
				loadTemplate(template).then(function(templateElement) {
					var childScope = scope.$new();
					childScope.$validationMessages = angular.copy(validationMsgs);
					childScope.$fieldId = attrs.ngModel.replace('.', '_').toLowerCase() + '_' + childScope.$id;
					childScope.$fieldLabel = labelContent;
					childScope.$watch('$field.$dirty && $field.$error', function(errorList) {
						childScope.$fieldErrors = [];
						angular.forEach(errorList, function(invalid, key) {
							if (invalid) {
								childScope.$fieldErrors.push(key);
							}
						});
					}, true);
					//set input element
					var inputElement = templateElement.find('input');
					angular.forEach(attrs.$attr, function(original, normalized) {
						var value = element.attr(original);
						inputElement.attr(original, value);
					});
					inputElement.attr('name', childScope.$fieldId);
					inputElement.attr('id', childScope.$fieldId);
					//set label element
					var labelElement = templateElement.find('label');
					labelElement.attr('for', childScope.$fieldId);
					labelElement.html(labelContent);
					//append template
					element.append(templateElement);
					$compile(templateElement)(childScope);
					childScope.$field = inputElement.controller('ngModel');
				});
			};

			function getLabelContent(element) {
				var label = element.find('label');
				return label[0] && label.html();
			}

			function getValidationMessageMap(element) {
				var messageFns = {};
				var validators = element.find('validator');
				angular.forEach(validators, function(validator) {
					validator = angular.element(validator);
					messageFns[validator.attr('key')] = $interpolate(validator.text());
				});
				return messageFns;
			}

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
