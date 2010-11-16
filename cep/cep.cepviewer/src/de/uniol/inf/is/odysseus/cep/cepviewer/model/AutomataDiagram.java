package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

/**
 * This class defines the layout of an automata diagram.
 * 
 * @author Christian
 */
public class AutomataDiagram extends FreeformLayeredPane {

	/**
	 * This is the constructor.
	 */
	public AutomataDiagram() {
		setLayoutManager(new FreeformLayout());
		setBorder(new MarginBorder(5));
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
	}

}