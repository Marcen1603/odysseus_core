package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class defines the cep perspective.
 * 
 * @author Christian
 */
public class CEPPerspective implements IPerspectiveFactory {

	// id of the list view
	private static final String VIEW_ID = "cepviewer.listView";
	// id of the automata view
	private static final String AUTOMATA_ID = "cepviewer.automataView";
	// id of the query view
	private static final String QUERY_ID = "cepviewer.queryView";
	// id of the state view
	private static final String STATE_ID = "cepviewer.stateView";

	/**
	 * This methods creates the layout of the perspective and adds the views.
	 * 
	 * @param myLayout
	 *            is the layout of the perspective
	 */
	public void createInitialLayout(IPageLayout myLayout) {

		// deactive the editor area
		myLayout.setEditorAreaVisible(false);

		// ListView in die Perspektive einbauen (ViewID, Ort, Größe, Bezug)
		myLayout.addView(VIEW_ID, IPageLayout.LEFT, 0.25f, myLayout
				.getEditorArea());

		// MainView in die Perspektive einbauen (ViewID, Ort, Größe, Bezug)
		myLayout.addView(AUTOMATA_ID, IPageLayout.RIGHT, 0.75f, myLayout
				.getEditorArea());

		// QueryMainView in die Perspektive einbauen (ViewID, Ort, Größe, Bezug)
		myLayout.addView(QUERY_ID, IPageLayout.BOTTOM, 0.75f, AUTOMATA_ID);

		// StateView in die Perspektive einbauen (ViewID, Ort, Größe, Bezug)
		myLayout.addView(STATE_ID, IPageLayout.RIGHT, 0.5f, QUERY_ID);

	}
}
