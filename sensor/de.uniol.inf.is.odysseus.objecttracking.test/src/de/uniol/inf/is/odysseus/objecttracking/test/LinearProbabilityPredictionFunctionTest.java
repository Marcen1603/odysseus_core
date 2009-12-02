package de.uniol.inf.is.odysseus.objecttracking.test;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;


public class LinearProbabilityPredictionFunctionTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		try{
			SDFAttributeList schema = new SDFAttributeList();
			
			SDFAttribute a = new SDFAttribute(null, "a");
			SDFAttribute b = new SDFAttribute(null, "b");
			SDFAttribute c = new SDFAttribute(null, "c");
			
			schema.add(a);
			schema.add(b);
			schema.add(c);
			
			SDFSource source = new SDFSource("source", "RelationalStreaming");
			AccessAO access = new AccessAO(source);
			access.setOutputSchema(schema);
			
			AttributeResolver resolver = new AttributeResolver();
			resolver.addSource("source", access);
//			resolver.addAttribute(a);
//			resolver.addAttribute(b);
//			resolver.addAttribute(c);
			
			SDFExpression expr_a = new SDFExpression(null, "2*b*t + c", resolver);
			SDFExpression expr_b = new SDFExpression(null, "4*a*t + 2*c", resolver);
			SDFExpression expr_c = new SDFExpression(null, "2*a*t + 2*b", resolver);
			
			SDFExpression[] exprList = new SDFExpression[3];
			exprList[0] = expr_a;
			exprList[1] = expr_b;
			exprList[2] = expr_c;
			
			ITimeInterval interval = new TimeInterval(new PointInTime(0,0), new PointInTime(10, 0));//, new TimeInterval(new PointInTime(3,0), new PointInTime(10, 0))};
			
			double[][] cov = new double[3][3];
			
			cov[0][0] = 2.0;
			cov[0][1] = 1.5;
			cov[0][2] = 1.2;
			
			cov[1][0] = 1.5;
			cov[1][1] = 2.0;
			cov[1][2] = 1.5;
			
			cov[2][0] = 1.2;
			cov[2][1] = 1.5;
			cov[2][2] = 2;
			
			Object[] vals = new Object[3];
			vals[0] = 60;
			vals[1] = 70;
			vals[2] = 80;
			
			double[][] q = new double[3][3];
//			for(int i = 0; i<q.length; i++){
//				for(int u = 0; u<q[i].length; u++){
//					q[i][u] = i * 3 + u + 1;
//				}
//			}
//			
			long sum = 0;
			for(int i = 0; i<1; i++){
			
				MVRelationalTuple<Probability> tuple = new MVRelationalTuple<Probability>(vals);
				Probability prob = new Probability(cov);
				tuple.setMetadata(prob);
				
				LinearProbabilityPredictionFunction<Probability> predFkt = new LinearProbabilityPredictionFunction<Probability>(exprList, interval, q);
			
				long start = System.nanoTime();
				MVRelationalTuple<Probability> predTuple = predFkt.predictData(schema, tuple, new PointInTime(5,0));
				predTuple.setMetadata(predFkt.predictMetadata(schema, tuple, new PointInTime(5,0)));
				long end = System.nanoTime();
				
				System.out.println(predTuple.toString());
				System.out.println("Duration: " + (end - start));
				sum += (end-start);
				
				predTuple = null;
				predFkt = null;
				
				System.gc();
			}
			
			System.out.println("Avg Duration: " + sum/1000);
//			
//			JEP myJep = new JEP();
//			myJep.setImplicitMul(true);
//			try {
//				myJep.addStandardConstants();
//				myJep.addStandardFunctions();
//				myJep.setAllowUndeclared(true);
//				myJep.addVariable("a", null);
//				myJep.addVariable("b", null);
//				myJep.addVariable("c", null);
//				myJep.addVariable("t", null);
//				SimpleNode n = (SimpleNode)myJep.parse("- 3 a t + (4 t b + 5 c)");
//				ParserDumpVisitor v = new ParserDumpVisitor();
//				v.visit(n, null);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
