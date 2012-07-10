/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import static org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartExtensionPointResolver;

public class DashboardPartExtensionPointResolverTest {
	
  @Test(dataProvider = "convertValueDataProvider")
  public void testConvertValue( String strValue, String type, Object expectedValue) throws Throwable  {
	  assertEquals( TestUtil.invoke("convertValue", DashboardPartExtensionPointResolver.class, strValue, type), expectedValue);
  }
  
  @Test
  public void testCheckAndGetDashboardPartClass() throws Throwable {
	  assertEquals( TestUtil.invoke("checkAndGetDashboardPartClass", DashboardPartExtensionPointResolver.class, new TestDashboardPart()), TestDashboardPart.class);
  }
  
  @Test(expectedExceptions = Exception.class)
  public void testCheckAndGetDashboardPartClassFail() throws Throwable {
	  assertEquals( TestUtil.invoke("checkAndGetDashboardPartClass", DashboardPartExtensionPointResolver.class, new Object()), TestDashboardPart.class);
  }
  
  @SuppressWarnings("unused")
@DataProvider
  private static Object[][] convertValueDataProvider() {
	  return new Object[][] {
			  {"123", "Integer", 123},
			  {"123.0", "Double", 123.0},
			  {"1", "Long", 1L},
			  {"123.5f", "Float", 123.5f},
			  {"Moin", "String", "Moin"},
			  {"", "String", ""},
			  {"true", "Boolean", true},
			  {"false", "Boolean", false},
			  
			  {null, "Integer", null},
			  {null, "Long", null},
			  {null, "Float", null},
			  {null, "Double", null},
			  {null, "String", null},
			  {null, "Boolean", null},
	  };
  }
}
