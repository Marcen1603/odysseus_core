/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack.scai.interpreter;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.IInterpreter;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIConfigurationValue;
import de.offis.scampi.stack.scai.builder.types.SCAIDataStreamElement;
import de.offis.scampi.stack.scai.builder.types.SCAIPermission;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;
import de.offis.scampi.stack.scai.interpreter.interpreterText.Constants;
import de.offis.scampi.stack.scai.translator.TranslatorText;

import de.offis.xml.schema.scai20.*;
import org.apache.xmlbeans.XmlOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author Claas
 */
public class InterpreterText implements IInterpreter {
   
    private String exclude(String parameter) {
        return parameter.split("=", 2)[1].subSequence(1, parameter.split("=", 2)[1].length() - 1).toString().trim();
    }

    @Override
    public String getEncryptionType(Object data) {
        return null;
    }

    @Override
    public SCAIDocument interpret(Object data) {
        try {
            return parse((String) data);
        } catch (Exception ex) {
            Logger.getLogger(InterpreterText.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private ArrayList<SCAIConfigurationValue> matchRecursive(
            ArrayList<SCAIConfigurationValue> scaiConfigurationValues, String line) {
        Matcher cvMatcher = Pattern.compile(Constants.CONFIGURATIONVALUE).matcher(line);

        while (cvMatcher.find()) {
            SCAIConfigurationValue scv = new SCAIConfigurationValue();
            scv.setName(parseVariableValue(cvMatcher.group(2)));

            if (cvMatcher.group(3) != null) {
                scv.setIndex(parseIntValue(cvMatcher.group(3)));
            }

            if (cvMatcher.group(4) != null) {
                scv.setValue(parseStringValue(cvMatcher.group(4)));
            }

            if (cvMatcher.group(6) != null) {
                scv.setConfigurationValues(
                        matchRecursive(new ArrayList<SCAIConfigurationValue>(), cvMatcher.group(6)));
            }

            scaiConfigurationValues.add(scv);
        }

        return scaiConfigurationValues;
    }

    private ArrayList<String> balance(String line) {
        ArrayList<String> result = new ArrayList();
        String regex = "[a-zA-Z]{1,3}=\\(.*\\)";
        Matcher matcher = Pattern.compile(regex).matcher(line.trim());
        if (matcher.matches()) {
            String unbalanced = matcher.group();
            do {
                int i = unbalanced.indexOf("(");
                int pCount = 1;
                boolean inString = false;
//                System.out.println("" + i + " " + pCount + " " + inString + "\t" + unbalanced);
                do {
                    i++;
                    if (unbalanced.charAt(i) == '"' && unbalanced.charAt(i - 1) != '\\') inString = !inString;
                    if (unbalanced.charAt(i) == '(' && !inString) pCount++;
                    else if (unbalanced.charAt(i) == ')' && !inString) pCount--;
                } while (pCount > 0);
                result.add(unbalanced.substring(0, i + 1));
                unbalanced = unbalanced.substring(i + 1, unbalanced.length()).trim();
            } while (!unbalanced.equals("") && unbalanced.length() > 1);
        } else result.add(line);
//        System.out.println(result.toString());
        return result;
    }

    /**
     *
     * @param data
     * @return
     * @throws Exception
     */
    private SCAIDocument parse(Object data) throws Exception {
        Stack<String> lines = new Stack<String>();
        IBuilder builder = new BuilderSCAI20();
        String input = ((String) data).replaceFirst(Constants.PROTOCOL_IDENTIFIER + ":", "").trim();
        int lastEnd = 0;
        boolean openQuotation = false;

        for (int i = 0; i < input.toCharArray().length; i++) {
            Character c = input.toCharArray()[i];

            if (c == '"') {
                openQuotation = !openQuotation;
            }

            if ((c == ';') & !openQuotation) {
                lines.push(input.substring(lastEnd, i).trim());
                lastEnd = i + 1;
            }
        }

        Collections.reverse(lines); // put the lines back in order

        builder = selectParser(lines, builder);

        if (builder.getDocument() == null) {
            throw new Exception("No SCAIDocument");
        }
        String debugXML = ((SCAIDocument)builder.getDocument()).xmlText(new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(4));
        return (SCAIDocument) builder.getDocument();
    }

    private IBuilder parseAccessControl(String line, IBuilder builder) {
        Matcher matcher;

        // <editor-fold defaultstate="collapsed" desc="createUser">
        if ((matcher = Pattern.compile(Constants.AC_USERD).matcher(line)).matches()) {
            ArrayList<SCAIPermission> permissions = new ArrayList();

            if (matcher.group(5) != null) {
                Matcher pMatcher = Pattern.compile(Constants.PERMISSION).matcher(matcher.group(5));

                while (pMatcher.find()) {
                    SCAIPermission scaiPermission = new SCAIPermission();
                    scaiPermission.setPrivilege(parseStringValue(pMatcher.group(2)));
                    if (pMatcher.group(3).startsWith("g")) scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.GRANT);
                    else scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.WITHDRAW);
                    scaiPermission.setInheritable(parseBooleanValue(pMatcher.group(5)));
                    Map<String, String> properties = new HashMap();
                    if (pMatcher.group(4) != null) {
                        String[] props = pMatcher.group(4).split(" p=\\(");

                        for (String property : props) {
                            if ((property != null) & !property.equals(" ") & !property.isEmpty()) {
                                String[] kv = property.trim().split("\\s", 2);
                                properties.put(
                                        parseVariableValue(kv[0]),
                                        parseStringValue(kv[1].subSequence(0, kv[1].lastIndexOf(")")).toString()));
                            }
                        }
                    }
                    scaiPermission.setProperties(properties);

                    permissions.add(scaiPermission);
                }
            }

            ArrayList<String> roles = new ArrayList();
            if (matcher.group(14) != null & !matcher.group(14).isEmpty()) {
                Matcher roleMatcher = Pattern.compile(" rl=("+Constants.VARIABLE_TYPE+")").matcher(matcher.group(14));
                while (roleMatcher.find()) {
                    roles.add(roleMatcher.group(1));
                }
            }

            builder.addCreateUser(
                    parseVariableValue(matcher.group(3)),
                    parseBinaryValue(matcher.group(4)),
                    permissions.toArray(new SCAIPermission[0]),
                    roles.toArray(new String[0]),
                    parseVariableValue(matcher.group(15)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="removeUser">
        else if ((matcher = Pattern.compile(Constants.AR_USERD).matcher(line)).matches()) {
            builder.addRemoveUser(
                    parseReference(matcher.group(2)),
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="updateUser">
        else if ((matcher = Pattern.compile(Constants.AU_USERD).matcher(line)).matches()) {
            ArrayList<SCAIPermission> permissions = new ArrayList();

            if (matcher.group(8) != null) {
                Matcher pMatcher = Pattern.compile(Constants.PERMISSION).matcher(matcher.group(8));

                while (pMatcher.find()) {
                    SCAIPermission scaiPermission = new SCAIPermission();
                    scaiPermission.setPrivilege(parseStringValue(pMatcher.group(2)));
                    if (pMatcher.group(3).startsWith("g")) scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.GRANT);
                    else scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.WITHDRAW);
                    scaiPermission.setInheritable(parseBooleanValue(pMatcher.group(5)));
                    Map<String, String> properties = new HashMap();
                    if (pMatcher.group(4) != null) {
                        String[] props = pMatcher.group(4).split(" p=\\(");

                        for (String property : props) {
                            if ((property != null) & !property.equals(" ") & !property.isEmpty()) {
                                String[] kv = property.trim().split("\\s", 2);
                                properties.put(
                                        parseVariableValue(kv[0]),
                                        parseStringValue(kv[1].subSequence(0, kv[1].lastIndexOf(")")).toString()));
                            }
                        }
                    }
                    scaiPermission.setProperties(properties);

                    permissions.add(scaiPermission);
                }
            }

            ArrayList<String> roles = new ArrayList();
            if (matcher.group(17) != null & !matcher.group(17).isEmpty()) {
                Matcher roleMatcher = Pattern.compile(" rl=("+Constants.VARIABLE_TYPE+")").matcher(matcher.group(17));
                while (roleMatcher.find()) {
                    roles.add(roleMatcher.group(1));
                }
            }

            builder.addUpdateUser(
                    parseReferenceValue(matcher.group(2)),
                    parseVariableValue(matcher.group(6)),
                    parseBinaryValue(matcher.group(7)),
                    permissions.toArray(new SCAIPermission[0]),
                    roles.toArray(new String[0]),
                    parseVariableValue(matcher.group(18)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="authenticateUser">
        else if ((matcher = Pattern.compile(Constants.AAU_USER).matcher(line)).matches()) {
            builder.addAuthenticateUser(
                    parseVariableValue(matcher.group(3)),
                    parseBinaryValue(matcher.group(4)),
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="destroySession">
        else if ((matcher = Pattern.compile(Constants.AD_SESSN).matcher(line)).matches()) {
            builder.addDestroySession(
                    parseStringValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="getUserData">
        else if ((matcher = Pattern.compile(Constants.AG_USERD).matcher(line)).matches()) {
            builder.addGetUserData(
                    parseReferenceValue(matcher.group(2)),
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="grantSensorAccess">
        else if ((matcher = Pattern.compile(Constants.AGR_ACCS).matcher(line)).matches()) {
            Matcher accMatcher = Pattern.compile(Constants.ACCESSOR).matcher(matcher.group(6));
            ArrayList<SCAIReference> users = new ArrayList();
            ArrayList<SCAIReference> domains = new ArrayList();
            while (accMatcher.find()) {
                if (accMatcher.group(1).startsWith("u")) {
                    users.add(parseReferenceValue(accMatcher.group(2)));
                } else if (accMatcher.group(1).matches("d")) {
                    domains.add(parseReferenceValue(accMatcher.group(2)));
                }
            }
            builder.addGrantSensorAccess(parseSensorReferenceValue(matcher.group(2)),
                    users.toArray(new SCAIReference[0]), domains.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(12)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="withdrawSensorAccess">
        else if ((matcher = Pattern.compile(Constants.AWD_ACCS).matcher(line)).matches()) {
            Matcher accMatcher = Pattern.compile(Constants.ACCESSOR).matcher(matcher.group(6));
            ArrayList<SCAIReference> users = new ArrayList();
            ArrayList<SCAIReference> domains = new ArrayList();
            while (accMatcher.find()) {
                if (accMatcher.group(1).startsWith("u")) {
                    users.add(parseReferenceValue(accMatcher.group(2)));
                } else if (accMatcher.group(1).matches("d")) {
                    domains.add(parseReferenceValue(accMatcher.group(2)));
                }
            }
            builder.addWithdrawSensorAccess(parseSensorReferenceValue(matcher.group(2)),
                    users.toArray(new SCAIReference[0]), domains.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(12)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllSensorAccessors">
        else if ((matcher = Pattern.compile(Constants.ALS_ACCS).matcher(line)).matches()) {

            builder.addListAllSensorAccessors(
                    parseSensorReferenceValue(matcher.group(2)),
                    parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        return builder;
    }

    private IBuilder parseAcknowledgment(String line, Stack<String> lines, IBuilder builder) {
        Matcher matcher;
        // <editor-fold defaultstate="collapsed" desc="Exception">
        if ((matcher = Pattern.compile(Constants.ACK_EXCP).matcher(line)).matches()) {
            builder.addAcknowledgmentException(
                    parseStringValue(matcher.group(2)),
                    parseIntValue(matcher.group(3)),
                    parseVariableValue(matcher.group(4)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Success">
        else if ((matcher = Pattern.compile(Constants.ACK_SUCS).matcher(line)).matches()) {
            builder.addAcknowledgmentSuccess(
                    parseStringValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Reply">
        else if ((matcher = Pattern.compile(Constants.ACK_RPLY).matcher(line)).matches()) {
            ArrayList<XmlObject> replyData = new ArrayList();
            if (matcher.group(2) != null) {
                for (String replyItem : balance(matcher.group(2))) {

                    Matcher dsaMatcher = Pattern.compile(Constants.DATASTREAM).matcher(replyItem);
                    while (dsaMatcher.find()) {
                        Matcher dsMatcher =
                                Pattern.compile(Constants.DATASTREAMELEMENT).matcher(dsaMatcher.group());
                        ArrayList<SCAIDataStreamElement> dataStreamElements = new ArrayList();

                        while (dsMatcher.find()) {
                            SCAIDataStreamElement scaiDataStreamElement = new SCAIDataStreamElement();
                            scaiDataStreamElement.setPath(parseStringValue(dsMatcher.group(1)));

                            if (dsMatcher.group(2) != null) {
                                scaiDataStreamElement.setQuality(parseDecimalValue(dsMatcher.group(2)));
                            }

                            if (dsMatcher.group(3) != null) {
                                if (parseVariableValue(dsMatcher.group(3)).equals("s")) {
                                    scaiDataStreamElement.setErrortype(
                                            SCAIDataStreamElement.Errortype.SENSORDEAD);
                                    scaiDataStreamElement.setErrorMessage(
                                            parseStringValue(dsMatcher.group(4)));
                                } else if (parseVariableValue(dsMatcher.group(3)).equals("n")) {
                                    scaiDataStreamElement.setErrortype(
                                            SCAIDataStreamElement.Errortype.NAN);
                                    scaiDataStreamElement.setErrorMessage(
                                            parseStringValue(dsMatcher.group(4)));
                                } else if (parseVariableValue(dsMatcher.group(3)).equals("b")) {
                                    scaiDataStreamElement.setErrortype(
                                            SCAIDataStreamElement.Errortype.BATTERYDEAD);
                                    scaiDataStreamElement.setErrorMessage(
                                            parseStringValue(dsMatcher.group(4)));
                                } else if (parseVariableValue(dsMatcher.group(3)).equals("u")) {
                                    scaiDataStreamElement.setErrortype(
                                            SCAIDataStreamElement.Errortype.UNKNOWN);
                                    scaiDataStreamElement.setErrorMessage(
                                            parseStringValue(dsMatcher.group(4)));
                                }
                            }

                            scaiDataStreamElement.setData(parseStringValue(dsMatcher.group(5)));
                            dataStreamElements.add(scaiDataStreamElement);
                        }
                        replyData.add((XmlObject) builder.buildReplyDataStream(parseTimeStampValue(dsaMatcher.group(1)), parseVariableValue(dsaMatcher.group(2)), parseVariableValue(dsaMatcher.group(3)), parseIntValue(dsaMatcher.group(4)), dataStreamElements.toArray(new SCAIDataStreamElement[0])));
                    }

                    ArrayList<SCAIConfigurationValue> scaiConfigurationValues = new ArrayList();
                    scaiConfigurationValues = matchRecursive(scaiConfigurationValues, replyItem);
                    for (SCAIConfigurationValue scaiConfigurationValue : scaiConfigurationValues) {
                        replyData.add((XmlObject) builder.buildReplyDataConfigurationValue(scaiConfigurationValue.getName(), scaiConfigurationValue.getIndex(), scaiConfigurationValue.getValue(), scaiConfigurationValue.getConfigurationValues()));
                    }

                }
                builder.addAcknowledgmentData(replyData.toArray(new XmlObject[0]), parseVariableValue(matcher.group(21)));
            }
        } // </editor-fold>
        return builder;
    }

    private byte[] parseBinaryValue(String parameter) {
        String result = null;

        if (parameter == null) {
            return null;
        }

        String str = parameter.split("=", 2)[1];

        result = str.substring(1, str.lastIndexOf("\'"));
        byte[] resultBytes = new byte[result.length()];
        for (int i = 0; i < resultBytes.length; i++) {
            resultBytes[i] = (byte)result.charAt(i);
        }
        return resultBytes;
    }

    private Boolean parseBooleanValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        String str = parameter.split("=", 2)[1];

        return Boolean.parseBoolean(str);
    }

    private Float parseDecimalValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        return Float.parseFloat(parameter.split("=", 2)[1]);
    }

    private IBuilder parseIdentification(String line, IBuilder builder) {
        // <editor-fold defaultstate="collapsed" desc="Identification">
        Matcher matcher = Pattern.compile(Constants.IDACCESS).matcher(line);

        if (matcher.matches()) {
            builder.addIdentification(
                    parseVariableValue(matcher.group(3)),
                    parseVariableValue(matcher.group(5)),
                    parseBinaryValue(matcher.group(6)));
        } // </editor-fold>

        return builder;
    }

    private Integer parseIntValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        try {
            return Integer.parseInt(parameter.split("=", 2)[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLongValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        try {
            return Long.parseLong(parameter.split("=", 2)[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private IBuilder parseMeasurements(String line, IBuilder builder) {
        // <editor-fold defaultstate="collapsed" desc="DataStream">
        Matcher matcher = Pattern.compile(Constants.MDSTREAM).matcher(line);

        if (matcher.matches()) {
            Matcher dsMatcher = Pattern.compile(Constants.DATASTREAMELEMENT).matcher(matcher.group(6));
            ArrayList<SCAIDataStreamElement> dataStreamElements = new ArrayList();

            while (dsMatcher.find()) {
                SCAIDataStreamElement scaiDataStreamElement = new SCAIDataStreamElement();
                scaiDataStreamElement.setPath(parseStringValue(dsMatcher.group(1)));

                if (dsMatcher.group(2) != null) {
                    scaiDataStreamElement.setQuality(parseDecimalValue(dsMatcher.group(2)));
                }

                if (dsMatcher.group(3) != null) {
                    if (parseVariableValue(dsMatcher.group(3)).equals("s")) {
                        scaiDataStreamElement.setErrortype(
                                SCAIDataStreamElement.Errortype.SENSORDEAD);
                        scaiDataStreamElement.setErrorMessage(parseStringValue(dsMatcher.group(4)));
                    } else if (parseVariableValue(dsMatcher.group(3)).equals("n")) {
                        scaiDataStreamElement.setErrortype(SCAIDataStreamElement.Errortype.NAN);
                        scaiDataStreamElement.setErrorMessage(parseStringValue(dsMatcher.group(4)));
                    } else if (parseVariableValue(dsMatcher.group(3)).equals("b")) {
                        scaiDataStreamElement.setErrortype(
                                SCAIDataStreamElement.Errortype.BATTERYDEAD);
                        scaiDataStreamElement.setErrorMessage(parseStringValue(dsMatcher.group(4)));
                    } else if (parseVariableValue(dsMatcher.group(3)).equals("u")) {
                        scaiDataStreamElement.setErrortype(SCAIDataStreamElement.Errortype.UNKNOWN);
                        scaiDataStreamElement.setErrorMessage(parseStringValue(dsMatcher.group(4)));
                    }
                }

                scaiDataStreamElement.setData(parseStringValue(dsMatcher.group(5)));
                dataStreamElements.add(scaiDataStreamElement);
            }

            builder.addDataStream(
                    parseTimeStampValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)),
                    parseVariableValue(matcher.group(4)),
                    parseIntValue(matcher.group(5)),
                    dataStreamElements.toArray(new SCAIDataStreamElement[0]));
        } // </editor-fold>

        return builder;
    }

    private SCAIReference parseReference(String parameter) {
        return parseReferenceValue(exclude(parameter));
    }

    private SCAIReference parseReferenceValue(String parameter) {
        Pattern pattern = Pattern.compile(Constants.REFERENCE);
        Matcher matcher = pattern.matcher(parameter);

        if (matcher.matches()) {
            return new SCAIReference(
                    parseVariableValue(matcher.group(3)),
                    parseVariableValue(matcher.group(2)));
        } else {
            return null;
        }
    }

    private IBuilder parseSensorControl(String line, IBuilder builder) {
        Matcher matcher;

        // <editor-fold defaultstate="collapsed" desc="startSensor">
        if ((matcher = Pattern.compile(Constants.SST_SNSR).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addStartSensor(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="stopSensor">
        else if ((matcher = Pattern.compile(Constants.SSP_SNSR).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addStopSensor(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="configureSensor">
        else if ((matcher = Pattern.compile(Constants.SCF_SNSR).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            ArrayList<SCAIConfigurationValue> scaiConfigurationValues = new ArrayList();
            scaiConfigurationValues = matchRecursive(scaiConfigurationValues, matcher.group(6));

            builder.addConfigureSensor(
                    sensor,
                    scaiConfigurationValues.toArray(new SCAIConfigurationValue[0]),
                    parseVariableValue(matcher.group(13)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="getConfiguration">
        else if ((matcher = Pattern.compile(Constants.SG_CONFG).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addGetConfiguration(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="subscribeDatastream">
        else if ((matcher = Pattern.compile(Constants.SSUBS_DS).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addSubscribeDatastream(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="unsubscribeDatastream">
        else if ((matcher = Pattern.compile(Constants.SSUNS_DS).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addUnsubscribeDatastream(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="getSupportedModules">
        else if ((matcher = Pattern.compile(Constants.SG_SPMOD).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addGetSupportedModules(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="getValue">
        else if ((matcher = Pattern.compile(Constants.SG_VALUE).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addGetValue(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>

        return builder;
    }

    private SCAISensorReference parseSensorReferenceValue(String parameter) {
        Matcher matcher = Pattern.compile(Constants.SENSORREFERENCE).matcher(parameter);

        if (matcher.matches()) {
            if (matcher.group(2) != null) {
                return new SCAISensorReference(parseVariableValue(matcher.group(2)));
            } else {
                return new SCAISensorReference(
                        parseVariableValue(matcher.group(3)),
                        parseVariableValue(matcher.group(4)));
            }
        } else {
            return null;
        }
    }

    private IBuilder parseSensorRegistryControl(String line, IBuilder builder) {
        Matcher matcher;

        // <editor-fold defaultstate="collapsed" desc="addChildCategoryToSensorCategory">
        // Command is a_cg2cg for addChildCategoryToSensorCategory
        if ((matcher = Pattern.compile(Constants.RA_CG2CG).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addAddChildCategoryToSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="addSensorCategoryToSensorDomain">
        // Command is a_cg2dm for addSensorCategoryToSensorDomain
        else if ((matcher = Pattern.compile(Constants.RA_CG2DM).matcher(line)).matches()) {
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference domain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addAddSensorCategoryToSensorDomain(
                    category,
                    domain,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="addSensorTypeToSensorCategory">
        // Command is a_tp2cg for addSensorTypeToSensorCategory
            else if ((matcher = Pattern.compile(Constants.RA_TP2CG).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addAddSensorTypeToSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="addSensorTypeToSensorDomain">
        // Command is a_tp2dm for addSensorTypeToSensorDomain
        else if ((matcher = Pattern.compile(Constants.RA_TP2DM).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addAddSensorTypeToSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="addDataElementToDataStreamType">
        // Command is a_de2ds for addDataElementToDataStreamType
        else if ((matcher = Pattern.compile(Constants.RA_DE2DS).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addAddSensorTypeToSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>        
        // <editor-fold defaultstate="collapsed" desc="CreateConfigurationParameter">
        // Command is c_cfgpm for CreateConfigurationParameter
        else if ((matcher = Pattern.compile(Constants.RC_CFGPM).matcher(line)).matches()) {
            // ConfigurationParameter is AtomicParameter
            if (matcher.group(8) != null) {
                SCAIReference dataType =
                        new SCAIReference(
                        parseVariableValue(matcher.group(16)),
                        parseVariableValue(matcher.group(15)));
                builder.addCreateConfigurationParameterAtomicParameter(
                        parseVariableValue(matcher.group(2)),
                        parseBooleanValue(matcher.group(3)),
                        parseVariableValue(matcher.group(9)),
                        parseBooleanValue(matcher.group(10)),
                        dataType,
                        parseVariableValue(matcher.group(27)));
            } // ConfigurationParameter is ComplexParameter
            else if (matcher.group(17) != null) {
                Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(7));
                ArrayList<SCAIReference> configurationParameters = new ArrayList<SCAIReference>();

                while (referenceMatcher.find()) {
                    SCAIReference configurationParameter =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    configurationParameters.add(configurationParameter);
                }

                builder.addCreateConfigurationParameterComplexParameter(
                        parseVariableValue(matcher.group(2)),
                        parseBooleanValue(matcher.group(3)),
                        configurationParameters.toArray(new SCAIReference[0]),
                        parseVariableValue(matcher.group(27)));
            } // ConfigurationParameter is SequenceParameter
            else if (matcher.group(21) != null) {
                SCAIReference configurationParameter =
                        new SCAIReference(
                        parseVariableValue(matcher.group(26)),
                        parseVariableValue(matcher.group(25)));
                builder.addCreateConfigurationParameterSequenceParameter(
                        parseVariableValue(matcher.group(2)),
                        parseBooleanValue(matcher.group(3)),
                        parseIntValue(matcher.group(22)),
                        parseIntValue(matcher.group(23)),
                        configurationParameter,
                        parseVariableValue(matcher.group(27)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateDataElement">
        // Command is c_datel for CreateDataElement
        else if ((matcher = Pattern.compile(Constants.RC_DATEL).matcher(line)).matches()) {
            // DataElement is AtomicElement
            if (matcher.group(3).startsWith("a")) {
                SCAIReference dataType =
                        new SCAIReference(
                        parseVariableValue(matcher.group(11)),
                        parseVariableValue(matcher.group(10)));
                builder.addCreateDataElementAtomicElement(
                        parseVariableValue(matcher.group(6)),
                        dataType,
                        parseVariableValue(matcher.group(15)));
            } // DataElement is ComplexElement
            else if (matcher.group(3).startsWith("c")) {
                Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(7));
                ArrayList<SCAIReference> dataElements = new ArrayList<SCAIReference>();

                while (referenceMatcher.find()) {
                    SCAIReference dataElement =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    dataElements.add(dataElement);
                }

                builder.addCreateDataElementComplexElement(
                        parseVariableValue(matcher.group(6)),
                        dataElements.toArray(new SCAIReference[0]),
                        parseVariableValue(matcher.group(15)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateDataStreamType">
        // Command is c_dstyp for CreateDataStreamType

        if ((matcher = Pattern.compile(Constants.RC_DSTYP).matcher(line)).matches()) {
            Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
            Matcher referenceMatcher = referencePattern.matcher(matcher.group(3));
            ArrayList<SCAIReference> dataElements = new ArrayList<SCAIReference>();

            while (referenceMatcher.find()) {
                SCAIReference dataElement =
                        new SCAIReference(
                        parseVariableValue(referenceMatcher.group(3)),
                        parseVariableValue(referenceMatcher.group(2)));

                dataElements.add(dataElement);
            }

            builder.addCreateDataStreamType(
                    parseVariableValue(matcher.group(2)),
                    dataElements.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(11)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateDataType">
        // Command is c_datyp for CreateDataType
        else if ((matcher = Pattern.compile(Constants.RC_DATYP).matcher(line)).matches()) {
            DataTypeDescription dtd = DataTypeDescription.Factory.newInstance();
            dtd.setName(parseVariableValue(matcher.group(2)));

            // identify and construct StringType
            if (matcher.group(4) != null) {
                builder.addCreateDataTypeStringType(
                        parseVariableValue(matcher.group(2)),
                        parseLongValue(matcher.group(5)),
                        parseLongValue(matcher.group(6)),
                        parseStringValue(matcher.group(7)),
                        parseStringValue(matcher.group(8)),
                        parseVariableValue(matcher.group(28)));
            } // identify and construct DeciamlType
            else if (matcher.group(9) != null) {
                builder.addCreateDataTypeDecimalType(
                        parseVariableValue(matcher.group(2)),
                        parseDecimalValue(matcher.group(10)),
                        parseDecimalValue(matcher.group(11)),
                        parseDecimalValue(matcher.group(12)),
                        parseDecimalValue(matcher.group(13)),
                        parseVariableValue(matcher.group(28)));
            } // identify and construct BinaryType
            else if (matcher.group(14) != null) {
                builder.addCreateDataTypeBinaryType(
                        parseVariableValue(matcher.group(2)),
                        parseLongValue(matcher.group(15)),
                        parseLongValue(matcher.group(16)),
                        parseBinaryValue(matcher.group(17)),
                        parseVariableValue(matcher.group(28)));
            } // identify and construct ListType
            else if (matcher.group(18) != null) {
                ArrayList<String> allowedValues = new ArrayList();
                ArrayList<String> defaultValues = new ArrayList();

                Matcher allowedMatcher = Pattern.compile("a=" + Constants.STRING_TYPE).matcher(matcher.group(21));
                while (allowedMatcher.find()) {
                    allowedValues.add(parseStringValue(allowedMatcher.group()));
                }
                Matcher defaultMatcher = Pattern.compile("d=" + Constants.STRING_TYPE).matcher(matcher.group(22));
                while (defaultMatcher.find()) {
                    defaultValues.add(parseStringValue(defaultMatcher.group()));
                }
                
                builder.addCreateDataTypeListType(
                        parseVariableValue(matcher.group(2)),
                        parseLongValue(matcher.group(19)),
                        parseLongValue(matcher.group(20)),
                        allowedValues.toArray(new String[allowedValues.size()]),
                        defaultValues.toArray(new String[defaultValues.size()]),
                        parseVariableValue(matcher.group(28)));
            } // identify and construct EnumType
            else if (matcher.group(23) != null) {
                Map<Long, String> allowedValues = new HashMap();
                Matcher allowedMatcher = Pattern.compile(Constants.ENUM_ITEM).matcher(matcher.group(24));
                while (allowedMatcher.find()) {
                    allowedValues.put(new Long(parseIntValue(allowedMatcher.group(1))), parseStringValue(allowedMatcher.group(2)));
                }
                builder.addCreateDataTypeEnumType(
                        parseVariableValue(matcher.group(2)),
                        allowedValues,
                        parseStringValue(matcher.group(27)),
                        parseVariableValue(matcher.group(28)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateOperator">
        // Command is c_oprtr for CreateOperator
        else if ((matcher = Pattern.compile(Constants.RC_OPRTR).matcher(line)).matches()) {
            SCAIReference operatorGroup = parseReferenceValue(matcher.group(2));

            HashMap<String, String> properties = new HashMap<String, String>();

            if (matcher.group(6) != null) {
                String[] props = matcher.group(6).split(" p=\\(");

                for (String property : props) {
                    if ((property != null) & !property.equals(" ") & !property.isEmpty()) {
                        String[] kv = property.trim().split("\\s", 2);
                        properties.put(
                                parseVariableValue(kv[0]),
                                parseStringValue(
                                kv[1].subSequence(0, kv[1].lastIndexOf(")")).toString()));
                    }
                }
            }

            if (matcher.group(7).startsWith("s")) {
                builder.addCreateOperatorServiceOperator(
                        operatorGroup,
                        parseVariableValue(matcher.group(5)),
                        properties,
                        parseReferenceValue(matcher.group(14)),
                        parseVariableValue(matcher.group(17)));
            } else if (matcher.group(7).startsWith("i")) {
                builder.addCreateOperatorInputOperator(
                        operatorGroup,
                        parseVariableValue(matcher.group(5)),
                        properties,
                        parseSensorReferenceValue(matcher.group(9)),
                        parseVariableValue(matcher.group(17)));
            } else if (matcher.group(7).startsWith("o")) {
                builder.addCreateOperatorOutputOperator(
                        operatorGroup,
                        parseVariableValue(matcher.group(5)),
                        properties,
                        parseSensorReferenceValue(matcher.group(9)),
                        parseVariableValue(matcher.group(17)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateOperatorGroup">
        // Command is c_opgrp for CreateOperatorGroup
        else if ((matcher = Pattern.compile(Constants.RC_OPGRP).matcher(line)).matches()) {
            builder.addCreateOperatorGroup(
                    parseVariableValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateOperatorType">
        // Command is c_optyp for CreateOperatorType
        else if ((matcher = Pattern.compile(Constants.RC_OPTYP).matcher(line)).matches()) {
            Pattern tempPattern = Pattern.compile(Constants.PROPERTY_TYPE_RW);
            HashMap<String, String> properties = new HashMap<String, String>();
            HashMap<String, Boolean> propertiesrw = new HashMap<String, Boolean>();

            if (matcher.group(4) != null) {
                String[] props = matcher.group(4).split(" p=\\(");

                for (String property : props) {
                    if ((property != null) & !property.equals(" ") & !property.isEmpty()) {
                        property = " p=(" + property;

                        Matcher tempMatcher = tempPattern.matcher(property);

                        if (tempMatcher.matches()) {
                            properties.put(
                                    parseVariableValue(tempMatcher.group(2)),
                                    parseStringValue(tempMatcher.group(3)));
                            propertiesrw.put(
                                    parseVariableValue(tempMatcher.group(2)),
                                    parseBooleanValue(tempMatcher.group(4)));
                        }
                    }
                }
            }

            builder.addCreateOperatorType(
                    parseVariableValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)),
                    properties,
                    propertiesrw,
                    parseVariableValue(matcher.group(11)),
                    parseVariableValue(matcher.group(12)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateSensor">
        // Command is c_sensr for CreateSensor
        else if ((matcher = Pattern.compile(Constants.RC_SENSR).matcher(line)).matches()) {
            SCAIReference sensorDomain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(6)),
                    parseVariableValue(matcher.group(5)));
            SCAIReference sensorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(10)),
                    parseVariableValue(matcher.group(9)));
            builder.addCreateSensor(
                    parseVariableValue(matcher.group(2)),
                    sensorDomain,
                    sensorType,
                    parseBooleanValue(matcher.group(11)),
                    parseVariableValue(matcher.group(15)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateSensorCategory">
        // Command is c_secat for CreateSensorCategory
        else if ((matcher = Pattern.compile(Constants.RC_SECAT).matcher(line)).matches()) {
            SCAIReference parentSensorCategory =
                    new SCAIReference(
                    parseVariableValue(matcher.group(6)),
                    parseVariableValue(matcher.group(5)));
            Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
            ArrayList<SCAIReference> SensorDomains = new ArrayList<SCAIReference>();

            if (matcher.group(8) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(8));

                while (referenceMatcher.find()) {
                    SCAIReference SensorType =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    SensorDomains.add(SensorType);
                }
            }

            builder.addCreateSensorCategory(
                    parseVariableValue(matcher.group(2)),
                    parentSensorCategory,
                    SensorDomains.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(15)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateSensorDomain">
        // Command is c_sedom for CreateSensorDomain
        else if ((matcher = Pattern.compile(Constants.RC_SEDOM).matcher(line)).matches()) {
            builder.addCreateSensorDomain(
                    parseVariableValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="CreateSensorType">
        // Command is c_setyp for CreateSensorType

        if ((matcher = Pattern.compile(Constants.RC_SETYP).matcher(line)).matches()) {
            SCAIReference dataStreamType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));

            Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
            ArrayList<SCAIReference> configurationParameters = new ArrayList<SCAIReference>();
            ArrayList<SCAIReference> sensorCategories = new ArrayList<SCAIReference>();
            ArrayList<SCAIReference> sensorDomains = new ArrayList<SCAIReference>();

            if (matcher.group(8) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(8));

                while (referenceMatcher.find()) {
                    SCAIReference configurationParameter =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    configurationParameters.add(configurationParameter);
                }
            }

            if (matcher.group(16) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(16));

                while (referenceMatcher.find()) {
                    SCAIReference sensorCategory =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    sensorCategories.add(sensorCategory);
                }
            }

            if (matcher.group(24) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(24));

                while (referenceMatcher.find()) {
                    SCAIReference sensorDomain =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    sensorDomains.add(sensorDomain);
                }
            }

            builder.addCreateSensorType(
                    parseVariableValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)),
                    dataStreamType,
                    configurationParameters.toArray(new SCAIReference[0]),
                    sensorCategories.toArray(new SCAIReference[0]),
                    sensorDomains.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(32)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="DeployOperatorGroup">
        // Command is de_opgp for DeployOperatorGroup
        else if ((matcher = Pattern.compile(Constants.RDE_OPGP).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            SCAIReference processingUnit = null;

            if (!(matcher.group(7) == null) || !(matcher.group(6) == null)) {
                processingUnit =
                        new SCAIReference(
                        parseVariableValue(matcher.group(7)),
                        parseVariableValue(matcher.group(6)));
            }

            builder.addDeployOperatorGroup(
                    operatorGroup,
                    processingUnit,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetConfigurationParameter">
        // Command is g_cfgpm for GetConfigurationParameter
        else if ((matcher = Pattern.compile(Constants.RG_CFGPM).matcher(line)).matches()) {
            SCAIReference configurationParameter =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetConfigurationParameter(
                    configurationParameter,
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetDataElement">
        // Command is g_datel for GetDataElement
        else if ((matcher = Pattern.compile(Constants.RG_DATEL).matcher(line)).matches()) {
            SCAIReference dataElement =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetDataElement(dataElement, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetDataStreamType">
        // Command is g_dstyp for GetDataStreamType
        else if ((matcher = Pattern.compile(Constants.RG_DSTYP).matcher(line)).matches()) {
            SCAIReference dataStreamType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetDataStreamType(dataStreamType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetDataType">
        // Command is g_datyp for GetDataType
        else if ((matcher = Pattern.compile(Constants.RG_DATYP).matcher(line)).matches()) {
            SCAIReference dataType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetDataType(dataType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetOperator">
        // Command is g_oprtr for GetOperator
        else if ((matcher = Pattern.compile(Constants.RG_OPRTR).matcher(line)).matches()) {
            SCAIReference operator =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference group =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));

            builder.addGetOperator(operator, group, parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetOperatorGroup">
        // Command is g_opgrp for GetOperatorGroup
        else if ((matcher = Pattern.compile(Constants.RG_OPGRP).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetOperatorGroup(operatorGroup, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetOperatorGroupStatus">
        // Command is g_ogpst for GetOperatorGroupStatus
        else if ((matcher = Pattern.compile(Constants.RG_OGPST).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetOperatorGroupStatus(operatorGroup, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetOperatorType">
        // Command is g_optype for GetOperatorType
        else if ((matcher = Pattern.compile(Constants.RG_OPTYP).matcher(line)).matches()) {
            SCAIReference operatorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetOperatorType(operatorType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetProcessingUnit">
        // Command is g_prunt for GetProcessingUnit
        else if ((matcher = Pattern.compile(Constants.RG_PRUNT).matcher(line)).matches()) {
            builder.addGetProcessingUnit(parseReferenceValue(matcher.group(2)), parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetSensor">
        // Command is g_sensr for GetSensor
        else if ((matcher = Pattern.compile(Constants.RG_SENSR).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addGetSensor(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetSensorCategory">
        // Command is g_secat for GetSensorCategory
        else if ((matcher = Pattern.compile(Constants.RG_SECAT).matcher(line)).matches()) {
            SCAIReference sensorCategory =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetSensorCategory(sensorCategory, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetSensorDomain">
        // Command is g_sedom for GetSensorDomain
        else if ((matcher = Pattern.compile(Constants.RG_SEDOM).matcher(line)).matches()) {
            SCAIReference sensorDomain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetSensorDomain(sensorDomain, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="GetSensorType">
        // Command is g_setyp for GetSensorType
        else if ((matcher = Pattern.compile(Constants.RG_SETYP).matcher(line)).matches()) {
            SCAIReference sensorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addGetSensorType(sensorType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="LinkOperators">
        // Command is link_op for LinkOperators
        else if ((matcher = Pattern.compile(Constants.RLINK_OP).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(5)),
                    parseVariableValue(matcher.group(4)));
            SCAIReference source = parseReferenceValue(matcher.group(8));
            SCAIReference destination = parseReferenceValue(matcher.group(13));

            if (matcher.group(7).equals("io")) {
                if (matcher.group(12).equals("oo")) {
                    builder.addLinkOperatorsInputToOutput(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                } else {
                    builder.addLinkOperatorsInputToService(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                }
            }

            if (matcher.group(7).equals("so")) {
                if (matcher.group(12).equals("oo")) {
                    builder.addLinkOperatorsServiceToOutput(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                } else {
                    builder.addLinkOperatorsServiceToService(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                }
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllConfigurationParameters">
        // Command is l_cfgpm for listAllConfigurationParameters
        else if ((matcher = Pattern.compile(Constants.RL_CFGPM).matcher(line)).matches()) {
            builder.addListAllConfigurationParameters(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllDataElements">
        // Command is l_datel for listAllDataElements
        else if ((matcher = Pattern.compile(Constants.RL_DATEL).matcher(line)).matches()) {
            builder.addListAllDataElements(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllDataStreamTypes">
        // Command is l_dstyp for listAllDataStreamTypes
        else if ((matcher = Pattern.compile(Constants.RL_DSTYP).matcher(line)).matches()) {
            builder.addListAllDataStreamTypes(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllDataTypes">
        // Command is l_datyp for listAllDataTypes
        else if ((matcher = Pattern.compile(Constants.RL_DATYP).matcher(line)).matches()) {
            builder.addListAllDataTypes(parseVariableValue(matcher.group(2)));
        } // </editor-fold>              
        // <editor-fold defaultstate="collapsed" desc="listAllLinks">
        // Command is l_links for listAllLinks
        else if ((matcher = Pattern.compile(Constants.RL_LINKS).matcher(line)).matches()) {
            builder.addListAllLinks(
                    parseReferenceValue(matcher.group(2)),
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllOperators">
        // Command is l_oprtr for listAllOperators
        else if ((matcher = Pattern.compile(Constants.RL_OPRTR).matcher(line)).matches()) {
            builder.addListAllOperators(
                    parseReferenceValue(matcher.group(2)),
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllOperatorGroups">
        // Command is l_opgrp for listOperatorGroups
        else if ((matcher = Pattern.compile(Constants.RL_OPGRP).matcher(line)).matches()) {
            builder.addListAllOperatorGroups(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllOperatorTypes">
        // Command is l_optyp for listOperatorTypes
        else if ((matcher = Pattern.compile(Constants.RL_OPTYP).matcher(line)).matches()) {
            builder.addListAllOperatorTypes(parseVariableValue(matcher.group(2)));
        } // </editor-fold>        
        // <editor-fold defaultstate="collapsed" desc="listAllProcessingUnits">
        // Command is l_prunt for listAllProcessingUnits
        else if ((matcher = Pattern.compile(Constants.RL_PRUNT).matcher(line)).matches()) {
            builder.addListAllProcessingUnits(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllSensorCategories">
        // Command is l_secat for listAllSensorCategories
        else if ((matcher = Pattern.compile(Constants.RL_SECAT).matcher(line)).matches()) {
            if (matcher.group(2) != null) {
                builder.addListAllSensorCategoriesBySensorDomain(parseReferenceValue(
                        matcher.group(2)), parseVariableValue(matcher.group(5)));
            } else {
                builder.addListAllSensorCategories(parseVariableValue(matcher.group(5)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllSensorDomains">
        // Command is l_sedom for listAllSensorDomains
        else if ((matcher = Pattern.compile(Constants.RL_SEDOM).matcher(line)).matches()) {
            builder.addListAllSensorDomains(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllSensors">
        // Command is l_sensr for listAllSensors
        else if ((matcher = Pattern.compile(Constants.RL_SENSR).matcher(line)).matches()) {
            builder.addListAllSensors(parseVariableValue(matcher.group(2)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="listAllSensorTypes">
        // Command is l_setyp for listAllSensorTypes
        else if ((matcher = Pattern.compile(Constants.RL_SETYP).matcher(line)).matches()) {
            if (matcher.group(2) == null) {
                builder.addListAllSensorTypes(parseVariableValue(matcher.group(6)));
            }
            else if (matcher.group(2).equals("c")) {
                builder.addListAllSensorTypesBySensorCategory(parseReferenceValue(matcher.group(3)),
                        parseVariableValue(matcher.group(6)));
            }
            else if (matcher.group(2).equals("d")) {
                builder.addListAllSensorTypesBySensorDomain(parseReferenceValue(matcher.group(3)),
                        parseVariableValue(matcher.group(6)));
            }
        } // </editor-fold>       
        // <editor-fold defaultstate="collapsed" desc="RegisterProcessingUnit">
        // Command is reg_pru for RegisterProcessingUnit
        else if ((matcher = Pattern.compile(Constants.RREG_PRU).matcher(line)).matches()) {
            builder.addRegisterProcessingUnit(
                    parseVariableValue(matcher.group(2)),
                    parseVariableValue(matcher.group(3)),
                    parseDecimalValue(matcher.group(4)).doubleValue(),
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="removeChildCategoryFromSensorCategory">
        // Command is r_cg2cg for removeChildCategoryFromSensorCategory
        else if ((matcher = Pattern.compile(Constants.RR_CG2CG).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addRemoveSensorTypeFromSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveConfigurationParameter">
        // Command is r_cfgpm for RemoveConfigurationParameter
        else if ((matcher = Pattern.compile(Constants.RR_CFGPM).matcher(line)).matches()) {
            SCAIReference configurationParameter =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            builder.addRemoveConfigurationParameter(
                    configurationParameter,
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveDataElement">
        // Command is r_datel for RemoveDataElement
        else if ((matcher = Pattern.compile(Constants.RR_DATEL).matcher(line)).matches()) {
            SCAIReference dataElement =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveDataElement(dataElement, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="removeDataElementFromDataStreamType">
        // Command is r_de2ds for removeDataElementFromDataStreamType
        else if ((matcher = Pattern.compile(Constants.RR_DE2DS).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addRemoveSensorTypeFromSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveDataType">
        // Command is r_datyp for RemoveDataType
        else if ((matcher = Pattern.compile(Constants.RR_DATYP).matcher(line)).matches()) {
            SCAIReference dataType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveDataType(dataType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveDataStreamType">
        // Command is r_dstyp for RemoveDataStreamType
        else if ((matcher = Pattern.compile(Constants.RR_DSTYP).matcher(line)).matches()) {
            SCAIReference dataElement =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveDataStreamType(dataElement, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveOperator">
        // Command is r_oprtr for RemoveOperator
        else if ((matcher = Pattern.compile(Constants.RR_OPRTR).matcher(line)).matches()) {
            SCAIReference operator =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference group =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));

            builder.addRemoveOperator(operator, group, parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveOperatorGroup">
        // Command is r_opgrp for RemoveOperatorGroup
        else if ((matcher = Pattern.compile(Constants.RR_OPGRP).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveOperatorGroup(operatorGroup, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveOperatorType">
        // Command is r_optyp for RemoveOperatorType
        else if ((matcher = Pattern.compile(Constants.RR_OPTYP).matcher(line)).matches()) {
            SCAIReference operatorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveOperatorType(operatorType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveSensor">
        // Command is r_sensr for RemoveSensor
        else if ((matcher = Pattern.compile(Constants.RR_SENSR).matcher(line)).matches()) {
            SCAISensorReference sensor;

            if (matcher.group(3) != null) {
                sensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                sensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            builder.addGetSensor(sensor, parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveSensorCategory">
        // Command is r_secat for RemoveSensorCategory
        else if ((matcher = Pattern.compile(Constants.RR_SECAT).matcher(line)).matches()) {
            SCAIReference sensorCategory =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveSensorCategory(sensorCategory, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="removeSensorCategoryFromSensorDomain">
        // Command is r_cg2dm for removeSensorCategoryFromSensorDomain
        else if ((matcher = Pattern.compile(Constants.RR_CG2DM).matcher(line)).matches()) {
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference domain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addRemoveSensorCategoryFromSensorDomain(
                    category,
                    domain,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveSensorDomain">
        // Command is r_sesom for RemoveSensorDomain
        else if ((matcher = Pattern.compile(Constants.RR_SEDOM).matcher(line)).matches()) {
            SCAIReference sensorDomain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveSensorDomain(sensorDomain, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemoveSensorType">
        // Command is r_setyp for RemoveSensorType
        else if ((matcher = Pattern.compile(Constants.RR_SETYP).matcher(line)).matches()) {
            SCAIReference sensorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addRemoveSensorType(sensorType, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="removeSensorTypeFromSensorCategory">
        // Command is r_tp2cg for removeSensorTypeFromSensorCategory
        else if ((matcher = Pattern.compile(Constants.RR_TP2CG).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addRemoveSensorTypeFromSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="removeSensorTypeFromSensorDomain">
        // Command is r_tp2dm for removeSensorTypeFromSensorDomain
        else if ((matcher = Pattern.compile(Constants.RR_TP2DM).matcher(line)).matches()) {
            SCAIReference type =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference category =
                    new SCAIReference(
                    parseVariableValue(matcher.group(7)),
                    parseVariableValue(matcher.group(6)));
            builder.addRemoveSensorTypeFromSensorCategory(
                    type,
                    category,
                    parseVariableValue(matcher.group(8)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UndeployOperatorGroup">
        // Command is ud_opgp for UndeployOperatorGroup
        else if ((matcher = Pattern.compile(Constants.RUD_OPGP).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addUndeployOperatorGroup(operatorGroup, parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UnlinkOperators">
        // Command is ulnk_op for UnlinkOperators
        else if ((matcher = Pattern.compile(Constants.RULNK_OP).matcher(line)).matches()) {
            SCAIReference operatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(5)),
                    parseVariableValue(matcher.group(4)));
            SCAIReference source = parseReferenceValue(matcher.group(8));
            SCAIReference destination = parseReferenceValue(matcher.group(13));

            if (matcher.group(7).equals("io")) {
                if (matcher.group(12).equals("oo")) {
                    builder.addUnlinkOperatorsInputToOutput(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                } else {
                    builder.addUnlinkOperatorsInputToService(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                }
            }

            if (matcher.group(7).equals("so")) {
                if (matcher.group(12).equals("oo")) {
                    builder.addUnlinkOperatorsServiceToOutput(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                } else {
                    builder.addUnlinkOperatorsServiceToService(
                            operatorGroup,
                            source,
                            destination,
                            parseVariableValue(matcher.group(16)));
                }
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UnregisterProcessingUnit">
        // Command is urg_pru for UnregisterProcessingUnit
        else if ((matcher = Pattern.compile(Constants.RURG_PRU).matcher(line)).matches()) {
            SCAIReference processingUnit =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addUnregisterProcessingUnit(
                    processingUnit,
                    parseVariableValue(matcher.group(5)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateConfigurationParameter">
        // Command is u_cfgpm for UpdateConfigurationParameter
        else if ((matcher = Pattern.compile(Constants.RU_CFGPM).matcher(line)).matches()) {
            SCAIReference oldConfigurationParameter =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            // ConfigurationParameter is AtomicParameter
            if (matcher.group(11) != null) {
                SCAIReference dataType =
                        new SCAIReference(
                        parseVariableValue(matcher.group(19)),
                        parseVariableValue(matcher.group(18)));
                builder.addUpdateConfigurationParameterAtomicParameter(
                        oldConfigurationParameter,
                        parseVariableValue(matcher.group(5)),
                        parseBooleanValue(matcher.group(6)),
                        parseVariableValue(matcher.group(12)),
                        parseBooleanValue(matcher.group(13)),
                        dataType,
                        parseVariableValue(matcher.group(30)));
            } // ConfigurationParameter is ComplexParameter
            else if (matcher.group(20) != null) {
                Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(10));
                ArrayList<SCAIReference> configurationParameters = new ArrayList<SCAIReference>();

                while (referenceMatcher.find()) {
                    SCAIReference configurationParameter =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    configurationParameters.add(configurationParameter);
                }

                builder.addUpdateConfigurationParameterComplexParameter(
                        oldConfigurationParameter,
                        parseVariableValue(matcher.group(5)),
                        parseBooleanValue(matcher.group(6)),
                        configurationParameters.toArray(new SCAIReference[0]),
                        parseVariableValue(matcher.group(30)));
            } // ConfigurationParameter is SequenceParameter
            else if (matcher.group(24) != null) {
                SCAIReference configurationParameter =
                        new SCAIReference(
                        parseVariableValue(matcher.group(29)),
                        parseVariableValue(matcher.group(28)));
                builder.addUpdateConfigurationParameterSequenceParameter(
                        oldConfigurationParameter,
                        parseVariableValue(matcher.group(5)),
                        parseBooleanValue(matcher.group(6)),
                        parseIntValue(matcher.group(25)),
                        parseIntValue(matcher.group(26)),
                        configurationParameter,
                        parseVariableValue(matcher.group(30)));
            }
        } // </editor-fold>                
        // <editor-fold defaultstate="collapsed" desc="UpdateDataElement">
        // Command is u_datel for UpdateDataElement
        else if ((matcher = Pattern.compile(Constants.RU_DATEL).matcher(line)).matches()) {
            SCAIReference oldDataElement =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            // DataElement is AtomicElement
            if (matcher.group(6).startsWith("a")) {
                SCAIReference dataType =
                        new SCAIReference(
                        parseVariableValue(matcher.group(14)),
                        parseVariableValue(matcher.group(13)));

                builder.addUpdateDataElementAtomicElement(
                        oldDataElement,
                        parseVariableValue(matcher.group(9)),
                        dataType,
                        parseVariableValue(matcher.group(18)));
            } // DataElement is ComplexElement
            else if (matcher.group(6).startsWith("c")) {
                Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(10));
                ArrayList<SCAIReference> dataElements = new ArrayList<SCAIReference>();

                while (referenceMatcher.find()) {
                    SCAIReference dataElement =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    dataElements.add(dataElement);
                }

                builder.addUpdateDataElementComplexElement(
                        oldDataElement,
                        parseVariableValue(matcher.group(9)),
                        dataElements.toArray(new SCAIReference[0]),
                        parseVariableValue(matcher.group(18)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateDataStreamType">
        // Command is u_dstyp for UpdateDataStreamType
        else if ((matcher = Pattern.compile(Constants.RU_DSTYP).matcher(line)).matches()) {
            SCAIReference oldDataElement =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
            Matcher referenceMatcher = referencePattern.matcher(matcher.group(6));
            ArrayList<SCAIReference> dataElements = new ArrayList<SCAIReference>();

            while (referenceMatcher.find()) {
                SCAIReference dataElement =
                        new SCAIReference(
                        parseVariableValue(referenceMatcher.group(3)),
                        parseVariableValue(referenceMatcher.group(2)));
                dataElements.add(dataElement);
            }

            builder.addUpdateDataStreamType(
                    oldDataElement,
                    parseVariableValue(matcher.group(5)),
                    dataElements.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(14)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateDataType">
        // Command is u_datyp for UpdateDataType
        else if ((matcher = Pattern.compile(Constants.RU_DATYP).matcher(line)).matches()) {
            SCAIReference dataType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            DataTypeDescription dtd = DataTypeDescription.Factory.newInstance();
            dtd.setName(parseVariableValue(matcher.group(5)));

            // identify and construct StringType
            if (matcher.group(7) != null) {
                builder.addUpdateDataTypeStringType(
                        dataType,
                        parseVariableValue(matcher.group(5)),
                        parseLongValue(matcher.group(8)),
                        parseLongValue(matcher.group(9)),
                        parseStringValue(matcher.group(10)),
                        parseStringValue(matcher.group(11)),
                        parseVariableValue(matcher.group(31)));
            } // identify and construct DeciamlType
            else if (matcher.group(12) != null) {
                builder.addUpdateDataTypeDecimalType(
                        dataType,
                        parseVariableValue(matcher.group(5)),
                        parseDecimalValue(matcher.group(13)),
                        parseDecimalValue(matcher.group(14)),
                        parseDecimalValue(matcher.group(15)),
                        parseDecimalValue(matcher.group(16)),
                        parseVariableValue(matcher.group(31)));
            } // identify and construct BinaryType
            else if (matcher.group(17) != null) {
                builder.addUpdateDataTypeBinaryType(
                        dataType,
                        parseVariableValue(matcher.group(5)),
                        parseLongValue(matcher.group(18)),
                        parseLongValue(matcher.group(19)),
                        parseBinaryValue(matcher.group(20)),
                        parseVariableValue(matcher.group(31)));
            } // identify and construct ListType
            else if (matcher.group(21) != null) {
                ArrayList<String> allowedValues = new ArrayList();
                ArrayList<String> defaultValues = new ArrayList();

                Matcher allowedMatcher = Pattern.compile("a=" + Constants.STRING_TYPE).matcher(matcher.group(24));
                while (allowedMatcher.find()) {
                    allowedValues.add(parseStringValue(allowedMatcher.group()));
                }
                Matcher defaultMatcher = Pattern.compile("d=" + Constants.STRING_TYPE).matcher(matcher.group(25));
                while (defaultMatcher.find()) {
                    defaultValues.add(parseStringValue(defaultMatcher.group()));
                }

                builder.addUpdateDataTypeListType(
                        dataType,
                        parseVariableValue(matcher.group(5)),
                        parseLongValue(matcher.group(22)),
                        parseLongValue(matcher.group(23)),
                        allowedValues.toArray(new String[allowedValues.size()]),
                        defaultValues.toArray(new String[defaultValues.size()]),
                        parseVariableValue(matcher.group(31)));
            } // identify and construct EnumType
            else if (matcher.group(26) != null) {
                Map<Long, String> allowedValues = new HashMap();
                Matcher allowedMatcher = Pattern.compile(Constants.ENUM_ITEM).matcher(matcher.group(27));
                while (allowedMatcher.find()) {
                    allowedValues.put(new Long(parseIntValue(allowedMatcher.group(1))), parseStringValue(allowedMatcher.group(2)));
                }
                builder.addUpdateDataTypeEnumType(
                        dataType,
                        parseVariableValue(matcher.group(2)),
                        allowedValues,
                        parseStringValue(matcher.group(30)),
                        parseVariableValue(matcher.group(31)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateOperator">
        // Command is u_oprtr for UpdateOperator
        else if ((matcher = Pattern.compile(Constants.RU_OPRTR).matcher(line)).matches()) {
            SCAIReference oldOperator = parseReferenceValue(matcher.group(2));

            SCAIReference operatorGroup = parseReferenceValue(matcher.group(5));

            HashMap<String, String> properties = new HashMap<String, String>();

            if (matcher.group(9) != null) {
                String[] props = matcher.group(9).split(" p=\\(");

                for (String property : props) {
                    if ((property != null) & !property.equals(" ") & !property.isEmpty()) {
                        String[] kv = property.trim().split("\\s", 2);
                        properties.put(
                                parseVariableValue(kv[0]),
                                parseStringValue(
                                kv[1].subSequence(0, kv[1].lastIndexOf(")")).toString()));
                    }
                }
            }

            if (matcher.group(10).startsWith("s")) {
                builder.addUpdateOperatorServiceOperator(
                        oldOperator,
                        operatorGroup,
                        parseVariableValue(matcher.group(8)),
                        properties,
                        parseReferenceValue(matcher.group(17)),
                        parseVariableValue(matcher.group(20)));
            } else if (matcher.group(10).startsWith("i")) {
                builder.addUpdateOperatorInputOperator(
                        oldOperator,
                        operatorGroup,
                        parseVariableValue(matcher.group(8)),
                        properties,
                        parseSensorReferenceValue(matcher.group(12)),
                        parseVariableValue(matcher.group(20)));
            } else if (matcher.group(10).startsWith("o")) {
                builder.addUpdateOperatorOutputOperator(
                        oldOperator,
                        operatorGroup,
                        parseVariableValue(matcher.group(8)),
                        properties,
                        parseSensorReferenceValue(matcher.group(12)),
                        parseVariableValue(matcher.group(20)));
            }
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateOperatorGroup">
        // Command is u_opgrp for UpdateOperatorGroup
        else if ((matcher = Pattern.compile(Constants.RU_OPGRP).matcher(line)).matches()) {
            SCAIReference oldOperatorGroup =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addUpdateOperatorGroup(
                    oldOperatorGroup,
                    parseVariableValue(matcher.group(5)),
                    parseVariableValue(matcher.group(6)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateOperatorType">
        // Command is u_optyp for UpdateOperatorType
        else if ((matcher = Pattern.compile(Constants.RU_OPTYP).matcher(line)).matches()) {
            SCAIReference oldOperatorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            Pattern tempPattern = Pattern.compile(Constants.PROPERTY_TYPE_RW);

            //                Matcher tempMatcher = tempPattern.matcher(
            //                        " " + matcher.group(11));
            HashMap<String, String> properties = new HashMap<String, String>();
            HashMap<String, Boolean> propertiesrw = new HashMap<String, Boolean>();

            if (matcher.group(7) != null) {
                String[] props = matcher.group(7).split(" p=\\(");

                for (String property : props) {
                    if ((property != null) & !property.equals(" ") & !property.isEmpty()) {
                        property = " p=(" + property;

                        //                            String[] kv = property.trim().split("\\;s", 2);
                        Matcher tempMatcher = tempPattern.matcher(property);

                        if (tempMatcher.matches()) {
                            properties.put(
                                    parseVariableValue(tempMatcher.group(2)),
                                    parseStringValue(tempMatcher.group(3)));
                            propertiesrw.put(
                                    parseVariableValue(tempMatcher.group(2)),
                                    parseBooleanValue(tempMatcher.group(4)));
                        }
                    }
                }
            }

            builder.addUpdateOperatorType(
                    oldOperatorType,
                    parseVariableValue(matcher.group(5)),
                    parseVariableValue(matcher.group(6)),
                    properties,
                    propertiesrw,
                    parseVariableValue(matcher.group(14)),
                    parseVariableValue(matcher.group(15)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateSensor">
        // Command is u_sensr for UpdateSensor
        else if ((matcher = Pattern.compile(Constants.RU_SENSR).matcher(line)).matches()) {
            SCAISensorReference oldSensor;

            if (matcher.group(3) != null) {
                oldSensor = new SCAISensorReference(parseVariableValue(matcher.group(3)));
            } else {
                oldSensor =
                        new SCAISensorReference(
                        parseVariableValue(matcher.group(4)),
                        parseVariableValue(matcher.group(5)));
            }

            SCAIReference sensorDomain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(9)),
                    parseVariableValue(matcher.group(8)));
            SCAIReference sensorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(14)),
                    parseVariableValue(matcher.group(13)));
            builder.addUpdateSensor(
                    oldSensor,
                    parseVariableValue(matcher.group(5)),
                    sensorDomain,
                    sensorType,
                    parseBooleanValue(matcher.group(15)),
                    parseVariableValue(matcher.group(19)));
        } // </editor-fold>          
        // <editor-fold defaultstate="collapsed" desc="UpdateSensorCategory">
        // Command is u_secat for UpdateSensorCategory
        else if ((matcher = Pattern.compile(Constants.RU_SECAT).matcher(line)).matches()) {
            SCAIReference oldSensorCategory =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));
            SCAIReference parentSensorCategory =
                    new SCAIReference(
                    parseVariableValue(matcher.group(9)),
                    parseVariableValue(matcher.group(8)));
            Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
            ArrayList<SCAIReference> SensorDomains = new ArrayList<SCAIReference>();

            if (matcher.group(11) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(11));

                while (referenceMatcher.find()) {
                    SCAIReference SensorType =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    SensorDomains.add(SensorType);
                }
            }

            builder.addUpdateSensorCategory(
                    oldSensorCategory,
                    parseVariableValue(matcher.group(2)),
                    parentSensorCategory,
                    SensorDomains.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(18)));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="UpdateSensorDomain">
        // Command is u_sedom for UpdateSensorDomain
        else if ((matcher = Pattern.compile(Constants.RU_SEDOM).matcher(line)).matches()) {
            SCAIReference oldSensorDomain =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            builder.addUpdateSensorDomain(
                    oldSensorDomain,
                    parseVariableValue(matcher.group(5)),
                    parseVariableValue(matcher.group(6)));
        } // </editor-fold>        
        // <editor-fold defaultstate="collapsed" desc="UpdateSensorType">
        // Command is u_setyp for UpdateSensorType
        else if ((matcher = Pattern.compile(Constants.RU_SETYP).matcher(line)).matches()) {
            SCAIReference oldSensorType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(4)),
                    parseVariableValue(matcher.group(3)));

            SCAIReference dataStreamType =
                    new SCAIReference(
                    parseVariableValue(matcher.group(10)),
                    parseVariableValue(matcher.group(9)));

            Pattern referencePattern = Pattern.compile(Constants.REFERENCE);
            ArrayList<SCAIReference> configurationParameters = new ArrayList<SCAIReference>();
            ArrayList<SCAIReference> sensorCategories = new ArrayList<SCAIReference>();
            ArrayList<SCAIReference> sensorDomains = new ArrayList<SCAIReference>();

            if (matcher.group(11) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(11));

                while (referenceMatcher.find()) {
                    SCAIReference configurationParameter =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    configurationParameters.add(configurationParameter);
                }
            }

            if (matcher.group(19) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(19));

                while (referenceMatcher.find()) {
                    SCAIReference sensorCategory =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    sensorCategories.add(sensorCategory);
                }
            }

            if (matcher.group(27) != null) {
                Matcher referenceMatcher = referencePattern.matcher(matcher.group(27));

                while (referenceMatcher.find()) {
                    SCAIReference sensorDomain =
                            new SCAIReference(
                            parseVariableValue(referenceMatcher.group(3)),
                            parseVariableValue(referenceMatcher.group(2)));
                    sensorDomains.add(sensorDomain);
                }
            }

            builder.addUpdateSensorType(
                    oldSensorType,
                    parseVariableValue(matcher.group(5)),
                    parseVariableValue(matcher.group(6)),
                    dataStreamType,
                    configurationParameters.toArray(new SCAIReference[0]),
                    sensorCategories.toArray(new SCAIReference[0]),
                    sensorDomains.toArray(new SCAIReference[0]),
                    parseVariableValue(matcher.group(35)));
        } // </editor-fold>                   

        return builder;
    }

    private String parseStringValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        String str = parameter.split("=", 2)[1];

        if (str.length() < 3) {
            return "";
        }

        return str.substring(1, str.lastIndexOf("\""));
    }

    private GregorianCalendar parseTimeStamp(String parameter) {
        if (parameter == null) {
            return null;
        }

        XMLGregorianCalendar result = null;
        Matcher tsMatcher =
                Pattern.compile(
                "(\\-?\\d\\d\\d\\d)\\-(\\d?\\d)\\-(\\d\\d)T(\\d\\d)\\:(\\d\\d)\\:(\\d\\d)(?:(?:\\.)(\\d*))?(?:(Z)|(\\+|\\-)(?:(\\d\\d)\\:(\\d\\d)))?").matcher(parameter);

        try {
            if (tsMatcher.matches()) {
                DatatypeFactory df = DatatypeFactory.newInstance();

                //                int year = Integer.parseInt(tsMatcher.group(1));
                //                int month = Integer.parseInt(tsMatcher.group(2));
                //                int day = Integer.parseInt(tsMatcher.group(3));
                //                int hour = Integer.parseInt(tsMatcher.group(4));
                //                int minute = Integer.parseInt(tsMatcher.group(5));
                //                int second = Integer.parseInt(tsMatcher.group(6));
                //                int millisecond = 0;
                //                if (tsMatcher.group(7) != null) {
                //                    millisecond = Integer.parseInt(tsMatcher.group(7));
                //                }
                //                int timezone = 0;
                //                if (tsMatcher.group(8).equals("Z")) {
                //                    // FIXME: set UTC
                //                    timezone = 0;
                //                }
                //                else if (tsMatcher.group(9) != null && tsMatcher.group(10) != null && tsMatcher.group(11) != null) {
                //                    timezone = Integer.parseInt(tsMatcher.group(9)+((Integer.parseInt(tsMatcher.group(10))*60)+Integer.parseInt(tsMatcher.group(11))));
                //                }
                //                result = df.newXMLGregorianCalendar(year, month, day, hour, minute, second, millisecond, timezone);
                result = df.newXMLGregorianCalendar(parameter);
            }
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(InterpreterText.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.toGregorianCalendar();
    }

    private GregorianCalendar parseTimeStampValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        String str = parameter.split("=", 2)[1];

        return parseTimeStamp(str);
    }

    private String parseVariableValue(String parameter) {
        if (parameter == null) {
            return null;
        }

        String str = parameter.split("=", 2)[1];

        return str;
    }

    private IBuilder selectParser(Stack<String> lines, IBuilder builder) {
        while (!lines.empty()) {
            String line = lines.pop();

            if (line.startsWith(Constants.ACKNOWLEDGMENTS[0].substring(0, 3))) {
                builder = parseAcknowledgment(line, lines, builder);
            } else if (line.startsWith(Constants.REGISTRYCMDS[0].substring(0, 1))) {
                builder = parseSensorRegistryControl(line, builder);
            } else if (line.startsWith(Constants.SENSORCMDS[0].substring(0, 1))) {
                builder = parseSensorControl(line, builder);
            } else if (line.startsWith(Constants.MEASUREMENTS[0].substring(0, 1))) {
                builder = parseMeasurements(line, builder);
            } else if (line.startsWith(Constants.IDENTIFICATION[0].substring(0, 1))) {
                builder = parseIdentification(line, builder);
            } else if (line.startsWith(Constants.ACCESSCMDS[0].substring(0, 1))) {
                builder = parseAccessControl(line, builder);
            }
        }

        return builder;
    }

        // use this for testing only
    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) {
//        System.out.println(Pattern.compile(Constants.RREG_PRU).pattern());
//        System.out.println(Pattern.compile(Constants.RURG_PRU).pattern());
        System.out.println(Pattern.compile(Constants.RC_DATYP).pattern());
        System.out.println(Pattern.compile(Constants.RU_DATYP).pattern());
//        System.out.println(Pattern.compile(Constants.ENUM_ITEM).pattern());
//        System.out.println(Pattern.compile(Constants.ACK_EXCP).pattern());
//        System.out.println(Pattern.compile(Constants.SG_CONFG).pattern());
        InterpreterText it = new InterpreterText();
        TranslatorText tt = new TranslatorText();
        //        System.out.println(it.parseTimeStamp("2001-10-26T21:32:52+02:00").toXMLFormat());
        String cmd;
        try {
//            cmd = "t01:mdstream ts=2011-7-29T10:34:44.0 n=MotionDetector_01 n=OFFIS ds=(pa=\"Voltage\" da=\"0\");";
//            cmd = "t01:mdstream ts=2011-07-29T10:34:44+00:00 n=MotionDetector_01 n=OFFIS ds=(pa=\"Voltage\" da=\"0\");";
//            cmd = "t01:rc_datyp n=test2 l=0.5 h=10.25 d=1.0 id=aaa;rc_datyp n=test1 l=0 h=15 d=\"null\" id=bbb;rc_datyp n=test3 r=\"\\S+\" d=\"lorem; ipsum\" id=ccc;rc_datyp n=datatype1 a=\"item1\" a=\"item2\" a=\"item3\" d=\"item2\" id=ddd;rc_datyp n=datatype2 l=0 h=1 a=\"item1\" a=\"item2\" a=\"item3\" d=\"item2\" id=eee;rc_datyp n=datatype3 a=(o=0 n=\"item1\") a=(o=1 n=\"item2\") d=\"item2\" id=fff;";
//            cmd = "t01:ru_datyp rn=test2 n=newtest l=0.2 h=10.0 s=1.5 d=1.0 id=aaa;ru_datyp ri=101 n=newtest2 l=1 h=32 d=\"null\" id=bbb;ru_datyp rn=newtest n=newtest3 d='UVdAUlQxcSE=' id=ccc;ru_datyp rn=ref n=datatype1 a=\"item1\" a=\"item2\" a=\"item3\" d=\"item2\" id=ddd;ru_datyp rn=ref n=datatype2 l=0 h=1 a=\"item1\" a=\"item2\" a=\"item3\" d=\"item2\" id=eee;ru_datyp rn=ref n=datatype3 a=(o=0 n=\"item1\") a=(o=1 n=\"item2\") d=\"item2\" id=fff;";
//            cmd = "t01:r_datyp n=test2 o=aaa;\nr_datyp i=101 id=bbb";
//            cmd = "t01:c_datel t=atomic n=testelement rn=testtype id=aaa;c_datel t=complex n=testelement1 ri=101 ri=123 rn=testelement2 id=bbb;";
//            cmd = "t01:u_datel rn=testelement t=atomic n=test2 rn=test id=aaa;u_datel ri=101 t=complex n=test2 ri=123 rn=bla id=bbb";
//            cmd = "t01:c_dstyp n=teststreamtype1 n=element1 n=element2 i=101 id=aaa;r_dstyp i=123 id=bbb;u_dstyp n=teststreamtyperef n=newname n=element1 n=element2 i=101 id=ccc";
//            cmd = "t01:c_cfgpm n=test1 o=false u=a1 i=true rn=ref1 id=aaa;c_cfgpm n=test2 o=false rn=ref3 ri=ref2 rn=ref1 id=bbb;c_cfgpm n=test3 o=false l=0 h=15 rn=ref1 id=ccc;r_cfgpm rn=ref1;u_cfgpm ri=123 n=test1 o=false u=a1 i=false rn=ref1 id=ddd;u_cfgpm rn=ref1 n=test2 o=false rn=ref3 ri=ref2 rn=ref1 id=eee;u_cfgpm ri=a123 n=test3 o=false l=0 h=15 rn=ref1 id=fff;";
//            cmd = "t01:c_setyp n=name a=adapter rn=dstypref p=(rn=cfgpm1 ri=cfgpm2) c=(rn=secat1 rn=secat2) d=(rn=sedom1 rn=sedom2) id=aaa;c_setyp n=name a=adapter rn=dstypref c=(rn=secat1) d=(rn=sedom1) id=bbb;r_setyp rn=ref1 id=ccc;u_setyp rn=oldsetyp n=name a=adapter rn=dstypref p=(rn=cfgpm1 ri=cfgpm2) c=(rn=secat1 rn=secat2) d=(rn=sedom1 rn=sedom2) id=ddd;u_setyp rn=oldsetyp n=name a=adapter rn=dstypref c=(rn=secat1) d=(rn=sedom1) id=eee;";
//            cmd = "t01:c_sedom n=test1 id=aaa;r_sedom rn=test2 id=bbb;u_sedom rn=ref1 n=test4 id=ccc;";
//            cmd = "t01:c_secat n=test1 d=(rn=bla) id=aaa;c_secat n=test2 rn=bla d=(rn=bla ri=blub) id=bbb;r_secat rn=test2 id=ccc;u_secat rn=ref1 n=test4 rn=blub d=(rn=bla) id=ddd;";
//            cmd = "t01:g_sensr rn=ref123 dn=domainname id=fff;c_sensr n=test1 rn=ref1 rn=ref2 vi=false id=aaa;c_sensr n=test2 ri=bla rn=blub vi=true id=bbb;r_sensr rn=test2 dn=domainname id=ccc;u_sensr rn=ref1 dn=domainname n=test4 rn=blub rn=bla vi=true id=ddd;g_sensr ri=ref1 id=eee;";
//            cmd = "t01:l_sensr id=aaa;";
//            cmd = "t01:rl_setyp id=aaa;rl_setyp t=c rn=catref id=bbb;rl_setyp t=d rn=domref id=ccc;";
//            cmd = "t01:c_opgrp n=test1 id=aaa;r_opgrp rn=test1 id=bbb;u_opgrp rn=test1 n=test2 id=ccc;";
//            cmd = "t01:c_optyp n=test1 m=metatype1 p=(k=key1 v=\"value1\" w=true) p=(k=key2 v=\"value2\" w=false) ds=\"test\" id=aaa;r_optyp rn=test1 id=bbb;u_optyp rn=oldref n=test1 m=metatype1 p=(k=key1 v=\"value1\" w=true) p=(k=key2 v=\"value2\" w=false) ds=\"test\" id=ccc;l_sensr id=ddd;";
//            cmd = "t01:rc_oprtr rn=groupref n=name p=(k=key1 v=\"value1\") p=(k=key2 v=\"value2\") t=s rn=operatortyperef id=aaa;rc_oprtr rn=groupref n=name t=o rn=sensorref dn=domainref id=bbb;rc_oprtr rn=groupref n=name t=input rn=sensorref dn=domainref id=ccc;rr_oprtr rn=test2 rn=grp1 id=ddd;ru_oprtr rn=oldref1 rn=groupref n=name p=(k=key1 v=\"value1\") p=(k=key2 v=\"value2\") t=s rn=operatortyperef id=eee;";
//            cmd = "t01:rc_oprtr rn=groupref n=name lg=true p=(k=key v=\"value\") p=(k=key2 v=\"value2\") t=s rn=operatortyperef id=aaa;rc_oprtr rn=groupref n=name lg=true t=o rn=sensorref id=bbb;rc_oprtr rn=groupref n=name lg=true t=input rn=sensorref id=ccc;rr_oprtr rn=test2 rn=grp1 id=ddd;ru_oprtr rn=ref1 rn=groupref n=name lg=true t=s rn=operatortyperef id=eee;";
//            cmd = "t01:link_op rn=groupref io=(rn=inputref) oo=(rn=outputref) id=aaa;link_op rn=groupref io=(rn=inputref) so=(rn=outputref) id=bbb;";
//            cmd = "t01:c_cfgpm n=test1 o=false u=a1 i=true rn=ref1 id=aaa;";
//            cmd = "t01:rl_oprtr rn=test1 id=aaa;rg_oprtr rn=refoprtr rn=refopgrp id=bbb;";
//            cmd = "t01:c_sensr n=test1 rn=domainref rn=typeref vi=false id=aaa;r_sensr rn=sensrref id=bbb;u_sensr ri=oldref n=name123 rn=domainref rn=typeref vi=true id=ccc;l_sensr id=ddd;";
//            cmd = "t01:de_opgp rn=groupref rn=processingunitref id=aaa;de_opgp rn=groupref1 id=bbb;ud_opgp rn=groupref2 id=ccc;";
//            cmd = "t01:rreg_pru n=name adr=address v=1.0 id=aaa;rurg_pru rn=pruref id=bbb;";
//            cmd = "t01:rl_links rn=groupref id=aaa;";
//            cmd = "t01:ac_userd un=User pw='VGVzdDEyMyE=' pm=(g=\"readDataType\" p=(k=key1 v=\"value1\") ih=false) id=aaa;ac_userd un=User pw='VGVzdDEyMyE=' pm=(g=\"readDataType\" p=(k=key1 v=\"value1\") ih=false) pm=(g=\"readDataElement\" p=(k=key2 v=\"value2\") p=(k=key3 v=\"value3\") ih=false) rl=admin rl=super id=bbb;";
//            cmd = "t01:au_userd rn=refname un=user1 pw=\"bla\" pe=(pd=up pm=1111 rn=refname) id=aaa;ac_userd un=user3 pw=\"blub\" pe=(pd=cp pm=0000) pe=(pd=up pm=0101) pe=(pd=dt pm=1111 ri=refname) id=ccc;";
//            cmd = "t01:idaccess st=29066241-cdb3-496f-804e-273212a55e39;idaccess un=testuser pw=\"#pass123\";rc_secat n=test1 d=(rn=bla) id=aaa;mdstream ts=2001-10-26T21:32:52+02:00 n=sensorname n=domainname da=\"bla\";rc_secat n=test2 rn=bla d=(rn=bla ri=blub) id=bbb;rr_secat rn=test2 id=ccc;ru_secat rn=ref1 n=test4 rn=blub d=(rn=bla) id=ddd;";
//            cmd = "t01:a_cg2dm rn=catname ri=domid id=aaa;r_cg2dm ri=catid rn=domname id=bbb;a_tp2cg rn=typename ri=catid id=ccc;r_tp2cg ri=typeid rn=catname id=ddd;a_tp2dm rn=typename ri=domid id=eee;r_tp2dm ri=typid rn=domname id=fff;a_de2ds rn=dataelementname ri=datastreamtypeid id=ggg;r_de2ds ri=dataelementid rn=datastreamname id=hhh;a_cg2cg rn=catname ri=catid id=iii;r_cg2cg ri=catid rn=catname id=jjj;";
//            cmd = "t01:scf_snsr ri=sensorid cv=(n=name ix=0 va=\"0\") id=aaa;scf_snsr rn=sensorname dn=domainname cv=(n=name va=\"0\") id=bbb;scf_snsr rn=sensorname dn=domainname cv=(n=name cva=(cv=(n=name2 ix=1 va=\"100\"))) id=ccc;scf_snsr ri=sensorid cv=(n=name11 ix=0 cva=(cv=(n=name12 va=\"bla\") cv=(n=name13 ix=0 cva=(cv=(n=name21 va=\"123\"))))) id=ddd;";
//            cmd = "t01:scf_snsr ri=sensorid cv=(n=name11 ix=0 cva=(cv=(n=name12 va=\"v12\") cv=(n=name13 ix=0 va=\"v13\"))) id=aaa;";
//            cmd = "t01:scf_snsr ri=sensorid cv=(n=name11 ix=0 cva=(cv=(n=name12 va=\"bla\") cv=(n=name13 ix=0 cva=(cv=(n=name21 va=\"123\"))))) id=ddd;";
//            cmd = "t01:mdstream ts=2011-02-21T16:12:23 n=sensorname n=domainname ov=0 ds=(pa=\"path/to/whatever\" q=1.0 da=\"data\") ds=(pa=\"path/to/whatever\" q=1.0 et=s em=\"blaaa\" da=\"data\";";
//            cmd = "t01:ack_excp tx=\"bla\" id=aaa;ack_sucs tx=\"blub\" id=bbb;ack_excp tx=\"blub\" ec=101 id=ccc;ack_sucs id=ddd;";
//            cmd = "t01:ack_rply cv=(n=name12 cva=(cv=(n=name21 va=\"123\"))) id=aaa;ack_rply cv=(n=name11 ix=0 cva=(cv=(n=name12 va=\"bla\") cv=(n=name13 ix=0 cva=(cv=(n=name21 va=\"123\"))))) m=(ts=2011-02-21T16:12:23 n=sensorname n=domainname ov=0 ds=(pa=\"pathtowhatever\" et=s em=\"error\" da=\"data\")) cv=(n=name111 ix=0 cva=(cv=(n=name112 va=\"bla\") cv=(n=name113 ix=0 cva=(cv=(n=name121 va=\"123\"))))) id=bbb;";
//            cmd = "t01:ack_rply cv=(n=name11 ix=0 cva=(cv=(n=name12 va=\"v12\") cv=(n=name13 ix=0 va=\"v13\"))) id=aaa;";
//            cmd = "t01:ack_rply m=(ts=2011-02-21T15:52:23 n=Ardunio_01 n=OFFIS ov=10 ds=(pa=\"home/ardunio\" da=\"42\") ds=(pa=\"home/ardunio/bla\" et=u em=\"Fail\" da=\"23\")) id=aaa;";
//            cmd = "t01:ack_rply m=(ts=2011-02-21T15:52:23 n=Ardunio_01 n=OFFIS ov=10 ds=(pa=\"random/number\" q=100 et=u em=\"Unknown\" da=\"42\"));";
//            cmd = "t01:scf_snsr rn=GTH3165 dn=Innotec cv=(n=config cva=(cv=(n=ident cva=(cv=(n=name va=\"GTH3165_1\") cv=(n=domain va=\"Innotec\") cv=(n=id va=\"1234\"))) cv=(n=mode cva=(cv=(n=stream va=\"true\") cv=(n=interval va=\"10000\"))) cv=(n=gprs cva=(cv=(n=medium va=\"gprs\") cv=(n=target va=\"192.168.0.1\"))))) id=CONFIGALL;";
//            cmd = "t01:ack_rply cv=(n=config cva=(cv=(n=ident cva=(cv=(n=name va=\"GTH3165_1\") cv=(n=domain va=\"Innotec\") cv=(n=id va=\"1234\"))) cv=(n=mode cva=(cv=(n=stream va=\"true\") cv=(n=interval va=\"10000\"))) cv=(n=gprs cva=(cv=(n=medium va=\"gprs\") cv=(n=target va=\"192.168.0.1\"))))) id=GETCONFIG;";
//            cmd = "t01:agr_accs rn=sensorname dn=domainname u=(ri=userid) id=aaa;agr_accs rn=sensorname dn=domainname d=(rn=domainname) u=(rn=username) d=(ri=domain2id) d=(rn=domain3name) u=(ri=user2id) id=bbb;";
//            cmd = "t01:idaccess st=728403bc-f98b-4909-b328-ba846fb53c4a;awd_accs rn=sensorname dn=domainname u=(ri=userid) id=aaa;awd_accs rn=sensorname dn=domainname d=(rn=domainname) u=(rn=username) d=(ri=domain2id) d=(rn=domain3name) u=(ri=user2id) id=bbb;";
//            cmd = "t01:rg_prunt rn=operatorgroupname id=aaa;";
//            cmd = "t01:idaccess st=728403bc-f98b-4909-b328-ba846fb53c4a;aau_user un=SuperUser pw='UVdFUlQxcSE=' id=aaa;";
//            cmd = "t01:ack_rply cv=(n=GTHconfig cva=(cv=(n=name va=\"GTH3165_1\") cv=(n=domain va=\"Innotec\") cv=(n=id va=\"1234\") cv=(n=stream va=\"true\") cv=(n=interval va=\"10000\") cv=(n=medium va=\"gprs\") cv=(n=target va=\"192.168.0.1\"))) id=GETCONFIG;";
//            cmd = "t01:aau_user un=username pw='UVdAUlQxcSE=' id=aaa;";
//            cmd = "t01:ack_rply cv=(n=sessionid va=\"728403bc-f98b-4909-b328-ba846fb53c4a\") id=AUTHUSER;";
            cmd = "t01:mdstream ts=2011-03-29T17:08:54 n=Ardunio_01 n=OFFIS ds=(pa=\"movment\" da=\"1023\");";
//            cmd = "t01:mdstream ts=2011-03-11T13:17:15 n=device01 n=OFFIS ds=(pa=\"gps_lat\" da=\"53.148760199546814\") ds=(pa=\"gps_lng\" da=\"8.200950622558594\") ds=(pa=\"mag_x\" da=\"-5.625\") ds=(pa=\"mag_y\" da=\"-11.1875\") ds=(pa=\"mag_z\" da=\"-61.5625\") ds=(pa=\"acc_x\" da=\"0.8036005\") ds=(pa=\"acc_y\" da=\"6.4424243\") ds=(pa=\"acc_z\" da=\"6.2108784\");";
//            cmd = "t01:ru_datyp rn=test2 n=newtest l=1 h=8 d='UVdAUlQxcSE=' id=aaa;";
            System.out.println(cmd);
            SCAIDocument test = it.parse(cmd);
            System.out.println(
                    test.xmlText(new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(4)));
            System.out.println(test.validate());
            System.out.println(tt.translate(test));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
