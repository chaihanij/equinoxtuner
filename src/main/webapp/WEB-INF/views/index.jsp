<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="AngularSpringApp">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="resources/images/equinox-logo-16x16.png">


<title>Equinox Tuner Configuration</title>

<!-- JQuery -->
<script src="resources/js/jquery-1.10.2.js"></script>
<script src="resources/js/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css" />
<!-- AngulaJS -->
<script src="resources/angular/angular.js"></script>
<script src="resources/angular/angular-route.js"></script>
<script src="resources/js/angular-ui.js"></script>

<!-- Bootstrap core CSS && JS-->
<script src="resources/bootstrap/js/bootstrap.js"></script>
<link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="resources/bootstrap/css/bootstrap-theme.css"
	rel="stylesheet">
<link href="resources/bootstrap/css/iconbostrap.css" rel="stylesheet" />
<!-- Custom styles for this template -->
<link href="resources/css/starter-template.css" rel="stylesheet">
<link href="resources/css/docs.css" rel="stylesheet">
<!--  AngulaJS && Module Bootstrap-->
<script src="resources/js/ui-bootstrap-custom-0.9.0.js"></script>
<script src="resources/js/ui-bootstrap-custom-tpls-0.9.0.js"></script>
<!-- $D3 -->

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
	<script src="resources/js/html5shiv.js"></script>
	<script src="resources/js/response.js"></script>
<![endif]-->

</head>
<body ng-controller="MainCtrl">
	<!-- navigation bar -->
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand"> <img
					src="resources/images/equinox-100x20.png">
				</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li id="limonitor" ng-class="{ active: activePath=='/monitor' }">
						<a href="#/monitor">Monitor</a>
					</li>
					<li id="lituner" ng-class="{ active: activePath=='/tunerconfig' }">
						<a href="#/tunerconfig">Tuner</a>
					</li>
					<li id="loiconfig" ng-class="{ active: activePath=='/config' }"">
						<a href="#/config">Configuration</a>
					</li>
				</ul>
				<div class="navbar-text pull-right dropdown">
					Logged in as <a class="dropdown-toggle" role="button"
						data-toggle="dropdown"> ${username} </a>
					<ul class="dropdown-menu">
						<li><a href="<c:url value="/j_spring_security_logout"/>">Log
								out </a></li>
					</ul>
				</div>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<div class="container starter-template" ng-view></div>
	<!-- /.container -->

	<script src="resources/js/main.js"></script>
	<script src="resources/js/monitor.js"></script>
	<script src="resources/js/tuner.js"></script>
	<script src="resources/js/configapp.js"></script>

	<!-- D3 && NVD3 -->
	<link href="resources/nvd3/nv.d3.css" rel="stylesheet">
	<script src="resources/nvd3/d3.v3.js"></script>
	<script src="resources/nvd3/nv.d3.js"></script>
	<!-- <script src="resources/nvd3/angularjs-nvd3-directives.js"></script> -->
</body>
</html>