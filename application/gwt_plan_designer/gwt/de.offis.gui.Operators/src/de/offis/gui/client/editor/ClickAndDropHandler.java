package de.offis.gui.client.editor;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;

import de.offis.client.iconsgrid.IconGridWidget;
import de.offis.gui.client.module.AbstractModuleModel;
import de.offis.gui.client.module.inputs.DropPatternInput;
import de.offis.gui.client.module.operators.DropPatternOperatorAny;
import de.offis.gui.client.module.operators.DropPatternOperatorJoin;
import de.offis.gui.client.module.outputs.DropPatternOutput;
import de.offis.gui.client.widgets.SvgProjectPanel;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gwtsvgeditor.client.IncompatiblePortsEx;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.commands.ISvgEditorCommand;
import de.offis.gwtsvgeditor.client.events.GlobalKeyChangeEvent;

/**
 * MouseHandler for the working area.
 *
 * @author Alexander Funk
 * 
 */
public class ClickAndDropHandler implements ClickHandler, MouseDownHandler, MouseMoveHandler,
        MouseOutHandler, GlobalKeyChangeEvent.Handler {

    private SvgEditor editor;
    private SvgProjectPanel svgProject;
    private List<ISvgEditorCommand> cmds = null;
    private final IconGridWidget<AbstractModuleModel> moduleGrid;
//    private boolean alt = false;

     public ClickAndDropHandler(SvgProjectPanel svgProject, IconGridWidget<AbstractModuleModel> moduleGrid, EventBus events) {
        this.editor = svgProject.getSvgEditor();
        this.svgProject = svgProject;
        this.moduleGrid = moduleGrid;

        events.addHandler(GlobalKeyChangeEvent.TYPE, this);
    }

    public void onClick(ClickEvent event) {
        try {
            editor.commitCmds();
        } catch (IncompatiblePortsEx ex) {
            
        }

//        if(!alt){
//            tree.deselectElements();
//        }
    }

    public void onMouseMove(MouseMoveEvent event) {
        if(moduleGrid.getSelectedItem() == null){
            return;
        }

        double left = (event.getClientX() - editor.getAbsoluteLeft()) * editor.getZoomRatio() + editor.getViewBox()[0] - 50;
        double top = (event.getClientY() - editor.getAbsoluteTop()) * editor.getZoomRatio() + editor.getViewBox()[1] - 50;

        AbstractModuleModel m = moduleGrid.getSelectedItem();

        if(m == null){
            // a category is selected or the element just has no model            
            return;
        }

        switch (m.getModuleType()) {
            case OPERATOR:
                switch (((OperatorModuleModel) m).getMetaType()) {
                    case JOIN:
                        cmds = new DropPatternOperatorJoin(editor, left, top, new OperatorModuleModel((OperatorModuleModel) m)).getCommands();
                        break;
                    default:
                        cmds = new DropPatternOperatorAny(editor, left, top, new OperatorModuleModel((OperatorModuleModel) m)).getCommands();
                        break;
                }

                break;
            case OUTPUT:
                cmds = new DropPatternOutput(svgProject, left, top, new OutputModuleModel((OutputModuleModel) m)).getCommands();
                break;
            case SENSOR:
                cmds = new DropPatternInput(editor, left, top, new InputModuleModel((InputModuleModel) m)).getCommands();
                break;
        }

        editor.cancelCmds();

        for (ISvgEditorCommand c : cmds) {
            editor.addCmd(c);
        }

        editor.previewCmds();
    }

    public void onMouseOut(MouseOutEvent event) {
        editor.cancelCmds();        
        moduleGrid.deselect();
    }

    public void onGlobalKeyChange(GlobalKeyChangeEvent e) {
        if(e.getKeyCode() == KeyCodes.KEY_ESCAPE || e.getKeyCode() == 81/* Q */){            
            editor.cancelCmds();
            moduleGrid.deselect();
        } else if (e.getKeyCode() == KeyCodes.KEY_ALT){
//            alt = e.getValue();
        }
    }

    public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
    }
}
