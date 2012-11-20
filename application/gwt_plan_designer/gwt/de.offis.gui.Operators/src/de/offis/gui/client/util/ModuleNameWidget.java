package de.offis.gui.client.util;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.shape.Rectangle;
import de.offis.gui.client.gwtgraphics.shape.Text;

/**
 * SVG-Widget to enlarge text on hover on long names.
 *
 * @author Alexander Funk
 * 
 */
public class ModuleNameWidget extends Group implements MouseOverHandler, MouseOutHandler{
	private String text;
	private Text svgText;
	private Rectangle background;
	private static final int ELLIPS_COUNT = 7;
	private static final int CHAR_LENGTH_PX = 9;
	private double oldTranslateX = 0;
	private double oldTranslateY = 0;
	private boolean shortened = false;
	
	private static final int BACKGROUND_WIDTH = 100;
	
	public ModuleNameWidget(String text) {
		this.svgText = new Text(5, 15, text);
		this.svgText.setFontSize(15);
		this.svgText.setStrokeWidth(0);
		this.svgText.setFillColor("black");
		this.svgText.setFontFamily("monospace");
		
		this.background = new Rectangle(0, 0, BACKGROUND_WIDTH, 20);
		this.background.setFillColor("white");
		this.background.setStrokeColor("black");
		this.background.setStrokeWidth(1);
		this.background.setStrokeDashArray("5 5");
		this.background.setObjectOpacity(0.0);
		
		add(background);
		add(svgText);
		setText(text);
		addMouseOverHandler(this);
		addMouseOutHandler(this);
	}
	
	public void setText(String text) {
		this.text = text;
        
		shortened = false;
		
        if(text.length() > ELLIPS_COUNT){
        	text = text.substring(0, ELLIPS_COUNT-1) + "...";
        	shortened = true;
        }
		
		this.svgText.setText(text);
	}
	
	public String getText() {
		return text;
	}
	
	private void shrink(){
		setText(text);
		this.background.setObjectOpacity(0.0);
		this.background.setWidth(BACKGROUND_WIDTH);
		this.setTranslate(oldTranslateX, oldTranslateY);
	}
	
	private void enlarge(){
		if(shortened){
			svgText.setText(text);
			this.background.setObjectOpacity(1.0);
			
			oldTranslateX = getTranslate()[0];
			oldTranslateY = getTranslate()[1];
			
			int newWidth = (text.length()*CHAR_LENGTH_PX + 5 + 5);
			
			this.background.setWidth(newWidth);
			this.setTranslate(-(newWidth/2)+50, oldTranslateY);
		}		
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		shrink();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		enlarge();
	}
}
