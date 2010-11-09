package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.ArrayList;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.geometry.Point;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.EAction;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.Transition;

/**
 * This class defines an transition loop in an automata.
 * 
 * @author Christian
 */
public class TransitionLoop extends AbstractTransition {

	private final int MAX_BENDPOINTS = 3;
	private final int BENDPOINT_Y_SPACING = 20;

	private ArrayList<AbsoluteBendpoint> list;

	private AutomataState state;

	/**
	 * This is the constructor.
	 * 
	 * @param state
	 *            is the state which this loop belongs to.
	 * @param eAction
	 *            defines the layout of the loop
	 */
	public TransitionLoop(Anchor sourceAnchor, Anchor targetAnchor,
			Transition transition, AutomataState state) {
		super(sourceAnchor, targetAnchor, transition);
		this.state = state;
		// set the connection router to connect two anchors via some bend
		// points
		setConnectionRouter(new BendpointConnectionRouter());
		this.list = new ArrayList<AbsoluteBendpoint>();
		for (int i = 0; i < MAX_BENDPOINTS; i++) {
			this.list.add(new AbsoluteBendpoint(new Point(0, 0)));
		}
		this.setLocations();
		// add the bend points to the connection
		this.setRoutingConstraint(list);
	}

	private ArrayList<Point> createLocationPoints() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < MAX_BENDPOINTS; i++) {
			points.add(new Point((AbstractState.SIZE * i / 2),
					BENDPOINT_Y_SPACING * (1 + i % 2)));
		}
		return points;
	}
	
	private void setLocations() {
		ArrayList<Point> points = this.createLocationPoints();
		if (this.transition.getAction() == EAction.consumeBufferWrite) {
			for (int i = 0; i < MAX_BENDPOINTS; i++) {
				this.list.get(i).setLocation(
						this.state.getLocation().x + points.get(i).x,
						this.state.getLocation().y - points.get(i).y);
			}
		} else if (this.transition.getAction() == EAction.discard) {
			for (int i = 0; i < MAX_BENDPOINTS; i++) {
				this.list.get(i).setLocation(
						this.state.getLocation().x + points.get(i).x,
						this.state.getLocation().y + points.get(i).y
								+ AbstractState.SIZE);
			}
		}
	}

	public void repaint() {
		super.repaint();
		if (list != null) {
			this.setLocations();
		}
	}

}