package de.offis.gui.client.module.operators;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;


/**
 * Special svg class for a Select operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorSelectSvg extends AbstractOperatorSvg {
	
	public static final String PREDICATE = "Predicate";	
	
	public static final String PREDICATE_KEY = "predicate";
	
	public static final String PREDICATE_HOVER = "The predicate can consist of " +
			"multiple arithmetic operators like: addition, subtraction, division, " +
			"and multiplication using static values and attributes from the sensor " +
			"data stream. Further user defined functions can be used with sensor " +
			"stream attributes and combined with the arithmetic operators. Multiple " +
			"expressions can be combined using logical operators. In addition a predicate " +
			"can consist of string operators for processing character strings to perform " +
			"arbitrary text filtering through either regular expressions for pattern " +
			"matching or proofing of string or sub-string equality. " +
			"Example: voltage < 5 (and (voltage < 5) (voltage >= 0))";
	
	public static final String NAME = "Name";
		
	public static final String DESCRIPTION = "The Select operator allows the " +
			"filtering of a sensor data stream based on an user defined predicate. " +
			"The operator checks each incoming sensor stream element on the predicate " +
			"and only forwards sensor stream elements to the all successor operators " +
			"in the graph if and only if the evaluation of the predicate evaluates to " +
			"logical value true.";
	
	public OperatorSelectSvg(SvgEditor editor, OperatorModuleModel temp,
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
		m.setProperty(PREDICATE_KEY, form.getTextBoxData().get(PREDICATE));
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
		dialog.configBuilder.addTextBox(PREDICATE_HOVER, PREDICATE, m.getProperty(PREDICATE_KEY), true);
	}

	@Override
	public void onChangedConfig() {
		// nothing to do
	}
}
