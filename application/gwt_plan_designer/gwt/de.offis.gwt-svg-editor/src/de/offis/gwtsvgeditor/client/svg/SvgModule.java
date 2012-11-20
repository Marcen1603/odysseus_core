package de.offis.gwtsvgeditor.client.svg;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gui.client.gwtgraphics.animation.Animatable;
import de.offis.gui.client.gwtgraphics.shape.Rectangle;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.events.ModuleDeletedEvent;

/**
 * This class is the base class for a SVG module. 
 * Should be extended to form your own Modules.
 * 
 * @author Alexander Funk
 *
 */
public abstract class SvgModule extends Group implements Animatable, MouseOverHandler, MouseOutHandler {	
	public static enum ModuleState {
		NONE, INFO, WARNING, ERROR;
	}
	
    private String id;
    private Image background;
    private Rectangle selection;
    private boolean selected = false;
    private List<SvgPort> inputs = new ArrayList<SvgPort>();
    private List<SvgPort> outputs = new ArrayList<SvgPort>();
    private SvgEditor editor = null;
    private final int width;
    private final int height;
    private SvgModuleToolbar toolbar;
    private SvgModuleState stateIcon = new SvgModuleState();
   
    
    public SvgModule(SvgEditor editor, String id, double left, double top, int width, int height, String urlSvgPng) {
        this.editor = editor;
        this.id = id;
        this.width = width;
        this.height = height;
        this.selection = new Rectangle(-10, -10, width + 20, height + 20);
        this.selection.setFillOpacity(0.0);
        this.selection.setStrokeOpacity(0.0);
        this.selection.setStrokeDashArray("5,5");
        setStyleName("module");
        add(selection);
        
        this.background = new Image(0, 0, width, height, urlSvgPng);
        setTranslate(left, top);
        add(background);
                
        add(stateIcon);
        
        this.toolbar = getSvgModuleToolbar();
        this.toolbar.setObjectOpacity(0);
        this.toolbar.setTranslate(width + 10, -10);
        add(toolbar);
        
        addMouseOverHandler(this);
        addMouseOutHandler(this);
    }

    /**
     * Called by {@link SvgPort} whenever a change in the DataStream occurs.
     * DO NOT use setDataStream(...) on an Input in this method!(infinite loop)
     * @param port The port that triggered the event (changed his datastream).
     */
    public abstract void onDataStreamChange(SvgPort port);
    
    public abstract void onLinked(SvgLink link, boolean source);
    
    public abstract void onChangedConfig();    

	public abstract void forceUpdate();
    
    @Override
    public void onMouseOut(MouseOutEvent event) {
    	this.toolbar.setObjectOpacity(0);
    }
    
    @Override
    public void onMouseOver(MouseOverEvent event) {
    	this.toolbar.setObjectOpacity(1);
    }
    
    /**
     * Call super.getSvgModuleToolbar to get a standard toolbar with delete button
     * 
     * @return
     */
    public SvgModuleToolbar getSvgModuleToolbar(){
    	SvgModuleToolbar tools = new SvgModuleToolbar(height + 20);    	
    	tools.addButton(GWT.getModuleName() + "/SvgEditor/remove_icon.svg", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeModule();
			}
		});    	
    	
    	return tools;
    }
    
    public SvgPort getFreeInput() {
        for (SvgPort p : inputs) {
            if (!p.isConnected()) {
                return p;
            }
        }

        return null;
    }

    public void movePosition(double wegX, double wegY) {
        setTranslate(getTranslate()[0] + wegX, getTranslate()[1] + wegY);

        for (SvgPort port : inputs) {
            for (SvgLink link : port.getLinks()) {
                link.update();
            }
        }

        for (SvgPort port : outputs) {
            for (SvgLink link : port.getLinks()) {
                link.update();
            }
        }
    }

    public void setPosition(double x, double y) {
        setTranslate(x, y);

        for (SvgPort port : inputs) {
            for (SvgLink link : port.getLinks()) {
                link.update();
            }
        }

        for (SvgPort port : outputs) {
            for (SvgLink link : port.getLinks()) {
                link.update();
            }
        }
    }

    public void select(boolean select) {
        if (select) {
//            this.pop(selection);
            selected = true;
            selection.setStrokeOpacity(1.0);
            selection.setStrokeColor("black");
            selection.setStrokeWidth(2);
        } else {
            selected = false;
            selection.setStrokeOpacity(0.0);
            selection.setStrokeColor("black");
            selection.setStrokeWidth(2);
        }
    }

    public void removeModule() {
        for (SvgPort p : inputs) {
            p.removeFromParent();
        }

        for (SvgPort p : outputs) {
            p.removeFromParent();
        }

        inputs = null;
        outputs = null;

        editor.getEventBus().fireEvent(new ModuleDeletedEvent(id));

        if (isAttached()) {
            super.removeFromParent();
        }
    }

    protected void createInput(String nameFromParent, int number, String[] datastream) {
        SvgPort input = new SvgPort(editor, this, number, true, datastream);
        inputs.add(input);

        int portCorr = input.getWidth() / 2;

        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).setTranslate((getWidth() / (inputs.size() + 1)) * (i + 1) - portCorr, -portCorr*1.75);
        }

        add(input);
    }

    protected void createOutput(String nameFromParent, int number, String[] datastream) {
        SvgPort output = new SvgPort(editor, this, number, false, datastream);
        outputs.add(output);

        int portCorr = output.getWidth() / 2;

        for (int i = 0; i < outputs.size(); i++) {
            outputs.get(i).setTranslate((getWidth() / (outputs.size() + 1)) * (i + 1) - portCorr, getHeight() - portCorr);
        }

        add(output);
    }

    public boolean containsPort(SvgPort port) {
        if (inputs.contains(port) || outputs.contains(port)) {
            return true;
        }

        return false;
    }

    public void switchSelect() {
        if (selected) {
            select(false);
        } else {
            select(true);
        }
    }

    public void setPreview(boolean preview) {
        if (preview) {
            setObjectOpacity(0.3);
            stateIcon.hide();
        } else {
            setObjectOpacity(1.0);
            stateIcon.show();
            for(SvgPort p : inputs){
            	p.showPopup(true);
            }
            
            for(SvgPort p : outputs){
            	p.showPopup(true);
            }
        }
    }

    public List<SvgLink> getOutputLinks() {
        List<SvgLink> links = new ArrayList<SvgLink>();

        for (SvgPort p : outputs) {
            links.addAll(p.getLinks());
        }

        return links;
    }

    public void setPropertyDouble(String property, double value) {
        if ("translate x".equals(property)) {
            setPosition(value, getTranslate()[1]);
        } else if ("translate y".equals(property)) {
            setPosition(getTranslate()[0], value);
        }
    }

    public double getX() {
        return getTranslate()[0];
    }

    public double getY() {
        return getTranslate()[1];
    }

    public boolean isSelected() {
        return this.selected;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public List<SvgPort> getOutputs() {
        return new ArrayList<SvgPort>(outputs);
    }

    public List<SvgPort> getInputs() {
        return new ArrayList<SvgPort>(inputs);
    }

    public String getId() {
        return id;
    }

    public SvgEditor getEditor() {
        return editor;
    }
    
    public void setState(ModuleState state, String stateMessage) {
    	stateIcon.setState(state, stateMessage);
	}
    
    public ModuleState getState() {
		return stateIcon.getState();
	}
}
