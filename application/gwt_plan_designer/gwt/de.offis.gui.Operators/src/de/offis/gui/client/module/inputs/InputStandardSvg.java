package de.offis.gui.client.module.inputs;

import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Standard svg class for a input.
 * 
 * @author Alexander Funk
 *
 */
public class InputStandardSvg extends AbstractInputSvg {
    public static final String NAME = "Name";
    public static final String LOGGED = "Logged";
    public static final String SENSOR = "Sensor";
    public static final String DOMAIN = "Domain";
    
	public InputStandardSvg(SvgEditor editor, InputModuleModel temp, double left, double top) {
		super(editor, temp, left, top);
	}

	@Override
	public void onDataStreamChange(SvgPort port) {
		// not used
	}

	@Override
	public void onLinked(SvgLink link, boolean source) {
		// not used
	}

	@Override
	public void onChangedConfig() {
		// not used
	}

	@Override
	public void refreshModuleState(String infoMessage) {
		// not used
	}

	@Override
	public void forceUpdate() {
		// not used
	}

	@Override
	protected void createConfigDialog(ConfigDialog dialog) {
		String title = "Informations about: " + m.getId();
        dialog.setText(title);
        dialog.setTitle(title);

//        dialog.configBuilder.addTextBox(NAME, m.getId(), true);
        dialog.configBuilder.addTextBox(SENSOR, m.getSensor(), false);
        dialog.configBuilder.addTextBox(DOMAIN, m.getDomain(), false);
	}

	@Override
	protected void doConfigurationSave(ConfigurationForm form) {
		m.setId(form.getTextBoxData().get(InputStandardSvg.NAME));		
        name.setText(m.getId());		
	}
}
