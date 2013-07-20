/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.probabilistic.math.genz;

import java.util.ArrayList;
import java.util.Random;

public class Matrix {
	private double[][] data;
	private int rows;
	private int columns;

	public Matrix(final double[][] values) {
		this.data = new double[values.length][values[0].length];

		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				this.data[i][j] = values[i][j];
			}
		}

		this.rows = values.length;
		this.columns = values[0].length;
	}

	public Matrix(final Matrix copy) {
		this(copy.data);
	}

	public Matrix(final int m, final int n) {
		this.data = new double[m][n];
		this.rows = m;
		this.columns = n;
	}

	public Matrix(final double[] values, final boolean rowVector) {
		this.data = new double[1][values.length];

		for (int i = 0; i < values.length; i++) {
			this.data[0][i] = values[i];
		}

		this.rows = 1;
		this.columns = values.length;

		if (!rowVector) {
			this.data = this.transArray(this.data);
			this.rows = values.length;
			this.columns = 1;
		}
	}

	public final int getRowDimension() {
		return this.rows;
	}

	public final int getColumnDimension() {
		return this.columns;
	}

	public final double get(final int row, final int column) {
		return this.data[row - 1][column - 1];
	}

	public final Matrix getRowVector(final int row) {
		return new Matrix(this.data[row], true);
	}

	public final int getDimension() {
		this.methodNeedsVector();

		if (this.getRowDimension() > this.getColumnDimension()) {
			return this.getRowDimension();
		} else {
			return this.getColumnDimension();
		}
	}

	public final boolean isRowVector() {
		return !(this.getRowDimension() > this.getColumnDimension());
	}

	public final boolean isVector() {
		if ((this.getColumnDimension() == 1) || (this.getRowDimension() == 1)) {
			return true;
		}

		return false;
	}

	public final double get(final int i) {
		this.methodNeedsVector();

		if (this.isRowVector()) {
			return this.get(1, i);
		} else {
			return this.get(i, 1);
		}
	}

	public final void set(final int index, final double value) {
		this.methodNeedsVector();

		if (this.isRowVector()) {
			this.setElement(1, index, value);
		} else {
			this.setElement(index, 1, value);
		}
	}

	public final void setElement(final int row, final int col, final double value) {
		this.data[row - 1][col - 1] = value;
	}

	public final Matrix setColumn(final int col, final double[] values) {
		if (values.length != this.getRowDimension()) {
			throw new RuntimeException("setColumn params length differs from intern length!");
		}

		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColumnDimension(); j++) {
				if (col == j) {
					res[i][j] = values[i];
				} else {
					res[i][j] = this.get(i, j);
				}
			}
		}

		return new Matrix(res);
	}

	public final Matrix setRow(final int row, final double[] values) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColumnDimension(); j++) {
				if (row == i) {
					res[i][j] = values[i];
				} else {
					res[i][j] = this.get(i, j);
				}
			}
		}

		return new Matrix(res);
	}

	public final void setSubRow(final int row, final int from, final Matrix values) {
		if (values.isEmpty()) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < values.getRowDimension(); i++) {
			this.setElement(row, from + i, values.get(i + 1));
		}
	}

	public final void setSubCol(final int col, final int from, final Matrix values) {
		if (values.isEmpty()) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < values.getColumnDimension(); i++) {
			this.setElement(from + i, col, values.get(i + 1));
		}
	}

	public final void setSubCol(final int col, final int from, final int to, final double value) {
		if ((to - from) <= 0) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < (to - from); i++) {
			this.setElement(from + i, col, value);
		}
	}

	public final void setSubRow(final int row, final int from, final int to, final double value) {
		if ((to - from) <= 0) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < (to - from); i++) {
			this.setElement(row, from + i, value);
		}
	}

	public final Matrix getSubRow(final int row, final int from, final int to) {
		final int len = to - from;
		if (len == 0) {
			final double[] bla = new double[1];
			bla[0] = this.get(row, from);
			return new Matrix(bla, true);
		} else if (len < 0) {
			return Matrix.empty(this.getRowDimension(), 0);
		}
		final double[] res = new double[len + 1];

		for (int i = 0; i < res.length; i++) {
			res[i] = this.get(row, from + i);
		}

		return new Matrix(res, true);
	}

	public final Matrix getSubColumn(final int col, final int from, final int to) {
		final int len = to - from;
		if (len == 0) {
			final double[] bla = new double[1];
			bla[0] = this.get(from, col);
			return new Matrix(bla, false);
		} else if (len < 0) {
			return Matrix.empty(0, this.getColumnDimension());
		}

		final double[] res = new double[len + 1];

		for (int i = 0; i < res.length; i++) {
			res[i] = this.get(from + i, col);
		}

		return new Matrix(res, false);
	}

	public final Matrix getSubVector(final int from, final int to) {
		this.methodNeedsVector();

		if (this.isRowVector()) {
			return this.getSubRow(0 + 1, from, to);
		} else {
			return this.getSubColumn(0 + 1, from, to);
		}
	}

	public final double matlabMultiply(final Matrix m) {
		if (!this.isVector() || !m.isVector()) {
			throw new RuntimeException("Both Matrixes need to be Vector!");
		}

		if (this.getColumnDimension() == m.getRowDimension()) {
			final int dimension = this.getColumnDimension();

			int result = 0;
			for (int i = 1; i <= dimension; i++) {
				result += this.get(i) * m.get(i);
			}

			return result;
		} else {
			throw new RuntimeException("Cannot multiplay two Matrixes with not-macthing inenr and outer Dimensions here!");
		}
	}

	public final void divideColumn(final int column, final double divisor) {
		for (int i = 1; i <= this.getRowDimension(); i++) {
			this.setElement(i, column, this.get(i, column) / divisor);
		}
	}

	public final void divideRow(final int row, final double divisor) {
		for (int i = 1; i <= this.getColumnDimension(); i++) {
			this.setElement(row, i, this.get(row, i) / divisor);
		}
	}

	public final Matrix trans() {
		if (this.getRowDimension() == 0) {
			return Matrix.empty(0, this.getColumnDimension());
		}

		if (this.getColumnDimension() == 0) {
			return Matrix.empty(this.getRowDimension(), 0);
		}

		return new Matrix(this.transArray(this.data));
	}

	private double[][] transArray(final double[][] input) {
		// transponieren
		final double[][] res = new double[input[0].length][input.length];

		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColumnDimension(); j++) {
				res[j][i] = input[i][j];
			}
		}

		return res;
	}

	public final Matrix matlabMultiply(final double d) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = this.get(i, j) * d;
			}
		}

		return new Matrix(res);
	}

	public final Matrix substract(final Matrix m) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = this.get(i, j) - m.get(i, j);
			}
		}

		return new Matrix(res);
	}

	public final Matrix add(final Matrix m) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = this.get(i, j) + m.get(i, j);
			}
		}

		return new Matrix(res);
	}

	public final Matrix mod(final int mod) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = this.get(i, j) % mod;
			}
		}

		return new Matrix(res);
	}

	public final Matrix abs() {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = Math.abs(this.get(i, j));
			}
		}

		return new Matrix(res);
	}

	public final Matrix sqrt() {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = Math.sqrt(this.get(i, j));
			}
		}

		return new Matrix(res);

	}

	public final Matrix diag() {
		// v = diag(X,k) for matrix X, returns a column vector v formed from the elements of the kth diagonal of X.
		// v = diag(X) returns the main diagonal of X, same as above with k = 0 .

		final Matrix res = Matrix.zeros(this.getRowDimension(), 1);

		for (int i = 1; i <= this.getRowDimension(); i++) {
			res.set(i, this.get(i, i));
		}

		return res;
	}

	private void methodNeedsVector() {
		if ((this.getRowDimension() > 1) && (this.getColumnDimension() > 1)) {
			throw new RuntimeException("Method needs Vector, not Matrix!");
		}
	}

	public final Matrix max(final double value) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = this.get(i, j) > value ? this.get(i, j) : value;
			}
		}

		return new Matrix(res);
	}

	public final boolean isEmpty() {
		return (this.getRowDimension() == 0) || (this.getColumnDimension() == 0);
	}

	private static Matrix empty(final int m, final int n) {
		if ((m != 0) && (n != 0)) {
			throw new RuntimeException("Tried to create empty Matrix with both Dimesnons != 0");
		}

		return new Matrix(m, n);
	}

	public static Matrix zeros(final int n) {
		final double[] temp = new double[n];

		for (int i = 0; i < n; i++) {
			temp[i] = 0;
		}

		return new Matrix(temp, false);
	}

	public static Matrix zeros(final int m, final int n) {
		final double[][] d = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				d[i][j] = 0;
			}
		}

		// B = zeros(m,n) or B = zeros([m n]) returns an m-by-n matrix of zeros.
		return new Matrix(d);
	}

	public static Matrix rand(final int n) {
		// r = rand(n) returns an n-by-n matrix containing pseudorandom
		// values drawn from the standard uniform distribution on the
		// open interval (0,1). r = rand(m,n) or r = rand([m,n]) returns
		// an m-by-n matrix. r = rand(m,n,p,...) or r = rand([m,n,p,...])
		// returns an m-by-n-by-p-by-... array. r = rand returns a scalar. r = rand(size(A))
		// returns an array the same size as A.
		final double[] temp = new double[n];
		final Random r = new Random();
		for (int i = 0; i < n; i++) {
			temp[i] = r.nextDouble();
		}

		return new Matrix(temp, false);
	}

	public static Matrix primes(final int n) {
		// p = primes(n) returns a row vector of the prime numbers
		// less than or equal to n. A prime number is one that
		// has no factors other than 1 and itself.
		final ArrayList<Integer> prim = Util.generatePrimes(n);
		final double[] primes = new double[prim.size()];
		for (int i = 0; i < prim.size(); i++) {
			primes[i] = prim.get(i);
		}

		return new Matrix(primes, true);
	}

	@Override
	public final String toString() {
		final StringBuffer str = new StringBuffer();

		for (int i = 0; i < this.getRowDimension(); i++) {
			str.append("[");
			for (int k = 0; k < this.getColumnDimension(); k++) {
				str.append("[" + this.data[i][k] + "]");
			}
			str.append("]");
			str.append(System.getProperty("line.separator"));
		}

		return str.toString();
	}

	public final Matrix substract(final double number) {
		final double[][] res = new double[this.getRowDimension()][this.getColumnDimension()];

		for (int i = 1; i <= this.getRowDimension(); i++) {
			for (int j = 1; j <= this.getColumnDimension(); j++) {
				res[i - 1][j - 1] = this.get(i, j) - number;
			}
		}

		return new Matrix(res);
	}
}
