package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.impl;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.viewer.view.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;

public class SWTImageSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private final String imageName;
	private Image image;
	private int imageWidth;
	private int imageHeight;
	
	public SWTImageSymbolElement( String imageName ) {
		this.imageName = imageName;
		loadImage();
	}
	
	@Override
	public void draw( Vector pos, int width, int height, float zoomFactor  ) {
		loadImage();
		if (image != null){
		
			GC gc = getActualGC();
			
			if( gc == null )
				return;
			
			gc.drawImage( image, 0, 0, imageWidth, imageHeight, pos.getX(), pos.getY(), width, height );
		}
	}
	
	public final int getImageHeight() {
		return imageHeight;
	}
	
	public final int getImageWidth() {
		return imageWidth;
	}

	private void loadImage() {
		if( image == null || image.isDisposed() ) {
			// Bild neu holen
			image = Activator.getDefault().getImageRegistry().get(imageName);
			if( image != null ) {
				imageWidth = image.getBounds().width;
				imageHeight = image.getBounds().height;
			} else {
				// Kein Bild!
				return;
			}
		}
	}
}
