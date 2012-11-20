package de.offis.gui.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;

/**
 * Shown to the user when no other project is open.
 *
 * @author Alexander Funk
 * 
 */
public class WelcomeProject extends Composite {
	private FlexTable flex = new FlexTable();
//	private Label title = new Label("ScaiOperators");
	
	
	public WelcomeProject() {
		
		flex.setSize("100%", "100%");
//		flex.setWidget(0, 0, title);
//		
//		flex.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
//		flex.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
//		
//		title.getElement().getStyle().setFontSize(40, Unit.PX);
//		title.getElement().getStyle().setFontWeight(FontWeight.BOLDER);
//		
//		flex.getElement().getStyle().setBackgroundColor("white");
		
		Image logo = new Image("images/scampiLogo.png");
		logo.setWidth("50%");		
		
		flex.setWidget(0, 0, logo);
		flex.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		flex.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);		
		
		initWidget(flex);
	}
}
