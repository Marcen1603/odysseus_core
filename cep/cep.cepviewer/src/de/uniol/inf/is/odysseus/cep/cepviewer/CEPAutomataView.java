package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataDiagram;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.DragListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.TransitionLoop;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This class defines the automata view.
 * 
 * @author Christian
 */
public class CEPAutomataView extends ViewPart {

	// The id of this view.
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.automataview";

	private static final int STATE_X_SPACING = 100;

	private Composite parent;
	private AutomataDiagram diagram;
	private State currentState;
	private int stateCount;

	/**
	 * This is the constructor.
	 */
	public CEPAutomataView() {
		super();
	}

	/**
	 * This method creates the automata view. An automata example was
	 * implemented to test the components of the automata model.
	 * 
	 * @param parent
	 *            is the widget which contains the automata view.
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;
		Canvas canvas = new Canvas(this.parent, 0);

		// create the basic struktur of an automata diagram
		LightweightSystem lws = new LightweightSystem(canvas);
		this.diagram = new AutomataDiagram();
		lws.setContents(this.diagram);
	}

	@SuppressWarnings("unchecked")
	public void showAutomata(StateMachineInstance instance) {
		this.currentState = instance.getCurrentState();
		// TODO
		State firstState = instance.getStateMachine().getInitialState();
		AutomataState state = this.createState(firstState);
		this.createTransitions(state, firstState);
	}

	private void createTransitions(AutomataState oldState, State nextState) {
		for (Transition nextTrans : nextState.getTransitions()) {
			if (nextTrans.getNextState().equals(nextState)) {
				TransitionLoop loop = null;
				if (nextTrans.getAction() == EAction.consumeBufferWrite) {
					loop = new TransitionLoop(oldState.getTakeOutAnchor(),
							oldState.getTakeInAnchor(), nextTrans, oldState);
				} else if (nextTrans.getAction() == EAction.discard) {
					loop = new TransitionLoop(oldState.getIgnoreOutAnchor(),
							oldState.getIgnoreInAnchor(), nextTrans, oldState);
				}
				this.diagram.add(loop);
			} else {
				AutomataState newState = createState(nextTrans.getNextState());
				AutomataTransition path = new AutomataTransition(oldState
						.getOutAnchor(), newState.getInAnchor(), nextTrans);
				this.diagram.add(path);
				this.createTransitions(newState, nextTrans.getNextState());
			}
		}
	}

	private AutomataState createState(State rootState) {
		AutomataState newState = new AutomataState(this.parent,
				this.stateCount, rootState,
				this.currentState.equals(rootState), rootState.isAccepting());
		newState.setBounds(new Rectangle(STATE_X_SPACING
				* (this.stateCount + 1),
				(this.diagram.getSize().height - AbstractState.SIZE) / 2,
				AbstractState.SIZE, AbstractState.SIZE));
		this.diagram.add(newState);
		new DragListener(newState);
		this.stateCount++;
		return newState;
	}

	/**
	 * @deprecated never used
	 */
	public void update() {
		this.diagram.repaint();
	}

	public void clearView() {
		this.diagram.removeAll();
		this.stateCount = 0;
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	@Override
	public void setFocus() {
	}

}
