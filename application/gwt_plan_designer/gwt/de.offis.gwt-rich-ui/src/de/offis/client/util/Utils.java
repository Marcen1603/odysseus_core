package de.offis.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class Utils {
	public static void blockElementAsBusy(boolean busy, Element e) {
		String className = "gwt-blockElementAsBusy";
		
		Element child = null;
		for(int i = 0 ; i < DOM.getChildCount(e) ; i++){
			
			if(DOM.getChild(e, i).getClassName().equals(className)){
				child = DOM.getChild(e, i);
			}				
		}
		
		if(busy && child == null){
			Element div = DOM.createDiv();
			div.setClassName(className);
			div.getStyle().setPosition(Position.ABSOLUTE);
			div.getStyle().setLeft(0, Unit.PX);
			div.getStyle().setTop(0, Unit.PX);
			div.getStyle().setBackgroundColor("black");
			div.getStyle().setOpacity(0.8);
			div.getStyle().setWidth(100, Unit.PCT);
			div.getStyle().setHeight(100, Unit.PCT);
			div.getStyle().setColor("white");
			div.setInnerHTML("<img src='" + GWT.getModuleName() + "/gwt-rich-ui/busy.gif" + "' alt='Loading' style='position:absolute; top:50%; left: 50%; height:48px; width:48px; margin: -24px 0 0 -24px;' />");
			DOM.appendChild(e, div);
		} else {
			if(child != null){
				DOM.removeChild(e, child);
			}
		}	
	}
}
