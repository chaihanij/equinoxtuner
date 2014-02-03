'use strict';
angular.module('nvd3chart', []).directive('nvd3LineChart', function() {

	return {
		restrict : 'EAC',
		replace : true,
		scope : {
			
		},
		link : function(scope, element, attrs) {

			var chart = false;
			function initChart() {
				if (chart)
					chart.destroy();
			}

			scope.$on('$destroy', function() {
				if (chart)
					chart.destroy();
				element.remove();
			});

		}
	};
});