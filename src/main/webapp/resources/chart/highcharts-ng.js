'use strict';
angular
		.module('highcharts-ng', [])
		.directive(
				'highchart',
				function() {

					// IE8 support
					var indexOf = function(arr, find, i /* opt */) {
						if (i === undefined)
							i = 0;
						if (i < 0)
							i += arr.length;
						if (i < 0)
							i = 0;
						for ( var n = arr.length; i < n; i++)
							if (i in arr && arr[i] === find)
								return i;
						return -1;
					};

					function deepExtend(destination, source) {
						for ( var property in source) {
							if (source[property]
									&& source[property].constructor
									&& source[property].constructor === Object) {
								destination[property] = destination[property]
										|| {};
								deepExtend(destination[property],
										source[property]);
							} else {
								destination[property] = source[property];
							}
						}
						return destination;
					}

					var seriesId = 0;
					var ensureIds = function(series) {
						angular.forEach(series, function(s) {
							if (!angular.isDefined(s.id)) {
								s.id = 'series-' + seriesId++;
							}
						});
					};

					var getMergedOptions = function(scope, element, config) {
						var mergedOptions = {};

						var defaultOptions = {
							chart : {
								width : 600,
								events : {},
								zoomType : 'x'
							},
							title : {},
							subtitle : {},
							series : [],
							credits : {},
							plotOptions : {},
							tooltip : {
								formatter : function() {
									return '<b>'
											+ this.series.name
											+ '</b><br/>'
											+ Highcharts
													.dateFormat(
															'%Y-%m-%d %H:%M:%S',
															this.x) + '<br/>'
											+ Highcharts.numberFormat(this.y);
								}
							},
							xAxis : {
								type : 'datetime',
								tickPixelInterval : 150,
								title : {
									enabled : true,
									text : 'Time'
								},
								labels : {
									rotation : -25,
									formatter : function() {
										return Highcharts.dateFormat(
												'%H:%M:%S', this.value);
									}
								},
							},
							navigator : {
								enabled : true
							}
						};

						if (config.options) {
							mergedOptions = deepExtend(defaultOptions,
									config.options);
						} else {
							mergedOptions = defaultOptions;
						}
						mergedOptions.chart.renderTo = element[0];

						if (config.title) {
							mergedOptions.title = config.title;
						}
						if (config.subtitle) {
							mergedOptions.subtitle = config.subtitle;
						}
						if (config.credits) {
							mergedOptions.credits = config.credits;
						}
						return mergedOptions;
					};

					var processExtremes = function(chart, axis, axisName) {

					};

					var chartOptionsWithoutEasyOptions = function(options) {
						return angular.extend({}, options, {
							data : null,
							visible : null
						});
					};

					var prevOptions = {};

					var processSeries = function(chart, series) {

						var ids = [];
						if (series) {
							ensureIds(series);
							angular
									.forEach(
											series,
											function(s) {
												ids.push(s.id);

												var chartSeries = chart
														.get(s.id);

												if (chartSeries) {

													if (!angular
															.equals(
																	prevOptions[s.id],
																	chartOptionsWithoutEasyOptions(s))) {

														chartSeries
																.update(
																		angular
																				.copy(s),
																		false);

													} else {

														if (s.visible !== undefined
																&& chartSeries.visible !== s.visible) {
															chartSeries
																	.setVisible(
																			s.visible,
																			false);
														}

														if (chartSeries.options.data !== s.data) {

															chartSeries
																	.setData(
																			s.data,
																			false);

														}
													}

												} else {
													chart.addSeries(angular
															.copy(s), false);
												}
											});
						}

					};

					var initialiseChart = function(scope, element, config) {
						config = config || {};
						var mergedOptions = getMergedOptions(scope, element,
								config);
						var chart = config.useHighStocks ? new Highcharts.StockChart(
								mergedOptions)
								: new Highcharts.Chart(mergedOptions);

						if (config.loading) {
							chart.showLoading();
						}
						chart.redraw();
						return chart;
					};

					return {
						restrict : 'EAC',
						replace : true,
						template : '<div align="center"></div>',
						scope : {
							config : '=config',
							statname : '=statname'
						},
						link : function(scope, element, attrs) {

							var chart = false;
							function initChart() {
								if (chart)
									chart.destroy();
								prevOptions = {};
								seriesId = 0;
								chart = initialiseChart(scope, element,
										scope.config);
							}
							initChart();

							scope.$watch('config.series', function(newSeries,
									oldSeries) {
								// do nothing when called on registration
								if (newSeries === oldSeries)
									return;

								processSeries(chart, newSeries);
								chart.redraw();
							}, true);

							scope.$watch('config.title', function(newTitle) {
								chart.setTitle(newTitle, true);
							}, true);

							scope.$watch('config.subtitle', function(
									newSubtitle) {
								chart.setTitle(true, newSubtitle);
							}, true);

							scope.$watch('config.loading', function(loading) {
								if (loading) {
									chart.showLoading();
								} else {
									chart.hideLoading();
								}
							});

							scope.$watch('config.credits.enabled', function(
									enabled) {
								if (enabled) {
									chart.credits.show();
								} else if (chart.credits) {
									chart.credits.hide();
								}
							});

							scope.$watch('config.useHighStocks', function(
									useHighStocks) {
								initChart();
							});
							scope.$on('$destroy', function() {
								if (chart)
									chart.destroy();
								element.remove();
							});

							scope.$watch('statname', function(newStaname,
									oldStaname) {
								// do nothing when called on registration
								if (!(newStaname === oldStaname)) {
									chart.destroy();
									chart = false;
									initChart();
								}

							}, true);

						}
					};
				});