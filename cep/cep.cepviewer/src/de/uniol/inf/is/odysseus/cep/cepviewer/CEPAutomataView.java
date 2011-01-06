package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataDiagram;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.DragListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.TransitionLoop;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.IntConst;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Rectangle;
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
	// counter of all shown states
	private int stateCount;
	// the instance that should be shown as automata diagram
	private StateMachineInstance<?> shownInstance;

	/**
	 * This is the constructor.
	 */
	public CEPAutomataView() {
		super();
		this.stateCount = 0;
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
	public void showAutomata(StateMachineInstance<?> instance) {
		this.shownInstance = instance;
		// create the initial state of the automata
		AutomataState state = this.createState(instance.getStateMachine()
				.getInitialState());
		this.createTransitions(state);
	}

	/**
	 * This method creates the transitions of the automata. The transitions of
	 * the delivered state are distinguished into two types. If the start-state
	 * of the transition equals the end-state, it's a loop. If not, it's a line
	 * between two states and this method is recursively called with the
	 * corresponding AutomataState of its end-state.
	 * 
	 * @param automataState
	 *            is the the state which transitions should be drawn
	 */
	private void createTransitions(AutomataState automataState) {
		for (Transition nextTrans : automataState.getState().getTransitions()) {
			if (nextTrans.getNextState().equals(automataState.getState())) {
				// if the transition is a loop ...
				TransitionLoop loop = null;
				if (nextTrans.getAction() == EAction.consumeBufferWrite) {
					// if the action of the transition is to consume an event
					loop = new TransitionLoop(automataState.getTakeOutAnchor(),
							automataState.getTakeInAnchor(), nextTrans,
							automataState);
				} else if (nextTrans.getAction() == EAction.discard) {
					// if the action of the transition is to discard an event
					loop = new TransitionLoop(
							automataState.getIgnoreOutAnchor(),
							automataState.getIgnoreInAnchor(), nextTrans,
							automataState);
				}
				// add the loop to the diagram
				this.diagram.add(loop);
			} else {
				// if the transition is no loop create its end-state
				AutomataState newState = createState(nextTrans.getNextState());
				AutomataTransition path = new AutomataTransition(
						automataState.getOutAnchor(), newState.getInAnchor(),
						nextTrans);
				// add the transition to the diagramm
				this.diagram.add(path);
				this.createTransitions(newState);
			}
		}
	}

	/**
	 * This method creates an corresponding AutomataState to a State-instance.
	 * After initializing its location in the diagram is set and a new instance
	 * of the DragListener class is created to enable the drag and drop feature.
	 * 
	 * @param rootState is the State which should be tranformed into an AutomataState
	 * @return the created AutomataState-instance 
	 */
	private AutomataState createState(State rootState) {
		AutomataState newState = new AutomataState(this.parent,
				this.stateCount, rootState, this.shownInstance
						.getCurrentState().equals(rootState));
		// set location in the diagram
		newState.setBounds(new Rectangle(IntConst.X_SPACE_BETWEEN_STATES
				* (this.stateCount + 1),
				(this.diagram.getSize().height - IntConst.STATE_SIZE) / 2,
				IntConst.STATE_SIZE, IntConst.STATE_SIZE));
		// add the state to the diagramm
		this.diagram.add(newState);
		new DragListener(newState);
		this.stateCount++;
		return newState;
	}

	/**
	 * This method clears the AutomataDiagram-instance of all
	 * states and transitions and returns the state counter to 0.
	 */
	public void clearView() {
		this.diagram.removeAll();
		this.stateCount = 0;
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
	}

}
