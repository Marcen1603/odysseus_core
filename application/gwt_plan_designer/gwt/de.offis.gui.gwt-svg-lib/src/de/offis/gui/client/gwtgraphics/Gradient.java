package de.offis.gui.client.gwtgraphics;

/**
*
* @author Alexander Funk
*/
public abstract class Gradient extends Definition {

    public Gradient(String tag) {
        super(tag);
    }

    public Gradient addStop(GradientStop stop){
       getElement().appendChild(stop.getElement());
       return this;
    }
}
