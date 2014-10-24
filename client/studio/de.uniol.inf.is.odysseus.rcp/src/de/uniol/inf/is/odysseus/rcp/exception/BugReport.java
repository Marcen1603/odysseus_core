/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.exception;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.protocol.Protocol;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.report.IReportGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReport {
	
	public static final String BUGREPORT_BASEURL = "baseurl";
	public static final String BUGREPORT_PASSWORD = "password";
	public static final String BUGREPORT_USER = "user";
	
	private static final Logger LOG = LoggerFactory.getLogger(BugReport.class);
	private static final String JIRA_API = "rest/api/latest/";
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String COMPONENT_ID = "10023"; // Other
	private static final String PROJECT_KEY = "ODY";
	private static final String ISSUE_TYPE = "Bug";
	private static final String SUBJECT = "[ODY] I found a bug";
	private static final List<String> RECIPIENTS = new ArrayList<>();
	
	private static IReportGenerator reportGenerator;
	
	static {
		RECIPIENTS.add("odysseus@lists.offis.de");
	}
	
	private final Throwable exception;

	// called by OSGi-DS
	public static void bindReportGenerator(IReportGenerator serv) {
		reportGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindReportGenerator(IReportGenerator serv) {
		if (reportGenerator == serv) {
			reportGenerator = null;
		}
	}

	public BugReport(final Throwable exception) {
		this.exception = exception;
	}

	static private String getUser() {
		return OdysseusRCPConfiguration.get(BUGREPORT_USER, "odysseus_studio");
	}

	static private String getPassword() {
		return OdysseusRCPConfiguration.get(BUGREPORT_PASSWORD, "jhf4hdds673");
	}

	static private String getAuth(String login, String password) {
		return new String(Base64.encodeBase64((login + ':' + password).getBytes()));
	}

	static public boolean checkLogin(String username, String password) {
		try {
			final URI uri = new URI(getJira() + JIRA_API + "myself/");
			HttpClient client = new HttpClient();
			client.getHostConfiguration().setHost(uri.getHost(), uri.getPort(), Protocol.getProtocol(uri.getScheme()));

			HttpMethod method = new GetMethod(uri.toString());
			method.setRequestHeader(AUTHORIZATION_HEADER, "Basic " + getAuth(username, password));
			client.executeMethod(method);
			if (method.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
		} catch (URISyntaxException | IOException e) {
			LOG.debug(e.getMessage(), e);
		}
		return false;
	}

	static private String getJira() {
		return OdysseusRCPConfiguration.get(BUGREPORT_BASEURL, "http://jira.odysseus.offis.uni-oldenburg.de/");
	}

	/**
	 * Class constructor.
	 *
	 */
	public BugReport() {
		this.exception = null;
	}

	public void openEditor() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				final Shell shell;
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
					shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				} else {
					shell = new Shell();
				}
				BugReportEditor editor = new BugReportEditor(shell, reportGenerator.generateReport(OdysseusRCPPlugIn.getActiveSession(), exception));
				editor.open();
			}

		});
	}

	String getUserReport() {
		final StringBuilder report = new StringBuilder();
		report.append("*** Odysseus Bug Report *** \n\n");
		report.append("* If you want a reply please enter a valid e-mail adress:\n\n\n");
		report.append("* What led up to the situation?\n\n\n");
		report.append("* What exactly did you do (or not do) that was effective (or ineffective)?\n\n\n");
		report.append("* What was the outcome of this action?\n\n\n");
		report.append("* What outcome did you expect instead?\n\n\n\n");
		report.append("Please be aware that this report may contain private or other confidential information\n");
		return report.toString();
	}

	public static boolean send(final String description, final String log) throws IOException {
		try {
			// Report Bugs using email clients (does not work on Windows)
			// sendReport(RECIPIENTS, description + "\n" + log);
			// Report Bugs using JIRA
			return sendReport(description, log);
		} catch (URISyntaxException | JSONException e) {
			LOG.debug(e.getMessage(), e);
			throw new IOException(e);
		}
	}

	private static boolean sendReport(final String description, final String log) throws IOException, URISyntaxException, JSONException {
		final URI uri = new URI(getJira() + JIRA_API + "issue/");
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(uri.getHost(), uri.getPort(), Protocol.getProtocol(uri.getScheme()));

		final JSONObject request = new JSONObject();
		final JSONObject fields = new JSONObject();
		final JSONArray component = new JSONArray();
		final JSONObject components = new JSONObject();
		components.put("id", COMPONENT_ID);
		component.put(components);
		final JSONObject project = new JSONObject();
		project.put("key", PROJECT_KEY);
		final JSONObject issuetype = new JSONObject();
		issuetype.put("name", ISSUE_TYPE);
		final JSONArray labels = new JSONArray();
		labels.put("BugReport");
		fields.put("project", project);
		String summary = "";
		try (Scanner scanner = new Scanner(description)) {
			while ((scanner.hasNextLine()) && ((summary == null) || ("".equals(summary)))) {
				summary = scanner.nextLine();
			}
		}
		if ((summary == null) || ("".equals(summary))) {
			summary = "Bug Report";
		}
		fields.put("summary", summary);
		fields.put("description", description);
		fields.put("issuetype", issuetype);
		fields.put("components", component);
		fields.put("labels", labels);
		request.put("fields", fields);
		HttpMethod method = new PostMethod(uri.toString());
		((PostMethod) method).setRequestEntity(new StringRequestEntity(request.toString(), "application/json", null));
		method.setRequestHeader(AUTHORIZATION_HEADER, "Basic " + getAuth(getUser(), getPassword()));
		client.executeMethod(method);

		if (method.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			final JSONObject response = new JSONObject(method.getResponseBodyAsString());
			String key = response.getString("key");
			method = new PostMethod(uri.toString() + key + "/attachments");
			Part[] parts = new Part[] { new FilePart("file", new ByteArrayPartSource("system.log", log.getBytes())) };
			MultipartRequestEntity attachment = new MultipartRequestEntity(parts, method.getParams());
			((PostMethod) method).setRequestEntity(attachment);
			method.setRequestHeader(AUTHORIZATION_HEADER, "Basic " + getAuth(getUser(), getPassword()));
			method.setRequestHeader("X-Atlassian-Token", "nocheck");
			client.executeMethod(method);
			if (method.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
			LOG.error(method.getStatusLine().toString());
		}
		LOG.error(method.getStatusLine().toString());
		System.err.println("Unable to send bug report");
		System.out.println("Please send the following bug report to: " + RECIPIENTS.toString());
		System.out.println(description.toString());
		System.out.println(log.toString());
		throw new IOException(method.getStatusLine().toString());

	}

	@SuppressWarnings("unused")
	private static boolean sendReport(final List<String> recipients, final String report) throws IOException, URISyntaxException {
		final URI mailto = format(recipients, SUBJECT, report);
		try {
			if (Desktop.isDesktopSupported()) {
				final Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.MAIL)) {
					desktop.mail(mailto);
					return true;
				} else if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(mailto);
					return true;
				}
			} else {
				Runtime.getRuntime().exec(new String[] { "open ", mailto.toString() });
				return true;
			}
		} catch (final Exception e) {
			System.err.println("Unable to send bug report");
			System.out.println("Please send the following bug report to: " + recipients.toString());
			System.out.println(report.toString());
		}
		return false;
	}
	
	private static URI format(final List<String> recipients, final String subject, final String body) throws UnsupportedEncodingException, URISyntaxException {
		return new URI(String.format("mailto:%s?subject=%s&body=%s", join(",", recipients), urlEncode(subject), urlEncode(body)));
	}

	private final static String urlEncode(final String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
	}

	private final static String join(final String seperator, final Iterable<?> recipients) {
		final StringBuilder sb = new StringBuilder();
		for (final Object recipient : recipients) {
			if (sb.length() > 0) {
				sb.append(seperator);
			}
			sb.append(recipient);
		}
		return sb.toString();
	}
}
