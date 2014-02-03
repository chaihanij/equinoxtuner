<div class="row">
	<div class="col-md-4">
		<div ng-init="onLoadSysGroup()">
			<select class="form-control" ng-model="systemGroupModel"
				ng-options="obj.value+' '+obj.text for obj in sysGroupList"
				ng-change="sysGroupChanged(systemGroupModel)">
				<option value="">Please Select Node</option>
			</select>
		</div>
		
		<h5>Statistics List</h5>
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
		<table>
			<tr>
				<td><h5>Connect Node</h5></td>
				<td align="center"><h5>{{systemGroupModel.value}}</h5></td>
			</tr>
			<tr>
				<td><h5>Application</h5></td>
				<td align="center"><h5>{{systemGroupModel.text}}</h5></td>
			</tr>
			<tr>
				<td><h5>Statistics File</h5></td>
				<td align="center"><h5>{{statemonitor.text}}</h5></td>
			</tr>
		</table>
		
		<div ng-show="visibleCharts">
		
			<div style="margin-top: 5px">
				<accordion>
					<accordion-group is-open="true">
						<accordion-heading>
							<span style="color: black;">
								System Internal Status
							</span>
						</accordion-heading>
						<div class="bs-docs-example">
							<div class="demo-container">
								<table class="table">
									<tr>
										<th><h5>Statistics</h5></th>
										<th><h5>Value</h5></th>
									</tr>
									<tr ng-repeat="object in dbSystem">
										<td ng-repeat="(key, value) in object">{{key}}</td>
										<td ng-repeat="(key, value) in object">{{value}}</td>
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
							<div id="accumulating">
								<svg></svg>
							</div>
						</div>
					</accordion-group>
				 </accordion>
			 </div>
		</div>
	</div>
</div>