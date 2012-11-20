package de.offis.gwtsvgeditor.client.svg;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gwtsvgeditor.client.DataStreamPopup;
import de.offis.gwtsvgeditor.client.Gfx;
import de.offis.gwtsvgeditor.client.PortPopup;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.events.LinkCreatedEvent;
import de.offis.gwtsvgeditor.client.events.ModuleCreatedEvent;
import de.offis.gwtsvgeditor.client.events.PortClickEvent;

/**
 * This class is a SVG port on a SvgModule. These ports act as Connectionpoints.
 * 
 * @author Alexander Funk
 *
 */
public class SvgPort extends Image implements ClickHandler, 
								PortClickEvent.Handler,
								MouseOverHandler, 
								MouseOutHandler, 
								LinkCreatedEvent.Handler, 
								ModuleCreatedEvent.Handler {

    private static final String PORT_GFX = Gfx.getPort();
    private static final String PORT_SELECTED_GFX = Gfx.getPortSelected();
    private static final String PORT_YES_GFX = Gfx.getPortYes();
    private static final String PORT_NO_GFX = Gfx.getPortNo();

    private final String _id; // nameFromParent + input/output + number
    private boolean input;
    private final SvgEditor editor;
    private final SvgModule module;
    private boolean selected = false;
    private List<SvgLink> connections = new ArrayList<SvgLink>();
    private String[] datastream;
    private DataStreamPopup popup = null;
    private PortPopup portPopup = null;
    private String error = null;
    private boolean showPopup = false;
    
    
    /**
     * nameFromParent ist der eindeutige Name des Elements (SensorModule, OperatorModule, OutputModule) der durch
     * den Nutzer festgelegt wurde. Der erstellte PortSVG wird dann als Teil diesen Elements betrachtet.
     * Die number dient als Id des Ports, also 0 fuer den Port ganz links, 1 für den zweiten Port usw.
     * Der boolean input zeigt an ob der Port ein Eingang (oben am SVG) oder ein Ausgang ist.
     *
     * @param nameFromParent
     * @param number
     * @param input
     */
    public SvgPort(SvgEditor editor, SvgModule module, int number, boolean input, String[] datastream) {
        super(0, 0, 20, 20, PORT_GFX);
        this._id = "[" + module.getId() + (input ? " input " : " output ") + number + "]";
        this.editor = editor;
        this.module = module;
        this.input = input;
        
        for(int i = 0 ; i < datastream.length ; i++){
        	datastream[i] = datastream[i].trim();
        }
        
        this.datastream = datastream;

        addClickHandler(this);
        addMouseOverHandler(this);
        addMouseOutHandler(this);

        editor.getEventBus().addHandler(PortClickEvent.TYPE, this);
        editor.getEventBus().addHandler(LinkCreatedEvent.TYPE, this);
        editor.getEventBus().addHandler(ModuleCreatedEvent.TYPE, this);
    }
    
    /**
     * This method is called by {@link SvgEditor} whenever this particular 
     * SvgPort gets connected ("linked") to another SvgPort through a SvgLink.
     * 
     */
    public void onLinked(SvgLink link){ 
    	if(link == null)
    		return;
    	
        if(link.getDestination().equals(this)){
            // when this port is the destination
            // copy datastream from source
            setDatastream(link.getSource().getDatastream());
            module.onLinked(link, false);
        } else {
        	module.onLinked(link, true);
        }       
    }

    public void onMouseOver(final MouseOverEvent event) {
        if (popup == null && showPopup) {
            popup = new DataStreamPopup(datastream);
            popup.setPopupPositionAndShow(new PositionCallback() {

                public void setPosition(int offsetWidth, int offsetHeight) {
                	int x = 0;
                	int y = 0;
                	
                	// center x
                	x = (event.getClientX() - offsetWidth / 2) - event.getRelativeX(SvgPort.this.getElement()) + SvgPort.this.getWidth()/2;
                	
                	// TODO
                	if(input){
                		// input -> show datastream top of port                		
                        y = (event.getClientY() - offsetHeight) - event.getRelativeY(SvgPort.this.getElement());
                	} else {
                		// output -> show datastream under port                		
                        y = (event.getClientY() + offsetHeight) - event.getRelativeY(SvgPort.this.getElement());
                	}
                    
                    popup.setPopupPosition(x, y);
                }
            });
        }
        
        if(error != null && showPopup){
        	portPopup = new PortPopup(error);
        	portPopup.setPopupPositionAndShow(new PositionCallback() {

                public void setPosition(int offsetWidth, int offsetHeight) {
                    int x = (event.getClientX() - offsetWidth / 2) - event.getRelativeX(SvgPort.this.getElement());
                    int y = (event.getClientY() + offsetHeight) - event.getRelativeY(SvgPort.this.getElement());
                    portPopup.setPopupPosition(x, y);
                }
            });
        }
    }

    public void onMouseOut(MouseOutEvent event) {
        if (popup != null) {
            popup.hide();
            popup = null;
        }
        
        if (portPopup != null) {
        	portPopup.hide();
        	portPopup = null;
        }
    }

    /**
     * Die Methode wird aufgerufen wenn _diese_ Instanz geklickt wird.
     */
    public void onClick(ClickEvent event) {
        // verhindere das das element (svgmodule) das click-event bekommt
        event.stopPropagation();

        editor.getEventBus().fireEvent(new PortClickEvent(this));
    }

    /**
     * Diese Methode wird immer aufgerufen wenn _irgendein_ Port geklickt
     * wurde(auch andere Instanzen).
     *
     * @param e
     */
    public void onPortClicked(PortClickEvent e) {
    	if(!e.getPort().editor.equals(this.editor)){
    		return;
    	}
    	
        if (selected) {
            return;
        }

        if (!e.getPort().selected) {
            setHref(PORT_GFX);
            error = null;
            return;
        }

        error = editor.getPortLogic().match(e.getPort(), this);

        
        
        if (error == null) {
            setHref(PORT_YES_GFX);
        } else {
            setHref(PORT_NO_GFX);
        }
    }

    public void onLinkCreated(LinkCreatedEvent e) {
    	select(false);
        error = null;
    }
    
	@Override
	public void onOperatorDrop(ModuleCreatedEvent e) {
		select(false);
        error = null;
	}

    public void addLink(SvgLink e) {
        connections.add(e);
    }

    public void removeLink(SvgLink e) {
        if (connections.contains(e)) {
            connections.remove(e);
        }

        module.forceUpdate();
        
        // TODO removed for outputs not showing *
        if(input && (connections.isEmpty()) && module.getOutputs().size() > 0){
            setDatastream(new String[]{"*"});
        }        
    }

    @Override
    public void removeFromParent() {
        for (SvgLink e : new ArrayList<SvgLink>(connections)) {        	
            e.removeFromParent();
        }
        
        module.forceUpdate();

        if (isAttached()) {
            super.removeFromParent();
        }
    }

    public void select() {
        if (!selected) {
            select(true);
        } else {
            select(false);
        }
    }

    public void select(boolean select) {
        if (select) {
            selected = true;
            setHref(PORT_SELECTED_GFX);
        } else {
            selected = false;
            setHref(PORT_GFX);
        }
    }

    public boolean isConnected() {
        if (this.connections.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public String getId() {
        return _id;
    }

    @Override
    public int getX() {
        return (int) module.getTranslate()[0] + (int) getTranslate()[0];
    }

    @Override
    public int getY() {
        return (int) module.getTranslate()[1] + (int) getTranslate()[1];
    }

    public boolean isInput() {
        return input;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SvgPort)) {
            return false;
        }

        SvgPort temp = (SvgPort) obj;

        if (!this._id.equals(temp._id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this._id != null ? this._id.hashCode() : 0);
        hash = 47 * hash + (this.input ? 1 : 0);
        hash = 47 * hash + (this.module != null ? this.module.hashCode() : 0);
        return hash;
    }

    /**
     * Returns a copy Object that represents the datastream.
     * 
     * @return
     */
    public String[] getDatastream() {
        String[] temp = new String[datastream.length];

        for(int i = 0 ; i < datastream.length ; i++){
            temp[i] = datastream[i];
        }

        return temp;
    }

    public void setDatastream(String[] datastream) {
        this.datastream = datastream;
        
        for(int i = 0 ; i < this.datastream.length ; i++){
        	this.datastream[i] = this.datastream[i].trim();
        }
        
        if(input){
            // inform module if this port
            // is a input
            this.module.onDataStreamChange(this);
        } else {
            // inform all destinations
            // when its a output
            for(SvgLink link : connections){
                link.getDestination().setDatastream(this.datastream);
            }
        }
        
    }

    public List<SvgLink> getLinks(){
        return connections;
    }

    public boolean isNeighbor(SvgPort port){
        return this.module.equals(port.module);
    }

    public SvgModule getModule(){
        return this.module;
    }
    
    public void showPopup(boolean popup){
    	this.showPopup = popup;
    }
}
