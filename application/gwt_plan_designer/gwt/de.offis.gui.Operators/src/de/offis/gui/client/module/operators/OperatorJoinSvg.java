package de.offis.gui.client.module.operators;

import java.util.ArrayList;
import java.util.Arrays;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Join operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorJoinSvg extends AbstractOperatorSvg {
	
	public static final String DESCRIPTION = "The Join operator joins two sensor " +
			"data streams into a new data stream using a join condition. This join" +
			" condition is given by a predicate that is evaluated for each incoming " +
			"sensor stream element. Thereby the user has the same possibilities to " +
			"define the predicate like the Select operator. Thus the user can use " +
			"arithmetic operators, logical operators, and user defined functions. " +
			"To perform a join operation on a sensor data stream both incoming sensor " +
			"data streams have to be bounded. To do so the sensor data stream elements " +
			"have to be annotated with a validity using a Window operator.";
	
	public static final String PROPERTYS = "Propertys";
	
	public static final String PREDICATE = "Predicate";
	
	public static final String PREDICATE_KEY = "predicate";
	
	public static final String PREDICATE_HOVER = "The user defined predicate that decides which" +
			" sensor stream elements should be combined to form the resulting stream. To access " +
			"elements from both sensor data streams the domain and sensor name concatenated with a " +
			"colon can be used as a namespace if both incoming sensor data streams include the same " +
			"attribute names. The separation between namespace and attribute name is done through a " +
			"dot, e.g., domain:sensor.attribute. Example: sensorA.voltage < sensorB.voltage";
	
	public static final String NAME = "Name";
	
	public OperatorJoinSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {
		// this method should change all datastreams of output-ports
		ArrayList<String> temp = new ArrayList<String>();

		for (SvgPort p : getInputs()) {
			temp.addAll(Arrays.asList(p.getDatastream()));
		}

		// join has only one output, we set all outputs just in case ...
		for (SvgPort p : getOutputs()) {
			p.setDatastream(temp.toArray(new String[0]));
		}
	}
	
	@Override
	public boolean changesTheDataStream(){
		// JOIN always changes the datastream
		return true;
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
		// TODO Auto-generated method stub
		
	}
}
