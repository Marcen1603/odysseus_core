package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class OwdData {

	public static class GridCellList extends LinkedList<GridCell> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3509667401421124671L;
				
		private GridCell tail;
		
		@Override
		public boolean add(GridCell c) {
			if(this.tail != null) {
				this.tail.next = c;
				c.previous = this.tail;
				this.tail = c;
			} else {
				this.tail = c;
			}
			return super.add(c);
		}
		

	    public GridCell pollLast() {
	    	if(this.size() > 1) {
	    		this.tail = this.tail.previous;
	    		this.tail.next = null;
	    	} else {
	    		this.tail = null;
	    	}
	    	return super.pollLast();
	    }

	}

	public static class GridCell implements Iterable<GridCell> {
		
		private final int x;
		private final int y;
		
		private GridCell previous;
		private GridCell next;
		
		public GridCell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public GridCell getPrevious() {
			return previous;
		}

		public GridCell getNext() {
			return next;
		}
		
		@Override
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}

		@Override
		public Iterator<GridCell> iterator() {
			return new Iterator<OwdData.GridCell>() {

				private GridCell current = GridCell.this;
				
				@Override
				public boolean hasNext() {
					return this.current != null;
				}

				@Override
				public GridCell next() {
					if(!this.hasNext()) {
						throw new NoSuchElementException();
					}
					final GridCell result = this.current;
					this.current = this.current.next;
					return result;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}
	
	private final GridCellList gridCells;
	
	public OwdData(final GridCellList sgridCells) {
		this.gridCells = sgridCells;
	}

	public GridCellList getGridCells() {
		return this.gridCells;
	}
}
