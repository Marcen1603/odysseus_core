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
 * Standard drop pattern for operators.
 *
 * @author Alexander Funk
 * 
 */
public class DropPatternOperatorAny implements IDropPattern {
    private final SvgEditor editor;
    private final double left;
    private final double top;
    private final OperatorModuleModel m;

    public DropPatternOperatorAny(SvgEditor editor, double left, double top, OperatorModuleModel m) {
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
            m.setId("Operator-" + (editor.getModules().size() + 1));
            AbstractOperatorSvg s = getSvg(left, top);

            temp.add(new ModuleCreateCmd(s));

            // wenn keiner selektiert ist, verbinde mit dem dichtesten passenden
            // port im radius 100 px
            SvgPort match = editor.getNearestPortThatMatches(s.getInputs().get(0), 200);

            if (match != null) {
                temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, match, s.getInputs().get(0))));
            }

            i++;
        } else {
//            temp.addAll(new ConnectOperatorsSelectionPattern(editor, m, left, top).getCommands());

            if (m.getInputCount() == 1) {
                for (SvgModule module : editor.getSelectedModulesOrdered()) {
                    if (!module.getOutputs().isEmpty()) {
                        m.setId("Operator-" + (editor.getModules().size() + 1 + i));
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

                // wenn ungerade oder grade anzahl an modulen
                if (modules.size() % 2 == 0) {
                    // grade ...
                    i = 1;
                    AbstractOperatorSvg s = null;
                    for (SvgModule mod : modules) {
                        if (i % 2 == 0) {
                            // sind bei einem graden ausgang -> verbinde mit zweiten eingang eines OPs
                            temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, mod.getOutputs().get(0), s.getInputs().get(1))));
                        } else {
                            m.setId("Operator-" + (editor.getModules().size() + 1 + i));
                            s = getSvg(left + ((i - 1) * 120), top);

                            temp.add(new ModuleCreateCmd(s));

                            temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, mod.getOutputs().get(0), s.getInputs().get(0))));
                        }
                        i++;
                    }
                } else {
                    // ungerade

                    List<AbstractOperatorSvg> createdOps = new ArrayList<AbstractOperatorSvg>();

                    for (int k = 0; k < modules.size() - 1; k++) {
                        m.setId("Operator-" + (editor.getModules().size() + 1 + i));
                        AbstractOperatorSvg s = getSvg(left + (i * 120), top);
                        createdOps.add(s);
                        temp.add(new ModuleCreateCmd(s));

                        i++;
                    }

                    // verbindungen erstellen
                    int j = 0;
                    for (AbstractOperatorSvg mod : createdOps) {
                        temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, modules.get(j).getOutputs().get(0), mod.getInputs().get(0))));
                        temp.add(new LinkCreateCmd(new OperatorLinkSvg(editor, modules.get(j + 1).getOutputs().get(0), mod.getInputs().get(1))));
                        j++;
                    }
                }
            }
        }

        return temp;
    }

}
