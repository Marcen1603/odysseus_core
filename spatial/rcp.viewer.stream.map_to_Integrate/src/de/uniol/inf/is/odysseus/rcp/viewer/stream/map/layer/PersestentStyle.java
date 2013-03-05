package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.io.Serializable;
import java.util.LinkedList;

import org.eclipse.swt.graphics.RGB;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class PersestentStyle implements Serializable{

    private static final long serialVersionUID = 4156756396139876014L;

	private int lineWidth = 1;
	private RGB lineColor = null;
	private RGB fillColor = null;
	
	private String predicate = null;
	
	private String type = null;
	
	private LinkedList<PersestentStyle> substyles = null;
    
    public PersestentStyle(Style style) {
    	if(style instanceof PointStyle)
    		type = "point";
        if(style instanceof LineStyle)
        	type = "line";
        if(style instanceof PolygonStyle)
          	type = "polygon";	
        if(style instanceof CollectionStyle)
        	type = "collection";
        
    	this.lineWidth = style.getLineWidth();
    	if(style.getLineColor() != null)
    		this.lineColor = style.getLineColor().getRGB();
    	if(style.getFillColor() != null)
    		this.lineColor = style.getFillColor().getRGB();
    	if(style.hasSubstyles()){
    		substyles = new LinkedList<PersestentStyle>();
    		for(Style substyle :style.getSubstyles()){
    			if(substyle != null)
    				substyles.add(new PersestentStyle(substyle));
    		}
    	}
    }

	public int getLineWidth() {
    	return lineWidth;
    }

	public RGB getLineColor() {
    	return lineColor;
    }

	public RGB getFillColor() {
    	return fillColor;
    }

	public String getPredicate() {
    	return predicate;
    }

	public LinkedList<PersestentStyle> getSubstyles() {
    	return substyles;
    }

	public String getType() {
    	return type;
    }
	
	

}
