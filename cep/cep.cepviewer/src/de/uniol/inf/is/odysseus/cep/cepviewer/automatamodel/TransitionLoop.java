package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

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
		super(sourceAnchor, targetAnchor, transition, state);
		this.state = state;
		// set the connection router to connect two anchors via some bend
		// points
		setConnectionRouter(new BendpointConnectionRouter());
		this.list = new ArrayList<AbsoluteBendpoint>();
		for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
			this.list.add(new AbsoluteBendpoint(new Point(0, 0)));
		}
		this.setLocations();
		// add the bend points to the connection
		this.setRoutingConstraint(list);
	}

	private ArrayList<Point> createLocationPoints() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
			points.add(new Point((IntConst.STATE_SIZE * i / 2),
					IntConst.Y_SPACE_BETWEEN_BENDPOINTS * (1 + i % 2)));
		}
		return points;
	}
	
	private void setLocations() {
		ArrayList<Point> points = this.createLocationPoints();
		if (this.transition.getAction() == EAction.consumeBufferWrite) {
			for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
				this.list.get(i).setLocation(
						this.state.getLocation().x + points.get(i).x,
						this.state.getLocation().y - points.get(i).y);
			}
		} else if (this.transition.getAction() == EAction.discard) {
			for (int i = 0; i < IntConst.MAX_BENDPOINTS_OF_LOOP; i++) {
				this.list.get(i).setLocation(
						this.state.getLocation().x + points.get(i).x,
						this.state.getLocation().y + points.get(i).y
								+ IntConst.STATE_SIZE);
			}
		}
	}

	public void repaint() {
		if(this.list != null) {
			this.setLocations();
		}
		super.repaint();
	}

}