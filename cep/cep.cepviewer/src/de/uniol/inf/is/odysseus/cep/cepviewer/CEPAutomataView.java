package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AutomataDiagram;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This class defines the automata view.
 * 
 * @author Christian
 */
public class CEPAutomataView extends ViewPart {

	// the composite which is the parent to this view
	private Composite parent;
	// the widget which displays the diagram
	private AutomataDiagram diagram;

	/**
	 * This is the constructor.
	 */
	public CEPAutomataView() {
		super();
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
		Canvas canvas = new Canvas(this.parent, SWT.BORDER);
		// create the basic struktur of an automata diagram
		LightweightSystem lws = new LightweightSystem(canvas);
		this.diagram = new AutomataDiagram();
		lws.setContents(this.diagram);
	}

	/**
	 * This method is called to create an automata diagram to an
	 * StateMachineInstance and displays it in this view.
	 * 
	 * @param instance
	 *            is the instance of an StateMachine that should be shown in
	 *            this view
	 */
	public void showAutomata(CEPInstance instance) {
		this.diagram.setInstance(instance);
		for(AbstractState state : instance.getStateList()) {
			this.diagram.add(state);
		}
		for(AbstractTransition state : instance.getTransitionList()) {
			this.diagram.add(state);
		}
	}

	/**
	 * This method clears the AutomataDiagram-instance of all
	 * states and transitions and returns the state counter to 0.
	 */
	public void clearView() {
		this.diagram.removeAll();
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
	}

}
