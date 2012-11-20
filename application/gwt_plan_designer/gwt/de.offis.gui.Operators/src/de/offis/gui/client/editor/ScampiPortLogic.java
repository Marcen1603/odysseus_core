package de.offis.gui.client.editor;

import de.offis.gui.client.module.OperatorLinkSvg;
import de.offis.gwtsvgeditor.client.AbstractPortLogic;
import de.offis.gwtsvgeditor.client.IncompatiblePortsEx;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.commands.LinkCreateCmd;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;
import de.offis.scai.GwtScai;

/**
 * Implementation of AbstractPortLogic for gwt-svg-editor.
 *
 * @author Alexander Funk
 * 
 */
public class ScampiPortLogic extends AbstractPortLogic {

    private static final ScampiPortLogic INSTANCE = new ScampiPortLogic();
    private SvgPort portTemp = null;

    public static ScampiPortLogic get() {
        return INSTANCE;
    }

    /**
     * 
     *
     * @param start
     * @param end
     * @return
     */
    public String match(SvgPort start, SvgPort end) {
    	if (start.isInput() || !end.isInput()) {
    	  	return "Connections are from an Output-Port(Bottom) to an Input-Port(Top)!";
      	}
    	
        // keine verbindungen von ausgang und eingang des gleichen moduls
        if (start.isNeighbor(end)) {
            return "You cant connect Output and Input from the same Module!";
        }

        // nur ein link pro eingang
        if (end.isConnected()) {
            return "This Port can only have one Connection!";
        }

        if ((start.isInput() && end.isInput()) || (!start.isInput() && !end.isInput())) {
            // keine 2 eingänge oder 2 ausgänge verbinden
            return "You cant connect two Inputs or two Outputs!";
        }

        if(!GwtScai.checkDataStreamEquals(start.getDatastream(), end.getDatastream()) && !start.getDatastream()[0].equals("*") && !end.getDatastream()[0].equals("*")){
            // check datastreams
            return "DataStreams are not compatible!";
        }

        return null;
    }

    public void onPortClicked(SvgEditor editor, SvgPort port) {
    	if(!editor.equals(port.getModule().getEditor())){
    		return;
    	}
    	
        if (portTemp == null) {
            // select
            portTemp = port;
            portTemp.select(true);
        } else if (portTemp.equals(port)) {
            // deselect if clicked twice
            portTemp.select(false);
            portTemp = null;
        } else {
            try {
                editor.addCmd(new LinkCreateCmd(new OperatorLinkSvg(editor, portTemp, port)));
                editor.commitCmds();
                portTemp.select(false);
                portTemp = null;
            } catch (IncompatiblePortsEx e1) {
                portTemp.select(false);
                portTemp = null;
            }
        }
    }

	@Override
	public void onModuleCreated(SvgModule module) {
//		portTemp.select(false); // nicht notwendig da es schon in SvgPort getan wird?
		portTemp = null;
	}

	@Override
	public void onLinkCreated(SvgLink link) {
		// TODO Auto-generated method stub
		
	}
}
