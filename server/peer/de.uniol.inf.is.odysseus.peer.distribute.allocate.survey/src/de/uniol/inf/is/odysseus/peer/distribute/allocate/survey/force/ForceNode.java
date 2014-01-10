package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

import java.util.Collection;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

public final class ForceNode {

	private final Collection<Force> attachedForces = Lists.newArrayList();
	
	private Vector3D position;
	private ILogicalQueryPart queryPart;
	
	public ForceNode( Vector3D position, ILogicalQueryPart queryPart ) {
		Preconditions.checkNotNull(position, "Position for forceNode must not be null!");
		Preconditions.checkNotNull(queryPart, "QueryPart for ForceNode must not be null!");
		
		this.position = position;
		this.queryPart = queryPart;
	}
	
	public Vector3D getPosition() {
		return position;
	}
	
	public ILogicalQueryPart getQueryPart() {
		return queryPart;
	}
	
	void attachForce( Force force ) {
		attachedForces.add(force);
	}
}
