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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.protocol.Protocol;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
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
import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReport {
    private static final Logger LOG = LoggerFactory.getLogger(BugReport.class);
    private static final String JIRA_API = "rest/api/latest/issue/";
//    private static final String AUTH = "b2R5c3NldXNfc3R1ZGlvOmpoZjRoZGRzNjcz";
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
    
    static private String getUser(){
        return OdysseusRCPConfiguration.get("bugreport.user", "odysseus_studio");    	
    }
    
    static private String getPassword(){
        return OdysseusRCPConfiguration.get("bugreport.password", "jhf4hdds673");    	
    }
    
    static private String getAuth(String login, String password){
    	return new String(Base64.encodeBase64((login+ ':' + password).getBytes()));    	
    }
    
    static public boolean checkLogin(String login, String password){
    	// TODO: Check Login
    	return true;
    }
    
    static private String getJira(){
    	return OdysseusRCPConfiguration.get("bugreport.baseurl", "http://jira.odysseus.offis.uni-oldenburg.de/");
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
                editor.setUserReport(BugReport.this.getUserReport());
                editor.setGeneratedReport(BugReport.this.getGeneratedReport());

            }

        });
    }

    public String getReport() {
        final StringBuilder report = new StringBuilder();
        report.append(getUserReport()).append("\n");
        report.append(getGeneratedReport());
        return report.toString();

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

    String getGeneratedReport() {
        final StringBuilder report = new StringBuilder();
        if (this.exception != null) {
            final StringBuilder stackStrace = BugReport.getStackTraceReport(this.exception);
            report.append("\n\n##########################################################################################\n");
            report.append("## Stack Trace:\n");
            report.append("##########################################################################################\n");
            report.append(stackStrace);
        }
        final StringBuilder queryReport = BugReport.getQueryReport();
        final StringBuilder consoleReport = BugReport.getConsoleReport();
        final StringBuilder systemReport = BugReport.getSystemReport();
        final StringBuilder bundlesReport = BugReport.getBundlesReport();
        final StringBuilder servicesReport = BugReport.getServicesReport();
        final StringBuilder reloadLog = BugReport.getReloadLogReport();
        report.append("\n\n##########################################################################################\n");
        report.append("## Console Information:\n");
        report.append("##########################################################################################\n");
        report.append(consoleReport);
        report.append("\n\n##########################################################################################\n");
        report.append("## Reload Log:\n");
        report.append("##########################################################################################\n");
        report.append(reloadLog);        
        report.append("\n\n##########################################################################################\n");
        report.append("## Odysseus Information:\n");
        report.append("##########################################################################################\n");
        report.append(queryReport);
        report.append("\n\n##########################################################################################\n");
        report.append("## System Information:\n");
        report.append("##########################################################################################\n");
        report.append(systemReport);
        report.append("\n\n##########################################################################################");
        report.append("## Bundles Information:\n");
        report.append("##########################################################################################\n");
        report.append(bundlesReport);
        report.append("\n\n##########################################################################################");
        report.append("## Services Information:\n");
        report.append("##########################################################################################\n\n");
        report.append(servicesReport);
        return report.toString();
    }

    public static boolean send(final String description, final String log) throws IOException {
        try {
            // Report Bugs using email clients (does not work on Windows)
            // BugReport.sendReport(RECIPIENTS, description + "\n" + log);
            // Report Bugs using JIRA
            return BugReport.sendReport(description, log);
        }
        catch (URISyntaxException | JSONException e) {
            BugReport.LOG.debug(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    private static boolean sendReport(final String description, final String log) throws IOException, URISyntaxException, JSONException {
        final URI uri = new URI(getJira()+BugReport.JIRA_API);
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(uri.getHost(), uri.getPort(), Protocol.getProtocol(uri.getScheme()));

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
        request.put("fields", fields);
        HttpMethod method = new PostMethod(uri.toString());
        ((PostMethod) method).setRequestEntity(new StringRequestEntity(request.toString(), "application/json", null));
        method.setRequestHeader(BugReport.AUTHORIZATION_HEADER, "Basic " + getAuth(getUser(), getPassword()));
        client.executeMethod(method);

        if (method.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
            final JSONObject response = new JSONObject(method.getResponseBodyAsString());
            String key = response.getString("key");
            method = new PostMethod(uri.toString() + key + "/attachments");
            Part[] parts = new Part[] { new FilePart("file", new ByteArrayPartSource("system.log", log.getBytes())) };
            MultipartRequestEntity attachment = new MultipartRequestEntity(parts, method.getParams());
            ((PostMethod) method).setRequestEntity(attachment);
            method.setRequestHeader(BugReport.AUTHORIZATION_HEADER, "Basic " + getAuth(getUser(), getPassword()));
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
        final URI mailto = BugReport.format(recipients, BugReport.SUBJECT, report);
        try {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.MAIL)) {
                    desktop.mail(mailto);
                    return true;
                }
                else if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(mailto);
                    return true;
                }
            }
            else {
                Runtime.getRuntime().exec(new String[] { "open ", mailto.toString() });
                return true;
            }
        }
        catch (final Exception e) {
            System.err.println("Unable to send bug report");
            System.out.println("Please send the following bug report to: " + recipients.toString());
            System.out.println(report.toString());
        }
        return false;
    }
    
    private static StringBuilder getQueryReport() {
        final StringBuilder report = new StringBuilder();
        IExecutor executor = OdysseusRCPPlugIn.getExecutor();
        ISession session = OdysseusRCPPlugIn.getActiveSession();
        if (executor != null) {
            report.append("\n### Queries:\n");
            Collection<Integer> logicalQueries = executor.getLogicalQueryIds(session);
            for (Integer id : logicalQueries) {
                ILogicalQuery logicalQuery = executor.getLogicalQueryById(id.intValue(), session);
                report.append("\t* ").append(logicalQuery.getID()).append("\t").append(logicalQuery.getQueryText().replace('\n', ' ').replace('\r', ' ')).append("\n");
            }
            report.append("\n### Sinks:\n");
            List<SinkInformation> sinks = executor.getSinks(session);
            for (SinkInformation sink : sinks) {
                report.append("\t* ").append(sink.getName()).append(" (").append(sink.getOutputSchema()).append(")\n");
            }
            report.append("\n### Streams and Views:\n");
            List<ViewInformation> streams = executor.getStreamsAndViewsInformation(session);
            for (ViewInformation stream : streams) {
                report.append("\t* ").append(stream.getName()).append(" (").append(stream.getOutputSchema()).append(")\n");
            }
            report.append("\n### Datatypes:\n");
            Set<SDFDatatype> datatypes = executor.getRegisteredDatatypes(session);
            for (SDFDatatype datatype : datatypes) {
                report.append("\t* ").append(datatype).append("\n");
            }
            report.append("\n### Wrappers:\n");
            Set<String> wrappers = executor.getRegisteredWrapperNames(session);
            for (String wrapper : wrappers) {
                report.append("\t* ").append(wrapper).append("\n");
            }
            report.append("\n### Schedulers:\n");
            Set<String> schedulers = executor.getRegisteredSchedulers(session);
            for (String scheduler : schedulers) {
                report.append("\t* ").append(scheduler).append("\n");
            }
            report.append("\n### Scheduling Strategies:\n");
            Set<String> schedulingStrategies = executor.getRegisteredSchedulingStrategies(session);
            for (String schedulingStrategy : schedulingStrategies) {
                report.append("\t* ").append(schedulingStrategy).append("\n");
            }
            report.append("\n### Parsers:\n");
            Set<String> parsers = executor.getSupportedQueryParsers(session);
            for (String parser : parsers) {
                report.append("\t* ").append(parser).append("\n");
            }
            report.append("\n### Operators:\n");
            List<String> operators = executor.getOperatorNames(session);
            for (String operator : operators) {
                report.append("\t* ").append(operator).append("\n");
            }
        }
        return report;
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
        report.append("\t* Operating System: ").append(osName).append(" ").append(osVersion).append(" (").append(osArch).append(")\n");
        report.append("\t* Java VM: ").append(vmName).append(" ").append(vmVersion).append(" (").append(vmVendor).append(")\n");
        report.append("\t* Product: ").append(productVersion).append("\n");
        report.append("\t* CPUs: ").append(cpu).append("\n");
        report.append("\t* Memory: ").append(mem).append("\n");
        report.append("\t* Locale: ").append(locale).append("\n");
        report.append("\t* System Properties: ").append("\n");
        for (Entry<Object, Object> property : System.getProperties().entrySet()) {
            report.append("\t\t- ").append(property.getKey()).append(": ").append(property.getValue()).append("\n");
        }
        report.append("\t* System Environment: ").append("\n");
        for (Entry<String, String> property : System.getenv().entrySet()) {
            report.append("\t\t- ").append(property.getKey()).append(": ").append(property.getValue()).append("\n");
        }
        return report;
    }

    private static StringBuilder getBundlesReport() {
        final StringBuilder report = new StringBuilder();
        final Bundle[] bundles = Activator.getBundleContext().getBundles();
        report.append("| Id").append("\t| ").append("State").append("\t| ").append("Name").append("\t| ").append("Bundle").append(" |\n");

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
            report.append("| ").append(bundle.getBundleId()).append("\t| ").append(stateString).append("\t| ").append(bundle.getSymbolicName()).append("\t| ").append(bundle.toString()).append(" |\n");
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

                    report.append("* ").append(serviceId).append(" ").append(Arrays.toString(clazzes)).append("\n");
                    for (final String propertyKey : service.getPropertyKeys()) {
                        if (!propertyKey.equals(Constants.OBJECTCLASS)) {
                            report.append("\t+ ").append(propertyKey).append(": ").append(service.getProperty(propertyKey)).append("\n");
                        }
                    }
                    if (service.getUsingBundles() != null) {
                        report.append("\tUsed by:\n");
                        for (final Bundle b : service.getUsingBundles()) {
                            report.append("\t\t- ").append(b.toString()).append("\n");
                        }
                    }

                }
            }
        }
        return report;
    }

    private static StringBuilder getConsoleReport() {
        final StringBuilder report = new StringBuilder();
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        if (plugin != null) {
            IConsoleManager conMan = plugin.getConsoleManager();
            IConsole[] existing = conMan.getConsoles();
            for (int i = 0; i < existing.length; i++) {
                MessageConsole console = (MessageConsole) existing[i];
                IDocument document = console.getDocument();
                report.append("\t* ").append(console.getName()).append(":\n");
                report.append(document.get()).append("\n\n");
            }
        }

        String localRootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
        Path path = FileSystems.getDefault().getPath(localRootLocation, ".metadata", ".log");
        loadFromFile(report, path, "Info Service Log");
        return report;
    }

	public static void loadFromFile(final StringBuilder report,
			Path path, String title) {
		
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                report.append("\t* "+title+":\n");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    report.append(line).append("\n");
                }
                report.append("\n");
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
	}

    private static StringBuilder getReloadLogReport(){
        final StringBuilder report = new StringBuilder();
        Path path = FileSystems.getDefault().getPath(OdysseusRCPConfiguration.ODYSSEUS_HOME_DIR, "reloadlog.store");
        loadFromFile(report, path, "Reload Log");
        return report;
    }

    
}
