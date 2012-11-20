package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Map operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorMapSvg extends AbstractOperatorSvg {
	public static final String DESCRIPTION = "The Map operator performs arbitrary arithmetic operations and user defined functions on selected attributes of the sensor data stream. This allows mapping multiple attributes on a new value for further processing. The name of the resulting attribute is the predicate as a string. The attribute can be renamed using the Rename operator.";
	public static final String EXPRESSION = "Expression";
	public static final String EXPRESSION_KEY = "expression";
	public static final String EXPRESSION_HOVER = "The mapping function using arithmetic operations and user defined functions. Example: voltage * 5 or voltage + 3; speed / 3";
	public static final String NAME = "Name";
	
	public OperatorMapSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {
		AbstractOperatorSvg moduleSvg = this;
		OperatorModuleModel moduleModel = moduleSvg.getModel();

		String mapTo = moduleModel.getProperties().get("expression");

		for (SvgPort p : getOutputs()) {
			p.setDatastream(mapTo != null ? mapTo.split(";") : new String[] { "" });
		}
	}
	
	@Override
	public boolean changesTheDataStream(){
    	String[] incomingDatastream = getInputs().get(0).getDatastream();
		String[] outgoingDatastream = getOutputs().get(0).getDatastream();
		
		String[] smallerStream = (incomingDatastream.length < outgoingDatastream.length ? incomingDatastream : outgoingDatastream);
		
		for(int i = 0 ; i < smallerStream.length ; i++){
			if(!incomingDatastream[i].equals(outgoingDatastream[i])){
				return true;
			}
		}
		
		return false;
    }
	
	@Override
	public void doConfigurationSave(ConfigurationForm form) {
		HashMap<String, String> data = form.getTextBoxData();
		m.setProperty(EXPRESSION_KEY, data.get(EXPRESSION));
	}

	@Override
	public void refreshModuleState(String infoMessage) {

		ModuleState state = null;
		String stateMessage = "";
		String temp;

		temp = getModel().getProperties().get("expression");
		if (temp == null || temp.equals("")) {
			state = ModuleState.ERROR;
			stateMessage = "Error: expression needs to be specified";
		}

		if (state != null) {
			setState(state, stateMessage);
		} else {
			setState(ModuleState.NONE, "");
		}
	}

	@Override
	protected void createConfigDialog(ConfigDialog dialog) {		
		String title = "Informations about: " + m.getId();
		dialog.setText(title);
		
		dialog.configBuilder.addDescription(DESCRIPTION);		
		dialog.configBuilder.addTextBox(NAME, m.getName(), false);        
		dialog.configBuilder.addTextBox(EXPRESSION_HOVER, EXPRESSION, m.getProperty(EXPRESSION_KEY), true);
	}

	@Override
	public void onChangedConfig() {
		// TODO Auto-generated method stub
		
	}
}
