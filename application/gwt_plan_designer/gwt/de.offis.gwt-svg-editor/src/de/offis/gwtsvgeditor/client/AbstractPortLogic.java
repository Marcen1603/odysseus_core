package de.offis.gwtsvgeditor.client;

import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.svg.SvgModule;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * The actual logic if two ports can be connected and some 
 * other methods to integrate by extending this class.
 * 
 * @author Alexander Funk
 *
 */
public abstract class AbstractPortLogic {	
	// return null means its ok, if a string is returned thats the error message!
	public abstract String match(SvgPort start, SvgPort end);
    public abstract void onPortClicked(SvgEditor editor, SvgPort port);    
	public abstract void onModuleCreated(SvgModule module);
	public abstract void onLinkCreated(SvgLink link);
}
