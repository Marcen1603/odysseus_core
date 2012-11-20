package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special svg class for a Complex operator.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorComplexSvg extends AbstractOperatorSvg {
	
	public static final String DESCRIPTION = "The Complex operator is a " +
			"template for a partial processing plan. The partial plan is " +
			"described using JSON and allows placeholders for schema " +
			"attribute names. A partial plan consists of one or more sources, " +
			"multiple operators at which the last operator act as the output. " +
			"Therefore a partial plan has no processing output. Placeholders " +
			"can be used to set user defined parameters. A placeholder is marked " +
			"by %(name). Where name is replaced with the corresponding parameter value.";
	
	public static final String NAME = "Name";
	
	public static final String TEMPLATE = "Template";
	public static final String TEMPLATE_KEY = "template";
	public static final String TEMPLATE_HOVER = "Path to the template file. The template " +
			"files are located at glassfish/domains/domain1/config/operators in the " +
			"Glassfish directory. Example: geofilter.json";
	
	public static final String SCHEMA = "Schema";
	public static final String SCHEMA_KEY = "schema";
	public static final String SCHEMA_HOVER = "The list of names for the sensor data " +
			"stream attributes for the output schema. The placeholder $n marks the n-th " +
			"input attribute. Example: voltage,speed,$1";
	
	public static final String INPUTSCHEMA = "inputSchema";
	public static final String INPUTSCHEMA_KEY = "inputSchema";
	public static final String INPUTSCHEMA_HOVER = "The required input schema of the " +
			"n-th input slot. Example: voltage,speed,position";
	
	public OperatorComplexSvg(SvgEditor editor, OperatorModuleModel temp,
			double left, double top) {
		super(editor, temp, left, top);
	}
	
	@Override
	protected void setOutgoingDataStream() {
		// this method should change all datastreams of output-ports
		AbstractOperatorSvg moduleSvg = this;
		OperatorModuleModel moduleModel = moduleSvg.getModel();

		String schema = moduleModel.getProperties().get("schema");

		if (schema == null) {
			schema = "";
		}

		for (SvgPort p : getOutputs()) {
			p.setDatastream(schema.split(";"));
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
		m.setProperty(TEMPLATE_KEY, data.get(TEMPLATE));
		m.setProperty(SCHEMA_KEY, data.get(SCHEMA));
		
		for(int i = 1 ; i <= Integer.parseInt(m.getProperty("inputSlots")) ; i++ ){
			m.setProperty(INPUTSCHEMA_KEY + "["+i+"]", data.get(INPUTSCHEMA + "["+i+"]"));
		}
	}

	@Override
	public void refreshModuleState(String infoMessage) {
		setState(ModuleState.NONE, "");
	}

	@Override
	protected void createConfigDialog(ConfigDialog dialog) {		
		String title = "Informations about: " + m.getId();
		dialog.setText(title);
		
		dialog.configBuilder.addLabel(DESCRIPTION);
		dialog.configBuilder.addTextBox(NAME, m.getName(), false);
		dialog.configBuilder.addTextBox(TEMPLATE_HOVER, TEMPLATE, m.getProperty(TEMPLATE_KEY), true);
		dialog.configBuilder.addTextBox(SCHEMA_HOVER, SCHEMA, m.getProperty(SCHEMA_KEY), true);
		
		for(int i = 1 ; i <= Integer.parseInt(m.getProperty("inputSlots")) ; i++ ){
			dialog.configBuilder.addTextBox(INPUTSCHEMA_HOVER, INPUTSCHEMA + "["+i+"]", m.getProperty(INPUTSCHEMA_KEY + "["+i+"]"), true);
		}
	}

	@Override
	public void onChangedConfig() {
		// nothing to do
	}
}
