package de.offis.gui.client.widgets;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import de.offis.gui.client.widgets.DualListBox.DualListChangeHandler;

/**
 * Used to show a configuration form and storing the inputted data.
 *
 * @author Alexander Funk
 * 
 */
public class ConfigurationForm extends Composite {

    private FlexTable table;
    private HashMap<String, String> textBoxData;
    private HashMap<String, Boolean> checkBoxData;
    private HashMap<String, String> listBoxData;
    private HashMap<String, KeyValueTable> tableData;
    private HashMap<String, String[]> dualListBoxData;
    
    private final String emptyListBoxText = "... Select an Item ...";
    
    public ConfigurationForm() {
    	textBoxData = new HashMap<String, String>();
    	checkBoxData = new HashMap<String, Boolean>();
    	listBoxData = new HashMap<String, String>();
    	tableData = new HashMap<String, KeyValueTable>();
    	dualListBoxData = new HashMap<String, String[]>();
    	
        table = new FlexTable();
        
        table.setWidth("400px"); // min width
        
        initWidget(table);
    }

    public void createTextBox(String hoverText, final String title, String value, boolean enabled){
        final TextBox box = new TextBox();
        box.setValue(value);
        textBoxData.put(title, value);
        box.setEnabled(enabled);
        box.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
            	textBoxData.put(title, box.getValue());
            }
        });

        box.setWidth("100%");
        Label leftTitle = new Label(title + ": ");
        leftTitle.setTitle(hoverText);
        int rows = table.getRowCount();
//        table.setText(rows, 0, title + ": ");
        table.setWidget(rows, 0, leftTitle);
        table.setWidget(rows, 1, box);
        table.getFlexCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public void createCheckBox(final String title, boolean value, boolean enabled){
        final CheckBox box = new CheckBox();
        box.setValue(value);
        checkBoxData.put(title, value);
        box.setEnabled(enabled);
        box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            public void onValueChange(ValueChangeEvent<Boolean> event) {
            	checkBoxData.put(title, box.getValue());
            }
        });

        int rows = table.getRowCount();
        table.setText(rows, 0, title + ": ");
        table.setWidget(rows, 1, box);
        table.getFlexCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public void createListBox(String hoverText, final String title, String[] items, String value, boolean enabled){
        final ListBox box = new ListBox(false);
        for(String e : items){
            box.addItem(e);
        }
        box.addItem(emptyListBoxText);
        box.setEnabled(enabled);

        // otherwise problems when user does not change selection
        if(items.length > 0){
        	listBoxData.put(title, box.getValue(box.getSelectedIndex()));
        }
        
        
        box.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
            	String boxValue = box.getValue(box.getSelectedIndex());
            	if(!boxValue.equals(emptyListBoxText)){
                	listBoxData.put(title, boxValue);
            	} else {
            		listBoxData.put(title, "");
            	}
            }
        });

        int indexEmptyString = -1;
        int indexValue = -1;
        for(int i = 0 ; i < box.getItemCount() ; i++){
        	String itemText = box.getItemText(i);
        	
        	if(itemText.equals(emptyListBoxText)){
        		indexEmptyString = i;
        	}
        	
            if(itemText.equals(value)){
                indexValue = i;
            }
        }
        
        if(indexValue != -1){
        	box.setSelectedIndex(indexValue);
            String boxValue = box.getValue(box.getSelectedIndex());
        	listBoxData.put(title, boxValue);        	
        } else {
        	box.setSelectedIndex(indexEmptyString);        	
        	listBoxData.put(title, "");        	
        }
        
        
        box.setWidth("100%");
        Label leftTitle = new Label(title + ": ");
        leftTitle.setTitle(hoverText);
        int rows = table.getRowCount();
//        table.setText(rows, 0, title + ": ");
        table.setWidget(rows, 0, leftTitle);
        table.setWidget(rows, 1, box);
        table.getFlexCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public void createTextBoxList(final String title, String[] values, boolean enabled){
//        MultiWordSuggestOracle bla = new MultiWordSuggestOracle();
//        bla.
    }

    public void createTable(String title, String header, HashMap<String, String> value){
        // beliebig viele hashmaps: pro hashmap eine neue spalte und jeweils
        // den KEY als zeile

    	KeyValueTable keyvalue = new KeyValueTable(header, value);
    	tableData.put(title, keyvalue);
    	
    	keyvalue.setWidth("100%");
    	
        int rows = table.getRowCount();
        table.setText(rows, 0, title + ": ");
        table.setWidget(rows, 1, keyvalue);
    }
    
	public void createLabel(String text) {
		final Label box = new Label(text);

        box.setWidth("100%");
        
        int rows = table.getRowCount();
        table.setWidget(rows, 0, box);
        table.getFlexCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_CENTER);
        table.getFlexCellFormatter().setColSpan(rows, 0, 2);
	}

	public void createDescription(final String description) {
		final String CLICK_INFO ="Click on 'Description' to show it";
		final Label box = new Label(CLICK_INFO);
     

        box.setWidth("100%");
        Label leftTitle = new Label("Description: ");
        leftTitle.addClickHandler(new ClickHandler() {
			
        	private boolean expanded = false;
        	
			@Override
			public void onClick(ClickEvent event) {
				if(expanded){
					box.setText(CLICK_INFO);
					expanded = false;
				} else {
					box.setText(description);
					expanded = true;
				}
			}
		});
//        leftTitle.setTitle(hoverText);
        int rows = table.getRowCount();
//        table.setText(rows, 0, title + ": ");
        table.setWidget(rows, 0, leftTitle);
        table.setWidget(rows, 1, box);
        table.getFlexCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	}	


	public void addDualListBox(String hoverText, final String title, String[] valuesPool, String[] values, boolean enabled) {
		final DualListBox lists = new DualListBox(valuesPool, values);
		lists.setEnabled(enabled);
		

        // otherwise problems when user does not change selection
//        if(lists.length > 0){
//        	listBoxData.put(title, box.getValue(box.getSelectedIndex()));
//        }
        
        
        lists.addChangeHandler(new DualListChangeHandler() {

            public void onChange() {
            	dualListBoxData.put(title, lists.getValues());
            }
        });
        
        lists.setWidth("100%");
        Label leftTitle = new Label(title + ": ");
        leftTitle.setTitle(hoverText);
        int rows = table.getRowCount();
//        table.setText(rows, 0, title + ": ");
        table.setWidget(rows, 0, leftTitle);
        table.setWidget(rows, 1, lists);
        table.getFlexCellFormatter().setHorizontalAlignment(rows, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	}
    
    public HashMap<String, String> getTextBoxData(){
    	return new HashMap<String, String>(textBoxData);
    }
    
    public HashMap<String, Boolean> getCheckBoxData(){
    	return new HashMap<String, Boolean>(checkBoxData);
    }
    
    public HashMap<String, String> getListBoxData(){
    	return new HashMap<String, String>(listBoxData);
    }
    
    public HashMap<String, String[]> getDualListBoxData() {
		return new HashMap<String, String[]>(dualListBoxData);
	}
    
    public HashMap<String, HashMap<String, String>> getTableData(){
    	HashMap<String, HashMap<String, String>> bla = new HashMap<String, HashMap<String,String>>();
    	for(String k : tableData.keySet()){
    		bla.put(k, tableData.get(k).getData());
    	}
    	
    	return bla;
    }
}
