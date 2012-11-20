package de.offis.gwtsvgeditor.client.svg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gwtsvgeditor.client.ModuleStatePopup;
import de.offis.gwtsvgeditor.client.svg.SvgModule.ModuleState;

/**
 * Represents SVG states, like Error, Warning etc. 
 * Will display icons as SVG-Images.
 * 
 * @author Alexander Funk
 *
 */
public class SvgModuleState extends Group implements MouseOverHandler, MouseOutHandler {
	
	private static final String STATE_INFO_ICON_URL = GWT.getModuleName() + "/" + "SvgEditor/" + "info_icon.svg";
    private static final String STATE_WARNING_ICON_URL = GWT.getModuleName() + "/" + "SvgEditor/" + "warning_icon.svg";
    private static final String STATE_ERROR_ICON_URL = GWT.getModuleName() + "/" + "SvgEditor/" + "error_icon.svg";
	

    private Image stateIcon;
    private ModuleState state = ModuleState.NONE;
    private ModuleStatePopup statePopup = null;
	private String stateMessage = "";
	
	public SvgModuleState() {
		this.stateIcon = new Image(0, 0, 50, 50, STATE_INFO_ICON_URL);        
        setObjectOpacity(0); // hide
        setTranslate(75, -25);
        add(stateIcon);
        addMouseOverHandler(this);
        addMouseOutHandler(this);
		
		setObjectOpacity(0); // hide
        setTranslate(75, -25);
	}
	
	@Override
	public void onMouseOver(final MouseOverEvent event) {
		 if (statePopup == null && !stateMessage.equals("")) {
			 statePopup = new ModuleStatePopup(stateMessage);
			 statePopup.setPopupPositionAndShow(new PositionCallback() {

	                public void setPosition(int offsetWidth, int offsetHeight) {
	                    int x = (event.getClientX() - offsetWidth / 2) - event.getRelativeX(SvgModuleState.this.getElement());
	                    int y = (event.getClientY() - offsetHeight) - event.getRelativeY(SvgModuleState.this.getElement());
	                    statePopup.setPopupPosition(x, y);
	                }
	            });
	        }
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (statePopup != null) {
			statePopup.hide();
			statePopup = null;
        }
	}
	
	public void setState(ModuleState state, String stateMessage) {
		switch (state) {
		case NONE:
			setObjectOpacity(0.0);
			stateIcon.setHref(STATE_INFO_ICON_URL);
			break;
		case INFO:
			stateIcon.setHref(STATE_INFO_ICON_URL);
			setObjectOpacity(1.0);
			break;
		case WARNING:
			stateIcon.setHref(STATE_WARNING_ICON_URL);
			setObjectOpacity(1.0);
			break;
		case ERROR:
			stateIcon.setHref(STATE_ERROR_ICON_URL);
			setObjectOpacity(1.0);
			break;
		}    	
    	
		this.state = state;
		this.stateMessage = stateMessage;
	}
	
	public ModuleState getState() {
		return state;
	}
	
	public void hide(){
		setObjectOpacity(0.0);
	}
	
	public void show(){
		setState(state, stateMessage);
	}
}
