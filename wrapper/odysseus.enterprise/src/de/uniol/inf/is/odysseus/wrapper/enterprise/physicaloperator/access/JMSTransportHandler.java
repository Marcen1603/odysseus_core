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
package de.uniol.inf.is.odysseus.wrapper.enterprise.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.xml.sax.InputSource;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * Generic transport handler for Java Message System
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class JMSTransportHandler extends AbstractTransportHandler implements
		MessageListener {

	private String url;
	private String subject;
	private Connection connection;
	private Session session;
	private PipedInputStream input;
	private PipedOutputStream output;

	public JMSTransportHandler() {

	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		JMSTransportHandler handler = new JMSTransportHandler();
		handler.url = options.get("url");
		handler.subject = options.get("subject");
		handler.input = new PipedInputStream();
		try {
			handler.output = new PipedOutputStream(handler.input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		return input;
	}

	@Override
	public String getName() {
		return "JMS";
	}

	@Override
	public void process_open() throws UnknownHostException, IOException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				this.url);
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(this.subject);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process_close() throws IOException {
		if (session != null) {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				this.output.write(((TextMessage) message).getText().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {

		}
	}

}
