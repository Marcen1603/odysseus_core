/*******************************************************************************
 * Copyright 2016 The Odysseus Team
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
 ******************************************************************************/
package de.uniol.inf.is.odysseus.developer.mep.command;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public final class ParameterGenerator {

	private static final int TEST_CASES = 8;

	/**
	 * Generates parameter values for the given function by estimating all
	 * permutations of accepted types and generating test cases for every type
	 * combination
	 * 
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
	 *            The function to generate parameter values for
	 * @return An array with generated parameter values
	 */
	public static Object[][] getFunctionValues(final IMepFunction<?> function) {
		if (function.getArity() == 0) {
			final Object[][] values = new Object[TEST_CASES][];
			Arrays.fill(values, new Object[] {});
			return values;
		}

		// Number of permutations for possible parameter combination
		final int permutations = (int) Math.pow(2.0, function.getArity());
		// Number of permutations for accepted types
		int typePermutations = 1;
		int[] typesPerPosition = new int[function.getArity()];
		for (int i = 0; i < function.getArity(); i++) {
			typesPerPosition[i] = function.getAcceptedTypes(i).length;
			typePermutations *= typesPerPosition[i];
		}

		final Object[][] values = new Object[permutations * typePermutations * TEST_CASES][function.getArity()];
		int[] type = new int[function.getArity()];
		for (int i = 0; i < type.length; i++) {
			type[i] = 0;
		}
		int offset = 0;
		for (int p = 0; p < typePermutations; p++) {
			int cases = -1;
			// Generate test cases for the current type combination
			for (int n = 0; n < permutations * TEST_CASES; n++) {
				int pos = n % permutations;
				if (pos == 0) {
					cases++;
				} else {
					Object[] value = new Object[function.getArity()];
					for (int i = 0; i < function.getArity(); i++) {
						if ((n & (0x1 << i)) != 0) {
							value[i] = generateValue(function.getAcceptedTypes(i)[type[i]], cases);
						} else {
							value[i] = null;
						}
					}
					values[offset + n - (int) (1.0 + (n / permutations))] = value;
				}
			}
			offset += permutations * TEST_CASES;
			// Change selected data type for next permutations
			type[0]++;
			for (int i = 0; i < function.getArity() - 1; i++) {
				if (type[i] >= typesPerPosition[i]) {
					type[i + 1]++;
					type[i] = 0;
				}
			}
		}
		return values;

	}

	/**
	 * Generates a test case value for the selected data type. Possible test
	 * cases are:
	 * 
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
	 * @param type
	 *            The data type
	 * @param testCase
	 *            The test case index
	 * @return
	 */
	private static Object generateValue(SDFDatatype type, int testCase) {
		if (SDFDatatype.CHAR.equals(type)) {
			return generateCharacter(testCase);
		} else if (SDFDatatype.BOOLEAN.equals(type)) {
			return generateBoolean(testCase);
		} else if (SDFDatatype.BYTE.equals(type)) {
			return generateByte(testCase);
		} else if (SDFDatatype.SHORT.equals(type)) {
			return generateShort(testCase);
		} else if (SDFDatatype.INTEGER.equals(type)) {
			return generateInteger(testCase);
		} else if ((SDFDatatype.LONG.equals(type)) || (SDFDatatype.TIMESTAMP.equals(type))
				|| (SDFDatatype.START_TIMESTAMP.equals(type)) || (SDFDatatype.END_TIMESTAMP.equals(type))) {
			return generateLong(testCase);
		} else if (SDFDatatype.FLOAT.equals(type)) {
			return generateFloat(testCase);
		} else if (SDFDatatype.DOUBLE.equals(type)) {
			return generateDouble(testCase);
		} else if (SDFDatatype.UNSIGNEDINT16.equals(type)) {
			return generateUnsignedInt16(testCase);
		} else if (SDFDatatype.STRING.equals(type)) {
			return generateString(testCase);
		} else if (SDFDatatype.MATRIX_BOOLEAN.equals(type)) {
			return generateBooleanMatrix(testCase);
		} else if (SDFDatatype.MATRIX_BYTE.equals(type)) {
			return generateByteMatrix(testCase);
		} else if (SDFDatatype.MATRIX_SHORT.equals(type)) {
			return generateShortMatrix(testCase);
		} else if (SDFDatatype.MATRIX_INTEGER.equals(type)) {
			return generateIntegerMatrix(testCase);
		} else if (SDFDatatype.MATRIX_LONG.equals(type)) {
			return generateLongMatrix(testCase);
		} else if (SDFDatatype.MATRIX_FLOAT.equals(type)) {
			return generateFloatMatrix(testCase);
		} else if (SDFDatatype.MATRIX_DOUBLE.equals(type)) {
			return generateDoubleMatrix(testCase);
		} else if (SDFDatatype.VECTOR_BOOLEAN.equals(type)) {
			return generateBooleanVector(testCase);
		} else if (SDFDatatype.VECTOR_BYTE.equals(type)) {
			return generateByteVector(testCase);
		} else if (SDFDatatype.VECTOR_SHORT.equals(type)) {
			return generateShortVector(testCase);
		} else if (SDFDatatype.VECTOR_INTEGER.equals(type)) {
			return generateIntegerVector(testCase);
		} else if (SDFDatatype.VECTOR_LONG.equals(type)) {
			return generateLongVector(testCase);
		} else if (SDFDatatype.VECTOR_FLOAT.equals(type)) {
			return generateFloatVector(testCase);
		} else if (SDFDatatype.VECTOR_DOUBLE.equals(type)) {
			return generateDoubleVector(testCase);
		} else if (SDFDatatype.DATE.equals(type)) {
			return generateDate(testCase);
		} else if (SDFDatatype.BITVECTOR.equals(type)) {
			return generateBitVector(testCase);
		} else if ((SDFDatatype.LIST_BOOLEAN.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateBooleanList(testCase);
		} else if ((SDFDatatype.LIST_BYTE.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateByteList(testCase);
		} else if ((SDFDatatype.LIST_SHORT.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateShortList(testCase);
		} else if ((SDFDatatype.LIST_INTEGER.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateIntegerList(testCase);
		} else if ((SDFDatatype.LIST_LONG.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateLongList(testCase);
		} else if ((SDFDatatype.LIST_FLOAT.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateFloatList(testCase);
		} else if ((SDFDatatype.LIST_DOUBLE.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateDoubleList(testCase);
		} else if ((SDFDatatype.LIST_STRING.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateStringList(testCase);
		} else if ((SDFDatatype.LIST_DATE.equals(type)) || (SDFDatatype.LIST.equals(type))) {
			return generateDateList(testCase);
		} else {
			return null;
		}
	}

	private static Object generateCharacter(int testCase) {
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

		return null;
	}

	private static Object generateBoolean(int testCase) {
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

		return null;
	}

	private static Object generateByte(int testCase) {
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

		return null;
	}

	private static Object generateShort(int testCase) {
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

		return null;
	}

	private static Object generateInteger(int testCase) {
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

		return null;
	}

	private static Object generateLong(int testCase) {
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

		return null;
	}

	private static Object generateFloat(int testCase) {
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

		return null;
	}

	private static Object generateDouble(int testCase) {
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

		return null;
	}

	private static Object generateUnsignedInt16(int testCase) {
		if (testCase == 0) {
			return new Integer((int) Math.pow(2, 15) - 1);
		} else if (testCase == 1) {
			return new Integer(0);
		} else if (testCase == 2) {
			return new Integer(0);
		} else if (testCase == 3) {
			return new Integer((((int) Math.pow(2, 15) - 1) / 2));
		} else if (testCase == 4) {
			return null;
		} else if (testCase == 5) {
			return null;
		} else if (testCase == 6) {
			return new Integer(0);
		} else if (testCase == 7) {
			return null;
		}
		return null;
	}

	private static Object generateString(int testCase) {
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

		return null;
	}

	private static Object generateDate(int testCase) {
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

		return null;
	}

	private static Object generateBitVector(int testCase) {
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

		return null;
	}

	private static Object generateBooleanVector(int testCase) {
		boolean[] value = new boolean[100];
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

		return value;
	}

	private static Object generateByteVector(int testCase) {
		byte[] value = new byte[100];
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

		return value;
	}

	private static Object generateShortVector(int testCase) {
		short[] value = new short[100];
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

		return value;
	}

	private static Object generateIntegerVector(int testCase) {
		int[] value = new int[100];
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

		return value;
	}

	private static Object generateLongVector(int testCase) {
		long[] value = new long[100];
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

		return value;
	}

	private static Object generateFloatVector(int testCase) {
		float[] value = new float[100];
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

		return value;
	}

	private static Object generateDoubleVector(int testCase) {
		double[] value = new double[100];
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

		return value;
	}

	private static Object generateBooleanMatrix(int testCase) {
		boolean[][] value = new boolean[2][100];
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

		return value;
	}

	private static Object generateByteMatrix(int testCase) {
		byte[][] value = new byte[2][100];
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

		return value;
	}

	private static Object generateShortMatrix(int testCase) {
		short[][] value = new short[2][100];
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

		return value;
	}

	private static Object generateIntegerMatrix(int testCase) {
		int[][] value = new int[2][100];
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

		return value;
	}

	private static Object generateLongMatrix(int testCase) {
		long[][] value = new long[2][100];
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

		return value;
	}

	private static Object generateFloatMatrix(int testCase) {
		float[][] value = new float[2][100];
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

		return value;
	}

	private static Object generateDoubleMatrix(int testCase) {
		double[][] value = new double[2][100];
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

		return value;
	}

	private static Object generateBooleanList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;

	}

	private static Object generateByteList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateShortList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateIntegerList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateLongList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateFloatList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateDoubleList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateDateList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private static Object generateStringList(int testCase) {
		List<Object> value = Arrays.asList(new Object[100]);
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

		return value;
	}

	private ParameterGenerator() {
	}
}
