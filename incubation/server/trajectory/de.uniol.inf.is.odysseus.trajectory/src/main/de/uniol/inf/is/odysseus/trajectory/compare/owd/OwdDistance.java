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
	
	
	private final double gridCellSize;
	
	private final double diagonalLength;
	
	public OwdDistance(final double gridCellSize, final double diagonalLength) {
		this.gridCellSize = gridCellSize;
		this.diagonalLength = diagonalLength;
	}
	

	@Override
	public double getDistance(ITrajectory<OwdData, ?> queryTrajectory,
			ITrajectory<OwdData, ?> dataTrajectory) {
		
		final GridCellList t1 = queryTrajectory.getData().getGridCells();
		final GridCellList t2 = dataTrajectory.getData().getGridCells();
				
		final double result = (0.5 * (this.getOwd(t1, t2) + this.getOwd(t2, t1)) * this.gridCellSize) / this.diagonalLength;
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("OWD distance between query trajectory " + queryTrajectory + " and data trajectory " + dataTrajectory + ": " + result);
		}
		return result;
	}
	
	private double getOwd(GridCellList trajectory1, GridCellList trajectory2) {

		final Iterator<GridCell> it = trajectory1.iterator();
		GridCell cellBefore = it.next();

		IndexedLinkedHashSet<GridCell> minPointsBefore = this.getLocalMinPoints(cellBefore, trajectory2);
		
		double owdDistance = this.shortestDistance(cellBefore, minPointsBefore);
		
		while(it.hasNext()) {

			final GridCell cellCurr = it.next();
			final IndexedLinkedHashSet<GridCell> minPointsCurr = new IndexedLinkedHashSet<>();

			for(int i = 0; i < minPointsBefore.size(); i++) {
				
				final GridCell minPointBefore = minPointsBefore.get(i);
				
				if((cellBefore.getY() == cellCurr.getY() && minPointBefore.getX() != cellBefore.getX())
						|| (cellBefore.getX() == cellCurr.getX() && minPointBefore.getY() != cellBefore.getY())) {
					minPointsCurr.add(minPointBefore);
				} else {

					GridCell cellFrom;
					if(i > 0) {
						cellFrom = minPointsBefore.get(i - 1).getNext();
					} else {
						cellFrom = trajectory2.getFirst();
					}
					
					GridCell cellTo;
					if(i == minPointsBefore.size() - 1) {
						cellTo = trajectory2.getLast();
					} else {
						cellTo = minPointsBefore.get(i + 1).getPrevious();
					}
					
					final Iterator<GridCell> cellIt = cellFrom.iterator();
					GridCell cellBetween;
					do {						
						cellBetween = cellIt.next();

						if(cellBefore.getY() == cellCurr.getY() || cellBefore.getX() == cellCurr.getX()) {
							if(this.isLocalMinPoint(cellBetween, cellCurr)) {
								minPointsCurr.add(cellBetween);
							}
						}
						
					} while(cellBetween != cellTo);
				}
			}

			owdDistance += this.shortestDistance(cellCurr, minPointsCurr);	
			
			cellBefore = cellCurr;
			minPointsBefore = minPointsCurr;
		}
		
		return owdDistance / trajectory1.size();
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
