package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.ArrayList;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.swt.graphics.Point;

/**
 * This class defines an transition loop in an automata.
 * 
 * @author Christian
 */
public class TransitionLoop extends PolylineConnection {

	/**
	 * This constant ensures that the loop will be drawn above the state.
	 */
	public static final int TAKE_LOOP = 0;

	/**
	 * This constant ensures that the loop will be drawn beneath the state.
	 */
	public static final int IGNORE_LOOP = 1;

	private final int MAX_BENDPOINTS = 3;
	private final int BENDPOINT_Y_SPACING = 20;

	private ArrayList<AbsoluteBendpoint> list;

	private AutomataState state;
	private int style;

	/**
	 * This is the constructor.
	 * 
	 * @param state
	 *            is the state which this loop belongs to.
	 * @param style
	 *            defines the layout of the loop
	 */
	public TransitionLoop(Anchor sourceAnchor, Anchor targetAnchor, AutomataState state, int style) {
		this.setSourceAnchor(sourceAnchor);
		this.setTargetAnchor(targetAnchor);
		
		this.setToolTip(new Label("Transition between " + ((Label)this.getSourceAnchor().getOwner().getToolTip()).getText() + " and " + ((Label)this.getTargetAnchor().getOwner().getToolTip()).getText()));
		this.state = state;
		this.style = style;

		// set the layout of the connection to resemble an arrow
		setTargetDecoration(new PolylineDecoration());

		// set the connection router to connect two anchors via some bend points
		setConnectionRouter(new BendpointConnectionRouter());

		// create the bendpoints an specific loop style
		list = new ArrayList<AbsoluteBendpoint>();
		for (Point point : this.createLocations(state)) {
			list.add(new AbsoluteBendpoint(point.x, point.y));
		}

		// add the bend points to the connection
		this.setRoutingConstraint(list);
	}

	private Point[] createLocations(AutomataState state) {

		Point[] points = new Point[MAX_BENDPOINTS];
		switch (style) {
		case 0:
			points[0] = new Point(state.getLocation().x, 
					state.getLocation().y - BENDPOINT_Y_SPACING);
			points[1] = new Point(state.getLocation().x + AbstractState.SIZE / 2,
					state.getLocation().y - BENDPOINT_Y_SPACING * 2);
			points[2] = new Point(state.getLocation().x + AbstractState.SIZE,
					state.getLocation().y - BENDPOINT_Y_SPACING);
			break;
		case 1:
			points[0] = new Point(state.getLocation().x,
					state.getLocation().y + AbstractState.SIZE + BENDPOINT_Y_SPACING);
			points[1] = new Point(state.getLocation().x + AbstractState.SIZE / 2,
					state.getLocation().y + AbstractState.SIZE + BENDPOINT_Y_SPACING * 2);
			points[2] = new Point(state.getLocation().x + AbstractState.SIZE,
					state.getLocation().y + AbstractState.SIZE + BENDPOINT_Y_SPACING);
			break;
		default:
			break;
		}
		return points;
	}

	public void repaint() {
		super.repaint();
		if (list != null) {
			Point[] points = this.createLocations(state);
			for (int i = 0; i < MAX_BENDPOINTS; i++) {
				this.list.get(i).setLocation(points[i].x, points[i].y);
			}
		}
	}

}