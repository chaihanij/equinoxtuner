'use strict';
var AngularSpringApp = {};
/*
 * 'ui', 'ui.bootstrap', 'highcharts-ng', 'ngRoute'
 */
var App = angular.module('AngularSpringApp',
		[ 'ngRoute', 'ui.bootstrap', 'ui' ]);

// Declare app level module which depends on filters, and services
App.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/monitor', {
		templateUrl : 'monitor/layout.htm',
		controller : MonitorController
	});

	$routeProvider.when('/tunerconfig', {
		templateUrl : 'tunerconfig/layout.htm',
		controller : TunerCongfigController
	});

	$routeProvider.when('/config', {
		templateUrl : 'config/layout.htm',
		controller : ConfigAppController
	});

	$routeProvider.otherwise({
		redirectTo : '/monitor'
	});
} ]);

App.controller('MainCtrl', function($scope, $routeParams, $location, $http) {

	$scope.activePath = null;
	$scope.$on('$routeChangeSuccess', function() {
		$scope.activePath = $location.path();
	});

});

// Get context with jQuery using jQuery's .get() method.
App.directive('uiSlider', [ 'ui.config', function(uiConfig) {
	'use strict';
	uiConfig.uiSlider = uiConfig.uiSlider || {};
	return {
		restrict : 'A',
		require : 'ngModel',
		scope : {
			value : '=ngModel',
			min : '=min',
			max : '=max',
			step : '=step'
		},
		link : function(scope, elm) {
			var expression = {};
			// Set attribute from element
			if (!angular.isUndefined(scope.min)) {
				expression['min'] = parseInt(scope.min);
			}
			if (!angular.isUndefined(scope.max)) {
				expression['max'] = parseInt(scope.max);
			}
			if (!angular.isUndefined(scope.step)) {
				expression['step'] = parseInt(scope.step);
			}

			var options = {
				range : 'max',
				value : scope.value,
				min : scope.min,
				max : scope.max,
				slide : function(event, ui) {
					scope.$apply(function() {
						scope.value = ui.value;
					});
				}
			};

			// Watch for changes in value, update all sliders bind to the same
			// model within scope
			scope.$watch('value', function(newVal, oldVal) {
				if (!angular.isUndefined(newVal) && newVal != oldVal) {
					elm.slider('value', newVal)
				}
			});

			scope.$watch('min', function(newVal, oldVal) {
				if (!angular.isUndefined(newVal) && newVal != oldVal) {
					elm.slider('option', 'min', newVal)
				}
			});

			scope.$watch('max', function(newVal, oldVal) {
				if (!angular.isUndefined(newVal) && newVal != oldVal) {
					elm.slider('option', 'max', newVal)
				}
			});

			scope.$watch('step', function(newVal, oldVal) {
				if (!angular.isUndefined(newVal) && newVal != oldVal) {
					elm.slider('option', 'step', newVal)
				}
			});

			// Set the options from the directive's configuration
			angular.extend(options, uiConfig.uiSlider, expression);
			elm.slider(options);
		}
	};
} ]);
// Get context with jQuery using jQuery's .get() method.
App.directive('integer', function() {
	return {
		require : 'ngModel',
		link : function(scope, ele, attr, ctrl) {

			var max = scope.$eval(attr.max);
			var min = scope.$eval(attr.min);

			ctrl.$parsers.unshift(function(viewValue) {
				if (viewValue === '' || viewValue == undefined) {
					return parseInt(min);
				} else if (parseInt(viewValue) >= parseInt(max)) {
					return parseInt(max);
				} else if ((parseInt(viewValue) <= parseInt(max))
						&& (parseInt(viewValue) >= parseInt(min))) {
					return parseInt(viewValue);
				}

			});

		}
	};
});

App.directive('datetimez', function() {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, element, attrs, ngModelCtrl) {
			element.datetimepicker({
				language : 'en',
				pickDate : false,
			}).on('changeDate', function(e) {
				ngModelCtrl.$setViewValue(e.date);
				scope.$apply();
			});
		}
	};
});
