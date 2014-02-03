'use strict';
var TunerCongfigController = function($scope, $http, $modal, $log) {

	/*
	 * Init Page
	 */

	$scope.sysGroupList;

	$scope.visbleConfigListFlie = false;

	$scope.onLoadSysGroup = function() {

		$http.get('tunerconfig/getsystemgroup.htm').success(

		function(data, status) {

			if (status == 200) {

				$scope.sysGroupList = data;

			} else {
				
			}

		}).error(function(data, status) {
			
		});
	};

	$scope.sysGroupChanged = function(obj) {

		$scope.configurationFileName = null;
		$scope.configurationSFLOG = null;
		$scope.configurationWram = null;

		$scope.visbleConfigListFlie = false;
		$scope.visbleConfiguration = false;

		if (obj != null && obj != undefined) {

			$http({
				url : 'tunerconfig/getcongfigurationname.htm',
				method : "POST",
				data : obj,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {

				if (status == 200) {

					if (!jQuery.isEmptyObject(data)) {

						$scope.listConfigFileName = customize(data);

						if ($scope.listConfigFileName.length - 1 <= 0) {

							alert("Is not configuration file.");
							$scope.visbleConfigListFlie = false;

						} else {

							$scope.visbleConfigListFlie = true;

						}

					} else {

						alert("Connection Fail");
						$scope.visbleConfigListFlie = false;
					}

				}

			}).error(function(data, status) {

				alert("Connection Fail");
			

			});
		}
	};

	var customize = function(data) {
		var listConfigFileName = [];
		if ((data.length > 0) && (data != null)) {
			var listConfigFileName = [];
			for (var i = 0; i < data.length; i++) {

				var arrayvalue = data[i].value.split(".");

				var object = {
					"text" : data[i].text,
					"value" : data[i].value,
					"app" : arrayvalue[0],
					"service" : arrayvalue[1],
					"site" : arrayvalue[2],
					"node" : arrayvalue[3]
				}
				listConfigFileName.push(object);

			}

			return listConfigFileName;

		} else {
			return [ {
				"text" : null,
				"value" : null
			} ];
		}
	};

	/*
	 * Variable Configuration
	 */

	$scope.configurationFileName = null;
	$scope.configurationSFLOG = null;
	$scope.configurationWram = null;

	$scope.selectedIndex;

	$scope.visbleConfiguration = false;
	$scope.visbleAccordionSFLOG = false;
	$scope.visbleAccordionWram = false;

	$scope.onClickConfigFileName = function($event, configName, $index) {

		$scope.configurationFileName = null;
		$scope.configurationSFLOG = null;
		$scope.configurationWram = null;

		$scope.visbleConfiguration = false;
		$scope.visbleAccordionSFLOG = false;

		$scope.selectedIndex = $index;
		$scope.configName = configName;

		$http({

			url : 'tunerconfig/getconfigurationdetail.htm',
			method : "POST",
			data : configName,
			headers : {
				'Content-Type' : 'applictaion/json',
				'mimeType' : 'application/json'
			}

		}).success(function(data, status) {

			if (status == 200) {

				if (!jQuery.isEmptyObject(data)) {

					$scope.visbleConfiguration = true;

					$scope.configurationFileName = data[0].name;
					$scope.configurationSFLOG = data[0].sflog;
					$scope.configurationWram = data[0].wram;

					if (!jQuery.isEmptyObject($scope.configurationSFLOG)) {

						$scope.visbleAccordionSFLOG = true;
						setValueCheckbok($scope.configurationSFLOG);
						$scope.valueSFLOG();

					} else {

						$scope.visbleAccordionSFLOG = false;

					}

					if (!jQuery.isEmptyObject($scope.configurationWram)) {

						$scope.visbleAccordionWram = true;

					} else {

						$scope.visbleAccordionWram = false;
					}

				} else {
					alert("Not Data");
				}

			} else {

			
			}

		}).error(function(data, status) {
		
		});
	};

	var setValueCheckbok = function(configurationSFLOG) {

		$scope.SFLOGCheckbox = [ {
			"name" : "FATAL",
			"checked" : false
		}, {
			"name" : "ERROR",
			"checked" : false
		}, {
			"name" : "WARN",
			"checked" : false
		}, {
			"name" : "INFO",
			"checked" : false
		}, {
			"name" : "DEBUG",
			"checked" : false
		} ];

		var arraySFLOG = configurationSFLOG.SFLOG.split("|");

		for (var i = 0; i < arraySFLOG.length; i++) {

			if (arraySFLOG[i] == 'FATAL') {
				// $scope.SFLOGFATAL = true;
				$scope.SFLOGCheckbox[0].checked = true;
			} else if (arraySFLOG[i] == 'ERROR') {
				// $scope.SFLOGERROR = true;
				$scope.SFLOGCheckbox[1].checked = true;
			} else if (arraySFLOG[i] == 'WARN') {
				// $scope.SFLOGWARN = true;
				$scope.SFLOGCheckbox[2].checked = true;
			} else if (arraySFLOG[i] == 'INFO') {
				// $scope.SFLOGINFO = true;
				$scope.SFLOGCheckbox[3].checked = true;
			} else if (arraySFLOG[i] == 'DEBUG') {
				// $scope.SFLOGDEBUG = true;
				$scope.SFLOGCheckbox[4].checked = true;
			}
		}
	};

	$scope.valueSFLOG = function() {
		var strSFLOG = "";
		for (var i = 0; i < $scope.SFLOGCheckbox.length; i++) {

			if ($scope.SFLOGCheckbox[i].checked != false) {

				strSFLOG += $scope.SFLOGCheckbox[i].name + "|";
			}

		}
		$scope.strSFLOG = strSFLOG.substring(0, strSFLOG.length - 1);
	};

	/** --------- onApply AlertBox --------- * */
	$scope.onApply = function() {

		var modalInstance = $modal.open({
			templateUrl : 'myModalApply.html',
			controller : ModalOnApplyInstanceCtrl,
			resolve : {
				fileName : function() {
					return $scope.configurationFileName;
				},
				SFLOG : function() {
					return $scope.strSFLOG;
				},
				WRAN : function() {
					return $scope.configurationWram;
				}
			}
		});

		modalInstance.result.then(function(jsObj) {
			/* selectedItem */
			$scope.objectConfig = jsObj;

			$http({
				url : 'tunerconfig/onclickapply.htm',
				method : "POST",
				data : jsObj,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {

			}).error(function(data, status) {
				alert("Error status= " + status + " data=" + data);
			});

		}, function() {
			// $log.info('Modal dismissed at: ' + new Date());
		});
	};

	/** --------- onRecovery AlertBox --------- * */
	$scope.onRecovery = function() {

		var modalInstance = $modal.open({
			templateUrl : 'myModalRecovery.html',
			controller : ModalOnRecoveryInstanceCtrl,
			resolve : {
				fileName : function() {
					return $scope.configurationFileName;
				}
			}
		});

		modalInstance.result.then(function(jsObj) {

			$http({
				url : 'tunerconfig/onclickrecovery.htm',
				method : "POST",
				data : jsObj,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {

				$scope.configurationFileName = data[0].name;
				$scope.configurationSFLOG = data[0].sflog;
				$scope.configurationWram = data[0].wram;

				if (!jQuery.isEmptyObject($scope.configurationSFLOG)) {

					$scope.visbleAccordionSFLOG = true;
					setValueCheckbok($scope.configurationSFLOG);
					$scope.valueSFLOG();

				} else {

					$scope.visbleAccordionSFLOG = false;

				}

				if (!jQuery.isEmptyObject($scope.configurationWram)) {

					$scope.visbleAccordionWram = true;

				} else {

					$scope.visbleAccordionWram = false;
				}

			}).error(function(data, status) {

			});

		}, function() {
			// $log.info('Modal dismissed at: ' + new Date());
		});
	};

	/** --------- onSave AlertBox --------- * */
	$scope.onSave = function() {

		var modalInstance = $modal.open({
			templateUrl : 'myModalSave.html',
			controller : ModalOnSaveInstanceCtrl,
			resolve : {
				fileName : function() {
					return $scope.configurationFileName;
				},
				SFLOG : function() {
					return $scope.strSFLOG;
				},
				WRAN : function() {
					return $scope.configurationWram;
				}
			}
		});

		modalInstance.result.then(function(jsObj) {
			/* selectedItem */
			$scope.objectConfig = jsObj;
			$http({
				url : 'tunerconfig/onclicksave.htm',
				method : "POST",
				data : jsObj,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {

			}).error(function(data, status) {
				alert("Error status= " + status + " data=" + data);
			});

		}, function() {
			$log.info('Modal dismissed at: ' + new Date());
		});
	};

	/** --------------- OnLoad AlertBox ----------------* */
	$scope.onLoad = function() {

		var modalInstance = $modal.open({
			templateUrl : 'myModalLoad.html',
			controller : ModalOnLoadInstanceCtrl,
			resolve : {
				fileName : function() {
					return $scope.configurationFileName;
				}
			}
		});

		modalInstance.result.then(function(jsObj) {
			$scope.loadtest = jsObj;
			$http({
				url : 'tunerconfig/actionload.htm',
				method : "POST",
				data : jsObj,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {
				$scope.data = data;
				$scope.configurationFileName = data[0].name;
				$scope.configurationSFLOG = data[0].sflog;
				$scope.configurationWram = data[0].wram;

				/*------- Create Slider and Check box  -------*/
				if (null !== $scope.configurationSFLOG) {

					$scope.visibleSFLOG = true;
					setValueCheckbok($scope.configurationSFLOG);
					$scope.valueSFLOG();
				}
			}).error(function(data, status) {
				alert("Error status= " + status + " data=" + data);
			});
		}, function() {
			$log.info('Modal dismissed at: ' + new Date());
		});
	};

};
// ========================================================================================================
/** --------- onApply AlertBox --------- * */
var ModalOnApplyInstanceCtrl = function($scope, $modalInstance, fileName,
		SFLOG, WRAN) {
	$scope.date = new Date().format("yyyy-MM-dd h:mm:ss");
	$scope.fileName = fileName;
	$scope.SFLOG = SFLOG;
	$scope.WRAN = WRAN;

	$scope.ok = function() {
		var jsObj = null;
		var wram = [];

		wram.push({
			"text" : "SFLOG",
			"value" : SFLOG
		});
		for (var i = 0; i < WRAN.length; i++) {
			var object = {
				"text" : WRAN[i].text,
				"value" : WRAN[i].value
			}
			wram.push(object);
		}
		jsObj = {
			"configname" : fileName,
			"wrams" : wram
		};
		$scope.objectConfig = jsObj;

		$modalInstance.close(jsObj);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

/** --------- OnRecovery AlertBox --------- * */
var ModalOnRecoveryInstanceCtrl = function($scope, $http, $modalInstance,
		fileName) {

	$scope.date = new Date().format("yyyy-MM-dd h:mm:ss");
	$scope.fileName = fileName;
	$scope.listrecoveryfile;
	$scope.selected;

	$http({
		url : 'tunerconfig/getrecoverylist.htm',
		method : "POST",
		data : fileName,
		headers : {
			'Content-Type' : 'applictaion/json',
			'mimeType' : 'application/json'
		}
	}).success(function(data, status) {

		$scope.listrecoveryfile = data.list

	}).error(function(data, status) {

		alert("Error status= " + status + " data=" + data);

	});

	$scope.onDBClickRecovery = function(recoveryfile) {

	};

	$scope.onSelectFile = function(recoveryfile) {
		$scope.selected = recoveryfile;
	};

	$scope.ok = function() {
		var daterecovery = $scope.selected;
		var jsObj = null;
		jsObj = {
			"filename" : $scope.fileName,
			"date" : daterecovery.value
		};
		$modalInstance.close(jsObj);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

/** --------- onSave AlertBox --------- * */
var ModalOnSaveInstanceCtrl = function($scope, $modalInstance, fileName, SFLOG,
		WRAN) {

	$scope.date = new Date().format("yyyyMMdd_h:mm:ss");

	$scope.fileName = fileName;
	$scope.SFLOG = SFLOG;
	$scope.WRAN = WRAN;

	$scope.ok = function(nickname) {

		var nick = "";
		if (nickname != undefined) {
			nick = nickname;
		}

		var jsObj = null;
		var wram = [];

		wram.push({
			"text" : "SFLOG",
			"value" : SFLOG
		});
		for (var i = 0; i < WRAN.length; i++) {
			var object = {
				"text" : WRAN[i].text,
				"value" : WRAN[i].value
			}
			wram.push(object);
		}
		jsObj = {
			"configname" : fileName,
			"date" : $scope.date,
			"nickname" : nick,
			"wrams" : wram
		};
		$scope.objectConfig = jsObj;

		$modalInstance.close(jsObj);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};

/** ===================== onLoad AlertBox =======================* */
var ModalOnLoadInstanceCtrl = function($scope, $http, $modal, $modalInstance,
		fileName) {

	$scope.fileName = fileName;
	$scope.loadlist;
	$scope.selected;

	$http({
		url : 'tunerconfig/getloadlist.htm',
		method : "POST",
		data : fileName,
		headers : {
			'Content-Type' : 'applictaion/json',
			'mimeType' : 'application/json'
		}
	}).success(function(data, status) {

		$scope.loadlist = data.list

	}).error(function(data, status) {

		alert("Error status= " + status + " data=" + data);

	});

	$scope.onDBClickLoad = function(loadfile) {

		var modalInstance = $modal.open({
			templateUrl : 'myShowValueConfig.html',
			controller : ModalShowValueConfigInstanceCtrl,
			resolve : {

			}
		});

		modalInstance.result.then(function() {

		}, function() {

		});

	};
	$scope.selected = null;
	$scope.onSelectFile = function(loadfile) {

		$scope.selected = loadfile;

	};

	$scope.ok = function() {
		var load = $scope.selected;
		var jsObj = null;
		jsObj = {
			"filename" : $scope.fileName,
			"value" : load
		};
		if ($scope.selected != null) {
			$modalInstance.close(jsObj);
		}
	};

	$scope.cancel = function() {

		$modalInstance.dismiss('cancel');
	};
};

// ========================================================================================================
/**
 * Validate is not Number
 */

function isNumberKey(evt) {
	var charCode = (evt.which) ? evt.which : event.keyCode
	return !(charCode > 31 && (charCode < 48 || charCode > 57));
};

/**
 * Time Format
 */
Date.prototype.format = function(format) { // author: meizz

	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};