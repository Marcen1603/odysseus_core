package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class defines the cep perspective.
 * 
 * @author Christian
 */
public class CEPViewer implements IPerspectiveFactory {
	
	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.cepviewer";

	/**
	 * This methods creates the layout of the perspective and adds the views.
	 * 
	 * @param myLayout
	 *            is the layout of the perspective
	 */
	public void createInitialLayout(IPageLayout myLayout) {
		// deactived the editor area
		myLayout.setEditorAreaVisible(false);
	}
}
