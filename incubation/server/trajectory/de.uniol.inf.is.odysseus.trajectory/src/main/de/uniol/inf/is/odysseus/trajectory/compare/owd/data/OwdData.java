package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class OwdData {

	public static class GridCellList implements Iterable<GridCell> {
		
		private LinkedList<GridCell> internal = new LinkedList<>();
		
		private GridCell tail;
		
		public boolean addGridCell(int x, int y) {
			final GridCell gridCell = new GridCell(x, y);
			if(this.tail != null) {
				final int distance = Math.abs(this.tail.getX() - gridCell.getX()) 
						+ Math.abs(this.tail.getY() - gridCell.getY());
				
				if(distance > 1) {
					throw new IllegalArgumentException("Cell not adjacent to last inserted cell");
				}
				
				if(distance == 0) {
					return false;
				}
						
				this.tail.next = gridCell;
				gridCell.previous = this.tail;
				this.tail = gridCell;
			} 
			this.tail = gridCell;
			return this.internal.add(gridCell);
		}
		
		public GridCell getFirst() {
			return this.internal.getFirst();
		}
		
		public GridCell getLast() {
			return this.internal.getLast();
		}
		
		public int size() {
			return this.internal.size();
		}

		@Override
		public Iterator<GridCell> iterator() {
			return this.internal.iterator();
		}

		@Override
		public String toString() {
			return this.internal.toString();
		}
	}

	public static class GridCell implements Iterable<GridCell> {
		
		private final int x;
		private final int y;
		
		private GridCell previous;
		private GridCell next;
		
		private GridCell(int x, int y) {
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
			return this.previous;
		}

		public GridCell getNext() {
			return this.next;
		}
		
		@Override
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}
		
		public boolean isAdjacent(GridCell other) {
			return (this.previous == other && other.next == this)
					|| (this.next == other && other.previous == this);
		}
		
		public double distance(GridCell other) {
			return Math.sqrt(Math.pow(this.getX() - other.getX(), 2) 
					+ Math.pow(this.getY() - other.getY(), 2));
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
