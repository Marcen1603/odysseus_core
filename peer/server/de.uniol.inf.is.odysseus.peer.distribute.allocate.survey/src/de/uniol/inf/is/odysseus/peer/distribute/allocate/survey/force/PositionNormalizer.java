package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

import java.util.Collection;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Preconditions;

final class PositionNormalizer {

	private final double minX;
	private final double minY;
	private final double minZ;
	
	private final double distX;
	private final double distY;
	private final double distZ;
	
	public PositionNormalizer( Collection<Vector3D> points ) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxZ = Double.MIN_VALUE;
		
		for( Vector3D position : points) {
			if( position.getX() < minX ) {
				minX = position.getX();
			}
			if( position.getY() < minY ) {
				minY = position.getY();
			}
			if( position.getZ() < minZ ) {
				minZ = position.getZ();
			}
			
			if( position.getX() > maxX ) {
				maxX = position.getX();
			}
			if( position.getY() > maxY ) {
				maxY = position.getY();
			}
			if( position.getZ() > maxZ ) {
				maxZ = position.getZ();
			}
		}
		
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		distX = maxX - minX;
		distY = maxY - minY;
		distZ = maxZ - minZ;	
	}
	
	public Vector3D normalize( Vector3D position ) {
		Preconditions.checkNotNull(position, "Position to normalize must not be null!");

		return new Vector3D( distX > 0 ? (position.getX() - minX) / distX : 1, 
				distY > 0 ? (position.getY() - minY) / distY : 1,
				distZ > 0 ? (position.getZ() - minZ) / distZ : 1);
	}
}
