package de.offis.gui.client.module.outputs;

import de.offis.gui.client.Operators;
import de.offis.gui.client.module.ConfigDialog;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;

/**
 * Standard svg class for a output.
 * 
 * @author Alexander Funk
 *
 */
public class OutputStandardSvg extends AbstractOutputSvg {

	public static final String NAME = "Name";
	public static final String VS_NAME = "[VS] Name";
	public static final String VS_DOMAIN = "[VS] SensorDomain";
	public static final String VS_SENSORTYPE = "[VS] SensorType";
	public static final String TARGET = "Target";

	public OutputStandardSvg(SvgEditor editor, OutputModuleModel temp, double left, double top) {
		super(editor, temp, left, top);
	}

	@Override
	public void forceUpdate() {
		
	}

	@Override
	protected void createConfigDialog(ConfigDialog dialog) {
		String title = "Informations about: " + m.getId();
        dialog.setText(title);
        dialog.setTitle(title);
        
        // properties of the virtual sensor wrapper
        dialog.configBuilder.addTextBox(VS_NAME, m.getVsSensorName(), true);
        dialog.configBuilder.addListBox(VS_DOMAIN, Operators.cache.getSensorDomains(), m.getVsSensorDomain(), true);
        dialog.configBuilder.addListBox(VS_SENSORTYPE, Operators.cache.getSensorTypes(), m.getVsSensorType(), true);
        dialog.configBuilder.addTextBox(TARGET, m.getTarget(), true);
        
	}

	@Override
	protected void doConfigurationSave(ConfigurationForm form) {
		
//			m.setName(form.getTextBoxData().get(OutputConfigurationDialog.NAME));
			m.setVsSensorName(form.getTextBoxData().get(OutputStandardSvg.VS_NAME));
			m.setVsSensorDomain(form.getListBoxData().get(OutputStandardSvg.VS_DOMAIN));
			m.setVsSensorType(form.getListBoxData().get(OutputStandardSvg.VS_SENSORTYPE));
			m.setProperty("target", form.getTextBoxData().get(OutputStandardSvg.TARGET));
			
			setSvgName(m.getVsSensorName());
	}

	@Override
	public void onChangedConfig() {
		// not used
	}

}
