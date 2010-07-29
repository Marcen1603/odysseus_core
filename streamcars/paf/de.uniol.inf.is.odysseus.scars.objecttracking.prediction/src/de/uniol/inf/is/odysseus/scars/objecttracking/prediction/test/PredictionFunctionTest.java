package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.test;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.LinearPredictionFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class PredictionFunctionTest {
	
	public static void main(String[] args) {
		SDFAttributeList scan = createScanSchema();
		SDFAttributeList time = createTimeSchema();
		
		MVRelationalTuple<IProbability> scanTuple = createScanTuple(scan);
		MVRelationalTuple<IProbability> timeTuple = createTimeTuple();
		
		IPredictionFunction<IProbability> pf = new LinearPredictionFunction<IProbability>();
		PredictionExpression[] e = createExpressions();
		pf.setExpressions(e);
		pf.init(scan, time);
		
		pf.predictData(scanTuple, timeTuple, 0);
		IProbability metadata = scanTuple.<MVRelationalTuple<IProbability>>getAttribute(0).<MVRelationalTuple<IProbability>>getAttribute(0).getMetadata();
		pf.predictMetadata(metadata, scanTuple, timeTuple, 0);
		System.out.println();
		
		MVRelationalTuple<IProbability> list = scanTuple.getAttribute(0);
		for(Object o : list.getAttributes()) {
			System.out.println(o);
		}

		
		
	}
	
	private static PredictionExpression[] createExpressions() {
		PredictionExpression[] e = new PredictionExpression[4];
		
		e[0] = new PredictionExpression("a.list:obj:pos:x", "a.list:obj:pos:x * (b.currentTime - a.scanTime)");
		e[1] = new PredictionExpression("a.list:obj:pos:y", "a.list:obj:pos:y * (b.currentTime - a.scanTime)");
		e[2] = new PredictionExpression("a.list:obj:pos:z", "a.list:obj:pos:z * (b.currentTime - a.scanTime)");
		e[3] = new PredictionExpression("a.list:obj:speed", "a.list:obj:speed * (b.currentTime - a.scanTime)");
		
//		e[0] = new PredictionExpression("a.list:obj:pos:x", "a.list:obj:pos:x");
//		e[1] = new PredictionExpression("a.list:obj:pos:y", "a.list:obj:pos:y");
//		e[2] = new PredictionExpression("a.list:obj:pos:x", "a.list:obj:pos:z");
//		e[3] = new PredictionExpression("a.list:obj:speed", "a.list:obj:speed");
		
		return e;
	}
	
	private static SDFAttributeList createScanSchema() {
		SDFAttributeList scan = new SDFAttributeList();
		
		SDFAttribute list = new SDFAttribute("a.list");
		list.setDatatype(SDFDatatypeFactory.getDatatype("List"));
		
		SDFAttribute obj = new SDFAttribute("obj");
		obj.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		
		SDFAttribute pos = new SDFAttribute("pos");
		pos.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		
		List<List<?>> cov = createObjectCovarianz();
		
		SDFAttribute x = new SDFAttribute("x");
		x.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		x.setCovariance((ArrayList<?>)cov.get(0));
		
		SDFAttribute y = new SDFAttribute("y");
		y.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		y.setCovariance((ArrayList<?>)cov.get(1));
		
		SDFAttribute z = new SDFAttribute("z");
		z.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		z.setCovariance((ArrayList<?>)cov.get(2));
		
		SDFAttribute speed = new SDFAttribute("speed");
		speed.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		speed.setCovariance((ArrayList<?>)cov.get(3));
		
		SDFAttribute time = new SDFAttribute("a.scanTime");
		time.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		
		scan.add(list);
		scan.add(time);
		list.addSubattribute(obj);
		obj.addSubattribute(pos);
		pos.addSubattribute(x);
		pos.addSubattribute(y);
		pos.addSubattribute(z);
		obj.addSubattribute(speed);
		
		System.out.println(obj.getAttributeName());
		return scan;
	}
	
	private static SDFAttributeList createTimeSchema() {
		SDFAttributeList time = new SDFAttributeList();
		SDFAttribute currentTime = new SDFAttribute("b.currentTime");
		currentTime.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		time.add(currentTime);
		
		return time;
	}
	
	private static<M extends IProbability> MVRelationalTuple<M> createScanTuple(SDFAttributeList schema) {
		MVRelationalTuple<M>[] objList = createObjectList();

		MVRelationalTuple<M> list = new MVRelationalTuple<M>(objList.length);
		for(int index=0; index<objList.length; index++) {
			list.setAttribute(index, objList[index]);
			
		}
		
		MVRelationalTuple<M> scan = new MVRelationalTuple<M>(2);
		scan.setAttribute(0, list);
		scan.setAttribute(1, 1);
		
		return scan;
	}
	
	private static<M extends IProbability> MVRelationalTuple<M> createObjectTuple() {
		MVRelationalTuple<M> obj = new MVRelationalTuple<M>(2);
		
		MVRelationalTuple<M> pos = new MVRelationalTuple<M>(3);
		pos.setAttribute(0, 1.0);
		pos.setAttribute(1, 1.0);
		pos.setAttribute(2, 0.0);
		
		obj.setAttribute(0, pos);
		obj.setAttribute(1, 45.0);
		
		
		double[][] cov = new double[4][4];
		cov[0][0] = 0.5;
		cov[0][1] = 0.5;
		cov[0][2] = 0.5;
		cov[0][3] = 0.5;
		
		cov[1][0] = 0.5;
		cov[1][1] = 0.5;
		cov[1][2] = 0.5;
		cov[1][3] = 0.5;
		
		cov[2][0] = 0.5;
		cov[2][1] = 0.5;
		cov[2][2] = 0.5;
		cov[2][3] = 0.5;
		
		cov[3][0] = 0.5;
		cov[3][1] = 0.5;
		cov[3][2] = 0.5;
		cov[3][3] = 0.5;
		
		ArrayList<int[]> paths = new ArrayList<int[]>();
		paths.add(new int[] {0, 0, 0, 0});
		paths.add(new int[] {0, 0, 0, 1});
		paths.add(new int[] {0, 0, 0, 2});
		paths.add(new int[] {0, 0, 1});
		
		Probability p = new Probability();
		p.setCovariance(cov);
		p.setAttributePaths(paths);
		obj.setMetadata((M) p);
		
		return obj;
	}
	
	
	private static List<List<?>> createObjectCovarianz() {
		List<List<?>> cov = new ArrayList<List<?>>(4);
		ArrayList<Double> row0 = new ArrayList<Double>(4);
		row0.add(0.5);
		row0.add(0.5);
		row0.add(0.5);
		row0.add(0.5);
		cov.add(row0);
		
		ArrayList<Double> row1 = new ArrayList<Double>(4);
		row1.add(0.5);
		row1.add(0.5);
		row1.add(0.5);
		row1.add(0.5);
		cov.add(row1);
		
		ArrayList<Double> row2 = new ArrayList<Double>(4);
		row2.add(0.5);
		row2.add(0.5);
		row2.add(0.5);
		row2.add(0.5);
		cov.add(row2);
		
		ArrayList<Double> row3 = new ArrayList<Double>(4);
		row3.add(0.5);
		row3.add(0.5);
		row3.add(0.5);
		row3.add(0.5);
		cov.add(row3);
		
		return cov;
	}
	
	private static<M extends IProbability> MVRelationalTuple<M>[] createObjectList() {
		MVRelationalTuple<M>[] list = new MVRelationalTuple[4];
		list[0] = createObjectTuple();
		list[1] = createObjectTuple();
		list[2] = createObjectTuple();
		list[3] = createObjectTuple();
		return list;
	}
	
	private static<M extends IProbability> MVRelationalTuple<M> createTimeTuple() {
		MVRelationalTuple<M> time = new MVRelationalTuple<M>(1);
		time.setAttribute(0, 10);
		return time;
	}


}
