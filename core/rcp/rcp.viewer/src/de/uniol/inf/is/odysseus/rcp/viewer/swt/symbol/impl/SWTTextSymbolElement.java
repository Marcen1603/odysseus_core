package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTTextSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private Font font;
	
	private int lastWidth;
	private int lastTextWidth;
	private int lastTextHeight;
	
	private Color color;
	
	
	public SWTTextSymbolElement(Color color){
		this.color = color;
	}
	
	@Override
	public void draw(Vector position, int width, int height, float zoomFactor) {
		GC gc = getActualGC();
		INodeView<C> view = getNodeView();
		String name = view.getModelNode().getContent().getClass().getSimpleName();

		boolean ok = false;
		int textWidth = 0;
		int textHeight = 0;
		int fontSize = 10;
		
		// calculate ideal font-size
		// save width to cache font...
		while( !ok && lastWidth != width) {
			
			if( font != null ) {
				font.dispose();
				font = null;
			}
			
			font = new Font(Display.getDefault(), "Arial", fontSize, SWT.BOLD);
			gc.setFont(font);
			textWidth = 0;
			for( char c : name.toCharArray() ) {
				textWidth += gc.getAdvanceWidth(c);
			}
			textHeight = gc.getFontMetrics().getHeight();
			
			if( textWidth > width * 0.9 ) {
				fontSize--;
			} else {
				ok = true;
				lastWidth = width;
				lastTextWidth = textWidth;
				lastTextHeight = textHeight;
			}
		}
		
		final int x = ((int)position.getX()) + (width / 2) - (lastTextWidth / 2); 
		final int y = ((int)position.getY()) + (height / 2) - ( lastTextHeight / 2);
		
		gc.setFont(font);
		gc.setForeground(color);
		gc.drawText(name, x, y, true);
	}
}
