'use strict';
var ConfigAppController = function($scope, $http, $modal, $log) {

	$scope.data;

	$scope.saveConfig = function() {

		var modalInstance = $modal.open({
			templateUrl : 'mySaveConfig.html',
			controller : modalOnSaveInstanceCtrl
		});

		modalInstance.result.then(function() {

			$http({
				method : 'POST',
				url : 'config/savecongfigurationapp.htm',
				data : $scope.data,
				headers : {
					'Content-Type' : 'applictaion/json',
					'mimeType' : 'application/json'
				}
			}).success(function(data, status) {

				if (status == 200) {
					alert("Save Successful");
				} else {
					alert("Save Fail");
				}

			}).error(function(data, status) {
				alert("Save Fail" + " Statue Error" + statue);
			});

		}, function() {

		});

	};

	/** ----------------------- init Page -------------------- * */
	$scope.initConfig = function() {
		$http({
			method : 'GET',
			url : 'config/getcongfigurationapp.htm'

		}).success(function(data, status) {

			if (status == 200) {
				$scope.data = data;
			}

		}).error(function(data, status) {
			$scope.data = data || "Request failed";
			$scope.status = status;
		});
	};

	// $scope.onClear = function() {
	// $http({
	// method : 'GET',
	// url : 'config/getcongfigurationapp.htm'
	//
	// }).success(function(data, status) {
	// $scope.status = status;
	//
	// if (status == 200) {
	// $scope.data = data;
	//
	// }
	// }).error(function(data, status) {
	// $scope.data = data || "Request failed";
	// $scope.status = status;
	// });
	// };

	$scope.addNode = function(node) {

		if (node != undefined && node != null) {
			var arraynode = {
				nodeName : node.nodeName,
				ip : node.ipaddress,
				user : node.username,
				password : node.password
			};

			$scope.data.nodeconnection.push(arraynode);

		} else {

		}
	};

	$scope.editNode = function($index) {

		var modalInstance = $modal.open({
			templateUrl : 'myEditNode.html',
			controller : modalOnEditNodeInstanceCtrl,
			resolve : {
				data : function() {
					return $scope.data.nodeconnection[$index];
				}
			}
		});

		modalInstance.result.then(function() {

		}, function() {

		});
	};

	$scope.removeNode = function($index) {

		var modalInstance = $modal.open({
			templateUrl : 'myDeleteNode.html',
			controller : modalOnDeleteNodeInstanceCtrl,
			resolve : {
				data : function() {

					return $scope.data.nodeconnection[$index];

				}
			}
		});

		modalInstance.result.then(function() {

			$scope.data.nodeconnection.splice($index, 1);

		}, function() {

		});

	};

	/** State Active * */
	$scope.addStat = function(stat) {

		if (stat != undefined && stat != null) {
			var arraynode = {
				name : stat.name,
				max : stat.max,
				min : stat.min,
				unit : stat.scale,
				description : stat.description
			};
			$scope.data.activeelement.push(arraynode);

		} else {

		}
	};

	$scope.removeStat = function($index) {

		var modalInstance = $modal.open({
			templateUrl : 'myDeleteElement.html',
			controller : modalOnDeleteElementInstanceCtrl,
			resolve : {
				data : function() {

					return $scope.data.activeelement[$index];

				}
			}
		});

		modalInstance.result.then(function() {

			$scope.data.activeelement.splice($index, 1);

		}, function() {

		});
	};

	/** State Equinox * */
	// InernalState
	$scope.removeInternalState = function($index) {

		var modalInstance = $modal.open({
			templateUrl : 'myDeleteInternalstat.html',
			controller : modalOnDeleteInternalStatInstanceCtrl,
			resolve : {
				data : function() {

					return $scope.data.statequinox.internalstat[$index];

				}
			}
		});

		modalInstance.result.then(function() {

			$scope.data.statequinox.internalstat.splice($index, 1);

		}, function() {

		});

	};

	$scope.addInternalState = function(internalstat) {

		var check = false;
		for ( var int = 0; int < $scope.data.statequinox.internalstat.length; int++) {
			if (internalstat == $scope.data.statequinox.internalstat[int].name) {
				check = true;
			}
		}

		var arraynode = {
			name : internalstat
		};

		if (check == false) {
			$scope.internalstat = "";
			$scope.data.statequinox.internalstat.push(arraynode);
		} else {
			alert("Duplicate");
		}

	};

	// Measurement
	$scope.removeMeasurementState = function($index) {

		var modalInstance = $modal.open({
			templateUrl : 'myDeleteMeasurementstat.html',
			controller : modalOnDeleteMeasurementStatInstanceCtrl,
			resolve : {
				data : function() {

					return $scope.data.statequinox.measurement[$index];

				}
			}
		});

		modalInstance.result.then(function() {

			$scope.data.statequinox.measurement.splice($index, 1);

		}, function() {

		});
	};

	$scope.addMeasurementState = function(measurementstate) {

		var check = false;
		for ( var int = 0; int < $scope.data.statequinox.measurement.length; int++) {
			if (DEBUG == true)
				console.log(measurementstate + " check "
						+ $scope.data.statequinox.measurement[int]);
			if (measurementstate == $scope.data.statequinox.measurement[int].name) {
				check = true;
			}
		}

		var arraynode = {
			name : measurementstate
		};

		if (check == false) {
			$scope.measurementstate = "";
			$scope.data.statequinox.measurement.push(arraynode);
		} else {
			alert("Duplicate");
		}
	};

	// Accumulating
	$scope.removeAccumulatingState = function($index) {

		var modalInstance = $modal.open({
			templateUrl : 'myDeleteAccumulatingstat.html',
			controller : modalOnDeleteAccumulatingStatInstanceCtrl,
			resolve : {
				data : function() {

					return $scope.data.statequinox.accumulating[$index];

				}
			}
		});

		modalInstance.result.then(function() {

			$scope.data.statequinox.accumulating.splice($index, 1);

		}, function() {

		});

	};

	$scope.addAccumulatingState = function(accumulatingstate) {

		var check = false;
		for ( var int = 0; int < $scope.data.statequinox.accumulating.length; int++) {
			if (DEBUG == true) {
				console.log(accumulatingstate + " check "
						+ $scope.data.statequinox.accumulating[int]);
			}
			if (accumulatingstate == $scope.data.statequinox.accumulating[int].name) {
				check = true;
			}
		}

		var arraynode = {
			name : accumulatingstate
		};

		if (check == false) {
			$scope.accumulatingstate = "";
			$scope.data.statequinox.accumulating.push(arraynode);
		} else {
			alert("Duplicate");
		}
	};

};

// ===================================================================================================
var modalOnSaveInstanceCtrl = function($scope, $modalInstance) {

	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};

// Delete
var modalOnDeleteNodeInstanceCtrl = function($scope, $modalInstance, data) {

	$scope.nodeconnect = data;

	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

var modalOnEditNodeInstanceCtrl = function($scope, $modalInstance, data) {

	$scope.nodes = data;

	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

var modalOnDeleteInternalStatInstanceCtrl = function($scope, $modalInstance,
		data) {
	$scope.internalstat = data;
	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

var modalOnDeleteMeasurementStatInstanceCtrl = function($scope, $modalInstance,
		data) {
	$scope.internalstat = data;
	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

var modalOnDeleteAccumulatingStatInstanceCtrl = function($scope,
		$modalInstance, data) {
	$scope.internalstat = data;
	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};
var modalOnDeleteElementInstanceCtrl = function($scope, $modalInstance, data) {

	$scope.internalstat = data;
	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

};

// ===================================================================================================

