package de.uniol.inf.is.odysseus.action.performanceTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Some performance tests with java reflection
 * Results: 1. map for caching is nearly 2x slower than without
 * 			2. interface comparison a bit slower than direct class access ~ 30%
 * 			3. direct method fetching faster than iterating and comparing -> Method 4 could be even faster
 * @author Simon
 *
 */
public class PerformanceTest {
	private static int runs = 1000;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
			for (int i=0; i<10; i++){
				classes.add(Class.forName("de.uniol.inf.is.odysseus.action.performanceTest.Adap1"));
				classes.add(Class.forName("de.uniol.inf.is.odysseus.action.performanceTest.Adap2"));
			}
			

			HashMap<Class<?>, Method> methods = new HashMap<Class<?>, Method>();
			
			System.out.println("Time taken [with map&interface, just interface, just interface with alt method fetching" +
					", no interface, no reflections]");
			
			//With Interface & map
			long now = System.nanoTime();
			for (Class<?> c : classes){
				for (Class<?> cI : c.getInterfaces()){
					Object o = c.newInstance();
					Object[] mArgs = new Object[]{};
					if (cI == IAdapter.class){
						Method m = methods.get(IAdapter.class);
						if (m == null){
							m = IAdapter.class.getMethod("output", new Class[]{});
							methods.put(IAdapter.class, m);
						}
						for (int i=0; i<runs; i++){
							m.invoke(o, mArgs);
						}
					}else if (cI == IAdapter2.class){
						Method m = methods.get(IAdapter2.class);
						if (m == null){
							m = IAdapter2.class.getMethod("output", new Class[]{});
							methods.put(IAdapter2.class, m);
						}
						for (int i=0; i<runs; i++){
							m.invoke(o, mArgs);
						}
					}
				}
			}
			
			long[] times= new long[4];
			times[0] = System.nanoTime() - now;
			
			//Just Interface 
			now = System.nanoTime();
			for (Class<?> c : classes){
				for (Class<?> cI : c.getInterfaces()){
					Object o = c.newInstance();
					Object[] mArgs = new Object[]{};
					if (cI == IAdapter.class){
						Method m = IAdapter.class.getMethod("output", new Class[]{});
						for (int i=0; i<runs; i++){
							m.invoke(o, mArgs);
						}
					}else if (cI == IAdapter2.class){
						Method m = IAdapter2.class.getMethod("output", new Class[]{});
						for (int i=0; i<runs; i++){
							m.invoke(o, mArgs);
						}
					}
				}
			}
			
			times[1] = System.nanoTime() - now;
			
			//Without Interfaces
			now = System.nanoTime();
			for (Class<?> c : classes){
				Object o = c.newInstance();
				Object[] mArgs = new Object[]{};
				Method m = c.getMethod("output", new Class[]{});
				for (int i=0; i<runs; i++){
					m.invoke(o, mArgs);
				}
			}
			
			times[2] = System.nanoTime() - now;;
			
			//no reflections
			now = System.nanoTime();
			for (Class<?> c : classes){
				Object o = null;
				if (c == Adap1.class){
					o = new Adap1();
					for (int i=0; i<runs; i++){
						((Adap1)o).output();
					}
				}else {
					o = new Adap2();
					for (int i=0; i<runs; i++){
						((Adap2)o).output();
					}
				}
				
			}
			
			times[3] = System.nanoTime()-now;
			long min = Long.MAX_VALUE;
			for (long time : times){
				min = Math.min(min, time);
			}
			
			for (long time : times){
				System.out.println(time + "("+(double)time/min+")");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
