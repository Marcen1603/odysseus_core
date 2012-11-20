package de.offis.gui.client.module.operators;

import java.util.HashMap;

import de.offis.gui.client.Operators;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gui.client.module.AbstractModuleSvg;
import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.util.Gfx;
import de.offis.gui.client.util.ModuleNameWidget;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Base class for all SVG operators.
 * 
 * @author Alexander Funk
 *
 */
public abstract class AbstractOperatorSvg extends AbstractModuleSvg {
	  	protected OperatorModuleModel m;
	    private Image opBackground;
	    
	    public AbstractOperatorSvg(SvgEditor editor, final OperatorModuleModel temp, double left, double top) {
	        super(editor, temp.getId(), left, top, 100, 100, Gfx.getOperatorModuleGfx());
	        m = new OperatorModuleModel(temp);
	        
	        opBackground = new Image(9, 22, 82, 70, Operators.BACKGROUND_IMAGES.getUrlForMetaTypeImage(temp.getMetaType().toString().toLowerCase()));
	        add(opBackground);
	        
	        // generate inputs
	        for (int i = 0; i < m.getInputCount(); i++) {
	            createInput(m.getId(), i, new String[]{"*"});
	        }

	        // generate outputs
	        for (int i = 0; i < m.getOutputCount(); i++) {
	            createOutput(m.getId(), i, new String[]{"*"});
	        }
	        
	        final ModuleNameWidget name = new ModuleNameWidget(m.getName());
	        name.setTranslate(1, 1);
	        add(name);
	        
	        refreshModuleState(null);
	    }
		
	    public abstract boolean changesTheDataStream();
	    protected abstract void setOutgoingDataStream();
		public abstract void refreshModuleState(String infoMessage);
		public abstract void onChangedConfig();
		
		@Override
		public void onDataStreamChange(SvgPort port) {
			setOutgoingDataStream();
		}

		@Override
		public void onLinked(SvgLink link, boolean source) {
			setOperatorOrder();
		}
		
		@Override
		public void forceUpdate() {
			setOperatorOrder();
			setOutgoingDataStream();
		}
		
		private void setOperatorOrder(){
			if(getInputs().size() <= 1)
				return;
			
			StringBuilder order = new StringBuilder();
			
			for(SvgPort p : getInputs()){
				if(p.getLinks().size() > 0){
					order.append(p.getLinks().get(0).getSource().getModule().getId() + ";");
				}
			}
	       	
	        // remove last pipe
			if(order.length() > 1)
				order.deleteCharAt(order.length()-1);
			
			m.setProperty("operatorOrder", order.toString());
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
			onDataStreamChange(null);
			refreshModuleState(null);
			dialog = null;
			editor.blockDelete = false;
		}

		@Override
		public void onConfigurationCancel() {
			dialog = null;
			editor.blockDelete = false;
		}
		
		public static AbstractOperatorSvg getInstanceOfSvg(SvgEditor editor, OperatorModuleModel m, double left, double top) {
	    	// TODO
	    	switch (m.getMetaType()) {
			case AGGREGATE:
				return new OperatorAggregateSvg(editor, m, left, top);
			case MAP:
				return new OperatorMapSvg(editor, m, left, top);
			case PROJECT:
				return new OperatorProjectSvg(editor, m, left, top);
			case RENAME:
				return new OperatorRenameSvg(editor, m, left, top);
			case JOIN:
				return new OperatorJoinSvg(editor, m, left, top);
			case COMPLEX:
				return new OperatorComplexSvg(editor, m, left, top);
			case DIFFERENCE:
				return new OperatorDifferenceSvg(editor, m, left, top);
			case INTERSECTION:
				return new OperatorIntersectionSvg(editor, m, left, top);
			case UNION:
				return new OperatorUnionSvg(editor, m, left, top);
			case WINDOW:
				return new OperatorWindowSvg(editor, m, left, top);
			case SELECT:
				return new OperatorSelectSvg(editor, m, left, top);
			default:
				return new OperatorStandardSvg(editor, m, left, top);
			}
		}
		
		@Override
		public HashMap<String, String> getProperties() {
			return new HashMap<String, String>(m.getProperties());
		}
		
		@Override
	    public OperatorModuleModel getModel() {
	        return m;
	    }    
}
