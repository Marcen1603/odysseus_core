package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AutomataDiagram;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This class defines the automata view which shows a state diagram.
 * 
 * @author Christian
 */
public class CEPAutomataView extends ViewPart {

	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.automataview";
	// the widget which displays the diagram
	private AutomataDiagram diagram;
	// the composite which is the parent to this view
	private Composite parent;

	/**
	 * This is the constructor.
	 */
	public CEPAutomataView() {
		super();
	}

	/**
	 * This method clears the view.
	 */
	public void clearView() {
		this.diagram.removeAll();
	}

	/**
	 * This method initializes the structure of this view. It creates an Canvas
	 * and adds an Instance of the LightweightSystem class. This instance allows
	 * to add class instances of the draw2d toolkit to the Canvas
	 * 
	 * @param parent
	 *            is the widget which contains the automata view.
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;
		Canvas canvas = new Canvas(this.parent, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		// create the basic struktur of an automata diagram
		LightweightSystem lws = new LightweightSystem(canvas);
		this.diagram = new AutomataDiagram();
		lws.setContents(this.diagram);
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
		// do nothing
	}
	
	/**
	 * This method is called to display the state diagram of a CEPInstance by
	 * setting the content of the diagram.
	 * 
	 * @param instance
	 *            is a CEPInstance which holds the elements of the state diagram
	 */
	public void showAutomata(CEPInstance instance) {
		this.diagram.setContent(instance);
	}

}
