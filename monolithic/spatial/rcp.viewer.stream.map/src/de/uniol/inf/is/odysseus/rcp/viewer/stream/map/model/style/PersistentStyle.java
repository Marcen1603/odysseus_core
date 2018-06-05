package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style;

import java.io.Serializable;
import java.util.LinkedList;
import org.eclipse.swt.graphics.RGB;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.ColorCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.IntegerCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.ShapeCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class PersistentStyle implements Serializable{

    private static final long serialVersionUID = 4156756396139876014L;

	private PersistentCondition lineWidth = null;
	private PersistentCondition lineColor = null;
	private PersistentCondition fillColor = null;
	
	private String type = null;

	private PersistentCondition shape = null;
	private PersistentCondition shapeSize = null;
	
	private LinkedList<PersistentStyle> substyles = null;
    public PersistentStyle() {};
    
    public PersistentStyle(Style style) {
    	if(style instanceof PointStyle){
    		type = "point";
    		this.shape = (((PointStyle) style).getSize().getSerializable());
    		this.shapeSize = (((PointStyle) style).getSize().getSerializable());
    	}
        if(style instanceof LineStyle)
        	type = "line";
        if(style instanceof PolygonStyle)
          	type = "polygon";	
        if(style instanceof CollectionStyle)
        	type = "collection";
        
    	if(style.getLineWidth() != null){
    		this.lineWidth = style.getLineWidth().getSerializable();
    	}
    	if(style.getLineColor() != null){
    		this.lineColor = style.getLineColor().getSerializable();
    		
    	}
    	if(style.getFillColor() != null){
    		this.fillColor = style.getFillColor().getSerializable();
    	}
    	if(style.hasSubstyles()){
    		substyles = new LinkedList<PersistentStyle>();
    		for(Style substyle :style.getSubstyles()){
    			if(substyle != null)
    				substyles.add(new PersistentStyle(substyle));
    		}
    	}
    }

	public static String convertRGBToHexadecimal(RGB rgb) {
		int red = rgb.red;
		int green = rgb.green;
		int blue = rgb.blue;
		String redHexadecimal = Integer.toHexString(red);
		String greenHexadecimal = Integer.toHexString(green);
		String blueHexadecimal = Integer.toHexString(blue);
		if (redHexadecimal.length() == 1) redHexadecimal = "0" + redHexadecimal;
		if (greenHexadecimal.length() == 1) greenHexadecimal = "0" + greenHexadecimal;
		if (blueHexadecimal.length() == 1) blueHexadecimal = "0" + blueHexadecimal;
		return "#" + redHexadecimal + greenHexadecimal + blueHexadecimal;
	}

	public static RGB convertHexadecimalToRGB(String hexadecimal) throws NumberFormatException{
		java.awt.Color col=null;
		try{
			col=java.awt.Color.decode(hexadecimal);
		}
		catch (Exception e) {
			col=java.awt.Color.WHITE;
		}
		int red=col.getRed();
		int blue=col.getBlue();
		int green=col.getGreen();

		return new RGB(red, green, blue);
	}
	
	public IntegerCondition getLineWidth() {
		if (this.lineWidth == null)
			return null;

    	return new IntegerCondition(this.lineWidth);
    }

	public ColorCondition getLineColor() {
		if (this.lineColor == null)
			return null;
		return new ColorCondition(this.lineColor);
    }
	
	public ColorCondition getFillColor() {
		if (this.fillColor == null)
			return null;
		return new ColorCondition(this.fillColor);
    }

	public LinkedList<PersistentStyle> getSubstyles() {
    	return substyles;
    }

	public String getType() {
    	return type;
    }

	public ShapeCondition getShape() {
		return new ShapeCondition(this.shape);
	}

	public IntegerCondition getShapeSize() {
		// TODO Auto-generated method stub
		return new IntegerCondition(this.shapeSize);
	}

	public void setType(String string) {
		this.type = string;
	}

	public void setFillColor(RGB defaultColor, String expression) {
		this.fillColor = new PersistentCondition(convertRGBToHexadecimal(defaultColor), expression);
	}

	public void addSubstyle(PersistentStyle persistentStyle) {
		if (this.substyles == null)
			this.substyles = new LinkedList<PersistentStyle>();
		this.substyles.add(persistentStyle);
	}

	public void setLineColor(RGB defaultColor, String expression) {
		this.lineColor = new PersistentCondition(convertRGBToHexadecimal(defaultColor), expression);
		
	}

	public void setShapeSize(int defaultSize, String expression) {
		this.shapeSize = new PersistentCondition(defaultSize, expression);
	}

	public void setShape(String defaultShape, String expression) {
		this.shape = new PersistentCondition(defaultShape, expression);
		
	}

	public void setLineWidth(int defaultLineWidth, String expression) {
		this.lineWidth = new PersistentCondition(defaultLineWidth, expression);
		
	}
}
