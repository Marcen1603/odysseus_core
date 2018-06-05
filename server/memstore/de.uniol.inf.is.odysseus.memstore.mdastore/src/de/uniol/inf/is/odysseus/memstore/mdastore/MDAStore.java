package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The multidimensional array (MDA) store works as follows: <br />
 * 1. Initialize the storage with n double arrays, where n is the number of
 * dimensions. <br />
 * 2. An arrays consists of the cell borders, so there is one cell less than the
 * array contains elements. <br />
 * 3. Request the n-dimensional cell of a given n-dimensional value. A
 * n-dimensional value is within a cell, if it's between two subsequent borders.
 * 
 * @author Michael
 */
public class MDAStore {

	/**
	 * The number of dimensions.
	 */
	private int numDims;

	/**
	 * A sorted, multidimensional array of the borders.
	 */
	private List<List<Double>> borders;

	/**
	 * Initializes the MDAStore.
	 * 
	 * @param borders
	 *            The borders for the MDAStore.
	 * @throws NullPointerException
	 *             if <code>borders</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>borders</code> is empty.
	 */
	public void initialize(List<List<Double>> borders) throws NullPointerException, IllegalArgumentException {
		validateBordersAllDims(borders);
		this.borders = borders;
		this.numDims = this.borders.size();
		sortBorders();
	}

	/**
	 * Checks borders for a dimension of the MDAStore.
	 * 
	 * @param borders
	 *            The given values.
	 * @throws NullPointerException
	 *             if <code>borders</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>borders</code> is empty.
	 */
	private void validateBorders1Dim(List<Double> borders) throws NullPointerException, IllegalArgumentException {
		if (borders == null) {
			throw new NullPointerException("Dimension to initialize MDAStore must be not null!");
		} else if (borders.size() < 1) {
			throw new IllegalArgumentException("There must be at least one value for the dimension of a MDAStore!");
		}
	}

	/**
	 * Checks borders for the MDAStore.
	 * 
	 * @param borders
	 *            The given values.
	 * @throws NullPointerException
	 *             if <code>borders</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>borders</code> is empty.
	 */
	private void validateBordersAllDims(List<List<Double>> borders)
			throws NullPointerException, IllegalArgumentException {
		if (borders == null) {
			throw new NullPointerException("Values to initialize MDAStore must be not null!");
		} else if (borders.size() < 1) {
			throw new IllegalArgumentException("There must be at least one dimension for the MDAStore!");
		}
		for (List<Double> dim : borders) {
			validateBorders1Dim(dim);
		}
	}

	/**
	 * Sorts the borders.
	 */
	private void sortBorders() {
		for (int dim = 0; dim < this.numDims; dim++) {
			Collections.sort(this.borders.get(dim));
		}
	}

	/**
	 * Gets the indices of the cell.
	 * 
	 * @param val
	 *            The searches value.
	 * @return The indices of the cell. -1 is the index for invalid values
	 *         (e.g., null or outlier)
	 * @throws NullPointerException
	 *             if {@link #initialize(List)} has not been called before.
	 */
	public List<Integer> getCellIndices(List<Double> val) throws NullPointerException {
		if (this.borders == null) {
			throw new NullPointerException("The MDAStore is not initialized!");
		}
		List<Integer> indices = Lists.newArrayList();
		if (val == null || val.size() != this.numDims) {
			for (int i = 0; i < this.numDims; i++) {
				indices.add(-1);
			}
		} else {
			for (int dim = 0; dim < this.numDims; dim++) {
				indices.add(binSearch(this.borders.get(dim), val.get(dim), 0, this.borders.get(dim).size() - 1));
			}
		}
		return indices;
	}

	/**
	 * Binary search for a value.
	 * 
	 * @param borders
	 *            The sorted search area as borders.
	 * @param val
	 *            The searched value.
	 * @param start
	 *            The smallest index for the search area.
	 * @param end
	 *            The largest index for the search area.
	 * @return The index for which <code>val</code> matches the value in the
	 *         search area or -1, if the search area does not contain
	 *         <coce>val</code>.
	 */
	private int binSearch(List<Double> borders, Double val, int start, int end) {
		final int mid = start + (end - start) / 2;
		final int diff = val.compareTo(borders.get(mid));
		final int minIndex = 0;
		final int maxIndex = borders.size() - 1;

		if (diff == 0) {
			if (mid == maxIndex) {
				return mid - 1;
			}
			return mid;
		} else if (diff < 0) {
			// left side
			if (mid == start) {
				// it's the cell to the left
				if (start == minIndex) {
					// outlier
					return -1;
				}
				return mid - 1;
			}
			return binSearch(borders, val, start, mid - 1);
		} else {
			// right side
			if (mid == end) {
				// it's the cell to the right
				if (end == maxIndex) {
					// outlier
					return -1;
				}
				return mid;
			}
			return binSearch(borders, val, mid + 1, end);
		}
	}
	
	public void addDimension(int index, List<Double> borders) throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		validateBorders1Dim(borders);
		Collections.sort(borders);
		this.borders.add(index, borders);
	}
	
	public void addDimension(List<Double> borders) throws NullPointerException, IllegalArgumentException {
		addDimension(this.borders.size(), borders);
	}
	
	public void removeDimension(int index) throws IndexOutOfBoundsException {
		this.borders.remove(index);
	}

}