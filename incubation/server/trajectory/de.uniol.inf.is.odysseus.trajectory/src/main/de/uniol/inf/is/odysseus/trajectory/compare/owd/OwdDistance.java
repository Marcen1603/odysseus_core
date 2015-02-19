package de.uniol.inf.is.odysseus.trajectory.compare.owd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.ITrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCell;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;

public class OwdDistance implements ISpatialDistance<OwdData> {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(OwdDistance.class);
	

	@Override
	public double getDistance(ITrajectory<OwdData, ?> queryTrajectory,
			ITrajectory<OwdData, ?> dataTrajectory) {
		
		final GridCellList t1 = queryTrajectory.getData().getGridCells();
		final GridCellList t2 = dataTrajectory.getData().getGridCells();
				
		final double result = (this.getOwd(t1, t2) + this.getOwd(t2, t1)) * 0.5;
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("OWD distance between query trajectory " + queryTrajectory + " and data trajectory " + dataTrajectory + ": " + result);
		}
		return result;
	}
	
	private double getOwd(GridCellList t1, GridCellList t2) {

		final Iterator<GridCell> itT1 = t1.iterator();
		GridCell g1 = itT1.next();

		IndexedLinkedHashSet<GridCell> lastMinPoints = this.getLocalMinPoints(g1, t2);
		
		double owdDistance = this.shortestDistance(g1, lastMinPoints);
		
		while(itT1.hasNext()) {

			final GridCell newG1 = itT1.next();
			final IndexedLinkedHashSet<GridCell> newMinPoints = new IndexedLinkedHashSet<>();

			for(int i = 0; i < lastMinPoints.size(); i++) {
				
				final GridCell gp = lastMinPoints.get(i);
				
				if((g1.getY() == newG1.getY() && gp.getX() != g1.getX())
						|| (g1.getX() == newG1.getX() && gp.getY() != g1.getY())) {
					newMinPoints.add(gp);
				} else {

					GridCell gp0;
					if(i > 0) {
						gp0 = lastMinPoints.get(i - 1).getNext();
					} else {
						gp0 = t2.getFirst();
					}
					
					GridCell gp2;
					if(i == lastMinPoints.size() - 1) {
						gp2 = t2.getLast();
					} else {
						gp2 = lastMinPoints.get(i + 1).getPrevious();
					}
					
					final Iterator<GridCell> itStrich = gp0.iterator();
					GridCell gstrich;
					do {						
						gstrich = itStrich.next();

						if(g1.getY() == newG1.getY() || g1.getX() == newG1.getX()) {
							if(this.isLocalMinPoint(gstrich, newG1)) {
								newMinPoints.add(gstrich);
							}
						}
						
					} while(gstrich != gp2);
				}
			}

			owdDistance += this.shortestDistance(newG1, newMinPoints);	
			
			lastMinPoints = newMinPoints;
			g1 = newG1;
		}
		
		return owdDistance/ t1.size();
	}
	
	
	private double shortestDistance(final GridCell g, final Iterable<GridCell> minPoints) {
		
		final Iterator<GridCell> it = minPoints.iterator();
		final GridCell min = it.next();
		
		double minDistance = g.distance(min);
		
		while(it.hasNext()) {
			final GridCell minNext = it.next();
			final double nextDistance = g.distance(minNext);
			
			if(nextDistance < minDistance) {
				minDistance = nextDistance;
			}
		}
		return minDistance;
	}
	
	private IndexedLinkedHashSet<GridCell> getLocalMinPoints(final GridCell g, final Iterable<GridCell> trajectory) {
		final IndexedLinkedHashSet<GridCell> result = new IndexedLinkedHashSet<>();
		for(final GridCell g1 : trajectory) {
			if(this.isLocalMinPoint(g1, g)) {
				result.add(g1);
			}
		}
		//LOGGER.info(result + "");
		return result;
	}
	
	private boolean isLocalMinPoint(final GridCell g1, final GridCell g) {
		if(g1.getPrevious() == null) {
			if(g1.getNext() == null) {
				return true;
			}
			return this.closer(g1, g1.getNext(), g) == g1;
		}
		if(g1.getNext() == null) {
			return this.closer(g1, g1.getPrevious(), g) == g1;
		}
		return this.closer(g1, g1.getNext(), g) == g1 
				&& this.closer(g1, g1.getPrevious(), g) == g1;
	}
	
	
	private GridCell closer(GridCell g1, GridCell g2, GridCell g) {
		if(!g1.isAdjacent(g2)) {
			throw new RuntimeException("");
		}
		if(g1.getY() == g2.getY()) {
			if(Math.abs(g1.getX() - g.getX()) < Math.abs(g2.getX() - g.getX())) {
				return g1;
			}
			return g2;
		}
		if(Math.abs(g1.getY() - g.getY()) < Math.abs(g2.getY() - g.getY())) {
			return g1;
		}
		return g2;
	}
	
	public static class IndexedLinkedHashSet<E> extends LinkedHashSet<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8977956093819575992L;
		
		
		private final ArrayList<E> indexList = new ArrayList<E>();
		
		
		public E get(int index) {
			return this.indexList.get(index);
		}
		
		
		@Override
		public boolean add(E elem) {
			if(super.add(elem)) {
				return this.indexList.add(elem);
			}
			return false;
		}
		
		@Override 
		public boolean remove(Object elem) {
			throw new UnsupportedOperationException();
		}
	}
}
