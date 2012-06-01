/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack;

import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.control.ControlModules;
import de.offis.scampi.stack.scai.translator.TranslatorXML;

/**
 *
 * @author Claas
 */
public class Stack {

    private Analyser analyser = new Analyser();
    private IBuilder builder = new BuilderSCAI20();
    private ITranslator translator = new TranslatorXML();

    public ProtocolObject loadControlModules(ProtocolObject stackObject) {
        stackObject.setControlModules(new ControlModules());
        return stackObject;
    }

    public Object parsePacket(Object packet) throws Exception {
        ProtocolObject stackObject = null;

        try {
            stackObject = analyser.buildSCAIStackObject(packet);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return generateExceptionResponse(e.getMessage());
        }

        if (stackObject == null || stackObject.getContent() == null) {
            return generateExceptionResponse("Packet Corrupted");
        } else {

            Object response = null;

            stackObject = loadControlModules(stackObject);

            try {
                response = stackObject.getProcessor().process(stackObject);
            } catch (Exception ex) {
                ex.printStackTrace();
                response = generateExceptionResponse(ex + " " + ex.getMessage());
            }

            if (response != null) {
                return response;
            } else {
                return null;
            }
        }
    }

    private String generateExceptionResponse(String text) {
        builder.addAcknowledgmentException(text, null, "Unknown");
        return (String) translator.translate(builder.getDocument());
    }
}
