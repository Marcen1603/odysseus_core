package de.uniol.inf.is.odysseus.probabilistic.math.genz;

public class Matrix {
	private double[][] data;
	private int rows;
	private int columns;
	private int offsetIndexRow = 1;
	private int offsetIndexCol = 1;

	public Matrix(double[][] values) {
		data = new double[values.length][values[0].length];

		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++)
				data[i][j] = values[i][j];

		rows = values.length;
		columns = values[0].length;
	}

	public Matrix(Matrix copy) {
		this(copy.data);
	}

	public Matrix(int m, int n) {
		data = new double[m][n];
		rows = m;
		columns = n;
	}

	public Matrix(double[] values, boolean rowVector) {
		data = new double[1][values.length];

		for (int i = 0; i < values.length; i++)
			data[0][i] = values[i];

		rows = 1;
		columns = values.length;

		if (!rowVector) {
			this.data = transArray(this.data);
			rows = values.length;
			columns = 1;
		}
	}

	public int getRowDimension() {
		return rows;
	}

	public int getColumnDimension() {
		return columns;
	}

	public double get(int row, int column) {
		return data[row - 1][column - 1];
	}

	public Matrix getRowVector(int row) {
		return new Matrix(data[row], true);
	}

	public int getDimension() {
		methodNeedsVector();

		if (getRowDimension() > getColumnDimension()) {
			return getRowDimension();
		} else {
			return getColumnDimension();
		}
	}

	public boolean isRowVector() {
		if (getRowDimension() > getColumnDimension()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isVector() {
		if (getColumnDimension() == 1 || getRowDimension() == 1) {
			return true;
		}

		return false;
	}

	public double get(int i) {
		methodNeedsVector();

		if (isRowVector()) {
			return get(1, i);
		} else {
			return get(i, 1);
		}
	}

	public void set(int index, double value) {
		methodNeedsVector();

		// boolean bla = isRowVector();

		if (isRowVector()) {
			setElement(1, index, value);
		} else {
			setElement(index, 1, value);
		}
	}

	public void setElement(int row, int col, double value) {
		data[row - 1][col - 1] = value;
	}

	public Matrix setColumn(int col, double[] values) {
		if (values.length != getRowDimension()) {
			throw new RuntimeException(
					"setColumn params length differs from intern length!");
		}

		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 0; i < getRowDimension(); i++) {
			for (int j = 0; j < getColumnDimension(); j++) {
				if (col == j) {
					res[i][j] = values[i];
				} else {
					res[i][j] = get(i, j);
				}
			}
		}

		return new Matrix(res);
	}

	public Matrix setRow(int row, double[] values) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 0; i < getRowDimension(); i++) {
			for (int j = 0; j < getColumnDimension(); j++) {
				if (row == i) {
					res[i][j] = values[i];
				} else {
					res[i][j] = get(i, j);
				}
			}
		}

		return new Matrix(res);
	}

	public void setSubRow(int row, int from, Matrix values) {
		if (values.isEmpty()) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < values.getRowDimension(); i++) {
			setElement(row, from + i, values.get(i + offsetIndexCol));
		}
	}

	public void setSubCol(int col, int from, Matrix values) {
		if (values.isEmpty()) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < values.getColumnDimension(); i++) {
			setElement(from + i, col, values.get(i + offsetIndexRow));
		}
	}

	public void setSubCol(int col, int from, int to, int value) {
		if ((to - from) <= 0) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < to - from; i++) {
			setElement(from + i, col, value);
		}
	}

	public void setSubRow(int row, int from, int to, int value) {
		if ((to - from) <= 0) {
			// ignore to copy matlab functionality
			return;
		}

		for (int i = 0; i < to - from; i++) {
			setElement(row, from + i, value);
		}
	}

	public Matrix getSubRow(int row, int from, int to) {
		int len = to - from;
		if (len == 0) {
			double[] bla = new double[1];
			bla[0] = get(row, from);
			return new Matrix(bla, true);
		} else if (len < 0) {
			return Matrix.empty(getRowDimension(), 0);
		}
		double[] res = new double[len + 1];

		for (int i = 0; i < res.length; i++) {
			res[i] = get(row, from + i);
		}

		return new Matrix(res, true);
	}

	public Matrix getSubColumn(int col, int from, int to) {
		int len = to - from;
		if (len == 0) {
			double[] bla = new double[1];
			bla[0] = get(from, col);
			return new Matrix(bla, false);
		} else if (len < 0) {
			return Matrix.empty(0, getColumnDimension());
		}

		double[] res = new double[len + 1];

		for (int i = 0; i < res.length; i++) {
			res[i] = get(from + i, col);
		}

		return new Matrix(res, false);
	}

	public Matrix getSubVector(int from, int to) {
		methodNeedsVector();

		if (isRowVector()) {
			return getSubRow(0 + offsetIndexRow, from, to);
		} else {
			return getSubColumn(0 + offsetIndexCol, from, to);
		}
	}

	public double matlabMultiply(Matrix m) {
		if (!isVector() || !m.isVector()) {
			throw new RuntimeException("Both Matrixes need to be Vector!");
		}

		if (this.getColumnDimension() == m.getRowDimension()) {
			int dimension = getColumnDimension();

			int result = 0;
			for (int i = 1; i <= dimension; i++) {
				result += get(i) * m.get(i);
			}

			return result;
		} else {
			throw new RuntimeException(
					"Cannot multiplay two Matrixes with not-macthing inenr and outer Dimensions here!");
		}
	}

	public void divideColumn(int column, double divisor) {
		for (int i = 1; i <= getRowDimension(); i++) {
			setElement(i, column, get(i, column) / divisor);
		}
	}

	public void divideRow(int row, double divisor) {
		for (int i = 1; i <= getColumnDimension(); i++) {
			setElement(row, i, get(row, i) / divisor);
		}
	}

	public Matrix trans() {
		if (getRowDimension() == 0) {
			return Matrix.empty(0, getColumnDimension());
		}

		if (getColumnDimension() == 0) {
			return Matrix.empty(getRowDimension(), 0);
		}

		return new Matrix(transArray(this.data));
	}

	private double[][] transArray(double[][] input) {
		// transponieren
		double[][] res = new double[input[0].length][input.length];

		for (int i = 0; i < getRowDimension(); i++) {
			for (int j = 0; j < getColumnDimension(); j++) {
				res[j][i] = input[i][j];
			}
		}

		return res;
	}

	public Matrix matlabMultiply(double d) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = get(i, j) * d;
			}
		}

		return new Matrix(res);
	}

	public Matrix substract(Matrix m) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = get(i, j) - m.get(i, j);
			}
		}

		return new Matrix(res);
	}

	public Matrix add(Matrix m) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = get(i, j) + m.get(i, j);
			}
		}

		return new Matrix(res);
	}

	public Matrix mod(int mod) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = get(i, j) % mod;
			}
		}

		return new Matrix(res);
	}

	public Matrix abs() {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = Math.abs(get(i, j));
			}
		}

		return new Matrix(res);
	}

	public Matrix sqrt() {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = Math.sqrt(get(i, j));
			}
		}

		return new Matrix(res);

	}

	public Matrix diag() {
		// v = diag(X,k) for matrix X, returns a column vector v formed from the
		// elements of the kth diagonal of X.
		// v = diag(X) returns the main diagonal of X, same as above with k = 0
		// .

		Matrix res = Matrix.zeros(getRowDimension(), 1);

		for (int i = 1; i <= getRowDimension(); i++) {
			res.set(i, get(i, i));
		}

		return res;
	}

	private void methodNeedsVector() {
		if (getRowDimension() > 1 && getColumnDimension() > 1) {
			throw new RuntimeException("Method needs Vector, not Matrix!");
		}
	}

	public Matrix max(int value) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = get(i, j) > value ? get(i, j) : value;
			}
		}

		return new Matrix(res);
	}

	public boolean isEmpty() {
		if (getRowDimension() == 0 || getColumnDimension() == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static Matrix empty(int m, int n) {
		if (m != 0 && n != 0) {
			throw new RuntimeException(
					"Tried to create empty Matrix with both Dimesnons != 0");
		}

		return new Matrix(m, n);
	}

	public static Matrix zeros(int n) {
		double[] temp = new double[n];

		for (int i = 0; i < n; i++) {
			temp[i] = 0;
		}

		return new Matrix(temp, false);
	}

	public static Matrix zeros(int m, int n) {
		double[][] d = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				d[i][j] = 0;
			}
		}

		// B = zeros(m,n) or B = zeros([m n]) returns an m-by-n matrix of zeros.
		return new Matrix(d);
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < getRowDimension(); i++) {
			str.append("[");
			for (int k = 0; k < getColumnDimension(); k++) {
				str.append("[" + data[i][k] + "]");
			}
			str.append("]");
			str.append(System.getProperty("line.separator"));
		}

		return str.toString();
	}

	public Matrix substract(int number) {
		double[][] res = new double[getRowDimension()][getColumnDimension()];

		for (int i = 1; i <= getRowDimension(); i++) {
			for (int j = 1; j <= getColumnDimension(); j++) {
				res[i - 1][j - 1] = get(i, j) - number;
			}
		}

		return new Matrix(res);
	}
}
