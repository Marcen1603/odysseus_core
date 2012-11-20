package de.offis.gui.client.module;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.offis.gui.client.util.ConfigurationDialogCallback;
import de.offis.gui.client.widgets.ConfigurationForm;

/**
 * Utility class for building a configuration window.
 *
 * @author Alexander Funk
 * 
 */
public class ConfigDialog extends DialogBox {

    public class ConfigBuilder {
        public void addTextBox(String hoverText, String title, String value, boolean enabled){
            form.createTextBox(hoverText, title, value, enabled);
        }
        
        public void addTextBox(String title, String value, boolean enabled){
            form.createTextBox(title, title, value, enabled);
        }
        
        public void addCheckBox(String title, boolean value, boolean enabled){
        	form.createCheckBox(title, value, enabled);
        }
        
        public void addListBox(String title, String[] items, String value, boolean enabled){
        	form.createListBox(title, title, items, value, enabled);
        }
        
        public void addListBox(String hoverText, String title, String[] items, String value, boolean enabled){
        	form.createListBox(hoverText, title, items, value, enabled);
        }
        
        public void addTable(String title, String header, HashMap<String, String> value){
        	form.createTable(title, header, value);
        }
        
        public void addLabel(String text){
        	form.createLabel(text);
        }

		public void addDescription(String description) {
			form.createDescription(description);
		}

		public void addDualListBox(String hoverText, String title, String[] valuesPool, String[] values, boolean enabled) {
			form.addDualListBox(hoverText, title, valuesPool, values, enabled);
		}
    }

    private ConfigurationForm form = new ConfigurationForm();
    protected ConfigurationDialogCallback callback = null;
    public ConfigBuilder configBuilder = new ConfigBuilder();
    
    
    public ConfigDialog() {
    	super(false, true);
        setGlassEnabled(true);
        setGlassStyleName("abstractconfigurationdialog-glass");
        
        Button save = new Button("Save", new ClickHandler() {

            public void onClick(ClickEvent event) {
            	callback.onConfigurationSave(form);
                ConfigDialog.this.hide();
            }
        });
        
        Button cancel = new Button("Cancel", new ClickHandler() {

            public void onClick(ClickEvent event) {
            	callback.onConfigurationCancel();           
            	ConfigDialog.this.hide();
            }
        });

        VerticalPanel vp = new VerticalPanel();
        vp.add(form);
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(save);
        hp.add(cancel);
        
        vp.add(hp);
        vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
        
        setWidget(vp);
    }
    
    
    
    public void setCallback(ConfigurationDialogCallback callback){
    	this.callback = callback;
    }
}
