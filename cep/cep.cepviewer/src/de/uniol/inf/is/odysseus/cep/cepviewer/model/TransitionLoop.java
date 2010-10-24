package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import java.util.ArrayList;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;

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
	
	/**
	 * This is the constructor.
	 * 
	 * @param state is the state which this loop belongs to.
	 * @param style defines the layout of the loop
	 */
	public TransitionLoop(NormalState state, int style) {
		
		// set the layout of the connection to resemble an arrow
		setTargetDecoration(new PolylineDecoration());
		
		// set the connection router to connect two anchors via some bend points
		setConnectionRouter(new BendpointConnectionRouter());
		
		// create the bendpoints an specific loop style
		ArrayList<AbsoluteBendpoint> list = new ArrayList<AbsoluteBendpoint>();
		switch(style) {
			case 0:
				list.add(new AbsoluteBendpoint(state.getLocation().x, state
						.getLocation().y - 20));
				list.add(new AbsoluteBendpoint(state.getLocation().x + State.SIZE / 2,
						state.getLocation().y - 40));
				list.add(new AbsoluteBendpoint(state.getLocation().x + State.SIZE,
						state.getLocation().y - 20));
				break;
			case 1:
				list.add(new AbsoluteBendpoint(state.getLocation().x, state
						.getLocation().y+ State.SIZE + 20));
				list.add(new AbsoluteBendpoint(state.getLocation().x + State.SIZE / 2,
						state.getLocation().y + State.SIZE + 40));
				list.add(new AbsoluteBendpoint(state.getLocation().x + State.SIZE,
						state.getLocation().y + State.SIZE + 20));
				break;
			default:
				break;
		}
		
		// add the bend points to the connection
		this.setRoutingConstraint(list);
	}
}