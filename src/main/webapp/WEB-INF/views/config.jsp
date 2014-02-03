<div class="row" ng-init="initConfig()">
	<div class="col-md-1"></div>
	<div class="col-md-10">
		<section id="system-path">
			<div class="page-header">
				<h3>Path</h3>
			</div>
			<table width="100%">
				<tr>
					<td width="30%"><h4>Configuration Path</h4></td>
					<td width="60%"><input type="text" class="form-control"
						name="configPath" ng-model="data.configPath" required></td>
					<td width="100%"><span ng-show="!data.configPath.length"
						style="color: red">&nbsp;&nbsp;Invalid</span></td>
				</tr>
				<tr>
					<td width="20%"><h4>Statistics Equinox Path</h4></td>
					<td width="60%"><input type="text" id="txt-temp-path"
						class="form-control" ng-model="data.configPatheStatEquinox"
						required></td>
					<td width="20%"><span
						ng-show="!data.configPatheStatEquinox.length" style="color: red">&nbsp;&nbsp;Invalid</span>
					</td>
				</tr>
			</table>
		</section>
		<section id="node_connection">
			<div class="page-header">
				<h3>Node</h3>
			</div>
			<div class="bs-docs-example">
				<table class="table">

					<tr>
						<th>No.</th>
						<th>Node</th>
						<th>IP Address</th>
						<th>User</th>
						<th>Password</th>
						<th></th>
						<th></th>
					</tr>

					<tr ng-repeat="data in data.nodeconnection">
						<td>{{$index+1}}</td>
						<td>{{data.nodeName}}</td>
						<td>{{data.ip}}</td>
						<td>{{data.user}}</td>
						<td>{{data.password}}</td>
						<!-- <td align="center"><img
							src="resources/images/png/glyphicons_030_pencil.png"
							ng-click="editNode($index)"></td> -->
						<td align="center"><img
							src="resources/images/png/glyphicons_016_bin.png"
							ng-click="removeNode($index)" /></td>
					</tr>

				</table>
				<form name="formNode" novalidate>
					<fieldset>
						<small>Add Node Connection</small>
						<table width="100%">
							<tr>
								<td></td>
								<td><input type="text" id="txt-nodename"
									class="form-control" ng-model="node.nodeName"
									placeholder="Node" required></td>
								<td><input type="text" id="txt-ipaddress"
									class="form-control" ng-model="node.ipaddress"
									placeholder="IP Address" required></td>
								<td><input type="text" id="txt-username"
									class="form-control" ng-model="node.username"
									placeholder="User Name" required></td>
								<td><input type="password" id="txt-username"
									class="form-control" ng-model="node.password"
									placeholder="Password" required></td>
								<td align="center">
									<button ng-click="addNode(node)" class="btn btn-primary"
										ng-disabled="formNode.$invalid">ADD</button>
								</td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
		</section>

		<section id="state_equinox">
			<div class="page-header">
				<h4>Statistics</h4>
			</div>
			<!-- Internal Status -->
			<h4>Internal Status Statistics</h4>
			<div class="bs-docs-example">
				<table class="table">
					<tr>
						<th>No.</th>
						<th>Internal Status</th>
						<th></th>
						<th></th>
					</tr>
					<tr ng-repeat="data in data.statequinox.internalstat">
						<td>{{$index+1}}</td>
						<td>{{data.name}}</td>
						<!-- 	<td align="center"><img
							src="resources/images/png/glyphicons_030_pencil.png"
							ng-click="editInternalState($index)"></td> -->
						<td align="center"><img
							src="resources/images/png/glyphicons_016_bin.png"
							ng-click="removeInternalState($index)"></td>
					</tr>
				</table>
				<form>
					<fieldset>
						<small>Add Internal Status Statistics</small>
						<table width="100%">
							<tr>
								<td></td>
								<td><input type="text" name="max" ng-model="internalstat"
									class="form-control" style="width: 100%"
									placeholder="Internal Status"></td>
								<td align="center"><button
										ng-click="addInternalState(internalstat)"
										class="btn btn-primary" ng-disabled="!internalstat.length">ADD</button></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<br>
			<!-- Measurement -->
			<h4>System Measurement Statistics</h4>
			<div class="bs-docs-example">
				<table class="table">
					<tr>
						<th>No.</th>
						<th>State Name</th>
						<th></th>
						<th></th>
					</tr>
					<tr ng-repeat="data in data.statequinox.measurement">
						<td>{{$index+1}}</td>
						<td>{{data.name}}</td>
						<!-- 	<td align="center"><img
							src="resources/images/png/glyphicons_030_pencil.png"
							ng-click="editMeasurementState($index)"></td> -->
						<td align="center"><img
							src="resources/images/png/glyphicons_016_bin.png"
							ng-click="removeMeasurementState($index)"></td>
					</tr>
				</table>
				<form>
					<fieldset>
						<small>Add System Measurement Statistics</small>
						<table width="100%">
							<tr>
								<td></td>
								<td><input type="text" name="max"
									ng-model="measurementstate" class="form-control"
									placeholder="System Measurement Statistics" required></td>
								<td align="center"><button
										ng-click="addMeasurementState(measurementstate)"
										class="btn btn-primary" ng-disabled="!measurementstate.length">ADD</button></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<br>
			<h4>Accumulating Counters Statistics</h4>
			<div class="bs-docs-example">
				<table class="table">
					<tr>
						<th>No.</th>
						<th>State Name</th>
						<th></th>
						<th></th>
					</tr>
					<tr ng-repeat="data in data.statequinox.accumulating">
						<td>{{$index+1}}</td>
						<td>{{data.name}}</td>
						<!-- <td align="center"><img
							src="resources/images/png/glyphicons_030_pencil.png"
							ng-click="editAccumulatingState($index)"></td> -->
						<td align="center"><img
							src="resources/images/png/glyphicons_016_bin.png"
							ng-click="removeAccumulatingState($index)"></td>
					</tr>
				</table>
				<form>
					<fieldset>
						<small>Add Accumulating Counter Statistics</small>
						<table width="100%">
							<tr>
								<td></td>
								<td><input type="text" ng-model="accumulatingstate"
									class="form-control"
									placeholder="Accumulating Counter Statistics" required></td>
								<td align="center"><button
										ng-click="addAccumulatingState(accumulatingstate)"
										class="btn btn-primary"
										ng-disabled="!accumulatingstate.length">ADD</button></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
		</section>

		<section id="active_element">
			<div class="page-header">
				<h3>Element Configuration</h3>
			</div>
			<div class="bs-docs-example">
				<table class="table">
					<thead>
						<tr>
							<th>No.</th>
							<th>Name</th>
							<th>Max</th>
							<th>Min</th>
							<th>Scale</th>
							<th>Description</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="element in data.activeelement">
							<td>{{$index+1}}</td>
							<td>{{element.name}}</td>
							<td>{{element.max}}</td>
							<td>{{element.min}}</td>
							<td>{{element.unit}}</td>
							<td>{{element.description}}</td>
							<!-- <td align="center"><img
								src="resources/images/png/glyphicons_030_pencil.png"
								ng-click="editStat($index)" /></td> -->
							<td align="center"><img
								src="resources/images/png/glyphicons_016_bin.png"
								ng-click="removeStat($index)" /></td>
						</tr>
					</tbody>
				</table>
				<form name="formActiveElement" novalidate>
					<fieldset>
						<small>Add Element Configuration</small>
						<table width="100%">
							<tr>
								<td>Name</td>
								<td colspan="3"><input type="text" name="name"
									ng-model="stat.name" class="form-control" placeholder="Name"
									required></td>
							</tr>
							<tr>
								<td>Max</td>
								<td><input type="number" name="max" ng-model="stat.max"
									class="form-control" placeholder="Max"
									onkeypress="return isNumberKey(event);" required></td>
								<td align="center">Min</td>
								<td><input type="number" name="min" ng-model="stat.min"
									class="form-control" placeholder="Min"
									onkeypress="return isNumberKey(event);" required></td>
							</tr>
							<tr>
								<td>Scale</td>
								<td colspan="3"><input type="text" name="scale"
									ng-model="stat.scale" class="form-control" placeholder="Scale"
									required></td>
							</tr>
							<tr>
								<td>Description</td>
								<td colspan="3"><textarea rows="3" class="form-control"
										ng-model="stat.description" style="width: 100%"
										placeholder="Description" required></textarea></td>
							</tr>
							<tr>
								<td colspan="4" align="center">

									<button ng-click="addStat(stat)" class="btn btn-primary"
										ng-disabled="formActiveElement.$invalid">ADD</button>

								</td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<div align="center" style="margin-top: 5px">
				<button ng-click="saveConfig()" class="btn btn-primary btn-lg">SAVE</button>
			</div>
		</section>
	</div>
	<div class="col-md-1"></div>
</div>
<!-- SaveConfig -->
<script type="text/ng-template" id="mySaveConfig.html">
	<div class="modal-body">
		<h4>Are you sure you want to save file ?</h4>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>
<!-- DeleteNode -->
<script type="text/ng-template" id="myDeleteNode.html">
	<div class="modal-header">
		<h4>Are you sure you want to permanetly delete this node?</h4>
	</div>
	<div class="modal-body">
		<table>
			<tr>
				<td><b>Node</b></td>
				<td><b>{{nodeconnect.nodeName}}</b></td>
			</tr>
			<tr>
				<td><b>IP Address</b></td>
				<td><b>{{nodeconnect.ip}}</b></td>
			</tr>
			<tr>
				<td><b>User</b></td>
				<td><b>{{nodeconnect.user}}</b></td>
			</tr>
		</table>		 
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>
<script type="text/ng-template" id="myEditNode.html">
	<div class="modal-header">
		<h4>Are you want to edit this node?</h4>
	</div>
	<div class="modal-body">
		<table width="100%">
			<tr>
				<td width="30%"><b>Node</b></td>
				<td width="70%"><input type="text" class="form-control" ng-model="nodes.nodeName"></td>
			</tr>
			<tr>
				<td><b>IP Address</b></td>
				<td><input type="text" class="form-control" ng-model="nodes.ip"></td>
			</tr>
			<tr>
				<td><b>User name</b></td>
				<td><input type="text" class="form-control" ng-model="nodes.user"></b></td>
			</tr>
			<tr>
				<td><b>Password</b></td>
				<td><input type="text" class="form-control" ng-model="nodes.password"></b></td>
			</tr>
		</table>
		{{node}}		 
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>
<script type="text/ng-template" id="myDeleteInternalstat.html">
	<div class="modal-header">
		<h4>Are you sure you want to permanetly delete this internal status statistics?</h4>
	</div>
	<div class="modal-body">
		<table>
			<tr>
				<td><b>statistics: </b></td>
				<td><b>{{internalstat.name}}</b></td>
			</tr>
			
		</table>		 
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>
<script type="text/ng-template" id="myDeleteMeasurementstat.html">
	<div class="modal-header">
		<h4>Are you sure you want to permanetly delete this System Measurement ?</h4>
	</div>
	<div class="modal-body">
		<table>
			<tr>
				<td><b>statistics: </b></td>
				<td><b>{{internalstat.name}}</b></td>
			</tr>
			
		</table>		 
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>
<script type="text/ng-template" id="myDeleteAccumulatingstat.html">
	<div class="modal-header">
		<h4>Are you sure you want to permanetly delete this Accumulating Counters Statistics?</h4>
	</div>
	<div class="modal-body">
		<table>
			<tr>
				<td><b>statistics: </b></td>
				<td><b>{{internalstat.name}}</b></td>
			</tr>
			
		</table>		 
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>
<script type="text/ng-template" id="myDeleteElement.html">
	<div class="modal-header">
		<h4>Are you sure you want to permanetly delete this Element Configuration?</h4>
	</div>
	<div class="modal-body">
		<table width="100%">
			<tr>
				<td><b>Element</b></td>
				<td><b>{{internalstat.name}}</b></td>
			</tr>
			<td><b>Description</b></td>
			<td><p>{{internalstat.description}}</p></td>
		</table>	 
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
	</div>
</script>