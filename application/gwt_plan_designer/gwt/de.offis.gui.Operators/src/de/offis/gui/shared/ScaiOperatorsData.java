package de.offis.gui.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model for inputs, outputs and operators. 
 *
 * @author Alexander Funk
 * 
 */
public class ScaiOperatorsData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1507788485230254246L;
	private ArrayList<OperatorModuleModel> operators;
	private ArrayList<InputModuleModel> inputs;
	private ArrayList<OutputModuleModel> outputs;

	protected ScaiOperatorsData(){
		
	}
	
	public ScaiOperatorsData(ArrayList<OperatorModuleModel> operators, ArrayList<InputModuleModel> inputs, ArrayList<OutputModuleModel> outputs) {
		this.operators = operators;
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	public ArrayList<InputModuleModel> getInputs() {
		return inputs;
	}
	
	public ArrayList<OutputModuleModel> getOutputs() {
		return outputs;
	}
	
	public ArrayList<OperatorModuleModel> getOperators() {
		return operators;
	}
}
