package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.ArrayList;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.geometry.Point;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.IntConst;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines an transition loop in an automata.
 * 
 * @author Christian
 */
public class TransitionLoop extends AbstractTransition {

	// the list of the BendPoints
	private ArrayList<AbsoluteBendpoint> list;

	// the state which is the start and end state
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
		this.list = createLocationPoints();
		this.setLocations();
		// add the bend points to the connection
		this.setRoutingConstraint(list);
	}

	/**
	 * This method creates the Bendpoints of this loop and computes their
	 * location relative to the state.
	 * 
	 * @return the list of Bendpoints.
	 */
	private ArrayList<AbsoluteBendpoint> createLocationPoints() {
		ArrayList<AbsoluteBendpoint> points = new ArrayList<AbsoluteBendpoint>();
		for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
			this.list.add(new AbsoluteBendpoint(new Point((IntConst.STATE_SIZE
							* i / 2), IntConst.Y_SPACE_BETWEEN_BENDPOINTS
							* (1 + i % 2))));
		}
		return points;
	}

	/**
	 * This methods sets the location of this transitions Bendpoints. If the
	 * Action of the transition is to consume an event, the loop will be drawn
	 * above the state, else below.
	 */
	private void setLocations() {
		if (this.transition.getAction() == EAction.consumeBufferWrite) {
			// if the action is to consume the events
			for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
				this.list.get(i).setLocation(
						this.state.getLocation().x + this.list.get(i).x,
						this.state.getLocation().y - this.list.get(i).y);
			}
		} else if (this.transition.getAction() == EAction.discard) {
			// if the action is to discard the events
			for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
				this.list.get(i).setLocation(
						this.state.getLocation().x + this.list.get(i).x,
						this.state.getLocation().y + this.list.get(i).y
								+ IntConst.STATE_SIZE);
			}
		}
	}

	/**
	 * This method calls the repaint method of the PolylineConnection.
	 */
	// TODO: Warum if?
	public void repaint() {
		super.repaint();
		if (list != null) {
			this.setLocations();
		}
	}

}