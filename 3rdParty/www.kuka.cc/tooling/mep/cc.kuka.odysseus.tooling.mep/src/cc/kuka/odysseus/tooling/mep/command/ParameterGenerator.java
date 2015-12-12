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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	 * 1. testcase -> positive max value
	 * 
	 * 2. testcase -> positive min value
	 * 
	 * 3. testcase -> 0 or empty string or false
	 * 
	 * 4. testcase -> average positive value (positive max - positive min)/2.0
	 * 
	 * 5. testcase -> average negative value (negative max - negative min)/2.0
	 * 
	 * 6. testcase -> null
	 * 
	 * 7. testcase -> negative min value
	 * 
	 * 8. testcase -> negative max value
	 * 
	 * 
	 * In case of matrix or list, 3. testcase is an empty list/array
	 * 
	 * @param function
	 * @return
	 */
	public static Object[][] getFunctionValues(final IFunction<?> function) {
		final int num = (int) (Math.pow(2.0, function.getArity()) * 8);

		if (function.getArity() == 0) {
			final Object[][] values = new Object[num * 8][];
			Arrays.fill(values, new Object[] { new Double(1.0) });
			return values;
		}
		final Object[][] values = new Object[num - 8][];
		int testCase = -1;
		for (int n = 0; n < num; n++) {
			final int pos = n % (int) (Math.pow(2.0, function.getArity()));
			if (pos == 0) {
				testCase++;
			} else {
				final Object[] value = new Object[function.getArity()];
				for (int i = 0; i < function.getArity(); i++) {
					final List<SDFDatatype> types = Arrays.asList(function.getAcceptedTypes(i));
					if (types.contains(SDFDatatype.CHAR)) {
						value[i] = generateCharacter(n, i, testCase);
					} else if (types.contains(SDFDatatype.BOOLEAN)) {
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
					} else if (types.contains(SDFDatatype.MATRIX_SHORT)) {
						value[i] = generateShortMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_INTEGER)) {
						value[i] = generateIntegerMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_LONG)) {
						value[i] = generateLongMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_FLOAT)) {
						value[i] = generateFloatMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.MATRIX_DOUBLE)) {
						value[i] = generateDoubleMatrix(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_BOOLEAN)) {
						value[i] = generateBooleanVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_BYTE)) {
						value[i] = generateByteVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_SHORT)) {
						value[i] = generateShortVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_INTEGER)) {
						value[i] = generateIntegerVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_LONG)) {
						value[i] = generateLongVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_FLOAT)) {
						value[i] = generateFloatVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.VECTOR_DOUBLE)) {
						value[i] = generateDoubleVector(n, i, testCase);
					} else if (types.contains(SDFDatatype.DATE)) {
						value[i] = generateDate(n, i, testCase);
					} else if (types.contains(SDFDatatype.BITVECTOR)) {
						value[i] = generateBitVector(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_BOOLEAN)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateBooleanList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_BYTE)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateByteList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_SHORT)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateShortList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_INTEGER)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateIntegerList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_LONG)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateLongList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_FLOAT)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateFloatList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_DOUBLE)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateDoubleList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_STRING)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateStringList(n, i, testCase);
					} else if ((types.contains(SDFDatatype.LIST_DATE)) || (types.contains(SDFDatatype.LIST))) {
						value[i] = generateDateList(n, i, testCase);
					} else {
						value[i] = null;
					}

				}
				values[n - (int) (1 + (n / (Math.pow(2, function.getArity()))))] = value;
			}
		}
		return values;
	}

	private static Object generateCharacter(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Character(Character.MAX_VALUE);
			} else if (testCase == 1) {
				return new Character(Character.MIN_VALUE);
			} else if (testCase == 2) {
				return new Character(' ');
			} else if (testCase == 3) {
				return new Character((char) ((Character.MAX_VALUE - Character.MIN_VALUE) / 2));
			} else if (testCase == 4) {
				return null;
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateBoolean(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Boolean(true);
			} else if (testCase == 1) {
				return new Boolean(false);
			} else if (testCase == 2) {
				return null;
			} else if (testCase == 3) {
				return null;
			} else if (testCase == 4) {
				return null;
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateByte(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Byte(Byte.MAX_VALUE);
			} else if (testCase == 1) {
				return new Byte((byte) 0);
			} else if (testCase == 2) {
				return new Byte((byte) 0);
			} else if (testCase == 3) {
				return new Byte((byte) ((Byte.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				return new Byte((byte) ((Byte.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return new Byte((byte) 0);
			} else if (testCase == 7) {
				return new Byte(Byte.MIN_VALUE);
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateShort(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Short(Short.MAX_VALUE);
			} else if (testCase == 1) {
				return new Short((short) 0);
			} else if (testCase == 2) {
				return new Short((short) 0);
			} else if (testCase == 3) {
				return new Short((short) ((Short.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				return new Short((short) ((Short.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return new Short((short) 0);
			} else if (testCase == 7) {
				return new Short(Short.MIN_VALUE);
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateInteger(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Integer(Integer.MAX_VALUE);
			} else if (testCase == 1) {
				return new Integer(0);
			} else if (testCase == 2) {
				return new Integer(0);
			} else if (testCase == 3) {
				return new Integer(((Integer.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				return new Integer(((Integer.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return new Integer(0);
			} else if (testCase == 7) {
				return new Integer(Integer.MIN_VALUE);
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateLong(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Long(Long.MAX_VALUE);
			} else if (testCase == 1) {
				return new Long(0);
			} else if (testCase == 2) {
				return new Long(0);
			} else if (testCase == 3) {
				return new Long(((Long.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				return new Long(((Long.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return new Long(0);
			} else if (testCase == 7) {
				return new Long(Long.MIN_VALUE);
			}
		} else {
			return null;
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
				return new Float(0f);
			} else if (testCase == 3) {
				return new Float(((Float.MAX_VALUE - Float.MIN_VALUE) / 2f));
			} else if (testCase == 4) {
				return new Float(((-Float.MAX_VALUE + Float.MIN_VALUE) / 2f));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return new Float(-Float.MIN_VALUE);
			} else if (testCase == 7) {
				return new Float(-Float.MAX_VALUE);
			}
		} else {
			return null;
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
				return new Double(0.0);
			} else if (testCase == 3) {
				return new Double(((Double.MAX_VALUE - Double.MIN_VALUE) / 2.0));
			} else if (testCase == 4) {
				return new Double(((-Double.MAX_VALUE + Double.MIN_VALUE) / 2.0));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return new Double(-Double.MIN_VALUE);
			} else if (testCase == 7) {
				return new Double(-Double.MAX_VALUE);
			}
		} else {
			return null;
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
				return ""; //$NON-NLS-1$
			} else if (testCase == 3) {
				return null;
			} else if (testCase == 4) {
				return null;
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateDate(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return new Date(Long.MAX_VALUE);
			} else if (testCase == 1) {
				return new Date(Long.MIN_VALUE);
			} else if (testCase == 2) {
				return new Date(0l);
			} else if (testCase == 3) {
				return null;
			} else if (testCase == 4) {
				return null;
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateBitVector(int n, int i, int testCase) {
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				return BitVector.createBitVector(new byte[] { Byte.MAX_VALUE });
			} else if (testCase == 1) {
				return BitVector.createBitVector(new byte[] { 0 });
			} else if (testCase == 2) {
				return BitVector.createBitVector(new byte[] {});
			} else if (testCase == 3) {
				return BitVector.createBitVector(new byte[] { (byte) ((Byte.MAX_VALUE) / 2) });
			} else if (testCase == 4) {
				return BitVector.createBitVector(new byte[] { (byte) ((Byte.MIN_VALUE) / 2) });
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return BitVector.createBitVector(new byte[] { 0 });
			} else if (testCase == 7) {
				return BitVector.createBitVector(new byte[] { Byte.MIN_VALUE });
			}
		} else {
			return null;
		}
		return null;
	}

	private static Object generateBooleanVector(int n, int i, int testCase) {
		boolean[] value = new boolean[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), true);
			} else if (testCase == 1) {
				Arrays.fill((value), false);
			} else if (testCase == 2) {
				value = new boolean[0];
			} else if (testCase == 3) {
				return null;
			} else if (testCase == 4) {
				return null;
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateByteVector(int n, int i, int testCase) {
		byte[] value = new byte[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Byte.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), (byte) 0);
			} else if (testCase == 2) {
				value = new byte[0];
			} else if (testCase == 3) {
				Arrays.fill((value), (byte) ((Byte.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				Arrays.fill((value), (byte) ((Byte.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill((value), (byte) 0);
			} else if (testCase == 7) {
				Arrays.fill((value), Byte.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateShortVector(int n, int i, int testCase) {
		short[] value = new short[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Short.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), (short) 0);
			} else if (testCase == 2) {
				value = new short[0];
			} else if (testCase == 3) {
				Arrays.fill((value), (short) ((Short.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				Arrays.fill((value), (short) ((Short.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill((value), (short) 0);
			} else if (testCase == 7) {
				Arrays.fill((value), Short.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateIntegerVector(int n, int i, int testCase) {
		int[] value = new int[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Integer.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), 0);
			} else if (testCase == 2) {
				value = new int[0];
			} else if (testCase == 3) {
				Arrays.fill((value), (Integer.MAX_VALUE) / 2);
			} else if (testCase == 4) {
				Arrays.fill((value), (Integer.MIN_VALUE) / 2);
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill((value), 0);
			} else if (testCase == 7) {
				Arrays.fill((value), Integer.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateLongVector(int n, int i, int testCase) {
		long[] value = new long[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Long.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), 0l);
			} else if (testCase == 2) {
				value = new long[0];
			} else if (testCase == 3) {
				Arrays.fill((value), (Long.MAX_VALUE) / 2);
			} else if (testCase == 4) {
				Arrays.fill((value), (Long.MIN_VALUE) / 2);
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill((value), 0l);
			} else if (testCase == 7) {
				Arrays.fill((value), Long.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateFloatVector(int n, int i, int testCase) {
		float[] value = new float[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Float.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Float.MIN_VALUE);
			} else if (testCase == 2) {
				value = new float[0];
			} else if (testCase == 3) {
				Arrays.fill((value), (Float.MAX_VALUE - Float.MIN_VALUE) / 2f);
			} else if (testCase == 4) {
				Arrays.fill((value), (-(Float.MAX_VALUE - Float.MIN_VALUE) / 2f));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill((value), -Float.MIN_VALUE);
			} else if (testCase == 7) {
				Arrays.fill((value), -Float.MAX_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateDoubleVector(int n, int i, int testCase) {
		double[] value = new double[100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill((value), Double.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill((value), Double.MIN_VALUE);
			} else if (testCase == 2) {
				value = new double[0];
			} else if (testCase == 3) {
				Arrays.fill((value), (Double.MAX_VALUE - Double.MIN_VALUE) / 2.0);
			} else if (testCase == 4) {
				Arrays.fill((value), (-(Double.MAX_VALUE - Double.MIN_VALUE) / 2.0));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill((value), -Double.MIN_VALUE);
			} else if (testCase == 7) {
				Arrays.fill((value), -Double.MAX_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateBooleanMatrix(int n, int i, int testCase) {
		boolean[][] value = new boolean[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], true);
				Arrays.fill(value[1], true);
			} else if (testCase == 1) {
				Arrays.fill(value[0], false);
				Arrays.fill(value[1], false);
			} else if (testCase == 2) {
				value = new boolean[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], true);
				Arrays.fill(value[1], false);
			} else if (testCase == 4) {
				Arrays.fill(value[0], false);
				Arrays.fill(value[1], true);
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateByteMatrix(int n, int i, int testCase) {
		byte[][] value = new byte[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Byte.MAX_VALUE);
				Arrays.fill(value[1], Byte.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], (byte) 0);
				Arrays.fill(value[1], (byte) 0);
			} else if (testCase == 2) {
				value = new byte[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], (byte) ((Byte.MAX_VALUE) / 2));
				Arrays.fill(value[1], (byte) ((Byte.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				Arrays.fill(value[0], (byte) ((Byte.MIN_VALUE) / 2));
				Arrays.fill(value[1], (byte) ((Byte.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill(value[0], (byte) 0);
				Arrays.fill(value[1], (byte) 0);
			} else if (testCase == 7) {
				Arrays.fill(value[0], Byte.MIN_VALUE);
				Arrays.fill(value[1], Byte.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateShortMatrix(int n, int i, int testCase) {
		short[][] value = new short[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Short.MAX_VALUE);
				Arrays.fill(value[1], Short.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], (short) 0);
				Arrays.fill(value[1], (short) 0);
			} else if (testCase == 2) {
				value = new short[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], (short) ((Short.MAX_VALUE) / 2));
				Arrays.fill(value[1], (short) ((Short.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				Arrays.fill(value[0], (short) ((Short.MIN_VALUE) / 2));
				Arrays.fill(value[1], (short) ((Short.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill(value[0], (short) 0);
				Arrays.fill(value[1], (short) 0);
			} else if (testCase == 7) {
				Arrays.fill(value[0], Short.MIN_VALUE);
				Arrays.fill(value[1], Short.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateIntegerMatrix(int n, int i, int testCase) {
		int[][] value = new int[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Integer.MAX_VALUE);
				Arrays.fill(value[1], Integer.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], 0);
				Arrays.fill(value[1], 0);
			} else if (testCase == 2) {
				value = new int[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], ((Integer.MAX_VALUE) / 2));
				Arrays.fill(value[1], ((Integer.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				Arrays.fill(value[0], ((Integer.MIN_VALUE) / 2));
				Arrays.fill(value[1], ((Integer.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill(value[0], 0);
				Arrays.fill(value[1], 0);
			} else if (testCase == 7) {
				Arrays.fill(value[0], Integer.MIN_VALUE);
				Arrays.fill(value[1], Integer.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateLongMatrix(int n, int i, int testCase) {
		long[][] value = new long[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Long.MAX_VALUE);
				Arrays.fill(value[1], Long.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], 0l);
				Arrays.fill(value[1], 0l);
			} else if (testCase == 2) {
				value = new long[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], ((Long.MAX_VALUE) / 2));
				Arrays.fill(value[1], ((Long.MAX_VALUE) / 2));
			} else if (testCase == 4) {
				Arrays.fill(value[0], ((Long.MIN_VALUE) / 2));
				Arrays.fill(value[1], ((Long.MIN_VALUE) / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill(value[0], 0l);
				Arrays.fill(value[1], 0l);
			} else if (testCase == 7) {
				Arrays.fill(value[0], Long.MIN_VALUE);
				Arrays.fill(value[1], Long.MIN_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateFloatMatrix(int n, int i, int testCase) {
		float[][] value = new float[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Float.MAX_VALUE);
				Arrays.fill(value[1], Float.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Float.MIN_VALUE);
				Arrays.fill(value[1], Float.MIN_VALUE);
			} else if (testCase == 2) {
				value = new float[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], ((Float.MAX_VALUE - Float.MIN_VALUE) / 2f));
				Arrays.fill(value[1], ((Float.MAX_VALUE - Float.MIN_VALUE) / 2f));
			} else if (testCase == 4) {
				Arrays.fill(value[0], (-((Float.MAX_VALUE - Float.MIN_VALUE) / 2f)));
				Arrays.fill(value[1], (-((Float.MAX_VALUE - Float.MIN_VALUE) / 2f)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill(value[0], -Float.MIN_VALUE);
				Arrays.fill(value[1], -Float.MIN_VALUE);
			} else if (testCase == 7) {
				Arrays.fill(value[0], -Float.MAX_VALUE);
				Arrays.fill(value[1], -Float.MAX_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateDoubleMatrix(int n, int i, int testCase) {
		double[][] value = new double[2][100];
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Arrays.fill(value[0], Double.MAX_VALUE);
				Arrays.fill(value[1], Double.MAX_VALUE);
			} else if (testCase == 1) {
				Arrays.fill(value[0], Double.MIN_VALUE);
				Arrays.fill(value[1], Double.MIN_VALUE);
			} else if (testCase == 2) {
				value = new double[2][0];
			} else if (testCase == 3) {
				Arrays.fill(value[0], ((Double.MAX_VALUE - Double.MIN_VALUE) / 2f));
				Arrays.fill(value[1], ((Double.MAX_VALUE - Double.MIN_VALUE) / 2f));
			} else if (testCase == 4) {
				Arrays.fill(value[0], (-((Double.MAX_VALUE - Double.MIN_VALUE) / 2f)));
				Arrays.fill(value[1], (-((Double.MAX_VALUE - Double.MIN_VALUE) / 2f)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Arrays.fill(value[0], -Double.MIN_VALUE);
				Arrays.fill(value[1], -Double.MIN_VALUE);
			} else if (testCase == 7) {
				Arrays.fill(value[0], -Double.MAX_VALUE);
				Arrays.fill(value[1], -Double.MAX_VALUE);
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateBooleanList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), Boolean.TRUE);
			} else if (testCase == 1) {
				Collections.fill((value), Boolean.FALSE);
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), Boolean.FALSE);
				Collections.fill((value.subList(0, 49)), Boolean.TRUE);
			} else if (testCase == 4) {
				Collections.fill((value), Boolean.TRUE);
				Collections.fill((value.subList(0, 49)), Boolean.FALSE);
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return value;

	}

	private static Object generateByteList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Byte(Byte.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Byte((byte) 0));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Byte((byte) (Byte.MAX_VALUE / 2)));
			} else if (testCase == 4) {
				Collections.fill((value), new Byte((byte) (Byte.MIN_VALUE / 2)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Byte((byte) 0));
			} else if (testCase == 7) {
				Collections.fill((value), new Byte(Byte.MIN_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateShortList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Short(Short.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Short((short) 0));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Short((short) (Short.MAX_VALUE / 2)));
			} else if (testCase == 4) {
				Collections.fill((value), new Short((short) (Short.MIN_VALUE / 2)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Short((short) 0));
			} else if (testCase == 7) {
				Collections.fill((value), new Short(Short.MIN_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateIntegerList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Integer(Integer.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Integer(0));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Integer(Integer.MAX_VALUE / 2));
			} else if (testCase == 4) {
				Collections.fill((value), new Integer(Integer.MIN_VALUE / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Integer(0));
			} else if (testCase == 7) {
				Collections.fill((value), new Integer(Integer.MIN_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateLongList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Long(Long.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Long(0l));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Long(Long.MAX_VALUE / 2));
			} else if (testCase == 4) {
				Collections.fill((value), new Long(Long.MIN_VALUE / 2));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Long(0l));
			} else if (testCase == 7) {
				Collections.fill((value), new Long(Long.MIN_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateFloatList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Float(Float.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Float(Float.MIN_VALUE));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Float(((Float.MAX_VALUE - Float.MIN_VALUE) / 2f)));
			} else if (testCase == 4) {
				Collections.fill((value), new Float(-((Float.MAX_VALUE - Float.MIN_VALUE) / 2f)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Float(-Float.MIN_VALUE));
			} else if (testCase == 7) {
				Collections.fill((value), new Float(-Float.MAX_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateDoubleList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Double(Double.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Double(Double.MIN_VALUE));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Double(((Double.MAX_VALUE - Double.MIN_VALUE) / 2.0)));
			} else if (testCase == 4) {
				Collections.fill((value), new Double(-((Double.MAX_VALUE - Double.MIN_VALUE) / 2.0)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Double(-Double.MIN_VALUE));
			} else if (testCase == 7) {
				Collections.fill((value), new Double(-Double.MAX_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateDateList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), new Date(Long.MAX_VALUE));
			} else if (testCase == 1) {
				Collections.fill((value), new Date(0l));
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), new Date((Long.MAX_VALUE / 2)));
			} else if (testCase == 4) {
				Collections.fill((value), new Date((Long.MIN_VALUE / 2)));
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				Collections.fill((value), new Date(0l));
			} else if (testCase == 7) {
				Collections.fill((value), new Date(Long.MIN_VALUE));
			}
		} else {
			return null;
		}
		return value;
	}

	private static Object generateStringList(int n, int i, int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
		if ((n & (0x1 << i)) != 0) {
			if (testCase == 0) {
				Collections.fill((value), "\"abcdefghijklmnopqrstuvwxyz'-+_=#$%&*()[]{}~<>?.!:/\"".toUpperCase()); //$NON-NLS-1$
			} else if (testCase == 1) {
				Collections.fill((value), "\"abcdefghijklmnopqrstuvwxyz'-+_=#$%&*()[]{}~<>?.!:/\""); //$NON-NLS-1$
			} else if (testCase == 2) {
				value = new ArrayList<>(0);
			} else if (testCase == 3) {
				Collections.fill((value), "abc"); //$NON-NLS-1$
			} else if (testCase == 4) {
				Collections.fill((value), ""); //$NON-NLS-1$
			} else if (testCase == 5) {
				return null;
			} else if (testCase == 6) {
				return null;
			} else if (testCase == 7) {
				return null;
			}
		} else {
			return null;
		}
		return value;
	}

	private ParameterGenerator() {
	}
}
