package de.uniol.inf.is.odysseus.scars.util.test;

import de.uniol.inf.is.odysseus.scars.util.CovarianceMapper;

public class CovarianceMapperTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Example example = new Example();
		
		CovarianceMapper mapper = new CovarianceMapper(example.getSchema());
		
		System.out.println(mapper);
	}

}
