package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.util.ImageUtil;

public class DashboardControl {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardControl.class);
	
	private final Composite composite;
	
	private IFile loadedBackgroundImageFile;
	private Image loadedBackgroundImage;
	private boolean isLoadedBackgroundImageStretched = false;
	
	public DashboardControl( Composite parent ) {
		Preconditions.checkNotNull(parent, "Parent for dashboard control must not be null!");
		
		composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FormLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setBackgroundImageFile( IFile bgFile, boolean stretched ) {
		if (areImageSettingsChanged(bgFile, stretched)) {

			disposeBackgroundImage();

			if (bgFile != null) {
				setNewBackgroundImage(bgFile, stretched);
			} else {
				clearBackgroundImage();
			}
		}
	}

	private boolean areImageSettingsChanged(IFile bgFile, boolean stretched) {
		return loadedBackgroundImageFile != bgFile || stretched != isLoadedBackgroundImageStretched;
	}

	private void clearBackgroundImage() {
		composite.setBackgroundImage(null);
		
		loadedBackgroundImageFile = null;
		loadedBackgroundImage = null;
		isLoadedBackgroundImageStretched = false;
	}

	private void setNewBackgroundImage(IFile bgFile, boolean stretched) {
		try {
			Image image = new Image(Display.getCurrent(), bgFile.getContents());
	
			if (stretched) {
				Image stretchedImage = ImageUtil.resizeImage(image, composite.getSize().x, composite.getSize().x);
				image.dispose();
				image = stretchedImage;
			}
			composite.setBackgroundImage(image);
			
			isLoadedBackgroundImageStretched = stretched;
			loadedBackgroundImageFile = bgFile;
			loadedBackgroundImage = image;
		} catch( CoreException ex ) {
			LOG.error("Could not set background image", ex);
			
			clearBackgroundImage();
		}
	}
	
	public void dispose() {
		disposeBackgroundImage();
	}

	private void disposeBackgroundImage() {
		if( loadedBackgroundImage != null ) {
			loadedBackgroundImage.dispose();
		}
	}

	public void update() {
		composite.layout();
		composite.redraw();
	}
	
	public Composite getComposite() {
		return composite;
	}
}
