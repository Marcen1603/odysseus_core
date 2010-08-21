package de.uniol.inf.is.odysseus.scars.testdata.provider.test;

import de.uniol.inf.is.odysseus.scars.testdata.provider.Provider;

public class ProviderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Provider provider = new Provider();
		provider.setDelay(50);
		provider.setNumOfCars(5);
		provider.init();
		for (int i = 0; i < 200; i++) {
			//provider.nextTuple();
			System.out.println(provider.nextTuple());
		}
		
	}

}
