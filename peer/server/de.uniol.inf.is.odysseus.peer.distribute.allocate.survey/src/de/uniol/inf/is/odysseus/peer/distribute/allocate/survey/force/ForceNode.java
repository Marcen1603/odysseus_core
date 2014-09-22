package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

import java.util.Collection;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

final class ForceNode {

	private static long idCounter = 0;

	private final Collection<Force> attachedForces = Lists.newArrayList();

	private final boolean fixed;
	private final ILogicalQueryPart queryPart;
	private final long id;

	private Vector3D position;

	public ForceNode(Vector3D position, ILogicalQueryPart queryPart) {
		this(position, queryPart, false);
	}
	
	public ForceNode(Vector3D position, ILogicalQueryPart queryPart, boolean fixed) {
		Preconditions.checkNotNull(position, "Position for forceNode must not be null!");
		Preconditions.checkNotNull(queryPart, "QueryPart for ForceNode must not be null!");

		this.position = position;
		this.queryPart = queryPart;

		this.fixed = fixed;
		id = idCounter++;
	}

	public ForceNode(Vector3D position) {
		Preconditions.checkNotNull(position, "Position for forceNode must not be null!");

		this.position = position;
		this.queryPart = null;

		fixed = true;
		id = idCounter++;
	}

	public Vector3D getPosition() {
		return position;
	}

	public ILogicalQueryPart getQueryPart() {
		if (queryPart == null) {
			throw new RuntimeException("Could not get the query part from a fixed force node!");
		}

		return queryPart;
	}

	void attachForce(Force force) {
		attachedForces.add(force);
	}

	public boolean isFixed() {
		return fixed;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("#").append(id).append(", ");
		sb.append(position.getX()).append("/").append(position.getY()).append("/").append(position.getZ()).append(", ");
		if( queryPart != null ) {
			sb.append("part=").append(queryPart);			
		}
		if (fixed) {
			sb.append("FIXED");
		} 

		if (!attachedForces.isEmpty()) {
			sb.append(", forces to: ");
			for (Force force : attachedForces) {
				if (force.getNodeA().equals(this)) {
					sb.append(force.getNodeB().id);
				} else {
					sb.append(force.getNodeA().id);
				}
				sb.append("<").append(force.getDataRate()).append(">");
				sb.append("  ");
			}
		}

		sb.append("}");
		return sb.toString();
	}

	public double tick(double frame) {
		if (!isFixed()) {
			Vector3D direction = new Vector3D(0,0,0);
			for (Force force : attachedForces) {
				ForceNode otherNode = null;
				if( force.getNodeA().equals(this)) {
					otherNode = force.getNodeB();
				} else {
					otherNode = force.getNodeA();
				}
				Vector3D dist = otherNode.position.subtract(position).scalarMultiply(force.getDataRate() * frame);
				direction = direction.add(dist);
			}
			
			position = position.add(direction);
			return Math.sqrt((direction.getX() * direction.getX()) + (direction.getY() * direction.getY()) + (direction.getZ() * direction.getZ()));
		} 
		
		return 0.0;

	}
}
