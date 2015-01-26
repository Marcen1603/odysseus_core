package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The multidimensional array (MDA) store works as follows: <br />
 * 1. Initialize the storage with n arrays, where n is the number of dimensions,
 * and the class of the values. <br />
 * 2. Request the n-dimensional cell of a given n-dimensional value or the
 * n-dimensional value of the cell. A n-dimensional value is within a cell, if
 * it's closer to the n-dimensional value of the cell than to the n-dimensional
 * values of every other cell.
 * 
 * @author Michael
 *
 * @param <T>
 */
public class MDAStore<T extends Comparable<? super T>> {

	/**
	 * The number of dimensions.
	 */
	private int numDims;

	/**
	 * A sorted, multidimensional array of the values.
	 */
	private List<List<T>> values;

	/**
	 * Initializes the MDAStore.
	 * 
	 * @param vals
	 *            The values for the MDAStore.
	 * @throws NullPointerException
	 *             if <code>vals</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>vals</code> contains only one dimension or if the
	 *             dimensions of <code>vals</code> have different numbers of
	 *             values.
	 */
	public void initialize(List<List<T>> vals) throws NullPointerException,
			IllegalArgumentException {
		validateValues(vals);
		this.values = vals;
		this.numDims = this.values.size();
		sortValues();
	}

	/**
	 * Checks values for the MDAStore.
	 * 
	 * @param vals
	 *            The given values.
	 * @throws NullPointerException
	 *             if <code>vals</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>vals</code> contains only one dimension.
	 */
	private void validateValues(List<List<T>> vals)
			throws NullPointerException, IllegalArgumentException {
		if (vals == null) {
			throw new NullPointerException(
					"Values to initialize MDAStore must be not null!");
		} else if (vals.size() < 1) {
			throw new IllegalArgumentException(
					"There must be at least one dimension for the MDAStore!");
		}
	}

	/**
	 * Sorts the values.
	 */
	private void sortValues() {
		for (int dim = 0; dim < this.numDims; dim++) {
			Collections.sort(this.values.get(dim));
		}
	}

	/**
	 * Gets the indices of the cell.
	 * 
	 * @param val
	 *            The searches value.
	 * @return The indices of the cell.
	 * @throws NullPointerException
	 *             if <code>val</code> is null.
	 */
	public int[] getCellIndices(List<T> val) throws NullPointerException {
		if (this.values == null) {
			throw new NullPointerException("The MDAStore is not initialized!");
		}
		validateValue(val, this.numDims);
		int[] indices = new int[this.numDims];
		for (int dim = 0; dim < this.numDims; dim++) {
			indices[dim] = binSearch(this.values.get(dim), val.get(dim), 0,
					this.values.get(dim).size() - 1);
		}
		return indices;
	}

	/**
	 * Checks values for the MDAStore.
	 * 
	 * @param val
	 *            The given values.
	 * @param sizeReference
	 *            The number of dimensions <code>val</code> must fit.
	 * @throws NullPointerException
	 *             if <code>val</code> is null.
	 * @throws IllegalArgumentException
	 *             if the dimensions of <code>val</code> do not fit
	 *             <code>sizeReference</code>.
	 */
	private void validateValue(List<T> val, int sizeReference)
			throws NullPointerException, IllegalArgumentException {
		if (val == null) {
			throw new NullPointerException(
					"Values for the MDAStore must be not null!");
		} else if (val.size() != sizeReference) {
			throw new IllegalArgumentException(
					"The number of dimensions does not fit the dimensions of the MDAStore!");
		}
	}

	/**
	 * Binary search for a value.
	 * 
	 * @param vals
	 *            The sorted search area.
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
	private int binSearch(List<T> vals, T val, int start, int end) {
		int mid = start + (end - start) / 2;
		
		if (val.compareTo(vals.get(mid)) == 0) {
			return mid;
		} else if (val.compareTo(vals.get(mid)) < 0) {
			// left side
			if(mid == start) {
				return -1;
			}
			return binSearch(vals, val, start, mid - 1);
		}
		// right side
		if(mid+1 == vals.size()) {
			return -1;
		} else if(val.compareTo(vals.get(mid+1)) < 0) {
			return mid;
		}
		return binSearch(vals, val, mid + 1, end);
	}

	/*
	 * Simple Test.
	 */
	public static void main(String[] args) {
		MDAStore<Double> store = new MDAStore<>();
		List<List<Double>> values = Lists.newArrayList();
		List<Double> values_firstdim = Lists.newArrayList(10.0, 5.0, 0.0, 20.0,
				15.0);
		values.add(values_firstdim);
		store.initialize(values);

		int[] cell = store.getCellIndices((List<Double>) Lists
				.newArrayList(15.0));
		System.out.println("Cell1 = " + cell[0]);
		cell = store.getCellIndices((List<Double>) Lists.newArrayList(12.0));
		System.out.println("Cell2 = " + cell[0]);
		cell = store.getCellIndices((List<Double>) Lists.newArrayList(-5.0));
		System.out.println("Cell3 = " + cell[0]);
		cell = store.getCellIndices((List<Double>) Lists.newArrayList(100.0));
		System.out.println("Cell4 = " + cell[0]);

		System.out.println("---");
		List<Double> values_snddim = Lists.newArrayList(0.0, 1.0, 2.0);
		values.add(values_snddim);
		store.initialize(values);

		cell = store.getCellIndices((List<Double>) Lists
				.newArrayList(15.0, 2.0));
		System.out.println("Cell1 = " + cell[0] + ", " + cell[1]);
		cell = store.getCellIndices((List<Double>) Lists
				.newArrayList(12.0, 0.5));
		System.out.println("Cell2 = " + cell[0] + ", " + cell[1]);
		cell = store.getCellIndices((List<Double>) Lists.newArrayList(-5.0,
				1.25));
		System.out.println("Cell3 = " + cell[0] + ", " + cell[1]);
		cell = store.getCellIndices((List<Double>) Lists.newArrayList(100.0,
				0.0));
		System.out.println("Cell4 = " + cell[0] + ", " + cell[1]);
	}

}