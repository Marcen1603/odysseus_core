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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

abstract public class AbstractSocketInputHandler<T> extends AbstractInputHandler<T> {

	private String hostname;
	private int port;
	private String user;
	private String password;
	private Socket socket;
	
	public AbstractSocketInputHandler(){
		// Default constructor needed for service
	}
	
	public AbstractSocketInputHandler(String hostname, int port, String user, String password){
		this.hostname = hostname;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	@Override
	public void init() {
		try {
			socket = new Socket(this.hostname, this.port);
			// Send login information
			if (user != null && password != null) {
				PrintWriter out = new PrintWriter
					    (socket.getOutputStream(), true);
				out.println(user);
				out.println(password);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	public InputStream getInputStream(){
		if (socket != null){
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void terminate() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
