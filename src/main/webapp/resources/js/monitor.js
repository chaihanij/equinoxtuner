'use strict';
var DEBUG = false;
var MonitorController = function($scope, $http, $interval) {

	/**
	 * Visible View
	 */
	$scope.visibleCharts = false;
	$scope.visbleStatistics = false;
	/** "Selection Node Monitor" * */
	$scope.sysGroupList = null;
	$scope.nodemonitorlist = null;
	$scope.selectedIndexNodeMonitor = null;

	$scope.statemonitor = null;

	// Innit Page
	$scope.onLoadSysGroup = function() {

		$http.get('monitor/getsystemgroupmoniotr.htm').success(

		function(data, status) {

			if (status == 200) {

				$scope.sysGroupList = data;

			} else {
				if (DEBUG != false)
					console.log("status = " + status + " error :" + data);
			}

		}).error(function(data, status) {

			if (DEBUG != false) {
				console.log("status = " + status + " error :" + data);
			}

		});
	};

	$scope.sysGroupChanged = function(obj) {
		stopCallData();
		$scope.visibleCharts = false;

		$scope.visbleStatistics = false;
		$scope.statemonitor = null;
		if (obj != null && obj != undefined) {

			$http({
				url : 'monitor/selectnodemonitor.htm',
				method : "POST",
				data : obj,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {

				if (status == 200) {

					$scope.nodemonitorlist = data;
					$scope.visbleStatistics = true;
					if ($scope.nodemonitorlist.length - 1 == 0) {
						$scope.visbleStatistics = false;
						alert("Node without statistics file");
					}

				} else {
					if (DEBUG != false)
						console.log("status = " + status + " error :" + data);
				}

			}).error(function(data, status) {
				if (DEBUG != false)
					console.log("status = " + status + " error :" + data);

			});
		}

	};

	var stop;
	var duration = 1000 * 10;
	$scope.onClickNodeMonitor = function($event, statemonitor, $index) {

		$scope.selectedIndexNodeMonitor = $index;

		stopCallData();
		$scope.dbMeasurement = [];
		$scope.dbAccumulating = [];
		$scope.dbmeasurementvalue = [];
		$scope.visibleCharts = true;
		xaxis = [];
		db_systeminternalstatus = [];
		db_systemmeasurement = [];
		db_systemmeasurementvalue = [];
		db_accumulating = [];

		getDataToChart(statemonitor);

		stop = $interval(function() {

			getDataToChart(statemonitor);

			if (DEBUG != false)
				console.log("onclick", statemonitor);

		}, duration);

	};

	var stopCallData = function() {
		if (angular.isDefined(stop)) {
			$interval.cancel(stop);
			stop = undefined;
		}
	};

	$scope.$on('$destroy', function() {
		// Make sure that the interval is destroyed too
		stopCallData();
	});

	/**
	 * Chart
	 */

	var getDataToChart = function(statemonitor) {

		$http({
			url : 'monitor/getstatequinox.htm',
			method : "POST",
			data : statemonitor,
			headers : {
				'Content-Type' : 'applictaion/json',
				'mimeType' : 'application/json'
			}
		}).success(function(data, status) {

			if (status == 200) {

				if (!jQuery.isEmptyObject(data)) {

					generateDataToChart(statemonitor, data);

				}

			} else {

				if (DEBUG != false) {
					console.log(status);
				}
			}

		}).error(function(data, status) {

			if (DEBUG != false)
				console.log(status);
			alert("Error");

		});

	};

	$scope.dbSystem;
	$scope.dbMeasurement;
	$scope.dbAccumulating;
	$scope.dbmeasurementvalue;

	// Defult Data
	var db_systeminternalstatus = [];

	var db_systemmeasurement = [];

	var db_systemmeasurementvalue = [];

	var db_accumulating = [];

	var xaxis = [];

	var oldData = null;

	$scope.dataRealtime;
	$scope.xaxis;

	var generateDataToChart = function(statemonitor, data) {

		$scope.statemonitor = statemonitor;
		$scope.dataRealtime = data;
		xaxis.push(data.time)

		var match = data.time.match(/^(\d+)-(\d+)-(\d+) (\d+)\:(\d+)\:(\d+)$/);
		var datetime = (Date.UTC(match[1], match[2] - 1, match[3], match[4],
				match[5], match[6]) - (7 * 3600 * 1000));

		if (jQuery.isEmptyObject(db_systeminternalstatus)
				&& jQuery.isEmptyObject(db_systemmeasurement)
				&& jQuery.isEmptyObject(db_systemmeasurementvalue)
				&& jQuery.isEmptyObject(db_accumulating)) {

			oldData = data;
			xaxis.push(data.time);
			if (!jQuery.isEmptyObject(data.internalstat)) {

				db_systeminternalstatus = data.internalstat
			}

			// Measurement === acceleration
			if (!jQuery.isEmptyObject(data.measurement)) {

				for ( var i = 0; i < data.measurement.length; i++) {
					var obj = data.measurement[i];
					for ( var key in obj) {
						var attrName = key;
						var attrValue = obj[key];
						var series = {
							key : attrName,
							values : [ {
								x : parseInt(datetime),
								y : parseInt(attrValue)
							} ]
						};
						db_systemmeasurement.push(series);
					}
				}

			}
			// Measurement === value
			if (!jQuery.isEmptyObject(data.measurementvalue)) {

				for ( var i = 0; i < data.measurementvalue.length; i++) {
					var obj = data.measurementvalue[i];
					for ( var key in obj) {
						var attrName = key;
						var attrValue = obj[key];
						var series = {
							key : attrName,
							values : [ {
								x : parseInt(datetime),
								y : parseInt(attrValue)
							} ]
						};
						db_systemmeasurementvalue.push(series);
					}
				}

			}
			// Acculating ===
			if (!jQuery.isEmptyObject(data.acculating)) {

				for ( var i = 0; i < data.acculating.length; i++) {
					var obj = data.acculating[i];
					for ( var key in obj) {
						var attrName = key;
						var attrValue = obj[key];

						var series = {
							key : attrName,
							values : [ {
								x : parseInt(datetime),
								y : parseInt(attrValue)
							} ]
						};

						db_accumulating.push(series);

					}
				}
			}

		} else {

			if (data.time != oldData.time) {
				oldData = data;
				xaxis.push(data.time);
				if (!jQuery.isEmptyObject(data.internalstat)) {

					db_systeminternalstatus = data.internalstat

				}

				// Measurement === acceleration
				if (!jQuery.isEmptyObject(data.measurement)) {
					for ( var int = 0; int < data.measurement.length; int++) {
						var obj = data.measurement[int];
						for ( var key in obj) {

							var attrName = key;
							var attrValue = obj[key];
							for ( var j = 0; j < db_systemmeasurement.length; j++) {

								if (db_systemmeasurement[j].key == attrName) {
									var inputData = {
										x : parseInt(datetime),
										y : parseInt(attrValue)
									};
									db_systemmeasurement[j].values
											.push(inputData);
								}
							}
						}
					}
				}

				// Measurement === Value
				if (!jQuery.isEmptyObject(data.measurementvalue)) {
					for ( var int = 0; int < data.measurementvalue.length; int++) {
						var obj = data.measurementvalue[int];
						for ( var key in obj) {

							var attrName = key;
							var attrValue = obj[key];
							for ( var j = 0; j < db_systemmeasurementvalue.length; j++) {

								if (db_systemmeasurementvalue[j].key == attrName) {
									var inputData = {
										x : parseInt(datetime),
										y : parseInt(attrValue)
									};
									db_systemmeasurementvalue[j].values
											.push(inputData);
								}
							}
						}
					}
				}

				// Acculating ===
				if (!jQuery.isEmptyObject(data.acculating)) {
					for ( var int = 0; int < data.acculating.length; int++) {
						var obj = data.acculating[int];
						for ( var key in obj) {

							var attrName = key;
							var attrValue = obj[key];
							for ( var j = 0; j < db_accumulating.length; j++) {

								if (db_accumulating[j].key == attrName) {
									var inputData = {
										x : parseInt(datetime),
										y : parseInt(attrValue)
									};
									db_accumulating[j].values.push(inputData);
								}
							}
						}
					}
				}
			}
		}
		$scope.xaxis = xaxis;
		$scope.dbSystem = db_systeminternalstatus;
		$scope.dbMeasurement = db_systemmeasurement;
		$scope.dbmeasurementvalue = db_systemmeasurementvalue;
		$scope.dbAccumulating = db_accumulating;

		var tatolpoint = 10;
		for ( var j = 0; j < db_accumulating.length; j++) {

			if (db_accumulating[j].values.length == tatolpoint) {
				db_accumulating[j].values.splice(0, 1);
			}
		}
		for ( var j = 0; j < db_systemmeasurement.length; j++) {

			if (db_systemmeasurement[j].values.length == tatolpoint) {
				db_systemmeasurement[j].values.splice(0, 1);
			}
		}
		for ( var j = 0; j < db_systemmeasurementvalue.length; j++) {

			if (db_systemmeasurementvalue[j].values.length == tatolpoint) {
				db_systemmeasurementvalue[j].values.splice(0, 1);
			}
		}

		redrawAccumulating();
		redrawMeasurement();
		redrawMeasurementvalue();
	};

	var chartMeasurement;
	var redrawMeasurement = function() {
		nv.addGraph(function() {
			chartMeasurement = nv.models.lineChart().showLegend(true)
					.showYAxis(true).showXAxis(true).useInteractiveGuideline(
							true);

			chartMeasurement.xAxis.axisLabel('Time').rotateLabels(-20)
					.tickFormat(function(d) {
						return d3.time.format('%X')(new Date(d))
					});

			chartMeasurement.yAxis.axisLabel('Value')
					.tickFormat(d3.format('d'));

			d3.select('#measurement svg').datum(db_systemmeasurement)
					.transition().duration(500).call(chartMeasurement);

			nv.utils.windowResize(chartMeasurement.update);

			return chartMeasurement;
		});
	};
	var chartMeasurementvalue;
	var redrawMeasurementvalue = function() {
		nv.addGraph(function() {
			chartMeasurementvalue = nv.models.lineChart().showLegend(true)
					.showYAxis(true).showXAxis(true).useInteractiveGuideline(
							true);

			chartMeasurementvalue.xAxis.axisLabel('Time').rotateLabels(-20)
					.tickFormat(function(d) {
						return d3.time.format('%X')(new Date(d))
					});

			chartMeasurementvalue.yAxis.axisLabel('Value').tickFormat(
					d3.format('d'));

			d3.select('#measurementvalue svg').datum(db_systemmeasurementvalue)
					.transition().duration(500).call(chartMeasurementvalue);

			nv.utils.windowResize(chartMeasurementvalue.update);

			return chartMeasurementvalue;
		});
	};

	var chartAccumulating;
	var redrawAccumulating = function() {

		nv.addGraph(function() {
			chartAccumulating = nv.models.lineChart().showLegend(true)
					.showYAxis(true).showXAxis(true).useInteractiveGuideline(
							true);

			chartAccumulating.xAxis.axisLabel('Time').rotateLabels(-20)
					.tickFormat(function(d) {
						return d3.time.format('%X')(new Date(d))
					});

			chartAccumulating.yAxis.axisLabel('Value').tickFormat(
					d3.format('d'));

			d3.select('#accumulating svg').datum(db_accumulating).transition()
					.duration(500).call(chartAccumulating);

			nv.utils.windowResize(chartAccumulating.update);

			return chartAccumulating;
		});
	};
};