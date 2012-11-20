package de.offis.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/** 
 * Bietet die Moeglichkeit kleine StatusPopups am unteren rechten Bildschirmrand anzuzeigen.
 * CSS-Styles:
 * dev-StatusPopup;
 * dev-StatusPopup-title;
 * dev-StatusPopup-body;
 * 
 * 
 * @author Alexander Funk
 *
 */
public class StatusPopup {
	private static class StatusWidget extends Widget {

		public StatusWidget(Widget title, Widget body, String stylename) {
			Element containerDiv = DOM.createDiv();
			Element	titleDiv = DOM.createDiv();
			Element bodyDiv = DOM.createDiv();
			containerDiv.appendChild(titleDiv);
			containerDiv.appendChild(bodyDiv);
			
			titleDiv.appendChild(title.getElement());
			bodyDiv.appendChild(body.getElement());
			
			containerDiv.setClassName(stylename);
			titleDiv.setClassName(stylename + "-title");
			bodyDiv.setClassName(stylename + "-body");


			containerDiv.getStyle().setHeight(MIN_HEIGHT, Unit.PX);			

			setElement(containerDiv);

			Timer timer = new Timer() {
				@Override
				public void run() {
					StatusWidget.this.removeFromParent();
				}
			};

			timer.schedule(STANDARD_TIMEOUT);
		}

		public void removeFromParent() {
			getElement().removeFromParent();
		}
	}

	private static final int MIN_HEIGHT = 50;
	private static final int STANDARD_WIDTH = 300;
	private static final String STANDARD_CAPTION = "StatusPopup";
	private static final int STANDARD_TIMEOUT = 5000;
	private static final String STANDARD_STYLENAME = "dev-StatusPopup";

	private static Element container = null;

	private static Timer closeTimer = new Timer() {
		@Override
		public void run() {
			container.removeFromParent();
			container = null;
		}
	};

	private static void createContainer() {
		container = DOM.createDiv();
		container.getStyle().setPosition(Position.ABSOLUTE);
		container.getStyle().setBottom(0, Unit.PX);
		container.getStyle().setRight(0, Unit.PX);
		container.getStyle().setWidth(STANDARD_WIDTH, Unit.PX);
		getBody().appendChild(container);
	}

	private static void add(StatusWidget w) {
		container.appendChild(w.getElement());
		closeTimer.cancel();

		// dient zum loeschen des containers, daher +500 damit sichergestellt
		// ist das alle statuspopups nicht mehr angezeigt werden
		closeTimer.schedule(STANDARD_TIMEOUT + 500);
	}

	/**
	 * Liefert das <body>-Element.
	 * 
	 * @return Referenz auf das Body-Element
	 */
	private static native Element getBody() /*-{
		return $doc.body;
	}-*/;

	public static void show(String caption, String content, String stylename) {
		if (container == null)
			createContainer();

		add(new StatusWidget(new Label(caption), new Label(content), stylename));
	}
	
	public static void show(String caption, String content) {
		show(caption, content, STANDARD_STYLENAME);
	}
	
	public static void show(String content) {
		show(STANDARD_CAPTION, content);
	}
}

