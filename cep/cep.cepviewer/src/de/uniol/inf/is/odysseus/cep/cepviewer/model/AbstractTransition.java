package de.uniol.inf.is.odysseus.cep.cepviewer.model;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;

import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

public class AbstractTransition extends PolylineConnection {
	
	protected Transition transition;
	protected String name;

	public AbstractTransition(Anchor sourceAnchor, Anchor targetAnchor, Transition transition) {
		this.name = transition.getCondition().getLabel();
		this.transition = transition;
		this.setSourceAnchor(sourceAnchor);
		this.setTargetAnchor(targetAnchor);
		setTargetDecoration(new PolylineDecoration());
		this.setToolTip(new Label(transition.getCondition().getLabel() + "/" + transition.getAction().toString()));
	}
	
	public String getName() {
		return name;
	}
	
}