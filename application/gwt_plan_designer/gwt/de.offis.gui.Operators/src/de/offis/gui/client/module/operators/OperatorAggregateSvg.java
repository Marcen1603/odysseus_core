package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Aggregate operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorAggregateSvg extends AbstractOperatorSvg {
	
	public static final String NAME = "Name";
	
	public static final String DESCRIPTION = "The Aggregate operator performs " +
			"arbitrary aggregations on a set of sensor data stream elements. " +
			"To do so, the sensor data stream elements have to be annotated with " +
			"a validity using a Window operator. The result of this operator is " +
			"the aggregated value of the set of sensor data stream elements.";
	
	public static final String TYPE = "type";
	
	public static final String ATTRIBUTE = "attribute";
	
	public static final String ATTRIBUTE_KEY = "attribute";
	
	public static final String ATTRIBUTE_HOVER = "The attribute parameter defines " +
			"the sensor data stream attribute to perform the aggregation on.";
	
	public static final String OUT_ATTRIBUTE_HOVER = "The outAttribute parameter " +
			"defines the name of the resulting aggregated data stream attribute.";
	
	public static final String OUT_ATTRIBUTE = "outAttribute";
	
	public static final String OUT_ATTRIBUTE_KEY = "outAttribute";
	
	public static final String RESOURCE = "resource";
	
	public static final String RESOURCE_KEY = "resource";
	
	public static final String RESOURCE_HOVER = "The resource is an optional " +
			"parameter to use with the aggregation type bean or script. The resource" +
			" parameter defines the path to the script file if script is used, or the " +
			"absolute name of the Java Bean class name if bean is used.";
	
	public OperatorAggregateSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {
		AbstractOperatorSvg moduleSvg = this;
		OperatorModuleModel moduleModel = moduleSvg.getModel();
		String aggTo = moduleModel.getProperties().get("attribute");

		for (SvgPort p : getOutputs()) {
			p.setDatastream(new String[] { aggTo });
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
		m.setProperty(OUT_ATTRIBUTE_KEY, data.get(OUT_ATTRIBUTE));
		m.setProperty(RESOURCE_KEY, data.get(RESOURCE));
		
		HashMap<String, String> dataList = form.getListBoxData();	
		m.setProperty(ATTRIBUTE_KEY, dataList.get(ATTRIBUTE));
	}

	@Override
	public void refreshModuleState(String infoMessage) {		
		ModuleState state = null;
		String stateMessage = "";
		String temp;
		String temp2;
		
		temp = getModel().getProperties().get("attribute");
		if(temp == null || temp.equals("")){
			state = ModuleState.ERROR;
			stateMessage = "Error: attribute needs to be specified";
		}
		
		temp2 = getModel().getProperties().get("outAttribute");
		if(temp2 == null || temp2.equals("")){
			state = ModuleState.ERROR;
			stateMessage = "Error: outAttribute needs to be specified";
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
		dialog.configBuilder.addTextBox(TYPE, m.getProperty("type"), false);
		dialog.configBuilder.addListBox(ATTRIBUTE_HOVER, ATTRIBUTE, getInputs().get(0).getDatastream(), m.getProperty(ATTRIBUTE_KEY), true);
		dialog.configBuilder.addTextBox(OUT_ATTRIBUTE_HOVER, OUT_ATTRIBUTE, m.getProperty(OUT_ATTRIBUTE_KEY), true);
		
		dialog.configBuilder.addTextBox(RESOURCE_HOVER, RESOURCE, m.getProperty(RESOURCE_KEY), true);
	}

	@Override
	public void onChangedConfig() {
		// TODO Auto-generated method stub
		
	}
}
