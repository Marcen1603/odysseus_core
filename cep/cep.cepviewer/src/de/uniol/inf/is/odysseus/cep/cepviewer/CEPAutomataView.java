package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.AutomataDiagram;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.DragListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.EndState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.NormalState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.State;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.Transition;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.TransitionLoop;

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
		Canvas canvas = new Canvas(parent, 0);

		// create the basic struktur of an automata diagram
		LightweightSystem lws = new LightweightSystem(canvas);
		AutomataDiagram diagram = new AutomataDiagram();
		lws.setContents(diagram);

		// create the states (the example automata starts here)
		NormalState start = new NormalState(parent);
		start.setText("S0");
		start.setBounds(new Rectangle(50, 50, State.SIZE, State.SIZE));
		NormalState dec = new NormalState(parent);
		dec.setText("S12");
		dec.setBounds(new Rectangle(150, 50, State.SIZE, State.SIZE));
		NormalState proc = new NormalState(parent);
		proc.setText("S2");
		proc.setBounds(new Rectangle(250, 50, State.SIZE, State.SIZE));
		EndState stop = new EndState(parent);
		stop.setText("S3");
		stop.setBounds(new Rectangle(350, 50, State.SIZE, State.SIZE));

		// create all transitions
		Transition path1 = new Transition();
		path1.setSourceAnchor(start.getOutAnchor());
		path1.setTargetAnchor(dec.getInAnchor());
		Transition path2 = new Transition();
		path2.setSourceAnchor(dec.getOutAnchor());
		path2.setTargetAnchor(proc.getInAnchor());
		Transition path3 = new Transition();
		path3.setSourceAnchor(proc.getOutAnchor());
		path3.setTargetAnchor(stop.getInAnchor());
		TransitionLoop loopA = new TransitionLoop(dec, TransitionLoop.TAKE_LOOP);
		loopA.setSourceAnchor(dec.getTakeOutAnchor());
		loopA.setTargetAnchor(dec.getTakeInAnchor());
		TransitionLoop loopB = new TransitionLoop(dec,
				TransitionLoop.IGNORE_LOOP);
		loopB.setSourceAnchor(dec.getIgnoreOutAnchor());
		loopB.setTargetAnchor(dec.getIgnoreInAnchor());

		// add all components to the diagram
		diagram.add(start);
		diagram.add(dec);
		diagram.add(proc);
		diagram.add(stop);
		diagram.add(path1);
		diagram.add(path2);
		diagram.add(path3);
		diagram.add(loopA);
		diagram.add(loopB);

		// add drag and drop feature to the states
		new DragListener(start);
		new DragListener(proc);
		new DragListener(dec);
		new DragListener(stop);
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	@Override
	public void setFocus() {
	}

}
