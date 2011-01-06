package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the cep perspective.
 * 
 * @author Christian
 */
public class CEPViewer implements IPerspectiveFactory {

	/**
	 * This methods creates the layout of the perspective and adds the views.
	 * 
	 * @param myLayout
	 *            is the layout of the perspective
	 */
	public void createInitialLayout(IPageLayout myLayout) {

		// deactived the editor area
		myLayout.setEditorAreaVisible(false);

		// integrate the views (ViewID, location, size, relatedTo)
		myLayout.addView(StringConst.LIST_VIEW_ID, IPageLayout.LEFT, 0.25f, myLayout
				.getEditorArea());
		myLayout.addView(StringConst.AUTOMATA_VIEW_ID, IPageLayout.RIGHT, 0.75f, myLayout
				.getEditorArea());
		myLayout.addView(StringConst.QUERY_VIEW_ID, IPageLayout.BOTTOM, 0.75f, StringConst.AUTOMATA_VIEW_ID);
		myLayout.addView(StringConst.STATE_VIEW_ID, IPageLayout.RIGHT, 0.5f, StringConst.QUERY_VIEW_ID);

	}
}
