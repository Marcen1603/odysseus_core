package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;


/**
 * Special svg class for a Window operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorWindowSvg extends AbstractOperatorSvg {
	
	public static final String NAME = "Name";
	
	public static final String DESCRIPTION = "The Window operator annotates incoming " +
			"sensor data stream elements with validity for successor operators like the " +
			"Join operator, the Union operator, or the Aggregate operator. The validity " +
			"can be checked by these operators to include the values into their result set " +
			"or discard the element when they are no longer valid.";	

	public static final String TYPE = "Type";
	public static final String TYPE_KEY = "type";
	public static final String TYPE_HOVER = "The type attribute defines the type of the window. The possible values are:" +
			"fixedtime: A fixed time window." +
			"periodictime: A periodic time window." +
			"jumpingtime: A jumping time window. The window is jumping over the incoming sensor data stream and elements with a timestamp between the bounds are declared as valid. Elements in this type of window cannot take part in multiple relations." +
			"slidingtime: A sliding time window. The window is sliding over the incoming sensor data stream and elements with a timestamp between the time bounds are declared as valid. Elements in this type of window can take part in multiple relations." +
			"periodictuple: A periodic tuple window." +
			"jumpingtuple: A jumping tuple window. This window type behaves similar to the jumping time window, except that the jump is performed over a given number of sensor data stream elements." +
			"slidingtuple: A sliding tuple window. This window type behaves similar to the sliding time window except that a number of elements are declared as valid instead of elements in a time range." +
			"predicate: A predicate window. Sensor data stream elements are checked again a given predicate and declared as not valid when the predicate no longer holds for an element." +
			"unbounded: An unbounded window. Every incoming sensor data stream element is unlimited valid.";	
	
	public static final String ATTRIBUTES = "Attributes";
	public static final String ATTRIBUTES_KEY = "attributes";
	public static final String ATTRIBUTES_HOVER = "attributes";
	
	public static final String PREDICATE = "Predicate";
	public static final String PREDICATE_KEY = "predicate";
	public static final String PREDICATE_HOVER = "The used predicate for validity checking " +
			"when using the predicate window type.";
	
	public static final String SLIDE = "Slide";
	public static final String SLIDE_KEY = "slide";
	public static final String SLIDE_HOVER = "The slide defines the movement of the window over the data stream.";
	
	public static final String SIZE = "Size";
	public static final String SIZE_KEY = "size";
	public static final String SIZE_HOVER = "The size defines the size of the window. The value is either in time unit or elements unit depending on the window type in use.";
	
	
	public OperatorWindowSvg(SvgEditor editor, OperatorModuleModel temp, double left, double top) {
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
		HashMap<String, String> data = form.getTextBoxData();
		m.setProperty(ATTRIBUTES_KEY, data.get(ATTRIBUTES));
		m.setProperty(PREDICATE_KEY, data.get(PREDICATE));
		
		m.setProperty(SLIDE_KEY, data.get(SLIDE));
		m.setProperty(SIZE_KEY, data.get(SIZE));
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
		dialog.configBuilder.addTextBox(TYPE_HOVER, TYPE, m.getProperty(TYPE_KEY), false);
		if(getProperties().get(TYPE_KEY).equals("predicate")){
			dialog.configBuilder.addTextBox(ATTRIBUTES_HOVER, ATTRIBUTES, m.getProperty(ATTRIBUTES_KEY), true);
			dialog.configBuilder.addTextBox(PREDICATE_HOVER, PREDICATE, m.getProperty(PREDICATE_KEY), true);
		}
		dialog.configBuilder.addTextBox(SLIDE_HOVER, SLIDE, m.getProperty(SLIDE_KEY), true);
		dialog.configBuilder.addTextBox(SIZE_HOVER, SIZE, m.getProperty(SIZE_KEY), true);
	}

	@Override
	public void onChangedConfig() {
		// nothing to do
	}
}
