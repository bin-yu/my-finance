'use strict';

angular.module('myFinance').directive('jqmTextInput', function() {
	return {
		// Restrict it to be an attribute in this case
		restrict : 'A',
		// responsible for registering DOM listeners as well as updating the DOM
		link : function(scope, element, attrs) {
			$(element).textinput();
			//refresh when 'ngDisabled' changes
			scope.$watch(attrs.ngDisabled, function(newValue, oldValue) {
				//if (newValue !== oldValue) {
				$(element).textinput( newValue ? 'disable' : 'enable');
				//}
			});
		}
	};
}); 