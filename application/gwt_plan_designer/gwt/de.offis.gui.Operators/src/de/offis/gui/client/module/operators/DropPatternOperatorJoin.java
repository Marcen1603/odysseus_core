package de.offis.gui.client.module.operators;

import java.util.ArrayList;
import java.util.List;

import de.offis.gui.client.module.OperatorLinkSvg;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.IDropPattern;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.commands.ISvgEditorCommand;
import de.offis.gwtsvgeditor.client.commands.LinkCreateCmd;
import de.offis.gwtsvgeditor.client.commands.ModuleCreateCmd;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Special drop pattern for Join-Operators.
 *
 * @author Alexander Funk
 * 
 */
public class DropPatternOperatorJoin implements IDropPattern {

    private final SvgEditor editor;
    private final double left;
    private final double top;
    private final OperatorModuleModel m;

    public DropPatternOperatorJoin(SvgEditor editor, double left, double top, OperatorModuleModel m) {
        this.editor = editor;
        this.left = left;
        this.top = top;
        this.m = m;
    }
    
    private AbstractOperatorSvg getSvg(double left, double top){
    	return AbstractOperatorSvg.getInstanceOfSvg(editor, m, left, top);
    }
    
    public List<ISvgEditorCommand> getCommands() {
        List<ISvgEditorCommand> temp = new ArrayList<ISvgEditorCommand>();
        int i = 0;

        if (editor.getSelectedModules().isEmpty()) {
            m.setId("Operator-" + (editor.getModules().values().size() + 1));
            AbstractOperatorSvg s = getSvg(left, top);

            temp.add(new ModuleCreateCmd(s));

            // wenn keiner selektiert ist, verbinde mit dem dichtesten passenden
            // port im radius 100 px
            SvgPort match = editor.getNearestPortThatMatches(s.getInputs().get(0), 200);

            if (match != null) {
                temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, match, s.getInputs().get(0))));
            }

            i++;

            i++;
        } else {
//            temp.addAll(new ConnectOperatorsSelectionPattern(editor, m, left, top).getCommands());

            if (m.getInputCount() == 1) {
                for (SvgModule module : editor.getSelectedModulesOrdered()) {
                    if (!module.getOutputs().isEmpty()) {
                        m.setId("Operator-" + (editor.getModules().values().size() + 1 + i));
                        AbstractOperatorSvg s = getSvg(left + (i * 120), top);
                        temp.add(new ModuleCreateCmd(s));

                        temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, module.getOutputs().get(0), s.getFreeInput())));

                        i++;
                    }
                }
            } else if (m.getInputCount() == 2) {
                List<SvgModule> modules = new ArrayList<SvgModule>();

                // finde alle module die genau einen ausgang haben
                for (SvgModule module : editor.getSelectedModulesOrdered()) {
                    if (module.getOutputs().size() == 1) {
                        modules.add(module);
                    }
                }


                // erstelle passende anzahl an moduleCreate-befehlen
                List<AbstractOperatorSvg> createdOps = new ArrayList<AbstractOperatorSvg>();
                for (int k = 0; k < modules.size() - 1; k++) {
                    m.setId("Operator-" + (editor.getModules().values().size() + 1 + i));
                    AbstractOperatorSvg s = getSvg(left + (i * 120), top + (i * 120));
                    createdOps.add(s);
                    temp.add(new ModuleCreateCmd(s));

                    i++;
                }

                // verbindungen erstellen
                for (int j = 0 ; j < createdOps.size() ; j++) {
                	AbstractOperatorSvg mod = createdOps.get(j);
                    if (j == 0) {
                        temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, modules.get(j).getOutputs().get(0), mod.getInputs().get(0))));
                    } else {
                        temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, createdOps.get(j-1).getOutputs().get(0), mod.getInputs().get(0))));
                    }

                    temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, modules.get(j + 1).getOutputs().get(0), mod.getInputs().get(1))));
                }
            }
        }
        return temp;
    }
}
