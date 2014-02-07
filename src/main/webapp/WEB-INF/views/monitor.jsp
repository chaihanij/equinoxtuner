<div class="row">
	<div class="col-md-4">
		<div ng-init="onLoadSysGroup()">
			<select class="form-control" ng-model="systemGroupModel"
				ng-options="obj.value+' '+obj.text for obj in sysGroupList"
				ng-change="sysGroupChanged(systemGroupModel)">
				<option value="">Please Select Node</option>
			</select>
		</div>
		<h6>Statistics List</h6>
		<div ng-show="visbleStatistics">
			<div >
				<input id="textSreach" type="text" class="form-control"
					ng-model="search.$" placeholder="Search File">
			</div>	
			<div class="bs-sidebar affix well mygrid-wrapper-div scrollbar" style="margin-top: 5px">
				<!-- style="overflow-y: scroll;" -->
				<ul class="nav" ng-repeat="nodemonitor in nodemonitorlist | filter:search">
					<li ng-class="{ 'active': $index == selectedIndexNodeMonitor }">
						<a ng-click="onClickNodeMonitor($event, nodemonitor, $index)">
							{{nodemonitor.text}} </a>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="col-md-8">
		<table class="col-md-12">
			<tr>
				<td colspan="1" width="20%"><h6>ConnectNode</h6></td>
				<td colspan="3" align="center"><h6>{{systemGroupModel.value}}</h6></td>
				
			</tr>
			<tr>
				<td colspan="1" width="20%"><h6>Application</h6></td>
				<td colspan="3" align="center"><h6>{{systemGroupModel.text}}</h6></td>
			</tr>
			<tr>
				<td colspan="1"  width="20%"><h6>StatisticsFile</h6></td>
				<td colspan="3"  width="30%" align="center"><h6>{{statemonitor.text}}</h6></td>
			</tr>
		</table>
		<hr>
		<br>
		<table class="col-md-12">
			<tr ng-show="visibleCharts">
				<td width="20%"><h6>TimeBegin</h6></td>
				<td width="60%" align="center">
					<div class="input-group date">
						<input type='text' class="form-control" ng-model="timebegin"/>
					 	<span class="input-group-addon dropdown-toggle">
				 			<span class="glyphicon glyphicon-time "></span>
						 </span>
						 <ul class="dropdown-menu">
						 		 <li>
						 	 		<div ng-model="timebeginmillisec" ng-change="changedTimebegin(timebeginmillisec)"style="display:inline-block;">
	        							<timepicker hour-step="1" minute-step="1" show-meridian="false"></timepicker>
	  								</div>
	  							</li>
						</ul>
					</div>
				</td>
				<td align="center" width="20%">
					<button type="button" class="btn btn-primary" ng-click="timeChange(timebegin)">OK</button>
				</td>
			</tr>
		</table>
		
		<div ng-show="visibleCharts" style="margin-top: 5px">
			<div style="margin-top: 5px">
				<accordion>
					<accordion-group is-open="true">
						<accordion-heading>
							<span style="color: black;">
								System Internal Status
							</span>
						</accordion-heading>
						<div class="bs-docs-example">
							<div class="demo-container" align="center">
								<table width="80%">
									<tr>
										<th><h6>Statistics</h6></th>
										<th><h6>Value</h6></th>
									</tr>
									<tr ng-repeat="object in dbSystem">
										<td ng-repeat="(key, value) in object"><h6>{{key}}</h6></td>
										<td ng-repeat="(key, value) in object"><h6>{{value}}</h6></td>
									</tr>
								</table>
							</div>		
						</div>
					</accordion-group>
				 </accordion>
			 </div>
	
			  <div style="margin-top: 5px">
				 <accordion >
					<accordion-group is-open="true">
						<accordion-heading>
							<span style="color: black;">
								System Measurement
							</span>
						</accordion-heading>
						<div class="bs-docs-example">
							<div id="measurementvalue">
								<svg></svg>
							</div>
						</div>
					</accordion-group>
				 </accordion>
			 </div>
			 <div style="margin-top: 5px">
				 <accordion >
					<accordion-group is-open="true">
						<accordion-heading>
							<span style="color: black;">
								System Measurement Acceleration
							</span>
						</accordion-heading>
						<div class="bs-docs-example">
							<div id="measurement">
								<svg></svg>
							</div>
						</div>
					</accordion-group>
				 </accordion>
			 </div>
			 <div style="margin-top: 5px">
				  <accordion>
					<accordion-group is-open="true">
						<accordion-heading>
							<span style="color: black;">
								Accumulating Counters 
							</span>
						</accordion-heading>
						<div class="bs-docs-example">
							<div id="accumulating" align="center">
								<svg></svg>
							</div>
						</div>
					</accordion-group>
				 </accordion>
			 </div>
		</div>
	</div>
</div>