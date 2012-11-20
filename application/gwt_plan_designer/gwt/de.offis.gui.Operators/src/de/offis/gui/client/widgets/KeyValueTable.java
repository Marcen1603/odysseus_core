package de.offis.gui.client.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Widget displays key-value pairs in a table.
 *
 * @author Alexander Funk
 * 
 */
public class KeyValueTable extends Composite{
	
	public static class Property {
		public String key; 
		public String value;
		
		public Property(String key, String value) {
			this.key = key;
			this.value = value;			
		}
	}
	
	private CellTable<Property> table = new CellTable<Property>();
	private ListDataProvider<Property> dataProvider;
	
	public KeyValueTable(String headers, Map<String, String> data) {
		initWidget(table);
		
		initColumns(headers, data);
		
		// Create a data provider.
	    dataProvider = new ListDataProvider<Property>();

	    // Connect the table to the data provider.
	    dataProvider.addDataDisplay(table);

	    	    
	    // Add the data to the data provider, which automatically pushes it to the
	    // widget.
	    List<Property> list = dataProvider.getList();
	    for (String key : data.keySet()) {
	    	list.add(new Property(key, data.get(key)));
	    }
		
	}
	
	private void initColumns(String header, Map<String, String> data){
		TextColumn<Property> keyColumn = new TextColumn<Property>() {
		      @Override
		      public String getValue(Property property) {
		        return property.key;
		      }
		};
		table.addColumn(keyColumn, "Key");		
		
		
			Column<Property, String> column = new Column<Property, String>(new EditTextCell()) {
			      @Override
			      public String getValue(Property property) {
			        return property.value;
			      }
			};
			table.addColumn(column, header);
			column.setFieldUpdater(new FieldUpdater<KeyValueTable.Property, String>() {
				
				public void update(int index, Property object, String value) {
//					// Called when the user changes the value.
			        object.value = value;
//			        ContactDatabase.get().refreshDisplays();
				}
			});
		
	}
	
	public HashMap<String, String> getData(){
		HashMap<String, String> bla = new HashMap<String, String>();
		for(Property p : dataProvider.getList()){
			bla.put(p.key, p.value);
		}
		
		return bla;
	}
}
