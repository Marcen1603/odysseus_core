package de.uniol.inf.is.odysseus.spatial.utilities;

public class Tester {

	public static void main(String[] args) {
		
		Ellipsoid ellipsoid =Ellipsoid.Clarke1858;
		try {
			Class<?> c = ellipsoid.getClass();
			java.lang.reflect.Field f = c.getDeclaredField("WGS84");
			f.setAccessible(true);
			Ellipsoid res = (Ellipsoid) f.get(ellipsoid);
			System.out.println(res.getSemiMajorAxis() + " " + res.getInverseFlattening());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}	
	}

}
