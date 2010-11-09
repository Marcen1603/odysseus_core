package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.Transition;

public class AbstractTransition extends PolylineConnection {
	
	protected Transition transition;

	public AbstractTransition(Anchor sourceAnchor, Anchor targetAnchor, Transition transition) {
		this.transition = transition;
		this.setSourceAnchor(sourceAnchor);
		this.setTargetAnchor(targetAnchor);
		setTargetDecoration(new PolylineDecoration());
		this.setToolTip(new Label(transition.getCondition().getLabel() + "/" + transition.getAction().toString()));
	}
	
}