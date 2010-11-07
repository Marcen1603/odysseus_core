package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;

/**
 * This class defines an transition in an automata.
 * 
 * @author Christian
 */
public class NormalTransition extends PolylineConnection {
	
	/**
	 * This is the constructor.
	 */
	public NormalTransition(Anchor sourceAnchor, Anchor targetAnchor) {
		this.setSourceAnchor(sourceAnchor);
		this.setTargetAnchor(targetAnchor);
		this.setToolTip(new Label("Transition between " + ((Label)this.getSourceAnchor().getOwner().getToolTip()).getText() + " and " + ((Label)this.getTargetAnchor().getOwner().getToolTip()).getText()));
		
		// set the layout of the connection to resemble an arrow
		setTargetDecoration(new PolylineDecoration());

		// set the connection router which avoids crossing states
//		setConnectionRouter(new ManhattanConnectionRouter());
		
		// set the connection router which avoids overlapping of connections
//		setConnectionRouter(new FanRouter());
		
		// set the connection router which directly connects two the anchors
		setConnectionRouter(ConnectionRouter.NULL);
	}
}