package de.offis.gui.client.module.operators;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Standard svg class for a operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorStandardSvg extends AbstractOperatorSvg {
	public static final String PROPERTYS = "Propertys";
	public static final String NAME = "Name";
	
	public OperatorStandardSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {
		// normally just copy the datastream from the
		// input to the outputs
		for (SvgPort p : getOutputs()) {
			p.setDatastream(getInputs().get(0).getDatastream());
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
		m.setProperties(form.getTableData().get(PROPERTYS));
	}

	@Override
	public void refreshModuleState(String infoMessage) {
		setState(ModuleState.NONE, "");
	}

	@Override
	protected void createConfigDialog(ConfigDialog dialog) {		
		String title = "Informations about: " + m.getId();
		dialog.setText(title);
		
		dialog.configBuilder.addTextBox(NAME, m.getName(), false);        
		dialog.configBuilder.addTable(PROPERTYS, "Value", m.getProperties());
	}

	@Override
	public void onChangedConfig() {
		// TODO Auto-generated method stub
		
	}
}
