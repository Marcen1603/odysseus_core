package de.offis.gwtsvgeditor.client.svg;

import com.google.gwt.event.dom.client.ClickHandler;

import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gui.client.gwtgraphics.shape.Rectangle;

/**
 * Is shown when user hovers over a module. 
 * Contains different buttons with specific actions to one module.
 * 
 * @author Alexander Funk
 *
 */
public class SvgModuleToolbar extends Group {
	
	private Rectangle background;
	
	private int verticalSpacing = 2;
	private int numberOfTools = 0;
	private int toolHeight = 30;
	private int toolWidth = 30;
	
	public SvgModuleToolbar(int height) {
		background = new Rectangle(0, 0, 34, height);
		background.setFillColor("white");
		background.setStrokeColor("black");
		background.setStrokeWidth(2);
		add(background);
	}
	
	public void addButton(String hrefImage, ClickHandler clickhandler){
		Image tool = new Image(2, verticalSpacing + (numberOfTools*toolHeight), toolWidth, toolHeight, hrefImage);
		tool.addClickHandler(clickhandler);
		add(tool);
		numberOfTools++;
	}
}
