package de.offis.gui.client.module.inputs;

import java.util.HashMap;
import java.util.Map;

import de.offis.gui.client.Operators;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gui.client.module.AbstractModuleSvg;
import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.util.Gfx;
import de.offis.gui.client.util.ModuleNameWidget;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Base class for all SVG inputs.
 * 
 * @author Alexander Funk
 *
 */
public abstract class AbstractInputSvg extends AbstractModuleSvg {
	protected InputModuleModel m;
    protected ModuleNameWidget name;
    private Image backIcon;
    
    public AbstractInputSvg(SvgEditor editor, final InputModuleModel temp, double left, double top) {
        super(editor, temp.getId(), left, top, 100, 100, Gfx.getSensorModuleGfx());
        m = new InputModuleModel(temp);
        
        // add background icon
        this.backIcon = new Image(9, 22, 82, 70, Operators.BACKGROUND_IMAGES.getUrlForSensorTypeImage(temp.getSensorType()));
        add(backIcon);
        
        

        createOutput(m.getId(), 0, m.getDataElements());

//        Rectangle textBack = new Rectangle(2, 26, 96, 16);
//        textBack.setStrokeWidth(0);
//        textBack.setStrokeOpacity(0.0);
//        textBack.setFillOpacity(0.5);
//        textBack.setFillColor("white");
//        add(textBack);

        name = new ModuleNameWidget(m.getSensor());    
        name.setTranslate(1, 1);
        add(name);
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
		onDataStreamChange(null);
		refreshModuleState(null);
		doConfigurationSave(form);
		onChangedConfig();
		dialog = null;
		editor.blockDelete = false;
	}
	
	@Override
	public void onConfigurationCancel() {
		dialog = null;
		editor.blockDelete = false;
	}

	public static AbstractInputSvg getInstanceOfSvg(SvgEditor editor, InputModuleModel temp, double left, double top) {		
		return new InputStandardSvg(editor, temp, left, top);
	}
	
    public SvgPort getOutput() {
        return getOutputs().get(0);
    }
    
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>(m.getProperties());
	}
	
	@Override
    public InputModuleModel getModel() {
        return m;
    }
}
