package de.offis.gui.client.widgets;

import java.util.HashMap;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget will display a LoadingScreen.
 *
 * @author Alexander Funk
 * 
 */
public class WaitInfoWidget extends DialogBox {
	private VerticalPanel vp = new VerticalPanel();
	private HashMap<String, Widget> texts = new HashMap<String, Widget>();
	
	public WaitInfoWidget() {		
		setTitle("Please Wait, Loading ...");
		setText("Please Wait, Loading ...");
		setWidget(vp);
		setGlassEnabled(true);
		setGlassStyleName("abstractconfigurationdialog-glass");
		setAutoHideEnabled(false);
		setModal(true);
		vp.setWidth("400px");
	}
	
	private void checkHide(){
		if(vp.getWidgetCount() <= 0){
			hide();
		}
	}

	public void hide(String text) {
		if(texts.containsKey(text)){
			vp.remove(texts.get(text));
			texts.remove(text);
		}
		
		checkHide();
	}

	public void show(String text) {
		center();
		if(!texts.containsKey(text)){
			Label value = new Label(text);
			texts.put(text, value);
			vp.add(value);
		}
	}
}