/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Algebraic;
import com.maplesoft.openmaple.Engine;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.MapleResultParserFacade;

@SuppressWarnings({"unchecked","rawtypes"})
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
