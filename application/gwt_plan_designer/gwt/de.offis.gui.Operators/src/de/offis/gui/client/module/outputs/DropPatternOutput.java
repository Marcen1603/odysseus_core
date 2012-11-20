package de.offis.gui.client.module.outputs;

import java.util.ArrayList;
import java.util.List;

import de.offis.gui.client.Operators;
import de.offis.gui.client.module.OperatorLinkSvg;
import de.offis.gui.client.widgets.SvgProjectPanel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gwtsvgeditor.client.IDropPattern;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.commands.ISvgEditorCommand;
import de.offis.gwtsvgeditor.client.commands.LinkCreateCmd;
import de.offis.gwtsvgeditor.client.commands.ModuleCreateCmd;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Standard drop pattern for outputs.
 *
 * @author Alexander Funk
 * 
 */
public class DropPatternOutput implements IDropPattern {

    private final SvgEditor editor;
    private SvgProjectPanel svgProject;
    private final double left;
    private final double top;
    private final OutputModuleModel m;

    public DropPatternOutput(SvgProjectPanel svgProject, double left, double top, OutputModuleModel m) {
    	this.editor = svgProject.getSvgEditor();
        this.svgProject = svgProject;
        this.left = left;
        this.top = top;
        this.m = m;
    }
    
    private AbstractOutputSvg getSvg(double left, double top){
    	return AbstractOutputSvg.getInstanceOfSvg(editor, m, left, top);
    }

    public List<ISvgEditorCommand> getCommands() {
        List<ISvgEditorCommand> temp = new ArrayList<ISvgEditorCommand>();
        int i = 0;

        if (editor.getSelectedModules().isEmpty()) {
        	int counter = editor.getModules().values().size() + 1  + i;
            m.setId("Output" + "-" + (counter));
            m.setVsSensorName(svgProject.getProjectName() + "-Virtual-" + (counter));
            AbstractOutputSvg s = getSvg(left + i * 220, top);

            temp.add(new ModuleCreateCmd(s));

            // wenn keiner selektiert ist, verbinde mit dem dichtesten passenden
            // port im radius 200 px
            SvgPort match = editor.getNearestPortThatMatches(s.getInputs().get(0), 200);

            if (match != null) {
                temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, match, s.getInputs().get(0))));
            }

            i++;
        } else {
            for (SvgModule module : editor.getSelectedModulesOrdered()) {
                if (!module.getOutputs().isEmpty()) {
                    m.setId("Output" + "-" + (editor.getModules().values().size() + 1 + i));
                    AbstractOutputSvg s = getSvg(left + i * 220, top);
                    temp.add(new ModuleCreateCmd(s));

                    temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, module.getOutputs().get(0), s.getFreeInput())));

                    i++;
                }
            }
        }
        
        // set domain to the first dropped sensors domain, if it exists
        if(Operators.DOMAIN_FROM_FIRST_DROPPED_SENSOR != null){
        	m.setVsSensorDomain(Operators.DOMAIN_FROM_FIRST_DROPPED_SENSOR);
        }

        return temp;
    }
}
