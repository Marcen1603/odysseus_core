/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.owd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCell;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;

/**
 * An implementation of <tt>ISpatialDistance</tt> which calculates the distance 
 * between a </tt>OwdQueryTrajectory</tt> and a </tt>OwdDataTrajectory</tt> based
 * on the <i>OWD distance function</i>
 * 
 * @author marcus
 *
 */
public class OwdDistance implements ISpatialDistance<OwdData> {
	
	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(OwdDistance.class);
	
	/** the width and height of a grid cell */
	private final double gridCellSize;
	
	/** the diagonal length of the Euclidean space */
	private final double diagonalLength;
	
	/**
	 * 
	 * @param gridCellSize the width and height of a grid cell
	 * @param diagonalLength the diagonal length of the Euclidean space
	 */
	public OwdDistance(final double gridCellSize, final double diagonalLength) {
		this.gridCellSize = gridCellSize;
		this.diagonalLength = diagonalLength;
	}
	
	/**
	 * {@inheritDoc}
	 * Here the distance calculation between a </tt>OwdQueryTrajectory</tt> and a </tt>OwdDataTrajectory</tt> 
	 * based on the <i>OWD distance function</i>
	 * 
	 */
	@Override
	public double getDistance(final IConvertedTrajectory<OwdData, ?> queryTrajectory,
			final IConvertedTrajectory<OwdData, ?> dataTrajectory) {
		
		final GridCellList t1 = queryTrajectory.getData().getGridCells();
		final GridCellList t2 = dataTrajectory.getData().getGridCells();
				
		final double result = (0.5 * (this.getOwd(t1, t2) + this.getOwd(t2, t1)) * this.gridCellSize) / this.diagonalLength;
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("OWD distance between query trajectory " + queryTrajectory + " and data trajectory " + dataTrajectory + ": " + result);
		}
		return result;
	}
	
	/**
	 * The main algorithm.
	 * 
	 * @param trajectory1 the first OWD trajectory
	 * @param trajectory2 the last OWD trajectory
	 * @return the OWD distance from <tt>trajectory1</i> to </i>trajectory2</i>
	 */
	private double getOwd(final GridCellList trajectory1, final GridCellList trajectory2) {

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
	
	/**
	 * Calculates and returns the shortest distance from <tt>GridCell</tt> to its
	 * <i>local minpoints</i>.
	 * 
	 * @param g the <tt>GridCell</tt>
	 * @param minPoints <i>local minpoints</i> to <i>g</i>
	 * @return the shortest distance from <i>g</i> to its <i>local minpoints</i>
	 */
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
	
	/**
	 * Calculates and returns the <i>local minpoints</i> to a <tt>GridCell</t>
	 * 
	 * @param g the <tt>GridCell</tt>
	 * @param trajectory the trajectory where to find the local mnpoints
	 * @return the <i>local minpoints</i> to a <i>g</i>
	 */
	private IndexedLinkedHashSet<GridCell> getLocalMinPoints(final GridCell g, final Iterable<GridCell> trajectory) {
		final IndexedLinkedHashSet<GridCell> result = new IndexedLinkedHashSet<>();
		for(final GridCell g1 : trajectory) {
			if(this.isLocalMinPoint(g1, g)) {
				result.add(g1);
			}
		}
		return result;
	}
	
	/**
	 * Checks whether <i>g1</i> is a local minpoint to <i>g</i>.
	 * 
	 * @param g1 the cell to check whether it is a local minpoint
	 * @param g the cell to check whether <i>g1</i> is a local minpoint
	 * @return <tt>true</tt> if <i>g1</i> is a local minpoint to <i>g</i>,
	 *         otherwise <tt>false</i>
	 */
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
	
	/**
	 * Returns the <tt>GridCell</tt> which is closer to <i>g</i>. <i>g1</i> and
	 * <i>g2</i> have to be adjacent.
	 * 
	 * @param g1 the first <tt>GridCell</tt> to check
	 * @param g2 the second <tt>GridCell</tt> to check
	 * @param g the <tt>GridCell</tt> to check whether <i>g1</i> or <i>g2</i> is closer
	 * @return <i>g1</i> if it is closer to <i>g</i>, otherwise <i>g2</i>
	 */
	private GridCell closer(final GridCell g1, final GridCell g2, final GridCell g) {
		if(!g1.isAdjacent(g2)) {
			throw new IllegalArgumentException("g1 and g2 are not adjacent");
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
	
	/**
	 * An extension to a <tt>LinkedHashSet</tt> which elements can be accessed 
	 * over an index.
	 * 
	 * @author marcus
	 *
	 * @param <E> the type of data stored in this <tt>IndexedLinkedHashSet</tt>
	 */
	private static class IndexedLinkedHashSet<E> extends LinkedHashSet<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8977956093819575992L;
		
		/** ArrayList for the index */
		private final ArrayList<E> indexList = new ArrayList<E>();
		
	    /**
	     * Returns the element at the specified position in this set.
	     *
	     * @param  index index of the element to return
	     * @return the element at the specified position in this set
	     * @throws IndexOutOfBoundsException {@inheritDoc}
	     */
		public E get(final int index) {
			return this.indexList.get(index);
		}
		
		
		@Override
		public boolean add(final E elem) {
			if(super.add(elem)) {
				return this.indexList.add(elem);
			}
			return false;
		}
		
		/**
		 * Not supported!
		 */
		@Override 
		public boolean remove(final Object elem) {
			throw new UnsupportedOperationException();
		}
	}
}
