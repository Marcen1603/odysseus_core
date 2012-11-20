package de.offis.gui.client.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;

import de.offis.client.iconsgrid.IconGridWidget;
import de.offis.gui.client.MessageConstants;
import de.offis.gui.client.OpsStorage;
import de.offis.gui.client.editor.ClickAndDropHandler;
import de.offis.gui.client.editor.ScampiPortLogic;
import de.offis.gui.client.events.OperatorGroupLoadedEvent;
import de.offis.gui.client.events.OperatorGroupSavedEvent;
import de.offis.gui.client.events.OperatorGroupSimulatedEvent;
import de.offis.gui.client.gwtgraphics.Marker;
import de.offis.gui.client.gwtgraphics.Pattern;
import de.offis.gui.client.gwtgraphics.shape.Path;
import de.offis.gui.client.gwtgraphics.shape.Rectangle;
import de.offis.gui.client.module.AbstractModuleModel;
import de.offis.gui.client.module.AbstractModuleSvg;
import de.offis.gui.client.module.OperatorLinkSvg;
import de.offis.gui.client.module.inputs.AbstractInputSvg;
import de.offis.gui.client.module.operators.AbstractOperatorSvg;
import de.offis.gui.client.module.outputs.AbstractOutputSvg;
import de.offis.gui.client.res.OperatorsIcons;
import de.offis.gui.client.rpc.OperatorsServiceProxy;
import de.offis.gui.client.util.CustomResizeHandler;
import de.offis.gui.client.util.SuccessCallback;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLinkModel;
import de.offis.gwtsvgeditor.client.IncompatiblePortsEx;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.SvgEditor.MouseMode;
import de.offis.gwtsvgeditor.client.SvgEditorChangeEvent;
import de.offis.gwtsvgeditor.client.SvgEditorChangeListener;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Contains one open project and the corrosponding SVG-Editor.
 *
 * @author Alexander Funk
 * 
 */
public class SvgProjectPanel extends FocusPanel implements ResizeHandler, CustomResizeHandler, SvgEditorChangeListener, OperatorGroupLoadedEvent.Handler {

	private String projectName;
    private SvgEditor svg;
    private EventBus events;
    private boolean unsavedChanges = false;
    
    public SvgProjectPanel(String projectName, EventBus events, IconGridWidget<AbstractModuleModel> moduleGrid) {
    	this.projectName = projectName;
    	this.events = events;        
        svg = new SvgEditor(ScampiPortLogic.get(), events);
        setWidget(svg);
        setSize("100%", "100%");
        
        events.addHandler(OperatorGroupLoadedEvent.TYPE, this);
        
        Path mark = new Path(0, 0);
        mark.lineTo(10, 4);
        mark.lineTo(10, 6);
        mark.lineTo(0, 10);
        mark.close();
        mark.setStrokeOpacity(0.0);
        mark.setFillColor("orange");
      Marker marker = new Marker("LinkEndMarker").addElement(mark.getElement());
      marker.getElement().setAttribute("viewBox", "0 0 10 10");
      marker.getElement().setAttribute("refX", "10");
//        Image arrow = new Image(0, 0, 20, 25, "images/up.png");
//        Marker marker = new Marker("LinkEndMarker").addElement(arrow.getElement());
//        marker.getElement().setAttribute("viewBox", "0 0 20 25");
//        marker.getElement().setAttribute("refX", "12");
//        marker.getElement().setAttribute("refY", "0");
        svg.addDefinition(marker);
        
        Pattern svgBack = new Pattern("projectBackground", "0", "0", "40", "40", "0 0 40 40");
        Path cross = new Path(0, 0);
        cross.moveTo(10, 15);
        cross.lineTo(10, 5);
        cross.moveTo(5, 10);
        cross.lineTo(15, 10);
        cross.close();
        cross.setStrokeColor("black");
        cross.setStrokeOpacity(0.5);
        svgBack.add(cross);
        svg.addDefinition(svgBack);
        Rectangle background = new Rectangle(-10000, -10000, 20000, 20000);
        background.setFillColor("url(#projectBackground)");
        background.setStrokeOpacity(0);
        svg.add(background);
        svg.addChangeListener(this);
        
        // delay onResize so correct height/width are available in DOM/Browser
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            public void execute() {
                onResize(null);
            }
        });

        ClickAndDropHandler handler = new ClickAndDropHandler(this, moduleGrid, events);
        svg.addClickHandler(handler);
        svg.addMouseMoveHandler(handler);
        svg.addMouseOutHandler(handler);
        svg.addMouseDownHandler(handler);
        
        Window.addResizeHandler(this);
    }

	@Override
	public void onResize() {
		onResize(null);
	}
    
    public void onResize(ResizeEvent event) {
        int width = getOffsetWidth();
        int height = getOffsetHeight();

        if(width > 0 && height > 0){
	        svg.setViewBoxSize(width, height);
        }
    }
    
    private SvgModule findModuleByInternalName(String internalName){    	
    	for(SvgModule m : getSvgEditor().getModules().values()){
    		if(m.getId().equals(internalName)){
    			return m;
    		}
    	}
    	
    	return null;
    }
    
    public void loadLinkHelper(String source, String destination){
    	SvgModule sourceModule = findModuleByInternalName(source);
    	SvgModule destModule = findModuleByInternalName(destination);
    	
    	if(sourceModule == null || destModule == null)
    		return;    	    	

    	int inputId = -1;
    	if(destModule.getInputs().size() > 1){
    		// operatorOrder muss betrachtet werden
    		String order = ((AbstractModuleSvg)destModule).getProperties().get("operatorOrder");
    		
    		if(order != null){
    			String[] opOrder =  order.split(";");
    			for(int i = 0 ; i < opOrder.length ; i++){
    				if(source.equals(opOrder[i])){
    					inputId = i;
    				}
    			}
    		}    		
    	}
    	
    	try {    		
    		SvgPort out = sourceModule.getOutputs().get(0);
    		SvgPort in = null;
    		if(inputId == -1){
        		in = destModule.getFreeInput();
    		} else {
    			in = destModule.getInputs().get(inputId);
    		}
    		
    		if(out == null || in == null){
    			return;
    		}
    		
			getSvgEditor().createVisualLink(new OperatorLinkSvg(getSvgEditor(), out, in));
		} catch (IncompatiblePortsEx e) {
			GWT.log("Should not happen! Error on Load. Incompatible Ports." + e.getMessage());
		}
    }

    public void arrangeModules(){
        svg.arrangeModules();
    }

    public void setMouseAreaDrag(){
        svg.setMouseMode(MouseMode.AREA_DRAG);
    }

    public void setMouseMultiSelect(){
        svg.setMouseMode(MouseMode.MULTI_SELECT);
    }

    public void deploy(){
    	int[] states = svg.getModuleStateCounts();
    	
    	if(states[0] > 0){
    		Window.alert(MessageConstants.ERROR_DOC);
    	} else {
            deployOperatorGroup(false, svg.getModules(), svg.getLinks());
    	}
    }
    
    public void show(){
    	deployOperatorGroup(true, svg.getModules(), svg.getLinks());
    }

    private void deployOperatorGroup(final boolean simulate, Map<String, SvgModule> modules, List<SvgLink> linkmap) {
    	String opgroup = projectName;
        List<InputModuleModel> sensors = new ArrayList<InputModuleModel>();
        List<OperatorModuleModel> ops = new ArrayList<OperatorModuleModel>();
        List<OutputModuleModel> outputs = new ArrayList<OutputModuleModel>();
        List<ScaiLinkModel> links = new ArrayList<ScaiLinkModel>();

        // write screen position and size for later retrieval
        double[] viewbox = svg.getViewBox();
        OpsStorage.get().setScreenPositionAndSize(projectName, viewbox[0], viewbox[1], viewbox[2], viewbox[3]);
        
        for(SvgModule m : modules.values()){
        	// write positions to localstorage for later retrieval
        	OpsStorage.get().setModulePosition(projectName, m.getId(), m.getX(), m.getY());
        	
            if(m instanceof AbstractInputSvg){
                sensors.add(((AbstractInputSvg)m).getModel());
            } else if (m instanceof AbstractOutputSvg){
                outputs.add(((AbstractOutputSvg)m).getModel());
            } else if (m instanceof AbstractOperatorSvg){
                ops.add(((AbstractOperatorSvg)m).getModel());
            }
        }

        for(SvgLink l : linkmap){
            SvgModule source = l.getSource().getModule();
            SvgModule dest = l.getDestination().getModule();

            int scaiLinkType = -1;
            String s = null;
            String d = null;

            if(source instanceof AbstractInputSvg && dest instanceof AbstractOutputSvg){
                scaiLinkType = ScaiLinkModel.INPUT_TO_OUTPUT;
                s = ((AbstractInputSvg)source).getModel().getId();
                d = ((AbstractOutputSvg)dest).getModel().getId();
            } else if(source instanceof AbstractInputSvg && dest instanceof AbstractOperatorSvg){
                scaiLinkType = ScaiLinkModel.INPUT_TO_SERVICE;
                s = ((AbstractInputSvg)source).getModel().getId();
                d = ((AbstractOperatorSvg)dest).getModel().getId();
            } else if(source instanceof AbstractOperatorSvg && dest instanceof AbstractOutputSvg){
                scaiLinkType = ScaiLinkModel.SERVICE_TO_OUTPUT;
                s = ((AbstractOperatorSvg)source).getModel().getId();
                d = ((AbstractOutputSvg)dest).getModel().getId();
            } else if(source instanceof AbstractOperatorSvg && dest instanceof AbstractOperatorSvg){
                scaiLinkType = ScaiLinkModel.SERVICE_TO_SERVICE;
                s = ((AbstractOperatorSvg)source).getModel().getId();
                d = ((AbstractOperatorSvg)dest).getModel().getId();
            }

            links.add(new ScaiLinkModel(scaiLinkType, s, d));
        }        

        OperatorsServiceProxy.get().deployOperatorGroup(simulate, opgroup, sensors, outputs, ops, links, new SuccessCallback<Void>() {
        	
        	@Override
        	public void onFailure(Throwable caught) {
        		// simulated xml gets here over exception
        		if(simulate)
        			events.fireEvent(new OperatorGroupSimulatedEvent());
        		
        		
        		super.onFailure(caught);
        	}
        	
            public void onSuccess(Void result) {
            	events.fireEvent(new OperatorGroupSavedEvent());
            	Window.alert(MessageConstants.SAVED_WITHOUT_ERRORS);
            	unsavedChanges = false;
            }
        });
    }
    
    public String getProjectName() {
		return projectName;
	}
    
    public SvgEditor getSvgEditor() {
		return svg;
	}

	public List<OperatorButton> getTitleBarButtons() {
		return Arrays.asList(
				new OperatorButton(OperatorsIcons.INSTANCE.large_tiles(), "Auto Arrange", new ClickHandler() {
					public void onClick(ClickEvent event) {
						arrangeModules();
					}
				}),
				new OperatorButton(OperatorsIcons.INSTANCE.application_xp_terminal(), "Show", new ClickHandler() {
					public void onClick(ClickEvent event) {
						show();
					}
				}),
				new OperatorButton(OperatorsIcons.INSTANCE.save_as(), "Save", new ClickHandler() {
					public void onClick(ClickEvent event) {
						deploy();
					}
				})
		);
	}
	
	public boolean onProjectClose(){
		// false == cancel close		
		return !unsavedChanges;
	}

	@Override
	public void onSvgEditorChange(SvgEditorChangeEvent event) {
		unsavedChanges = true;
	}

	@Override
	public void onOperatorGroupLoaded(OperatorGroupLoadedEvent e) {
		unsavedChanges = false;
		onResize();
	}
}
