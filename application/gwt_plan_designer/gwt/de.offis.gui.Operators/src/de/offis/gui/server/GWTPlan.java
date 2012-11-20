package de.offis.gui.server;

import java.util.List;

import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLinkModel;

/**
 * Container-class which contains all information about a plan in the
 * GWT-Context.
 * 
 * @author Merlin Wasmann
 * 
 */
public class GWTPlan {

	private final String operatorGroup;
	private final List<InputModuleModel> sensors;
	private final List<OutputModuleModel> outputs;
	private final List<OperatorModuleModel> operators;
	private final List<ScaiLinkModel> links;

	public GWTPlan(String operatorGroup, List<InputModuleModel> sensors,
			List<OutputModuleModel> outputs,
			List<OperatorModuleModel> operators, List<ScaiLinkModel> links) {
		super();
		this.operatorGroup = operatorGroup;
		this.sensors = sensors;
		this.outputs = outputs;
		this.operators = operators;
		this.links = links;
	}

	public String getOperatorGroup() {
		return operatorGroup;
	}

	public List<InputModuleModel> getSensors() {
		return sensors;
	}

	public List<OutputModuleModel> getOutputs() {
		return outputs;
	}

	public List<OperatorModuleModel> getOperators() {
		return operators;
	}

	public List<ScaiLinkModel> getLinks() {
		return links;
	}
}
