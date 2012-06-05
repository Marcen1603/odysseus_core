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
package de.uniol.inf.is.odysseus.generator.txt;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class TxtDataProvider extends Thread {

	ServerSocket server;
	String filename;

	public TxtDataProvider(int i, String filename) {
		try {
			server = new ServerSocket(i);
			this.filename = filename;
			System.out.println("Starting server on port "+i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String charset = "UTF-8";
	private Scanner scanner;
	private String delimiter = ";";
	private boolean keepDelimiter = true;

	@Override
	public void run() {
		while (true) {
			init();
			Socket socket = null;
			BufferedWriter out = null;
			try {
				socket = server.accept();
				System.out.println("Client connected ... Sending data");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				String elem;
				while ((elem = next()) != null) {
					out.write(elem);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void init() {
		System.out.println("Waiting for Connection ...");
		initFileStream();
	}

	public void close() {
		scanner.close();
	}

	private void initFileStream() {
		URL fileURL = Activator.getContext().getBundle()
				.getEntry(filename);
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
			this.scanner = new Scanner(inputStream, charset);
			scanner.useDelimiter(delimiter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String next() {

		String elem = null;
		if (scanner.hasNext()) {
			elem = scanner.next();
		}
		if (elem == null) {
			return null;
		}
		if (keepDelimiter) {
			elem = elem + scanner.delimiter();
		}
		System.out.println(elem);

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return elem;
	}

}
