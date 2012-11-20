package de.offis.gui.client.module.inputs;

import java.util.ArrayList;
import java.util.List;

import de.offis.gui.client.Operators;
import de.offis.gui.client.module.OperatorLinkSvg;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gwtsvgeditor.client.IDropPattern;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.commands.ISvgEditorCommand;
import de.offis.gwtsvgeditor.client.commands.LinkCreateCmd;
import de.offis.gwtsvgeditor.client.commands.ModuleCreateCmd;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Standard drop pattern for inputs.
 *
 * @author Alexander Funk
 * 
 */
public class DropPatternInput implements IDropPattern {

    private final SvgEditor editor;
    private final double left;
    private final double top;
    private final InputModuleModel m;

    public DropPatternInput(SvgEditor editor, double left, double top, InputModuleModel m) {
        this.editor = editor;
        this.left = left;
        this.top = top;
        this.m = m;
    }
    
    private AbstractInputSvg getSvg(){
    	return AbstractInputSvg.getInstanceOfSvg(editor, m, left, top);
    }

    public List<ISvgEditorCommand> getCommands() {
        List<ISvgEditorCommand> temp = new ArrayList<ISvgEditorCommand>();

        m.setId("Sensor-" + (editor.getModules().values().size() + 1));
        AbstractInputSvg s = getSvg();

        ModuleCreateCmd module = new ModuleCreateCmd(s);
        temp.add(module);

        if (editor.getSelectedModules().isEmpty()) {
            // wenn keiner selektiert ist, verbinde mit dem dichtesten passenden
            // port im radius 200 px
            SvgPort match = editor.getNearestPortThatMatches(s.getOutputs().get(0), 200);
            if (match != null) {
                temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, s.getOutputs().get(0), match)));
            }

        } else {
            // mit allen eingaengen der selektion verbinden(cmds erstellen)
            for (SvgModule svg : editor.getSelectedModulesOrdered()) {
                if (!svg.getInputs().isEmpty()) {
                    temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, s.getOutputs().get(0), svg.getInputs().get(0))));
                }
            }
        }
        
        // save the sensor domain if it was the first sensor drop
        if(Operators.DOMAIN_FROM_FIRST_DROPPED_SENSOR == null){
        	Operators.DOMAIN_FROM_FIRST_DROPPED_SENSOR = m.getDomain();
        }

        return temp;
    }
}
