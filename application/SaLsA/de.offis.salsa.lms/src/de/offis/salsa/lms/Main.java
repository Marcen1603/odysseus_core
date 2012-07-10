/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.offis.salsa.lms;

import java.io.FileNotFoundException;
import java.io.IOException;
import de.offis.salsa.lms.impl.SickConnectionImpl;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length >= 2) {
			final String host = args[0];
			final int port = Integer.parseInt(args[1]);
			final SickConnection connection;

			connection = new SickConnectionImpl(host, port);

			try {
				connection.open();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (connection.isConnected()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("Invalid arguments: <Host/IP> <Port>");
		}
	}
}
