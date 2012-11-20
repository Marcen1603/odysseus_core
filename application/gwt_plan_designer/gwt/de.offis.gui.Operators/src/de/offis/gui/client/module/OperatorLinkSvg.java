package de.offis.gui.client.module;

import java.util.List;

import de.offis.gui.client.gwtgraphics.shape.path.LineTo;
import de.offis.gui.client.module.inputs.AbstractInputSvg;
import de.offis.gui.client.module.operators.AbstractOperatorSvg;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Customized svg line between modules.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorLinkSvg extends SvgLink {	
	
    public OperatorLinkSvg(SvgEditor editor, SvgPort source, SvgPort destination) {
        super(editor, source, destination);
        link.lineTo(0, 2);
    }

    @Override
    public void update(int x1, int y1, int x2, int y2) {
        link.setStep(2, new LineTo(true, 0, 10));
        super.update(x1, y1, x2, y2-10);
    }
    
    public String getSensorTypeFromSource(){
    	if(source.getModule() instanceof AbstractInputSvg){
    		return ((AbstractInputSvg) source.getModule()).getModel().getSensorType();
    	} else if (source.getModule() instanceof AbstractOperatorSvg){
    		AbstractOperatorSvg svg = (AbstractOperatorSvg) source.getModule();
    		OperatorModuleModel op = svg.getModel();
    		switch(op.getMetaType()){
    		case JOIN:
    			// TODO
    			break;
    		case COMPLEX:
    			// TODO
    			break;
    		default:
    			if(!svg.changesTheDataStream()){
	    			// all operators with 1 in and 1 out that do not change the datastream
					List<SvgLink> links = svg.getInputs().get(0).getLinks();
					SvgLink link = null;
					if(links.size() > 0){
						link = links.get(0);
					}
					
	    			if(link != null && link instanceof OperatorLinkSvg){
	    				return ((OperatorLinkSvg)link).getSensorTypeFromSource();
	    			}	
	    		}
    			break;    			
    		}
    	}
    	
    	return null;    	
    }
}
