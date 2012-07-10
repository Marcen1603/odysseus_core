/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.webservicesclient;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * @author abolles
 *
 */
public class TestClient {

	public static void main(String[] args){
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://www.webservicex.net/CurrencyConvertor.asmx?WSDL");
		Client client2 = dcf.createClient("http://api.bing.net/search.wsdl");
		
//		Object currency = null;
//		try {
//			 currency = Thread.currentThread().getContextClassLoader().loadClass("net.webservicex.Currency").newInstance();
//		} catch (InstantiationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		Client client = dcf.createClient("http://svn.apache.org/viewvc/cxf/trunk/distribution/src/main/release/samples/wsdl_first/wsdl/CustomerService.wsdl?view=co");
		
//		Client client = dcf.createClient("C:/javalib/CustomerService.wsdl");
//		client.getEndpoint().getService().getServiceInfos().get(0).getInterface().getOperations().iterator().next().getInput().
//		
		
//
//		Method m = currency.getClass().getMethod("setName", String.class);
//		m.invoke(person, "Joe Schmoe");

//		client.invoke("addPerson", person);
		
		Object[] res = null;
		try {
			Object currency1 = Thread.currentThread().getContextClassLoader().loadClass("net.webservicex.Currency").getEnumConstants()[25];
			Object currency2 = Thread.currentThread().getContextClassLoader().loadClass("net.webservicex.Currency").getEnumConstants()[22];
			System.out.println("Cur1: " + currency1);
			System.out.println("Cur2: " + currency2);
			res = client.invoke("ConversionRate", currency1, currency2);
			
			
			// =========================
//			Object keywordRequest = Thread.currentThread().getContextClassLoader().loadClass("com.amazon.soap.KeywordRequest");
			res = client2.invoke("Search", "EHEC");
			System.out.println("Echo response: " + res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}

class TestItem{
	String name;
	String test;
}
