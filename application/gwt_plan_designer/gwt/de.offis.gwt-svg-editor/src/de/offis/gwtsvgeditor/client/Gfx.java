package de.offis.gwtsvgeditor.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * This class helps to differentiate between several browser to 
 * decide of svg or png should be used in some areas. Reason: 
 * Some browsers (FF) cant show SVG as <image />s (yet).
 * darstellen(bzw. noch nicht).
 * 
 * @author Alexander Funk
 */
public final class Gfx {

	public static boolean BROWSER_SUPPORTS_SVG = false;
	
    private static String PORT_GFX = "SvgEditor/port";
    private static String PORT_SELECTED_GFX = "SvgEditor/portSelected";
    private static String PORT_YES_GFX = "SvgEditor/portYes";
    private static String PORT_NO_GFX = "SvgEditor/portNo";
    
    static {
        String agent = Window.Navigator.getUserAgent().toLowerCase();

        if (agent.contains("chrome") || agent.contains("safari") || agent.contains("webkit") || agent.contains("mozilla/5.0")) {
            BROWSER_SUPPORTS_SVG = true;
        } else {
        	BROWSER_SUPPORTS_SVG = false;
        }
    }

    public static String getPort() {
        return GWT.getModuleName() + "/" + PORT_GFX + (BROWSER_SUPPORTS_SVG ? ".svg" : ".png");
    }

    public static String getPortNo() {
        return GWT.getModuleName() + "/" + PORT_NO_GFX + (BROWSER_SUPPORTS_SVG ? ".svg" : ".png");
    }

    public static String getPortSelected() {
        return GWT.getModuleName() + "/" + PORT_SELECTED_GFX + (BROWSER_SUPPORTS_SVG ? ".svg" : ".png");
    }

    public static String getPortYes() {
        return GWT.getModuleName() + "/" + PORT_YES_GFX + (BROWSER_SUPPORTS_SVG ? ".svg" : ".png");
    }
}
