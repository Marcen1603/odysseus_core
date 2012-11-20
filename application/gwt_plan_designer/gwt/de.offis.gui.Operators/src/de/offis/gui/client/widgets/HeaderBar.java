package de.offis.gui.client.widgets;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import de.offis.gui.client.events.OperatorGroupClosedEvent;
import de.offis.gui.client.events.OperatorGroupCreatedEvent;
import de.offis.gui.client.events.OperatorGroupLoadedEvent;
import de.offis.gui.client.util.CompositeEvents;

/**
 * Shows buttons and name of open operator group.
 *
 * @author Alexander Funk
 * 
 */
public class HeaderBar extends CompositeEvents implements OperatorGroupClosedEvent.Handler,
															OperatorGroupCreatedEvent.Handler,
															OperatorGroupLoadedEvent.Handler {

	private FlexTable flex = new FlexTable();
	private HorizontalPanel hp = new HorizontalPanel();
	private ArrayList<ButtonBase> tempButtons = new ArrayList<ButtonBase>();
	private Label opName = new Label();
	
	public HeaderBar(EventBus events) {
		super(events);
		events.addHandler(OperatorGroupClosedEvent.TYPE, this);
		events.addHandler(OperatorGroupCreatedEvent.TYPE, this);
		events.addHandler(OperatorGroupLoadedEvent.TYPE, this);
		
		initWidget(flex);
		flex.setWidget(0, 0, hp);
		hp.setSpacing(4);
		flex.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
		flex.setBorderWidth(0);
		flex.setCellSpacing(0);
		flex.setCellPadding(0);
		opName.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		opName.getElement().getStyle().setColor("white");
		opName.getElement().getStyle().setFontSize(20, Unit.PX);
		flex.setWidget(0, 1, opName);
		flex.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		flex.setWidth("100%");
		opName.getElement().getStyle().setMarginRight(50, Unit.PX);
	}

	public void addPermanentButton(ButtonBase button) {
		hp.add(button);
	}
	
	public void addTemporaryButton(ButtonBase button) {
		hp.add(button);
		tempButtons.add(button);
	}
	
	public void reset(){
		for(ButtonBase b : tempButtons){
			removeButton(b);
		}
	}
	
	public void removeButton(ButtonBase button){
		button.removeFromParent();
	}
	
	private void setHeaderName(String text){
		opName.setText(text);
	}

	@Override
	public void onOperatorGroupClosed(OperatorGroupClosedEvent e) {
		setHeaderName("");
	}

	@Override
	public void onOperatorGroupCreated(OperatorGroupCreatedEvent e) {
		setHeaderName(e.getName());
	}

	@Override
	public void onOperatorGroupLoaded(OperatorGroupLoadedEvent e) {
		setHeaderName(e.getName());
	}
}
