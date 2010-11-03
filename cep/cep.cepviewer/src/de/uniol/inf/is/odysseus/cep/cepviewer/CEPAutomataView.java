package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.event.CEPViewAgent;
import de.uniol.inf.is.odysseus.cep.cepviewer.event.CEPViewEvent;
import de.uniol.inf.is.odysseus.cep.cepviewer.event.ICEPViewListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataDiagram;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.DragListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.NormalTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.TransitionLoop;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.Action;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.State;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.Transition;

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
	public static final String ID = "MyView.mainView";

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
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		CEPViewAgent.getInstance().addCEPEventListener(new ICEPViewListener() {
			public void cepEventOccurred(CEPViewEvent event) {
				clearView();
				showAutomata((StateMachineInstance) event.getContent());
			}
		});
		Canvas canvas = new Canvas(this.parent, 0);

		// create the basic struktur of an automata diagram
		LightweightSystem lws = new LightweightSystem(canvas);
		this.diagram = new AutomataDiagram();
		lws.setContents(this.diagram);
	}

	public void showAutomata(StateMachineInstance instance) {
		this.currentState = instance.getCurrentState();
		State nextState = instance.getMachine().getStates()[0];
		AutomataState state = this.createNewState(nextState);
		this.createTransitions(state, nextState);
	}
	
	private void createTransitions(AutomataState oldState, State nextState) {
		try{
		for (Transition nextTrans : nextState.getTransition()) {
			if (nextTrans.getNextState().equals(nextState)) {
				TransitionLoop loop;
				if (nextTrans.getAction().equals(Action.CONSUME)) {
					loop = new TransitionLoop(oldState, TransitionLoop.TAKE_LOOP);
					loop.setSourceAnchor(oldState.getTakeOutAnchor());
					loop.setTargetAnchor(oldState.getTakeInAnchor());
				} else {
					loop = new TransitionLoop(oldState, TransitionLoop.IGNORE_LOOP);
					loop.setSourceAnchor(oldState.getIgnoreOutAnchor());
					loop.setTargetAnchor(oldState.getIgnoreInAnchor());
				}
				this.diagram.add(loop);
			} else {
				AutomataState newState = createNewState(nextTrans.getNextState());
				NormalTransition path = new NormalTransition();
				path.setSourceAnchor(oldState.getOutAnchor());
				path.setTargetAnchor(newState.getInAnchor());
				this.diagram.add(path);
				this.createTransitions(newState, nextTrans.getNextState());
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private AutomataState createNewState(State rootState) {
		AutomataState newState = new AutomataState(this.parent, rootState, this.currentState.equals(rootState), rootState.isAccepting());
		newState.setText("S" + rootState.getId());
		newState.setBounds(new Rectangle(50 + (100 * this.stateCount), 50,
				AbstractState.SIZE, AbstractState.SIZE));
		this.diagram.add(newState);
		new DragListener(newState);
		this.stateCount++;
		return newState;
	}

	private void clearView() {
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
