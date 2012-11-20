package de.offis.gui.client.module.outputs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.offis.gui.client.Operators;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gui.client.module.AbstractModuleSvg;
import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.module.OperatorLinkSvg;
import de.offis.gui.client.util.Gfx;
import de.offis.gui.client.util.ModuleNameWidget;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Base class for all SVG outputs.
 * 
 * @author Alexander Funk
 *
 */
public abstract class AbstractOutputSvg extends AbstractModuleSvg {
	    protected OutputModuleModel m;
	    protected ModuleNameWidget name;
	    private Image backIcon;

	    public AbstractOutputSvg(SvgEditor editor, final OutputModuleModel temp, double left, double top) {
	        super(editor, temp.getId(), left, top, 100, 100, Gfx.getOutputModuleGfx());
	        m = new OutputModuleModel(temp);
	        
	        // add background icon
	        this.backIcon = new Image(9, 22, 82, 70, Operators.BACKGROUND_IMAGES.getUrlForOutputImage(temp.getName()));   
	        add(backIcon);	        
	        
	        createInput(m.getId(), 0, new String[]{"*"});

	        if(m.getVsSensorDomain().equals("") && Operators.DOMAIN_FROM_FIRST_DROPPED_SENSOR != null){
	        	m.setVsSensorDomain(Operators.DOMAIN_FROM_FIRST_DROPPED_SENSOR);
	        }
	        
	        name = new ModuleNameWidget(m.getVsSensorName());
	        name.setTranslate(1, 1);
	        add(name);
	        refreshModuleState(null);
	        
	        setDatastreamFromPort1();
	    }	    
	    
	    protected void setSvgName(String text){
	    	String svgText = text;
	    	
	    	if(svgText.length() > 8){
	            svgText = svgText.substring(0, 7) + "...";
	        }
	    	
	    	if(name != null){
	    		name.setText(svgText);
	    	}
	    }

	    @Override
	    public void onDataStreamChange(SvgPort port) {
	        String st = guessSensorType();
			if(st != null){
				m.setVsSensorType(st);
				// TODO show warning/info that sensortype was changed?
			}
			
//			if(port.getDatastream().equals(new String[]{"*"})){
//				setDatastreamFromPort1();
//			}
			
	        refreshModuleState("SensorType was set to: " + st);
	    }

		@Override
		public void onLinked(SvgLink link, boolean source) {
//			Logger.getLogger("ScaiOperators.OutputModuleSvg").log(Level.INFO, "OutputModuleSvg.onLinked()"); // TODO remove		
			
			String st = guessSensorType();	
			if(st != null){
				m.setVsSensorType(st);
				// TODO show warning/info that sensortype was changed?
			}
			
			// only when this module is the endpoint/destination ... which actually is always true
			// because outputs are never startpoint
			if(!source && link instanceof OperatorLinkSvg){
				if(m.getVsSensorType() == null || m.getVsSensorType().equals("")){
					m.setVsSensorType(((OperatorLinkSvg)link).getSensorTypeFromSource());
				}
			}		

	        setDatastreamFromPort1();
	        
			refreshModuleState("SensorType was set to: " + st);
		}



		@Override
		public void refreshModuleState(String infoMessage) {
			
			
			// check if the incoming datastream is compatible with the set sensorType-datastream
			if(m.getVsSensorType() == null || m.getVsSensorType().equals("")){
				setState(ModuleState.ERROR, "Error: SensorType not set.");
			} else if(m.getVsSensorDomain().equals("")){
				setState(ModuleState.ERROR, "Error: SensorDomain not set.");
			} else {			
				setState(ModuleState.NONE, "");
				
				String sensorType = m.getVsSensorType();
				String[] definedDatastream = Operators.cache.getDataElementsFromSensorType(sensorType);
				String[] currentDatastream = getInput().getDatastream();
				if(getInput().getLinks().size() > 0){				
					currentDatastream = getInput().getLinks().get(0).getSource().getDatastream();
				}
				
				
				int minLength = definedDatastream.length < currentDatastream.length ? definedDatastream.length : currentDatastream.length;
				for(int i = 0 ; i < minLength ; i++){
					if(!currentDatastream[i].equals(definedDatastream[i])){
						setState(ModuleState.ERROR, "Error: DataStream not compatible. SensorType(" + sensorType + ") needs " + Arrays.toString(definedDatastream) + ", but is " + Arrays.toString(currentDatastream) + ".");
					}
				}
			}
			
//			if(getState() == ModuleState.NONE && (infoMessage != null && !infoMessage.equals(""))){			
//				setState(ModuleState.INFO, "Info: " + infoMessage);
//			}
		}
		
		protected void setDatastreamFromPort1(){
			if(m.getVsSensorType() == null || m.getVsSensorType().equals("")){
				return;
			}
			
			String[] definedDatastream = Operators.cache.getDataElementsFromSensorType(m.getVsSensorType());
			getInput().setDatastream(definedDatastream);
		}
		
		private String guessSensorType(){
			String[] currentDatastream = getInput().getDatastream();

			String guessed = null;		
			for(String key : Operators.cache.getDataElementsFromSensorTypeKeys()){
				String[] definedDatastream = Operators.cache.getDataElementsFromSensorType(key);
				if(currentDatastream.length != definedDatastream.length){
					continue;
				}
				
				guessed = key;
				for(int i = 0 ; i < currentDatastream.length ; i++){
					if(!currentDatastream[i].equals(definedDatastream[i])){
						guessed = null;
					}
				}
				
				if(guessed != null){
					break;
				}
				
			}
			
			return guessed;
		}
		
		@Override
		public void showConfigurationDialog() {
			dialog = new ConfigDialog();
			createConfigDialog(dialog);
	        dialog.center();
	        editor.blockDelete = true;
	        dialog.setCallback(this);
		}
		
		@Override
		public void onConfigurationSave(ConfigurationForm form) {
			doConfigurationSave(form);
			onChangedConfig();
			dialog = null;
			editor.blockDelete = false;
			setDatastreamFromPort1();			
			refreshModuleState(null);			
		}
		
		@Override
		public void onConfigurationCancel() {
			dialog = null;
			editor.blockDelete = false;
			setDatastreamFromPort1();		
		}

		public static AbstractOutputSvg getInstanceOfSvg(SvgEditor editor, OutputModuleModel m, double left, double top) {
	    	// TODO
//	    	if(m.getName().equals("HTTP")){
//	    		return new OutputStandardSvg(editor, m, left, top);
//	    	} else if(m.getName().equals("Scai")){
//	    		return new OutputStandardSvg(editor, m, left, top);
//	    	} else {
//	    		return new OutputStandardSvg(editor, m, left, top);
//	    	}
	    	return new OutputStandardSvg(editor, m, left, top);
		}
		
		@Override
		public Map<String, String> getProperties() {
			return new HashMap<String, String>(m.getProperties());
		}
		
	    public SvgPort getInput() {
	        return getInputs().get(0);
	    }

	    public OutputModuleModel getModel() {
	        return m;
	    }
}