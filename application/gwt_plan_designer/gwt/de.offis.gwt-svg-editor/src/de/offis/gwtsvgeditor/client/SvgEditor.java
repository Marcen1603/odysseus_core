package de.offis.gwtsvgeditor.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Composite;

import de.offis.gui.client.gwtgraphics.Definition;
import de.offis.gui.client.gwtgraphics.DrawingArea;
import de.offis.gui.client.gwtgraphics.VectorObject;
import de.offis.gwtsvgeditor.client.arrange.StandardArranger;
import de.offis.gwtsvgeditor.client.commands.ISvgEditorCommand;
import de.offis.gwtsvgeditor.client.dnd.MultiSelectHandler;
import de.offis.gwtsvgeditor.client.dnd.ObjectDragHandler;
import de.offis.gwtsvgeditor.client.dnd.WorkingAreaDragHandler;
import de.offis.gwtsvgeditor.client.events.GlobalKeyChangeEvent;
import de.offis.gwtsvgeditor.client.events.LinkCreatedEvent;
import de.offis.gwtsvgeditor.client.events.LinkDeletedEvent;
import de.offis.gwtsvgeditor.client.events.LinkSelectedEvent;
import de.offis.gwtsvgeditor.client.events.ModuleCreatedEvent;
import de.offis.gwtsvgeditor.client.events.ModuleDeletedEvent;
import de.offis.gwtsvgeditor.client.events.PortClickEvent;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Main SvgEditor Widget holds the actual SVG-DrawingArea-Widget.
 *
 * @author Alexander Funk
 * 
 */
public class SvgEditor extends Composite implements
        ModuleDeletedEvent.Handler, 
        LinkDeletedEvent.Handler,
        GlobalKeyChangeEvent.Handler, 
        PortClickEvent.Handler,
        LinkSelectedEvent.Handler {

    public enum MouseMode {
        AREA_DRAG, MULTI_SELECT;
    };

    private DrawingArea area;

    public boolean blockDelete = false;
    public boolean ctrlPressed = false;
    private MouseMode mouse;
    private Map<String, SvgModule> modules = new HashMap<String, SvgModule>();
    private List<SvgLink> links = new ArrayList<SvgLink>();
    private AbstractPortLogic portLogic;
    private double zoomRatio = 1.0;
    private double zoomMax = 222.0;
    private double zoomMin = 0.1;
    private double zoomInterval = 0.1;
    private ArrayList<HandlerRegistration> handlers;
    private List<ISvgEditorCommand> commandQueue = new ArrayList<ISvgEditorCommand>();
    private StandardArranger arranger = new StandardArranger(this);

    private EventBus events;

    public SvgEditor(AbstractPortLogic matcher){
        this(matcher, new SimpleEventBus());
    }

    public SvgEditor(AbstractPortLogic matcher, EventBus events) {
        area = new DrawingArea();

        initWidget(area);

        setSize("100%", "100%");
        
        this.events = events;
        this.portLogic = matcher;

        setMouseMode(MouseMode.AREA_DRAG);

        events.addHandler(LinkDeletedEvent.TYPE, this);
        events.addHandler(ModuleDeletedEvent.TYPE, this);

        events.addHandler(GlobalKeyChangeEvent.TYPE, this);
        events.addHandler(PortClickEvent.TYPE, this);
        events.addHandler(LinkSelectedEvent.TYPE, this);
    }

    public final void setMouseMode(MouseMode mouse) {
        if (mouse == null) {
            this.mouse = mouse;
        }

        if (this.mouse == mouse) {
            return;
        }

        if (handlers != null) {
            for (HandlerRegistration h : handlers) {
                h.removeHandler();
            }
        }

        handlers = new ArrayList<HandlerRegistration>();

        WorkingAreaDragHandler handler = null;

        switch (mouse) {
            case AREA_DRAG:
                handler = new WorkingAreaDragHandler(this);

                break;
            case MULTI_SELECT:
                handler = new MultiSelectHandler(this);
                break;
        }

        handlers.add(area.addMouseDownHandler(handler));
        handlers.add(area.addMouseMoveHandler(handler));
        handlers.add(area.addMouseUpHandler(handler));
        handlers.add(area.addMouseWheelHandler(handler));
        handlers.add(area.addMouseOutHandler(handler));

        this.mouse = mouse;
    }

    public SvgPort getNearestPortThatMatches(SvgPort port, int radius) {
        SvgPort nearest = null;
        double abstandMin = radius;

        for (SvgModule m : this.modules.values()) {
            List<SvgPort> ports = new ArrayList<SvgPort>();
            ports.addAll(m.getInputs());
            ports.addAll(m.getOutputs());

            for (SvgPort p : ports) {
                double abstand = abstandHelper(port, p);
                if ((portLogic.match(port, p) == null || portLogic.match(p, port) == null) && abstand < abstandMin) {
                    nearest = p;
                    abstandMin = abstand;
                }
            }
        }

        return nearest;
    }

    private static double abstandHelper(SvgPort a, SvgPort b) {
        double a1 = a.getX();
        double b1 = b.getX();
        double a2 = a.getY();
        double b2 = b.getY();
        // GWT.log("a.x: " + a1 + "| a.y: " + b1 + "| b.x: " + a2 + "| b.y: " +
        // b2);
        return Math.abs(Math.sqrt((a1 - b1) * (a1 - b1) + (a2 - b2) * (a2 - b2)));
    }

    public void arrangeModules() {
        arranger.arrange();
    }

    private ArrayList<SvgEditorChangeListener> svgEditorChangeListeners = new ArrayList<SvgEditorChangeListener>();
    
    public void addChangeListener(SvgEditorChangeListener listener){
    	svgEditorChangeListeners.add(listener);
    }
    
    public void removeChangeListener(SvgEditorChangeListener listener){
    	svgEditorChangeListeners.remove(listener);
    }
    
    private void fireSvgChangeEvent(SvgEditorChangeEvent event){
    	for(SvgEditorChangeListener l : svgEditorChangeListeners){
    		l.onSvgEditorChange(event);
    	}
    }
    
    public void createVisualLink(SvgLink link) throws IncompatiblePortsEx {
        SvgPort start = link.getSource();
        SvgPort end = link.getDestination();

        if (start == null || end == null || portLogic == null) {
            return;
        }

        if (portLogic.match(start, end) != null) {
            throw new IncompatiblePortsEx(portLogic.match(start, end));
        }

        link.init();

        this.area.add(link);
        this.links.add(link);
        
        // throw all events and notify elements
        start.onLinked(link);
        end.onLinked(link);
        this.events.fireEvent(new LinkCreatedEvent(link));
        portLogic.onLinkCreated(link);
        fireSvgChangeEvent(new SvgEditorChangeEvent(SvgEditorChangeEvent.CREATED_LINK));
    }

    public void createVisualObject(SvgModule svg) {

        ObjectDragHandler handler = new ObjectDragHandler(this, svg);
        svg.addMouseDownHandler(handler);
        svg.addMouseMoveHandler(handler);
        svg.addMouseUpHandler(handler);
        svg.addClickHandler(handler);
        svg.addMouseOverHandler(handler);

        area.add(svg);
        this.modules.put(svg.getId(), svg);

        events.fireEvent(new ModuleCreatedEvent(svg));
        portLogic.onModuleCreated(svg);
        
        fireSvgChangeEvent(new SvgEditorChangeEvent(SvgEditorChangeEvent.CREATED_MODULE));
    }

    /**
     * Selektiert jedes Modul das innerhalb des Bereichs liegt und deselektiert
     * jedes welches außerhalb liegt. Wenn Ctrl gedrückt ist wird kein Element
     * deslektiert.
     *
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void selectElementsinArea(double x1, double y1, double x2, double y2) {

        for (SvgModule m : modules.values()) {
            // wenn modul innerhalb der area ist, selektieren ...
            if ((m.getX() < x2 // modul ist links von rechter grenze
                    && m.getX() > x1 // ... und rechts von linker grenze
                    && m.getY() > y1 // ... ist unterhalb oberer Grenze
                    && m.getY() < y2) // ... und oberhalb unterer grenze
                    || (m.getX() + m.getWidth() < x2 && m.getX() + m.getWidth() > x1 && m.getY() > y1 && m.getY() < y2) // ecke oben rehcts
                    || (m.getX() + m.getWidth() < x2 && m.getX() + m.getWidth() > x1 && m.getY() + m.getHeight() > y1 && m.getY() + m.getHeight() < y2) //ecke unten rechts
                    || (m.getX() < x2 && m.getX() > x1 && m.getY() + m.getHeight() > y1 && m.getY() + m.getHeight() < y2) // ecke unten links
                    ) {
                m.select(true);
            } else {
                if (!ctrlPressed) {
                    m.select(false);
                }
            }
        }
    }

    public void deselectAll() {
        for (SvgModule m : modules.values()) {
            m.select(false);
        }
    }

    public List<SvgModule> getSelectedModules() {
        List<SvgModule> selected = new ArrayList<SvgModule>();

        for (SvgModule m : this.modules.values()) {
            if (m.isSelected()) {
                selected.add(m);
            }
        }

        return new ArrayList<SvgModule>(selected);
    }

    public List<SvgModule> getSelectedModulesOrdered() {
        List<SvgModule> ordered = getSelectedModules();

        Collections.sort(ordered, new Comparator<SvgModule>() {

            public int compare(SvgModule o1, SvgModule o2) {
                if (o1.getX() > o2.getX()) {
                    return 1;
                } else if (o1.getX() < o2.getX()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

//        GWT.log("Ordered Modules Selected: " + ordered.size());

        return ordered;
    }

    // ---------------------------------------------------------------------
    // ------------------------- ZOOM METHODS ------------------------------
    // ---------------------------------------------------------------------
    private void updateZoom(double left, double top) {
        double newWidth = getOffsetWidth() * zoomRatio;
        double newHeight = getOffsetHeight() * zoomRatio;

        double deltaWidth = area.getViewBox()[2] - newWidth;
        double deltaHeight = area.getViewBox()[3] - newHeight;

        double leftPct = left / getOffsetWidth();
        double topPct = top / getOffsetHeight();

        double newX = area.getViewBox()[0] + (deltaWidth * leftPct);
        double newY = area.getViewBox()[1] + (deltaHeight * topPct);

        area.setViewBox(newX, newY, newWidth, newHeight);
    }

    /**
     *
     * @param left
     *        X-Coordinate of the Mouse relative to the SvgEditor-Element.
     * @param top
     *        Y-Coordinate of the Mouse relative to the SvgEditor-Element.
     */
    public void zoomOut(int left, int top) {
        if (zoomRatio + zoomInterval > zoomMax) {
            return;
        }

        zoomRatio += zoomInterval;

        updateZoom(left, top);
    }

    /**
     *
     * @param left
     *        X-Coordinate of the Mouse relative to the SvgEditor-Element.
     * @param top
     *        Y-Coordinate of the Mouse relative to the SvgEditor-Element.
     */
    public void zoomIn(int left, int top) {
        if (zoomRatio - zoomInterval  < zoomMin) {
            return;
        }

        zoomRatio -= zoomInterval;

        updateZoom(left, top);
    }

    // ---------------------------------------------------------------------
    // ------------------------ COMMAND METHODS ----------------------------
    // ---------------------------------------------------------------------
    public void cancelCmds() {
        for (ISvgEditorCommand c : commandQueue) {
            c.cancel();
        }

        commandQueue.clear();
    }

    public void previewCmds() {
        for (ISvgEditorCommand c : commandQueue) {
            c.preview();
        }
    }

    public void commitCmds() throws IncompatiblePortsEx {
        try {
            for (ISvgEditorCommand c : commandQueue) {
                c.commit();
            }
        } catch (IncompatiblePortsEx e) {
            commandQueue.clear();
            throw e;
        }

        commandQueue.clear();
    }

    public void addCmds(IDropPattern pattern) {
        for (ISvgEditorCommand cmd : pattern.getCommands()) {
            addCmd(cmd);
        }
    }

    public void addCmd(ISvgEditorCommand cmd) {
        commandQueue.add(cmd);
    }

    // ---------------------------------------------------------------------
    // ------------------------ EVENT.Handler ----------------------------
    // ---------------------------------------------------------------------
    public void onGlobalKeyChange(GlobalKeyChangeEvent e) {
        if (e.getKeyCode() == KeyCodes.KEY_CTRL) {
            ctrlPressed = e.getValue();
        }

        if (e.getKeyCode() == KeyCodes.KEY_SHIFT) {
            if (e.getValue()) {
                setMouseMode(MouseMode.MULTI_SELECT);
            } else {
                setMouseMode(MouseMode.AREA_DRAG);
            }
        }

        if (e.getKeyCode() == KeyCodes.KEY_DELETE && !blockDelete) {
            List<SvgModule> selected = getSelectedModules();
            if (!selected.isEmpty()) {
                for (SvgModule m : selected) {
                    m.removeModule();
                }
            }
        }
    }

    public void onLinkSelected(LinkSelectedEvent e) {
    }

    public void onLinkDeleted(LinkDeletedEvent e) {
        links.remove(e.getLink());
        fireSvgChangeEvent(new SvgEditorChangeEvent(SvgEditorChangeEvent.REMOVED_LINK));
    }

    public void onModuleDeleted(ModuleDeletedEvent e) {
        modules.remove(e.getId());
        fireSvgChangeEvent(new SvgEditorChangeEvent(SvgEditorChangeEvent.REMOVED_MODULE));
    }

    public void onPortClicked(PortClickEvent e) {
        portLogic.onPortClicked(this, e.getPort());
    }

    // ---------------------------------------------------------------------
    // ------------------------ GETTER + SETTER ----------------------------
    // ---------------------------------------------------------------------
    public int[] getModuleStateCounts(){
    	int errors = 0;
    	int warnings = 0;
    	int infos = 0;
    	
    	for(SvgModule m : getModules().values()){
    		switch(m.getState()){
    		case ERROR:
    			errors++;
    			break;
    		case WARNING:
    			warnings++;
    			break;
    		case INFO:
    			infos++;
    			break;
    		}
    	}
    	
    	return new int[]{errors, warnings, infos};
    }
    
    public Map<String, SvgModule> getModules() {
        return new HashMap<String, SvgModule>(modules);
    }

    public List<SvgLink> getLinks() {
        return new ArrayList<SvgLink>(links);
    }

    public AbstractPortLogic getPortLogic() {
        return portLogic;
    }

    public double getZoomRatio() {
        return this.zoomRatio;
    }

    public MouseMode getMouseMode() {
        return mouse;
    }

    public EventBus getEventBus() {
        return events;
    }

    /**
     *
     * @return Double[] with four Elements: [0] = x, [1] = y, [2] = width, [3] = height
     */
    public double[] getViewBox() {
        return area.getViewBox();
    }

    public void setViewBox(double x, double y, double w, double h) {
        area.setViewBox(x, y, w, h);
    }

    public VectorObject add(VectorObject vo) {
        return area.add(vo);
    }

    public VectorObject pop(VectorObject vo) {
        return area.pop(vo);
    }

    public void setViewBoxSize(int width, int height) {
        area.setViewBoxSize(width, height);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return area.addClickHandler(handler);
    }

    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return area.addDoubleClickHandler(handler);
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return area.addMouseDownHandler(handler);
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return area.addMouseUpHandler(handler);
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return area.addMouseOutHandler(handler);
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return area.addMouseOverHandler(handler);
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return area.addMouseMoveHandler(handler);
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return area.addMouseWheelHandler(handler);
    }

    public void setSize(int w, int h) {
        area.setSize(w, h);
    }

    public void addDefinition(Definition g) {
        area.addDefinition(g);
    }
    
    public void setFill(String color){
    	area.setFill(color);
    }
}
