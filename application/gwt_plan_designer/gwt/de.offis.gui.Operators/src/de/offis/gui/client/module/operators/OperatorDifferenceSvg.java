package de.offis.gui.client.module.operators;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Difference operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorDifferenceSvg extends AbstractOperatorSvg {
	
	public static final String NAME = "Name";
	
	public static final String DESCRIPTION = "The Difference operator provides " +
			"the functionality of the difference in the set theory. The operator" +
			" only forwards sensor data that are not in both sensor data streams. " +
			"To do so the sensor data stream elements have to be annotated with a " +
			"validity using a Window operator.";
	
	public OperatorDifferenceSvg(SvgEditor editor, OperatorModuleModel temp,
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
		// no settings to save
	}

	@Override
	public void refreshModuleState(String infoMessage) {
		setState(ModuleState.NONE, "");
	}

	@Override
	protected void createConfigDialog(ConfigDialog dialog) {		
		String title = "Informations about: " + m.getId();
		dialog.setText(title);
		
		dialog.configBuilder.addDescription(DESCRIPTION);
		dialog.configBuilder.addTextBox(NAME, m.getName(), false);
	}

	@Override
	public void onChangedConfig() {
		// no actions to do after saving the settings since no settings can be changed
	}
}
