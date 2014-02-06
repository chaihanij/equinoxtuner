<div class="row">
	<div class="col-md-4">
		<div ng-init="onLoadSysGroup()">
			<select class="form-control" ng-model="systemGroupModel"
				ng-options="obj.value+' '+obj.text for obj in sysGroupList"
				ng-change="sysGroupChanged(systemGroupModel)">
				<option value="">Please Select Node</option>
			</select>
		</div>
		<h5>Configuration List</h5>
		<div ng-show="visbleConfigListFlie">
			<!-- search -->
			<div>
				<input id="textSreach" type="text" class="form-control"
					ng-model="search.$" placeholder="Search File">
			</div>
			<div>
				<label class="checkbox"> <input id="checkboxAdSreach"
					type="checkbox" ng-model="modelVisibleInput"> Advance
					Search
				</label>
			</div>
			<div class="row" ng-show="modelVisibleInput" align="center">
				<div class="col-md-3" align="center">
					<input type="text" class="form-control" ng-model="search.app">
				</div>
				<div class="col-md-3" align="center">
					<input type="text" class="form-control" ng-model="search.service">
				</div>
				<div class="col-md-3" align="center">
					<input type="text" class="form-control" ng-model="search.site">
				</div>
				<div class="col-md-3" align="center">
					<input type="text" class="form-control" ng-model="search.node">
				</div>
			</div>
			<!-- search -->
			<div class="bs-sidebar affix well mygrid-wrapper-div scrollbar">
				<!-- style="overflow-y: scroll;" -->
				<ul class="nav"
					ng-repeat="configFileName in listConfigFileName | filter:search">
					<li ng-class="{ 'active': $index == selectedIndex }"><a
						ng-click="onClickConfigFileName($event, configFileName, $index)">
							{{configFileName.text}}</a></li>
				</ul>
			</div>
		</div>

	</div>
	<div class="col-md-8">
		<table width="100%">
			<tr>
				<td width="20%"><h5>Connect Node</h5></td>
				<td width="30%" align="center"><h5>{{systemGroupModel.value}}</h5></td>
				<td width="20%"><h5>Application</h5></td>
				<td width="30%" align="center"><h5>{{systemGroupModel.text}}</h5></td>
			</tr>
			<tr>
				<td width="20%"><h5>Configuration File</h5></td>
				<td width="30%" align="center"><h5>{{configName.text}}</h5></td>
				<td></td>
				<td></td>
			</tr>
		
		</table>
		<hr>
		<div ng-show="visbleConfiguration">

			<accordion ng-show="visbleAccordionSFLOG">
				<accordion-group>
					<accordion-heading> 
						<span>
							SFLOG = 
							<span style="color: red;">{{strSFLOG}}</span>
						</span> 
					</accordion-heading>
					<div class="bs-docs-example">
						<table class="table table-striped">
							<tr ng-repeat="SFLOG in SFLOGCheckbox">
								<td><label class="checkbox">
									<input id="FATAL" type="checkbox" ng-model="SFLOG.checked" ng-checked="SFLOG.checked" ng-change="valueSFLOG()" /> 
										<span>{{SFLOG.name}}</span>
									</label>
								</td>
							</tr>
						</table>
					</div>
				</accordion-group>
			</accordion>
			<div style="margin-top: 5px" ng-show="visbleAccordionWram" ng-repeat="slider in configurationWram">
				<accordion> 
					<accordion-group>
						<accordion-heading>
							<span>{{slider.text}} = 
								<span style="color: red;">{{slider.value}}</span>
								&nbsp;&nbsp;&nbsp;&nbsp;{{slider.unit}}
							</span> 
						</accordion-heading>
						<div class="bs-docs-example">
							<table class="table table-striped">
								<tr>
									<td align="center">min</td>
									<td style="width: 100%" colspan="4"></td>
									<td class="text-center">max</td>
								</tr>
								<tr>
									<td align="center">{{slider.min}}</td>
									<td style="width: 100%" colspan="4">
										<div class="slider2" ui-slider min="slider.min" max="slider.max"
											ng-model="slider.value"></div>
									</td>
									<td class="text-center">{{slider.max}}</td>
								</tr>
								<tr>
									<td>Value</td>
									<td style="width: 100%;" colspan="4"><input
										class="form-control" name="input" type="text"
										ng-model="slider.value" max="slider.max" min="slider.min"
										onkeypress="return isNumberKey(event);"
										placeholder="{{slider.value}}" integer required></td>
									<td><a ng-click="isCollapsed = !isCollapsed">description</a></td>
								</tr>
							</table>
							<div collapse="!isCollapsed">
								<div class="well well-large">
									<p>{{slider.description}}</p>
								</div>
							</div>
						</div>
					</accordion-group> 
				</accordion>
			</div>

			<div class="row" style="margin-top: 5px" align="center">
				<div class="col-md-3">
					<button id="btnApply" class="btn btn-primary btn-lg btn-block"
						ng-click="onApply()">Apply</button>
				</div>
				<div class="col-md-3">
					<button id="btnRecovery" class="btn btn-primary btn-lg btn-block"
						ng-click="onRecovery()">Recovery</button>
				</div>
				<div class="col-md-3">
					<button id="btnSave" class="btn btn-primary btn-lg btn-block"
						ng-click="onSave()">Save</button>
				</div>
				<div class="col-md-3">
					<button id="btnCancel" class="btn btn-primary btn-lg btn-block"
						ng-click="onLoad()">Load</button>
				</div>
			</div>

		</div>
	</div>
</div>

<!-- myModalApply -->
<script type="text/ng-template" id="myModalApply.html">
        <div class="modal-header">
            <h3>Apply</h3>
        </div>
        <div class="modal-body">
			<div><label>Time:{{date}}</label></div>
        	<div><label>Apply File Nmae: {{fileName}}</label></div>
        </div>
        </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" ng-click="ok()">OK</button>
            <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
        </div>
</script>
<!-- /myModalApply -->
<!-- myModalRecovery -->
<script type="text/ng-template" id="myModalRecovery.html">
        <div class="modal-header">
            <h3>Recovery</h3>
        </div>
        <div class="modal-body">
	        <div><label>Time {{date}}</label></div>
       		<div><b>Recovery File :	{{fileName}}</b></div>
			<div>
				<ul>
                	<li ng-repeat="recoveryfile in listrecoveryfile">
                    	<a ng-click="onSelectFile(recoveryfile)" ng-dblclick="onDBClickRecovery(recoveryfile)">
                    		{{recoveryfile.value}}
                    	</a>
                	</li>
            	</ul>
			</div>
			<div>Selected: <b>{{selected.value}}</b></div>
        </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" ng-click="ok()">OK</button>
            <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
        </div>
</script>
<!-- /myModalRecovery -->
<!-- myModalSave -->
<script type="text/ng-template" id="myModalSave.html">
        <div class="modal-header">
            <h3>Save</h3>
        </div>
        <div class="modal-body">
        	<div><label>Configuration Name : {{fileName}}</label></div>
        	<div><input type="text" ng-model="nickname" style="display: block; 
        			min-height: 30px; border-box; -moz-box-sizing: 
        			border-box; box-sizing: border-box;" placeholder="Draft"
        		>
        	</div>
        	<div><label>Save File Name : {{fileName}}.{{date}}.{{nickname}}</label></div>
        </div>
        </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" ng-click="ok(nickname)">OK</button>
            <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
        </div>
</script>
<!-- /myModalSave -->
<!-- ================== myModalLoad ================-->
<script type="text/ng-template" id="myModalLoad.html">
        <div class="modal-header">
            <h3>Load</h3>
        </div>
        <div class="modal-body">
       		<div><b>Load File :	{{fileName}}</b></div>
			<div>
				<ul>
                	<li ng-repeat="loadfile in loadlist">
                    	<a ng-click="onSelectFile(loadfile)" ng-dblclick="onDBClickLoad(loadfile)">
                    		{{loadfile.value}}
                    	</a>
                	</li>
            	</ul>
			</div>
			<div>Selected: <b>{{selected.value}}</b></div>
        </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" ng-click="ok()">OK</button>
            <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
        </div>
</script>
<!-- ================== /myModalLoad ================-->
<script type="text/ng-template" id="myShowValueConfig.html">
<div class="modal-header">
	<h3>{{name}}</h3>
	</div>
	<div class="modal-body">
		<div class = "container-fluid">
			<div class="span6" style="color: red"></div>
			<div class="span6" style="color: blue"></div>
		</div>
	<div>	
	<div class="modal-footer">
		<button class="btn btn-warning" ng-click="cancel()">Close</button>
	</div>
</script>