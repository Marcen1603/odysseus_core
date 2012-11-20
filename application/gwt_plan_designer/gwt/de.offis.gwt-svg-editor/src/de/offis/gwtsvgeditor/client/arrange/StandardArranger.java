package de.offis.gwtsvgeditor.client.arrange;

import de.offis.gui.client.gwtgraphics.animation.Animate;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will arrange all modules on a SvgEditor from 
 * top (Inputs) to bottom (Outputs) and between those the operators.
 *
 * @author Alexander Funk
 * 
 */
public class StandardArranger {
    private final SvgEditor editor;
    private double maxTop = 0;
    
    public StandardArranger(SvgEditor editor) {
        this.editor = editor;
    }

    public void arrange(){
    	this.maxTop = 0;
        double startX = editor.getViewBox()[0];
        double startY = editor.getViewBox()[1];

        List<SvgModule> top = new ArrayList<SvgModule>();
        List<SvgModule> rest = new ArrayList<SvgModule>();
        List<SvgModule> bottom = new ArrayList<SvgModule>();

        // vorsortierung
        for (SvgModule m : editor.getModules().values()) {
            if (m.getOutputs().size() == 1 && m.getInputs().isEmpty()) {
                top.add(m);
            } else if(m.getOutputs().isEmpty()){
            	bottom.add(m);
            } else {
                rest.add(m);
            }
        }

        // fuer top-elemente an den oberen rand setzen       
        double offsetLeft = startX;
        double offsetTop = startY;
        
        double spacingVertical = 70;
        double spacingHorizontal = 50;
        for (int i = 0; i < top.size(); i++) {
            SvgModule m = top.get(i);

            offsetLeft += spacingHorizontal + recursiveArrange(0, m, offsetLeft, offsetTop, spacingHorizontal, spacingVertical);
        }
        
        // all outputs on an extra level as part 2
        for (int i = 0; i < bottom.size(); i++) {
            SvgModule m = bottom.get(i);

            Animate b = new Animate(m, "translate y", m.getTranslate()[1], this.maxTop + spacingVertical, 1000);
            b.start();
        }

        // TODO
        // group all elements that are not wired somewhere
        // setviewbox
    }

    private double recursiveArrange(int level, SvgModule module, double offsetLeft, double offsetTop, double spacingHorizontal, double spacingVertical) {


//        module.setPosition(maxLeft, maxTop);

        if (module.getOutputLinks().isEmpty()) {
            Animate a = new Animate(module, "translate x", module.getTranslate()[0], offsetLeft, 1000);
            Animate b = new Animate(module, "translate y", module.getTranslate()[1], offsetTop, 1000);
            a.start();
            b.start();
            return module.getWidth();
        } else {
            double partWidth = 0;
            double heightTemp = 0;
            
            for(int i = 0 ; i < module.getOutputLinks().size() ; i++){
                SvgLink link = module.getOutputLinks().get(i);
                SvgModule dest = link.getDestination().getModule();

                heightTemp = dest.getHeight();
                
                partWidth += recursiveArrange(level, dest, offsetLeft + partWidth, offsetTop + dest.getHeight() + spacingVertical, spacingHorizontal, spacingVertical);

                if(!(i == module.getOutputLinks().size()-1)){
                    partWidth += spacingHorizontal;
                }
            }
            
            Animate a = new Animate(module, "translate x", module.getTranslate()[0], offsetLeft + ((partWidth - module.getWidth())/2), 1000);
            Animate b = new Animate(module, "translate y", module.getTranslate()[1], offsetTop, 1000);
            a.start();
            b.start();

            // INFO: wanted side effect
            if(this.maxTop < offsetTop + heightTemp){
                this.maxTop = offsetTop + heightTemp;
            }

            return partWidth;
        }
    }
}
