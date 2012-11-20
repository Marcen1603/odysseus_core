package de.offis.gui.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Model for a link between two modules.
 *
 * @author Alexander Funk
 * 
 */
public class ScaiLinkModel implements IsSerializable {

    public static final int INPUT_TO_OUTPUT = 0;
    public static final int INPUT_TO_SERVICE = 1;
    public static final int SERVICE_TO_SERVICE = 2;
    public static final int SERVICE_TO_OUTPUT = 3;

    private int type;
    private String source;
    private String destination;

    public ScaiLinkModel(){
        // seriliazable
    }

    public ScaiLinkModel(int type, String source, String destination) {
        this.type = type;
        this.source = source;
        this.destination = destination;
    }

    public int getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "{Type: "+type+" Source: "+source+" Destination: "+destination+"}";
    }
}
