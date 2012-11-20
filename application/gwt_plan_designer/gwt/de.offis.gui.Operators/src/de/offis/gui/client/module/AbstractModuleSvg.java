package de.offis.gui.client.module;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import de.offis.gui.client.util.ConfigurationDialogCallback;
import de.offis.gui.client.widgets.ConfigurationForm;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgModuleToolbar;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Base class for all SVG modules.
 * 
 * @author Alexander Funk
 *
 */
public abstract class AbstractModuleSvg extends SvgModule implements ConfigurationDialogCallback {

	protected SvgEditor editor;
    protected ConfigDialog dialog;
    
	public AbstractModuleSvg(SvgEditor editor, String name, double left, double top, int width, int height, String urlSvgPng) {
		super(editor, name, left, top, width, height, urlSvgPng);
		this.editor = editor;
	}
	
	@Override
	public SvgModuleToolbar getSvgModuleToolbar() {
		SvgModuleToolbar tools = super.getSvgModuleToolbar();
		tools.addButton(GWT.getModuleName() + "/ScaiOperators/tools_icon.svg", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();

                showConfigurationDialog();
			}
		});
		
		return tools;
	}

    protected abstract void createConfigDialog(ConfigDialog dialog);
    
	protected abstract void doConfigurationSave(ConfigurationForm form);
	
	public abstract void showConfigurationDialog();
	
	public abstract void onChangedConfig();
	
	public abstract void onDataStreamChange(SvgPort port);

	public abstract void onLinked(SvgLink link, boolean source);
	
	public abstract void refreshModuleState(String infoMessage);
	
	public abstract Map<String, String> getProperties();
	
	public abstract AbstractModuleModel getModel();
}