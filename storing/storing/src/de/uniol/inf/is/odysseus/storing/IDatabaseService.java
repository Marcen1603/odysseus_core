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
package de.uniol.inf.is.odysseus.storing;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseService {

	public String getInfo();
	
	public String getName();
	
	public Connection getConnection(String url, String user, String password) throws SQLException;

	public String testDriver(String driverName) throws ClassNotFoundException; 
	
	public String getDefaultURL();

	public String getDefaultUser();
		
	public String getDefaultPassword();
}
