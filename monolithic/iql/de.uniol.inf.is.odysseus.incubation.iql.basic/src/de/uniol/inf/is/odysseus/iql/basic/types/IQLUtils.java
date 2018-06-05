package de.uniol.inf.is.odysseus.iql.basic.types;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IQLUtils {

	public static <T> List<T> copyList(List<T> list) {
		return new ArrayList<T>(list);
	}
	
	public static <K, S> Map<K, S> copyMap(Map<K, S> map) {
		return new HashMap<K,S>(map);
	}

	public static <T> T[] toArray1(Class<?> c, List<T> list) {
		T[] result = (T[]) Array.newInstance(c, list.size());
		int i = 0;
		for (T element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static short[] toShortArray1(List<Short> list) {
		short[] result = new short[list.size()];
		int i = 0;
		for (Short element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static byte[] toByteArray1(List<Byte> list) {
		byte[] result = new byte[list.size()];
		int i = 0;
		for (Byte element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static int[] toIntArray1(List<Integer> list) {
		int[] result = new int[list.size()];
		int i = 0;
		for (Integer element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static long[] toLongArray1(List<Long> list) {
		long[] result = new long[list.size()];
		int i = 0;
		for (Long element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static float[] toFloatArray1(List<Float> list) {
		float[] result = new float[list.size()];
		int i = 0;
		for (Float element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static double[] toDoubleArray1(List<Double> list) {
		double[] result = new double[list.size()];
		int i = 0;
		for (Double element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static boolean[] toBooleanArray1(List<Boolean> list) {
		boolean[] result = new boolean[list.size()];
		int i = 0;
		for (Boolean element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static char[] toCharArray1(List<Character> list) {
		char[] result = new char[list.size()];
		int i = 0;
		for (Character element : list) {
			result[i++] = element;
		}
		return result;
	}	
	
	public static <T> T[][] toArray2(Class<?> c, List<List<T>> list) {
		int l1 = list.size();
		int l2 = 0;
		if (list.get(0) != null) {
			l2 = list.get(0).size();
		}
		T[][] result = (T[][]) Array.newInstance(c, l1, l2);
		int i = 0;
		for (List<T> element : list) {
			result[i++] = toArray1(c, element);
		}
		return result;
	}
	
	public static byte[][] toByteArray2(List<List<Byte>> list) {
		byte[][] result = new byte[list.size()][];
		int i = 0;
		for (List<Byte> element : list) {
			result[i++] = toByteArray1(element);
		}
		return result;
	}
	
	public static short[][] toShortArray2(List<List<Short>> list) {
		short[][] result = new short[list.size()][];
		int i = 0;
		for (List<Short> element : list) {
			result[i++] = toShortArray1(element);
		}
		return result;
	}
	
	public static int[][] toIntArray2(List<List<Integer>> list) {
		int[][] result = new int[list.size()][];
		int i = 0;
		for (List<Integer> element : list) {
			result[i++] = toIntArray1(element);
		}
		return result;
	}
	
	public static long[][] toLongArray2(List<List<Long>> list) {
		long[][] result = new long[list.size()][];
		int i = 0;
		for (List<Long> element : list) {
			result[i++] = toLongArray1(element);
		}
		return result;
	}
	
	public static float[][] toFloatArray2(List<List<Float>> list) {
		float[][] result = new float[list.size()][];
		int i = 0;
		for (List<Float> element : list) {
			result[i++] = toFloatArray1(element);
		}
		return result;
	}
	
	public static double[][] toDoubleArray2(List<List<Double>> list) {
		double[][] result = new double[list.size()][];
		int i = 0;
		for (List<Double> element : list) {
			result[i++] = toDoubleArray1(element);
		}
		return result;
	}
	
	public static char[][] toCharArray2(List<List<Character>> list) {
		char[][] result = new char[list.size()][];
		int i = 0;
		for (List<Character> element : list) {
			result[i++] = toCharArray1(element);
		}
		return result;
	}
	
	public static boolean[][] toBooleanArray2(List<List<Boolean>> list) {
		boolean[][] result = new boolean[list.size()][];
		int i = 0;
		for (List<Boolean> element : list) {
			result[i++] = toBooleanArray1(element);
		}
		return result;
	}
	
	public static <T> T[][][] toArray3(Class<?> c, List<List<List<T>>> list) {
		int l1 = list.size();
		int l2 = 0;
		int l3 = 0;
		if (list.get(0) != null) {
			l2 = list.get(0).size();
			if (list.get(0).get(0) != null) {
				l3 = list.get(0).get(0).size();				
			}
		}
		T[][][] result = (T[][][]) Array.newInstance(c, l1, l2, l3);
		int i = 0;
		for (List<List<T>> element : list) {
			result[i++] = toArray2(c, element);
		}
		return result;
	}
	
	public static byte[][][] toByteArray3(List<List<List<Byte>>> list) {
		byte[][][] result = new byte[list.size()][][];
		int i = 0;
		for (List<List<Byte>> element : list) {
			result[i++] = toByteArray2(element);
		}
		return result;
	}
	
	public static short[][][] toShortArray3(List<List<List<Short>>> list) {
		short[][][] result = new short[list.size()][][];
		int i = 0;
		for (List<List<Short>> element : list) {
			result[i++] = toShortArray2(element);
		}
		return result;
	}
	
	public static int[][][] toIntArray3(List<List<List<Integer>>> list) {
		int[][][] result = new int[list.size()][][];
		int i = 0;
		for (List<List<Integer>> element : list) {
			result[i++] = toIntArray2(element);
		}
		return result;
	}
	
	public static long[][][] toLongArray3(List<List<List<Long>>> list) {
		long[][][] result = new long[list.size()][][];
		int i = 0;
		for (List<List<Long>> element : list) {
			result[i++] = toLongArray2(element);
		}
		return result;
	}
	
	public static float[][][] toFloatArray3(List<List<List<Float>>> list) {
		float[][][] result = new float[list.size()][][];
		int i = 0;
		for (List<List<Float>> element : list) {
			result[i++] = toFloatArray2(element);
		}
		return result;
	}

	public static double[][][] toDoubleArray3(List<List<List<Double>>> list) {
		double[][][] result = new double[list.size()][][];
		int i = 0;
		for (List<List<Double>> element : list) {
			result[i++] = toDoubleArray2(element);
		}
		return result;
	}
	
	public static boolean[][][] toBooleanArray3(List<List<List<Boolean>>> list) {
		boolean[][][] result = new boolean[list.size()][][];
		int i = 0;
		for (List<List<Boolean>> element : list) {
			result[i++] = toBooleanArray2(element);
		}
		return result;
	}
	
	public static char[][][] toCharArray3(List<List<List<Character>>> list) {
		char[][][] result = new char[list.size()][][];
		int i = 0;
		for (List<List<Character>> element : list) {
			result[i++] = toCharArray2(element);
		}
		return result;
	}

	
	public static List toList(Object[] array) {
		List result = new ArrayList();
		for (Object obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(byte[] array) {
		List result = new ArrayList();
		for (byte obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(short[] array) {
		List result = new ArrayList();
		for (short obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(int[] array) {
		List result = new ArrayList();
		for (int obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(long[] array) {
		List result = new ArrayList();
		for (long obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(float[] array) {
		List result = new ArrayList();
		for (float obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(double[] array) {
		List result = new ArrayList();
		for (double obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(boolean[] array) {
		List result = new ArrayList();
		for (boolean obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(char[] array) {
		List result = new ArrayList();
		for (char obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	
	public static List toList(Object[][] array) {
		List result = new ArrayList();
		for (Object[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(byte[][] array) {
		List result = new ArrayList();
		for (byte[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(short[][] array) {
		List result = new ArrayList();
		for (short[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(int[][] array) {
		List result = new ArrayList();
		for (int[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(long[][] array) {
		List result = new ArrayList();
		for (long[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(float[][] array) {
		List result = new ArrayList();
		for (float[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}	

	public static List toList(double[][] array) {
		List result = new ArrayList();
		for (double[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(boolean[][] array) {
		List result = new ArrayList();
		for (boolean[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(char[][] array) {
		List result = new ArrayList();
		for (char[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(Object[][][] array) {
		List result = new ArrayList();
		for (Object[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(byte[][][] array) {
		List result = new ArrayList();
		for (byte[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(short[][][] array) {
		List result = new ArrayList();
		for (short[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(int[][][] array) {
		List result = new ArrayList();
		for (int[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(long[][][] array) {
		List result = new ArrayList();
		for (long[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(float[][][] array) {
		List result = new ArrayList();
		for (float[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(double[][][] array) {
		List result = new ArrayList();
		for (double[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(char[][][] array) {
		List result = new ArrayList();
		for (char[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}

	public static List toList(boolean[][][] array) {
		List result = new ArrayList();
		for (boolean[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List createList(Object ... obj) {
		return new ArrayList(Arrays.asList(obj));
	}

	
//	public static List createList(List list) {		
//		return new ArrayList(list);
//	}
	
	public static List createEmptyList() {
		return new ArrayList();
	}
	
	public static Map createMap(Object ... obj) {
		Map map = new HashMap();
		for (int i = 0; i < obj.length; i = i+2) {
			map.put(obj[i], obj[i+1]);
		}
		return map;
	}
	
	public static Map createMap(Map obj) {
		return new HashMap(obj);
	}
	
	public static Map createEmptyMap() {
		return new HashMap();
	}
}
