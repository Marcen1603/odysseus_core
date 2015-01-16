package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	 * The class of T.
	 */
	private Class<T> cls;

	/**
	 * A sorted, multidimensional array of the values.
	 */
	private T values[][];

	/**
	 * Initializes the MDAStore.
	 * 
	 * @param The
	 *            class of the values to store.
	 * @param vals
	 *            The values for the MDAStore.
	 * @throws NullPointerException
	 *             if <code>vals</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>vals</code> contains only one dimension or if the
	 *             dimensions of <code>vals</code> have different numbers of
	 *             values.
	 */
	public void initialize(Class<T> classOfValues, T[][] vals)
			throws NullPointerException, IllegalArgumentException {
		validateValues(vals);
		this.cls = classOfValues;
		this.values = vals;
		this.numDims = this.values.length;
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
	private void validateValues(T[][] vals) throws NullPointerException,
			IllegalArgumentException {
		if (vals == null) {
			throw new NullPointerException(
					"Values to initialize MDAStore must be not null!");
		} else if (vals.length < 1) {
			throw new IllegalArgumentException(
					"There must be at least one dimension for the MDAStore!");
		}
	}

	/**
	 * Sorts the values.
	 */
	private void sortValues() {
		for (int dim = 0; dim < this.numDims; dim++) {
			List<T> valsAsList = Arrays.asList(this.values[dim]);
			Collections.sort(valsAsList);
			valsAsList.toArray(this.values[dim]);
		}
	}

	/**
	 * Gets the values of the cell.
	 * 
	 * @param val
	 *            The searches value.
	 * @return The value of the cell center spot.
	 * @throws NullPointerException
	 *             if <code>val</code> is null.
	 */
	public T[] getCellValues(T[] val) throws NullPointerException {
		int[] indizes = getCellIndices(val);
		@SuppressWarnings("unchecked")
		T[] retValue = (T[]) Array.newInstance(this.cls, indizes.length);
		for (int dim = 0; dim < indizes.length; dim++) {
			retValue[dim] = this.values[dim][indizes[dim]];
		}
		return retValue;
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
	public int[] getCellIndices(T[] val) throws NullPointerException {
		if (this.values == null) {
			throw new NullPointerException("The MDAStore is not initialized!");
		}
		validateValue(val, this.numDims);
		int[] indices = new int[this.numDims];
		for (int dim = 0; dim < this.numDims; dim++) {
			indices[dim] = binSearch(this.values[dim], val[dim], 0,
					this.values[dim].length - 1);
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
	private void validateValue(T[] val, int sizeReference)
			throws NullPointerException, IllegalArgumentException {
		if (val == null) {
			throw new NullPointerException(
					"Values for the MDAStore must be not null!");
		} else if (val.length != sizeReference) {
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
	private int binSearch(T[] vals, T val, int start, int end) {
		int mid = start + (end - start) / 2;
		if (val.compareTo(vals[mid]) == 0
				|| (mid == start && val.compareTo(vals[mid]) < 0)
				|| (mid == end && val.compareTo(vals[mid]) > 0)) {
			return mid;
		} else if (val.compareTo(vals[mid]) < 0) {
			if (mid > start && val.compareTo(vals[mid - 1]) > 0) {
				double distance = vals[mid].compareTo(vals[mid - 1]);
				if (val.compareTo(vals[mid - 1]) < distance / 2) {
					return mid - 1;
				}
				return mid;
			}
			return binSearch(vals, val, 0, mid - 1);
		}
		if (mid < end && val.compareTo(vals[mid + 1]) < 0) {
			double distance = vals[mid + 1].compareTo(vals[mid]);
			if (val.compareTo(vals[mid]) < distance / 2) {
				return mid;
			}
			return mid + 1;
		}
		return binSearch(vals, val, mid + 1, end);
	}

	/*
	 * Simple Test.
	 */
	public static void main(String[] args) {
		MDAStore<Double> store1d = new MDAStore<>();
		store1d.initialize(Double.class, new Double[][] { { 10.0, 5.0, 0.0,
				20.0, 15.0 } });

		int[] cell = store1d.getCellIndices(new Double[] { 15.0 });
		Double[] val = store1d.getCellValues(new Double[] { 15.0 });
		System.out.println("Cell1 = " + cell[0] + ", Value = " + val[0]);
		cell = store1d.getCellIndices(new Double[] { 12.0 });
		val = store1d.getCellValues(new Double[] { 12.0 });
		System.out.println("Cell2 = " + cell[0] + ", Value = " + val[0]);
		cell = store1d.getCellIndices(new Double[] { -5.0 });
		val = store1d.getCellValues(new Double[] { -5.0 });
		System.out.println("Cell3 = " + cell[0] + ", Value = " + val[0]);
		cell = store1d.getCellIndices(new Double[] { 100.0 });
		val = store1d.getCellValues(new Double[] { 100.0 });
		System.out.println("Cell4 = " + cell[0] + ", Value = " + val[0]);

		System.out.println("---");
		MDAStore<Double> store2d = new MDAStore<>();
		store2d.initialize(Double.class, new Double[][] {
				{ 10.0, 5.0, 0.0, 20.0, 15.0 }, { 0.0, 1.0, 2.0 } });

		cell = store2d.getCellIndices(new Double[] { 15.0, 2.0 });
		val = store2d.getCellValues(new Double[] { 15.0, 2.0 });
		System.out.println("Cell1 = " + cell[0] + ", " + cell[1] + ", Value = "
				+ val[0] + ", " + val[1]);
		cell = store2d.getCellIndices(new Double[] { 12.0, 0.5 });
		val = store2d.getCellValues(new Double[] { 12.0, 0.5 });
		System.out.println("Cell2 = " + cell[0] + ", " + cell[1] + ", Value = "
				+ val[0] + ", " + val[1]);
		cell = store2d.getCellIndices(new Double[] { -5.0, 1.25 });
		val = store2d.getCellValues(new Double[] { -5.0, 1.25 });
		System.out.println("Cell3 = " + cell[0] + ", " + cell[1] + ", Value = "
				+ val[0] + ", " + val[1]);
		cell = store2d.getCellIndices(new Double[] { 100.0, 0.0 });
		val = store2d.getCellValues(new Double[] { 100.0, 0.0 });
		System.out.println("Cell4 = " + cell[0] + ", " + cell[1] + ", Value = "
				+ val[0] + ", " + val[1]);
	}

}