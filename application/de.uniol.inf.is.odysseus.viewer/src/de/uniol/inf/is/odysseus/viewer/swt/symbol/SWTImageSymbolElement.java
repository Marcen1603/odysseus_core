package de.uniol.inf.is.odysseus.viewer.swt.symbol;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;

public class SWTImageSymbolElement<C> extends AbstractUnfreezableSWTSymbolElement<C> {

	private final String imageName;
	private Image image;
	private int imageWidth;
	private int imageHeight;
	
	public SWTImageSymbolElement( String imageName ) {
		this.imageName = imageName;
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

	private void loadImage() {
		if( image == null || image.isDisposed() ) {
			// Bild neu holen
			image = SWTResourceManager.getInstance().getImage( imageName );
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
