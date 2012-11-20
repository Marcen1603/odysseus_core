package de.offis.gui.client.widgets;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Implementaiton of a DualListBox.
 *
 * @author Alexander Funk
 * 
 */
public class DualListBox extends Composite {

	public interface DualListChangeHandler {
		void onChange();
	}
	
	private FlexTable flex = new FlexTable();

	private ListBox left = new ListBox(false);
	private ListBox right = new ListBox(false);
	private Button toRight;
	private Button toLeft;

	public DualListBox(String[] valuesPool, String[] values) {
		ArrayList<String> valuesList = new ArrayList<String>(Arrays.asList(values));
		ArrayList<String> valuesPoolList = new ArrayList<String>(Arrays.asList(valuesPool));
		
		for (String e : valuesPool) {
			if (!valuesList.contains(e))
				left.addItem(e);
		}

		for (String e : values) {
			if(valuesPoolList.contains(e))
				right.addItem(e);
		}
		
		left.setVisibleItemCount(5);
		right.setVisibleItemCount(5);
		left.setWidth("100px");
		right.setWidth("100px");
		
		toRight = new Button("->", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				toRight();
			}
		});

		toLeft = new Button("<-", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				toLeft();
			}
		});

		flex.setWidget(0, 0, left);
		flex.setWidget(0, 1, toRight);
		flex.setWidget(1, 0, toLeft);
		flex.setWidget(0, 2, right);
		
		flex.getFlexCellFormatter().setRowSpan(0, 0, 2);
		flex.getFlexCellFormatter().setRowSpan(0, 2, 2);
		
		initWidget(flex);
	}
	
	private void toLeft(){
		int selIndex = right.getSelectedIndex();
		String rightSelection = right.getValue(selIndex);
		right.removeItem(selIndex);
		
		left.addItem(rightSelection);
		fireChangeEvent();
	}
	
	private void toRight(){
		int selIndex = left.getSelectedIndex();
		String leftSelection = left.getValue(selIndex);
		left.removeItem(selIndex);
		
		right.addItem(leftSelection);
		fireChangeEvent();
	}
	
	public String[] getValues(){
		String[] vals = new String[right.getItemCount()];
		
		for(int i = 0 ; i < right.getItemCount() ; i++){
			vals[i] = right.getValue(i);
			
		}
		
		return vals;
	}

	public void setEnabled(boolean enabled) {
		left.setEnabled(enabled);
		right.setEnabled(enabled);
		toLeft.setEnabled(enabled);
		toRight.setEnabled(enabled);
	}
	
	private ArrayList<DualListChangeHandler> handlers = new ArrayList<DualListBox.DualListChangeHandler>();
	
	public void addChangeHandler(DualListChangeHandler handler){
		handlers.add(handler);
	}
	
	public void removeChangeHandler(DualListChangeHandler handler){
		handlers.remove(handler);
	}
	
	private void fireChangeEvent(){
		for(DualListChangeHandler h : handlers){
			h.onChange();
		}
	}
}
