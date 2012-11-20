package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Rename operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorRenameSvg extends AbstractOperatorSvg {

	private static final String RENAME_BOX_SEPERATOR = " rename to";
	
	public static final String DESCRIPTION = "The Rename operator allows the " +
			"renaming of sensor data stream attributes to provide a new output schema.";
			
	public static final String SCHEMA_KEY = "schema";
	
	public OperatorRenameSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {		
		AbstractOperatorSvg moduleSvg = this;
		OperatorModuleModel moduleModel = moduleSvg.getModel();
		
		String renameTo = moduleModel.getProperties().get(SCHEMA_KEY);
		// zB.: A,C,B

		for (SvgPort p : getOutputs()) {
			p.setDatastream(renameTo.split(";"));
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
		String value = "";
		
		for(String d : getInputs().get(0).getDatastream()){
			HashMap<String, String> temp = form.getTextBoxData();
			if(temp.containsKey(d + RENAME_BOX_SEPERATOR)){
				value += temp.get(d + RENAME_BOX_SEPERATOR)+";";
			}
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
		
		String[] incomingDatastream = getInputs().get(0).getDatastream();
		String schema = m.getProperty(SCHEMA_KEY);
		
		if(incomingDatastream == null || incomingDatastream.length == 0 || incomingDatastream[0].equals("*")){
			dialog.configBuilder.addLabel("There are no DataElements in the incoming DataStream to rename.");
		} else {
			String[] schemaStream = schema.split(";");
			
			int i = 0;
			for(String datelement : incomingDatastream){
				
				
				String schemaValue = "";
				
				if(schemaStream.length > i){
					schemaValue = schemaStream[i++];
				}
				
				dialog.configBuilder.addTextBox(datelement + RENAME_BOX_SEPERATOR, schemaValue, true);
			}
		}		
	}

	@Override
	public void onChangedConfig() {
		// nothing to do
	}
}
