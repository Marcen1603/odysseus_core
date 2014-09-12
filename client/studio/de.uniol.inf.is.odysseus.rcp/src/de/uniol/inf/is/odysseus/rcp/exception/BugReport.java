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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Activator;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReport {
    private static final Logger LOG = LoggerFactory.getLogger(BugReport.class);
    private static final String JIRA = "http://odysseus.informatik.uni-oldenburg.de:8081/rest/api/latest/issue/";
    private static final String LOGIN = "odysseus_studio";
    private static final String PW = "jhf4hdds673";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String COMPONENT_ID = "10023"; // Other
    private static final String PROJECT_KEY = "ODY";
    private static final String ISSUE_TYPE = "Bug";
    private static final String SUBJECT = "[ODY] I found a bug";
    private static final List<String> RECIPIENTS = new ArrayList<>();
    static {
        BugReport.RECIPIENTS.add("odysseus@lists.offis.de");
    }
    private final Throwable exception;

    /**
     * Class constructor.
     *
     */
    public BugReport(final Throwable exception) {
        this.exception = exception;
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
                }
                else {
                    shell = new Shell();
                }
                final BugReportEditor editor = new BugReportEditor(shell);
                editor.open();
                editor.setReport(BugReport.this.getReport());
            }

        });
    }

    public String getReport() {
        final StringBuilder report = new StringBuilder();
        report.append("Dear Developer,\n\n");
        report.append("*** Reporter, please consider answering these questions, where appropriate ***\n\n");
        report.append("* What led up to the situation?\n\n");
        report.append("* What exactly did you do (or not do) that was effective (or ineffective)?\n\n");
        report.append("* What was the outcome of this action?\n\n");
        report.append("* What outcome did you expect instead?\n\n");
        report.append("Please be aware that this report may contain private or other confidential information\n");
        if (this.exception != null) {
            final StringBuilder stackStrace = BugReport.getStackTraceReport(this.exception);
            report.append("\n-- Stack Trace:\n");
            report.append(stackStrace);
        }
        final StringBuilder systemReport = BugReport.getSystemReport();
        final StringBuilder bundlesReport = BugReport.getBundlesReport();
        final StringBuilder servicesReport = BugReport.getServicesReport();

        report.append("\n-- System Information:\n");
        report.append(systemReport);
        report.append("\n-- Bundles Information:\n");
        report.append(bundlesReport);
        report.append("\n-- Services Information:\n");
        report.append(servicesReport);
        return report.toString();
    }

    public static void send(final String report) {
        try {
            // Report Bugs using email clients (does not work on Windows)
            // BugReport.sendReport(RECIPIENTS, report.toString());
            // Report Bugs using JIRA
            BugReport.sendReport(report);

        }
        catch (IOException | URISyntaxException | JSONException e) {
            BugReport.LOG.debug(e.getMessage(), e);
        }
    }

    private static void sendReport(final String report) throws ClientProtocolException, IOException, URISyntaxException, JSONException {
        final BasicCookieStore cookieStore = new BasicCookieStore();
        final CredentialsProvider credsProvider = new BasicCredentialsProvider();
        final URI uri = new URI(BugReport.JIRA);
        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setDefaultCredentialsProvider(credsProvider).build()) {
            final JSONObject request = new JSONObject();
            final JSONObject fields = new JSONObject();
            final JSONArray component = new JSONArray();
            final JSONObject components = new JSONObject();
            components.put("id", BugReport.COMPONENT_ID);
            component.put(components);
            final JSONObject project = new JSONObject();
            project.put("key", BugReport.PROJECT_KEY);
            final JSONObject issuetype = new JSONObject();
            issuetype.put("name", BugReport.ISSUE_TYPE);
            fields.put("project", project);
            fields.put("summary", "Bug Report");
            fields.put("description", report);
            fields.put("issuetype", issuetype);
            fields.put("components", component);
            request.put("fields", fields);

            final StringEntity entity = new StringEntity(request.toString());
            entity.setChunked(true);
            entity.setContentType("application/json");
            final HttpUriRequest ticket = RequestBuilder.post().setUri(uri).setEntity(entity)
                    .setHeader(BugReport.AUTHORIZATION_HEADER, "Basic " + new String(Base64.encodeBase64((BugReport.LOGIN + ':' + BugReport.PW).getBytes())))
                    .setHeader("Content-Type", "application/json").build();
            try (CloseableHttpResponse response = httpclient.execute(ticket)) {
                final HttpEntity resEntity = response.getEntity();
                EntityUtils.consume(resEntity);
            }
        }

    }

    @SuppressWarnings("unused")
    private static void sendReport(final List<String> recipients, final String report) throws IOException, URISyntaxException {
        final URI mailto = BugReport.format(recipients, BugReport.SUBJECT, report);
        try {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.MAIL)) {
                    desktop.mail(mailto);
                }
                else if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(mailto);
                }
            }
            else {
                Runtime.getRuntime().exec(new String[] { "open ", mailto.toString() });
            }
        }
        catch (final Exception e) {
            System.err.println("Unable to send bug report");
            System.out.println("Please send the following bug report to: " + recipients.toString());
            System.out.println(report.toString());
        }
    }

    private static StringBuilder getStackTraceReport(final Throwable e) {
        final StringBuilder report = new StringBuilder();
        report.append("Message:\n").append(e.getMessage()).append("\n\n");
        final StackTraceElement[] stack = e.getStackTrace();
        for (final StackTraceElement s : stack) {
            report.append("\tat ");
            report.append(s);
            report.append("\n");
        }
        report.append("\n");
        // cause
        Throwable throwable = e.getCause();
        while (throwable != null) {
            final StackTraceElement[] trace = throwable.getStackTrace();
            int m = trace.length - 1, n = stack.length - 1;
            while ((m >= 0) && (n >= 0) && trace[m].equals(stack[n])) {
                m--;
                n--;
            }
            final int framesInCommon = trace.length - 1 - m;

            report.append(OdysseusNLS.CausedBy).append(": ").append(throwable.getClass().getSimpleName()).append(" - ").append(throwable.getMessage());
            report.append("\n\n");
            for (int i = 0; i <= m; i++) {
                report.append("\tat ").append(trace[i]).append("\n");
            }
            if (framesInCommon != 0) {
                report.append("\t... ").append(framesInCommon).append(" more\n");
            }
            throwable = throwable.getCause();
        }
        return report;
    }

    private static URI format(final List<String> recipients, final String subject, final String body) throws UnsupportedEncodingException, URISyntaxException {
        return new URI(String.format("mailto:%s?subject=%s&body=%s", BugReport.join(",", recipients), BugReport.urlEncode(subject), BugReport.urlEncode(body)));
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

    private static StringBuilder getSystemReport() {
        final StringBuilder report = new StringBuilder();
        final String osArch = System.getProperty("os.arch");
        final String osVersion = System.getProperty("os.version");
        final String osName = System.getProperty("os.name");
        final String vmVersion = System.getProperty("java.vm.version");
        final String vmVendor = System.getProperty("java.vm.vendor");
        final String vmName = System.getProperty("java.vm.name");
        final Runtime rt = Runtime.getRuntime();
        final long mem = rt.totalMemory();
        final int cpu = rt.availableProcessors();
        final IProduct product = Platform.getProduct();
        final Bundle bundle = product.getDefiningBundle();
        final Version version = bundle.getVersion();
        final String productVersion = version.toString();
        final String locale = Locale.getDefault().toString();
        report.append("Operating System: ").append(osName).append(" ").append(osVersion).append(" (").append(osArch).append(")\n");
        report.append("Java VM: ").append(vmName).append(" ").append(vmVersion).append(" (").append(vmVendor).append(")\n");
        report.append("Product: ").append(productVersion).append("\n");
        report.append("CPUs: ").append(cpu).append("\n");
        report.append("Memory: ").append(mem).append("\n");
        report.append("Locale: ").append(locale).append("\n");
        return report;
    }

    private static StringBuilder getBundlesReport() {
        final StringBuilder report = new StringBuilder();
        final Bundle[] bundles = Activator.getBundleContext().getBundles();
        report.append("Id").append("\t").append("State").append("\t").append("Name").append("\t").append("Bundle").append("\n");

        for (final Bundle bundle : bundles) {
            final int state = bundle.getState();
            String stateString = "UNKNOWN";
            if (state == Bundle.ACTIVE) {
                stateString = "ACTIVE";
            }
            else if (state == Bundle.INSTALLED) {
                stateString = "INSTALLED";
            }
            else if (state == Bundle.RESOLVED) {
                stateString = "RESOLVED";
            }
            else if (state == Bundle.STARTING) {
                stateString = "STARTING";
            }
            else if (state == Bundle.STOPPING) {
                stateString = "STOPPING";
            }
            else if (state == Bundle.UNINSTALLED) {
                stateString = "UNINSTALLED";
            }
            report.append(bundle.getBundleId()).append("\t").append(stateString).append("\t").append(bundle.getSymbolicName()).append("\t").append(bundle.toString()).append("\n");
        }
        return report;
    }

    private static StringBuilder getServicesReport() {
        final StringBuilder report = new StringBuilder();
        final Bundle[] bundles = Activator.getBundleContext().getBundles();
        for (final Bundle bundle : bundles) {
            final ServiceReference<?>[] services = bundle.getRegisteredServices();
            if (services != null) {
                for (final ServiceReference<?> service : services) {
                    final Object serviceId = service.getProperty(Constants.SERVICE_ID);
                    final Object[] clazzes = (Object[]) service.getProperty(Constants.OBJECTCLASS);

                    report.append(serviceId).append(" ").append(Arrays.toString(clazzes)).append("\n");
                    for (final String propertyKey : service.getPropertyKeys()) {
                        if (!propertyKey.equals(Constants.OBJECTCLASS)) {
                            report.append("\t").append(propertyKey).append(": ").append(service.getProperty(propertyKey)).append("\n");
                        }
                    }
                    if (service.getUsingBundles() != null) {
                        report.append("\tUsed by:\n");
                        for (final Bundle b : service.getUsingBundles()) {
                            report.append("\t\t").append(b.toString()).append("\n");
                        }
                    }

                }
            }
        }
        return report;
    }
}
