package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Project operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorProjectSvg extends AbstractOperatorSvg {
	
	public static final String DESCRIPTION = "The Project operator allows the " +
			"selection of specific sensor stream attributes for further processing. " +
			"This allows the pre-selection of attributes of interest. The selected " +
			"sensor stream attributes have to be selected using their fully " +
			"qualified name including the domain name and sensor name in addition " +
			"to the name of the sensor stream attribute itself.";
	
	public static final String PROPERTYS = "Propertys";
	
	public static final String NAME = "Name";
	
	public static final String SCHEMA = "Schema";
	public static final String SCHEMA_KEY = "schema";
	public static final String SCHEMA_HOVER = "The schema is a comma separated list of " +
			"the fully qualified sensor stream attribute names that should be included " +
			"in the result stream of this operator. Example: voltage,latitude,longitude";
	
	
	public OperatorProjectSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {
		AbstractOperatorSvg moduleSvg = this;
		OperatorModuleModel moduleModel = moduleSvg.getModel();
		
			String projTo = moduleModel.getProperties().get(SCHEMA_KEY);

			for (SvgPort p : getOutputs()) {
				p.setDatastream(projTo.split(";"));
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
		HashMap<String, String[]> data = form.getDualListBoxData();
		
		String value = "";
		
		for(String key : data.get(SCHEMA)){			
			value += key+";";			
		}
		
		// remove last ;
		if(value.length() > 0){
			value = value.substring(0, value.length()-1);
		}
				
		m.setProperty(SCHEMA_KEY, value);
	}

	@Override
	public void refreshModuleState(String infoMessage) {
		ModuleState state = null;
		String stateMessage = "";
		String temp;

		temp = getModel().getProperties().get(SCHEMA_KEY);
		if(temp == null || temp.equals("")){
			state = ModuleState.ERROR;
			stateMessage = "Error: schema needs to be specified";
		}
		
		if(state != null){
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
		
		String[] datastream = getInputs().get(0).getDatastream();
		if(datastream.length == 1 && datastream[0].equals("*")){
			dialog.configBuilder.addLabel("Operator does not yet specify an Datastream.");
		} else {
			dialog.configBuilder.addLabel("Choose the Elements you want to include in this Operators Output-Datastream.");
			
			String[] values = m.getProperty(SCHEMA_KEY).split(";");
			dialog.configBuilder.addDualListBox(SCHEMA_HOVER, SCHEMA, datastream, values, true);
		}
	}

	@Override
	public void onChangedConfig() {
		// nothing to do
	}
}
