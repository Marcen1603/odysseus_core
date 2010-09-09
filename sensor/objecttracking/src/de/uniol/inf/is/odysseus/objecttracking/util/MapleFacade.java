package de.uniol.inf.is.odysseus.objecttracking.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Algebraic;
import com.maplesoft.openmaple.Engine;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.MapleResultParserFacade;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class MapleFacade {

	private static Logger logger = LoggerFactory.getLogger(MapleFacade.class);
	
	private static MapleFacade instance = new MapleFacade();
	
	private Engine engine;
	private MapleFacade(){
		try{
			String[] a = {"java"};
			this.engine = new Engine(a, new OdysseusMapleCallBack(), null, null);
		}catch(MapleException e){
			throw new RuntimeException(e);
		}
	}
	
	public static MapleFacade getInstance(){
		if(instance == null){
			instance = new MapleFacade();
		}
		return instance;
	}
	
	public Map<IPredicate, ISolution> solveInequality(String inequality, IAttributeResolver attributeResolver){
		try{
			logger.debug("Solving inequality for t: " + inequality);
			Algebraic alg = this.engine.evaluate("with(SolveTools[Inequality]); LinearMultivariateSystem({" + inequality + "}, [t]);");
			logger.debug("Maple result: " + alg.toString());
			return MapleResultParserFacade.getInstance().parse("MAPLE " + alg.toString(), attributeResolver);
		}catch(MapleException e){
			e.printStackTrace();
			return null;
		}
	}
}
