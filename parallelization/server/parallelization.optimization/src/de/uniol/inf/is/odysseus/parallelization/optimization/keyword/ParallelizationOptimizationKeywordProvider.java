/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

/**
 * @author Dennis Nowak
 *
 */
public class ParallelizationOptimizationKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywordProviderMap = new HashMap<>();
		keywordProviderMap.put(ParallelizationOptimizationPreParserKeyword.KEYWORD,
				ParallelizationOptimizationPreParserKeyword.class);
		return keywordProviderMap;
	}

}
