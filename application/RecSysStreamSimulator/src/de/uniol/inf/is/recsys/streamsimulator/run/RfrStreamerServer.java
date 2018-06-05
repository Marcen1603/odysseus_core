/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.recsys.streamsimulator.run;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import de.uniol.inf.is.recsys.streamsimulator.data.ConfigData;
import de.uniol.inf.is.recsys.streamsimulator.data.RandomHelper;

/**
 * @author Cornelius Ludmann
 *
 */
public class RfrStreamerServer implements Streamer {

	private volatile boolean stop = false;

	private ServerSocket serverSocket = null;
	private Socket socket = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("Start ...");

		try {
			serverSocket = new ServerSocket(ConfigData.getInstance()
					.getRfrStreamPort());
			System.out.println("Waiting for accept ...");
			socket = serverSocket.accept();
			System.out.println("Connection accepted ...");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (!stop && socket.isConnected()) {

			RandomHelper rh = RandomHelper.getInstance();
			int user = rh.nextInt(0, ConfigData.getInstance().getNoOfUsers());

			try {
				sendRfr(user);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(ConfigData.getInstance().getRfrStreamDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
				stop = false;
			}

		}
		try {
			if (socket != null)
				socket.close();
			if (serverSocket != null) {
				serverSocket.close();
			}
			socket = null;
			serverSocket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop = false;
	}

	private void sendRfr(int user) throws UnknownHostException, IOException {
		if (socket == null) {
			System.out.println("socket null");
			return;
		}
		System.out.println("Send request for recommendations for user " + user
				+ ".");
		String msg;
		if (ConfigData.getInstance().getAddTimestamp()) {
			msg = user + "," + System.currentTimeMillis()
					+ System.lineSeparator();
		} else {
			msg = user + System.lineSeparator();
		}
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		printWriter.print(msg);
		printWriter.flush();
		// socket.close();
	}

	@Override
	public void stop() {
		stop = true;
	}

}
