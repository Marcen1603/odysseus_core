/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.tooling.mep.command;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public final class ParameterGenerator {
	/**
	 * 
	 * @param function
	 * @return
	 */
	public static Object[][] getFunctionValues(final IFunction<?> function) {
		final int num = (int) (Math.pow(2.0, function.getArity()) * 5);

		if (function.getArity() == 0) {
			final Object[][] values = new Object[num * 5][];
			Arrays.fill(values, new Object[] { new Double(1.0) });
			return values;
		}
		final Object[][] values = new Object[num - 5][];
		int testCase = -1;
		for (int n = 0; n < num; n++) {
			final int pos = n % (int) (Math.pow(2.0, function.getArity()));
			if (pos == 0) {
				testCase++;
			} else {
				final Object[] value = new Object[function.getArity()];
				for (int i = 0; i < function.getArity(); i++) {
					final List<SDFDatatype> types = Arrays.asList(function.getAcceptedTypes(i));
					if (types.contains(SDFDatatype.BOOLEAN)) {
						value[i] = generateBoolean(n, i, testCase);
					} else if (types.contains(SDFDatatype.BYTE)) {
						value[i] = generateByte(n, i, testCase);
					} else if (types.contains(SDFDatatype.SHORT)) {
						value[i] = generateShort(n, i, testCase);
					} else if (types.contains(SDFDatatype.INTEGER)) {
						value[i] = generateInteger(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LONG)) || (types.contains(SDFDatatype.TIMESTAMP))
							|| (types.contains(SDFDatatype.START_TIMESTAMP))
							|| (types.contains(SDFDatatype.END_TIMESTAMP))) {
						value[i] = generateLong(n, i, testCase);
					} else if (types.contains(SDFDatatype.FLOAT)) {
						value[i] = generateFloat(n, i, testCase);
					} else if (types.contains(SDFDatatype.DOUBLE)) {
						value[i] = generateDouble(n, i, testCase);
					} else if (types.contains(SDFDatatype.STRING)) {
						value[i] = generateString(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_BOOLEAN)) {
						value[i] = generateBooleanMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_BYTE)) {
						value[i] = generateByteMatrix(n, i, testCase);
						// } else if (types.contains(SDFDatatype.MATRIX_SHORT))
						// {
						// value[i] = generateShortMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_INTEGER)) {
						value[i] = generateIntegerMatrix(n, i, testCase);
						// } else if (types.contains(SDFDatatype.MATRIX_LONG)) {
						// value[i] = generateLongMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_FLOAT)) {
						value[i] = generateFloatMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_DOUBLE)) {
						value[i] = generateDoubleMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_BOOLEAN)) {
						value[i] = generateBooleanVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_BYTE)) {
						value[i] = generateByteVector(n, i, testCase);
						// } else if (types.contains(SDFDatatype.VECTOR_SHORT))
						// {
						// value[i] = generateShortVector(n, i, testCase);
						// } else if
						// (types.contains(SDFDatatype.VECTOR_INTEGER)) {
						// value[i] = generateIntegerVector(n, i, testCase);
						// } else if (types.contains(SDFDatatype.VECTOR_LONG)) {
						// value[i] = generateLongVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_FLOAT)) {
						value[i] = generateFloatVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_DOUBLE)) {
						value[i] = generateDoubleVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.DATE)) {
						value[i] = generateDate(n, i, testCase);
					} else if (types.contains(SDFDatatype.BITVECTOR)) {
						value[i] = generateBitVector(n, i, testCase);
					} else {
						value[i] = null;
					}

				}
				values[n - (int) (1 + (n / (Math.pow(2, function.getArity()))))] = value;
			}
		}
		return values;
	}

	private static Object generateBoolean(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Boolean(Boolean.TRUE);
			} else if (testCase == 1) {
				return new Boolean(Boolean.FALSE);
			} else if (testCase == 2) {
				return new Boolean(Boolean.TRUE);
			} else if (testCase == 3) {
				return new Boolean(Boolean.FALSE);
			} else if (testCase == 4) {
				return new Boolean(false);
			}
		} else {
			return new Boolean(true);
		}
		return null;
	}

	private static Object generateByte(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Byte(Byte.MAX_VALUE);
			} else if (testCase == 1) {
				return new Byte(Byte.MIN_VALUE);
			} else if (testCase == 2) {
				return new Byte(Byte.MAX_VALUE);
			} else if (testCase == 3) {
				return new Byte(Byte.MIN_VALUE);
			} else if (testCase == 4) {
				return new Byte((byte) 0);
			}
		} else {
			return new Byte((byte) 1);
		}
		return null;
	}

	private static Object generateShort(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Short(Short.MAX_VALUE);
			} else if (testCase == 1) {
				return new Short(Short.MIN_VALUE);
			} else if (testCase == 2) {
				return new Short(Short.MAX_VALUE);
			} else if (testCase == 3) {
				return new Short(Short.MIN_VALUE);
			} else if (testCase == 4) {
				return new Short((short) 0);
			}
		} else {
			return new Short((short) 1);
		}
		return null;
	}

	private static Object generateInteger(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Integer(Integer.MAX_VALUE);
			} else if (testCase == 1) {
				return new Integer(Integer.MIN_VALUE);
			} else if (testCase == 2) {
				return new Integer(-Integer.MAX_VALUE);
			} else if (testCase == 3) {
				return new Integer(-Integer.MIN_VALUE);
			} else if (testCase == 4) {
				return new Integer(0);
			}
		} else {
			return new Integer(1);
		}
		return null;
	}

	private static Object generateLong(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Long(Long.MAX_VALUE);
			} else if (testCase == 1) {
				return new Long(Long.MIN_VALUE);
			} else if (testCase == 2) {
				return new Long(-Long.MAX_VALUE);
			} else if (testCase == 3) {
				return new Long(-Long.MIN_VALUE);
			} else if (testCase == 4) {
				return new Long(0l);
			}
		} else {
			return new Long(1l);
		}
		return null;
	}

	private static Object generateFloat(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Float(Float.MAX_VALUE);
			} else if (testCase == 1) {
				return new Float(Float.MIN_VALUE);
			} else if (testCase == 2) {
				return new Float(-Float.MAX_VALUE);
			} else if (testCase == 3) {
				return new Float(-Float.MIN_VALUE);
			} else if (testCase == 4) {
				return new Float(0.0);
			}
		} else {
			return new Float(1.0);
		}
		return null;
	}

	private static Object generateDouble(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Double(Double.MAX_VALUE);
			} else if (testCase == 1) {
				return new Double(Double.MIN_VALUE);
			} else if (testCase == 2) {
				return new Double(-Double.MAX_VALUE);
			} else if (testCase == 3) {
				return new Double(-Double.MIN_VALUE);
			} else if (testCase == 4) {
				return new Double(0.0);
			}
		} else {
			return new Double(1.0);
		}
		return null;
	}

	private static Object generateString(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return "\"abcdefghijklmnopqrstuvwxyz'-+_=#$%&*()[]{}~<>?.!:/\"".toUpperCase(); //$NON-NLS-1$
			} else if (testCase == 1) {
				return "\"abcdefghijklmnopqrstuvwxyz'-+_=#$%&*()[]{}~<>?.!:/\""; //$NON-NLS-1$
			} else if (testCase == 2) {
				return "\"\""; //$NON-NLS-1$
			} else if (testCase == 3) {
				return "\" \""; //$NON-NLS-1$
			} else if (testCase == 4) {
				return null;
			}
		} else {
			return "\"abc\""; //$NON-NLS-1$
		}
		return null;
	}

	private static Object generateDate(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Date(Long.MAX_VALUE);
			} else if (testCase == 1) {
				return new Date(1);
			} else if (testCase == 2) {
				return new Date(Long.MIN_VALUE);
			} else if (testCase == 3) {
				return new Date(-1);
			} else if (testCase == 4) {
				return new Date(0);
			}
		} else {
			return new Date(1);
		}
		return null;
	}

	private static Object generateBitVector(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return BitVector.createBitVector(new byte[] { Byte.MAX_VALUE });
			} else if (testCase == 1) {
				return BitVector.createBitVector(new byte[] { (byte) 1 });
			} else if (testCase == 2) {
				return BitVector.createBitVector(new byte[] { Byte.MIN_VALUE });
			} else if (testCase == 3) {
				return BitVector.createBitVector(new byte[] { (byte) -1 });
			} else if (testCase == 4) {
				return BitVector.createBitVector(new byte[] { (byte) 0 });
			}
		} else {
			return BitVector.createBitVector(new byte[] { (byte) 1 });
		}
		return null;
	}

	private static Object generateBooleanVector(int n, int i, int testCase) {
		boolean[] value = new boolean[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Boolean.TRUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Boolean.FALSE);
			} else if (testCase == 2) {
				Arrays.fill((value), Boolean.TRUE);
			} else if (testCase == 3) {
				Arrays.fill((value), Boolean.FALSE);
			} else if (testCase == 4) {
				Arrays.fill((value), false);
			}
		} else {
			Arrays.fill((value), true);
		}
		return value;
	}

	private static Object generateByteVector(int n, int i, int testCase) {
		byte[] value = new byte[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Byte.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Byte.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill((value), Byte.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill((value), Byte.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill((value), (byte) 0);
			}
		} else {
			Arrays.fill((value), (byte) 1);
		}
		return value;
	}

	private static Object generateShortVector(int n, int i, int testCase) {
		short[] value = new short[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Short.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Short.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill((value), Short.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill((value), Short.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill((value), (short) 0);
			}
		} else {
			Arrays.fill((value), (short) 1);
		}
		return value;
	}

	private static Object generateIntegerVector(int n, int i, int testCase) {
		int[] value = new int[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Integer.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Integer.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill((value), -Integer.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill((value), -Integer.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill((value), 0);
			}
		} else {
			Arrays.fill((value), 1);
		}
		return value;
	}

	private static Object generateLongVector(int n, int i, int testCase) {
		long[] value = new long[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Long.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Long.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill((value), -Long.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill((value), -Long.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill((value), 0);
			}
		} else {
			Arrays.fill((value), 1l);
		}
		return value;
	}

	private static Object generateFloatVector(int n, int i, int testCase) {
		float[] value = new float[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Float.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Float.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill((value), -Float.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill((value), -Float.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill((value), 0.0f);
			}
		} else {
			Arrays.fill((value), 1.0f);
		}
		return value;
	}

	private static Object generateDoubleVector(int n, int i, int testCase) {
		double[] value = new double[2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Double.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Double.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill((value), -Double.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill((value), -Double.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill((value), 0.0);
			}
		} else {
			Arrays.fill((value), 1.0);
		}
		return value;
	}

	private static Object generateBooleanMatrix(int n, int i, int testCase) {
		boolean[][] value = new boolean[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Boolean.TRUE);
				Arrays.fill(value[1], Boolean.TRUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Boolean.FALSE);
				Arrays.fill(value[1], Boolean.FALSE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], true);
				Arrays.fill(value[1], true);
			} else if (testCase == 3) {
				Arrays.fill(value[0], false);
				Arrays.fill(value[1], false);
			} else if (testCase == 4) {
				Arrays.fill(value[0], false);
				Arrays.fill(value[1], false);
			}
		} else {
			Arrays.fill(value[0], true);
			Arrays.fill(value[1], true);
		}
		return value;
	}

	private static Object generateByteMatrix(int n, int i, int testCase) {
		byte[][] value = new byte[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Byte.MAX_VALUE);
				Arrays.fill(value[1], Byte.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Byte.MIN_VALUE);
				Arrays.fill(value[1], Byte.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], (byte) -1);
				Arrays.fill(value[1], (byte) -1);
			} else if (testCase == 3) {
				Arrays.fill(value[0], (byte) 0);
				Arrays.fill(value[1], (byte) 0);
			} else if (testCase == 4) {
				Arrays.fill(value[0], (byte) 0);
				Arrays.fill(value[1], (byte) 0);
			}
		} else {
			Arrays.fill(value[0], (byte) 1);
			Arrays.fill(value[1], (byte) 1);
		}
		return value;
	}

	private static Object generateShortMatrix(int n, int i, int testCase) {
		short[][] value = new short[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Short.MAX_VALUE);
				Arrays.fill(value[1], Short.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Short.MIN_VALUE);
				Arrays.fill(value[1], Short.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], Short.MAX_VALUE);
				Arrays.fill(value[1], Short.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill(value[0], Short.MIN_VALUE);
				Arrays.fill(value[1], Short.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill(value[0], (short) 0);
				Arrays.fill(value[1], (short) 0);
			}
		} else {
			Arrays.fill(value[0], (short) 1);
			Arrays.fill(value[1], (short) 1);
		}
		return value;
	}

	private static Object generateIntegerMatrix(int n, int i, int testCase) {
		int[][] value = new int[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Integer.MAX_VALUE);
				Arrays.fill(value[1], Integer.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Integer.MIN_VALUE);
				Arrays.fill(value[1], Integer.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], -Integer.MAX_VALUE);
				Arrays.fill(value[1], -Integer.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill(value[0], -Integer.MIN_VALUE);
				Arrays.fill(value[1], -Integer.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill(value[0], 0);
				Arrays.fill(value[1], 0);
			}
		} else {
			Arrays.fill(value[0], 1);
			Arrays.fill(value[1], 1);
		}
		return value;
	}

	private static Object generateLongMatrix(int n, int i, int testCase) {
		long[][] value = new long[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Long.MAX_VALUE);
				Arrays.fill(value[1], Long.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Long.MIN_VALUE);
				Arrays.fill(value[1], Long.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], -Long.MAX_VALUE);
				Arrays.fill(value[1], -Long.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill(value[0], -Long.MIN_VALUE);
				Arrays.fill(value[1], -Long.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill(value[0], 0l);
				Arrays.fill(value[1], 0l);
			}
		} else {
			Arrays.fill(value[0], 1l);
			Arrays.fill(value[1], 1l);
		}
		return value;
	}

	private static Object generateFloatMatrix(int n, int i, int testCase) {
		float[][] value = new float[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Float.MAX_VALUE);
				Arrays.fill(value[1], Float.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Float.MIN_VALUE);
				Arrays.fill(value[1], Float.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], -Float.MAX_VALUE);
				Arrays.fill(value[1], -Float.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill(value[0], -Float.MIN_VALUE);
				Arrays.fill(value[1], -Float.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill(value[0], 0.0f);
				Arrays.fill(value[1], 0.0f);
			}
		} else {
			Arrays.fill(value[0], 1.0f);
			Arrays.fill(value[1], 1.0f);
		}
		return value;
	}

	private static Object generateDoubleMatrix(int n, int i, int testCase) {
		double[][] value = new double[2][2];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Double.MAX_VALUE);
				Arrays.fill(value[1], Double.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Double.MIN_VALUE);
				Arrays.fill(value[1], Double.MIN_VALUE);
			} else if (testCase == 2) {
				Arrays.fill(value[0], -Double.MAX_VALUE);
				Arrays.fill(value[1], -Double.MAX_VALUE);
			} else if (testCase == 3) {
				Arrays.fill(value[0], -Double.MIN_VALUE);
				Arrays.fill(value[1], -Double.MIN_VALUE);
			} else if (testCase == 4) {
				Arrays.fill(value[0], 0.0);
				Arrays.fill(value[1], 0.0);
			}
		} else {
			Arrays.fill(value[0], 1.0);
			Arrays.fill(value[1], 1.0);
		}
		return value;
	}

	private ParameterGenerator() {
	}
}
