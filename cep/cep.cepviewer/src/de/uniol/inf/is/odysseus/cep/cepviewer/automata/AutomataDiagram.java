package de.uniol.inf.is.odysseus.cep.cepviewer.automata;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;

/**
 * This class defines the layout of an automata diagram.
 * 
 * @author Christian
 */
public class AutomataDiagram extends FreeformLayeredPane {

	private CEPInstance instance;
	/**
	 * This is the constructor of this class. It set the LayoutManager and
	 * determines the background color.
	 */
	public AutomataDiagram() {
		this.setLayoutManager(new FreeformLayout());
		this.setBorder(new MarginBorder(5));
		// TODO: background und opaque benötigt?
		this.setBackgroundColor(ColorConstants.white);
		this.setOpaque(true);
	}

	public void setInstance(CEPInstance instance) {
		this.instance = instance;
	}

	public CEPInstance getInstance() {
		return instance;
	}

}