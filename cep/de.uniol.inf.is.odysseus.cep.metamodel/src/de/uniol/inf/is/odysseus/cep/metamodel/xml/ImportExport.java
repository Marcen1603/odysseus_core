package de.uniol.inf.is.odysseus.cep.metamodel.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

/**
 * Diese Klasse stellt Methoden zum Speichern von Automaten in xml bzw. zum
 * Laden von Automaten aus xml zur Verfügung.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class ImportExport {

	public ImportExport() {

	}

	/**
	 * Lädt einen Automaten aus einer xml-Datei.
	 * 
	 * @param file
	 *            Die Datei, aus der geladen werden soll.
	 * @return Der geladene Automat.
	 * @throws JAXBException
	 *             Falls bei der xml-Verarbeitung Fehler auftreten.
	 * @throws FileNotFoundException
	 *             Falls die angegebene Datei nicht gefunden werden kann.
	 */
	public StateMachine loadFromFile(File file) throws JAXBException,
			FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(StateMachine.class);
		Unmarshaller um = context.createUnmarshaller();
		return (StateMachine) um.unmarshal(new FileReader(file));
	}

	/**
	 * Speichert einen Automaten als xml-Datei.
	 * 
	 * @param stateMachine
	 *            Der Automat der gespeichert werden soll.
	 * @param file
	 *            Die Datei, in die geschrieben werden soll
	 * @throws JAXBException
	 *             Falls bei der xml-Verarbeitung irgendwelche Fehler auftreten.
	 * @throws IOException
	 *             Falls aus irgendwelchen Gründen nicht in die angegebene Datei
	 *             geschrieben werden konnt.
	 */
	public void saveToFile(StateMachine stateMachine, File file)
			throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(StateMachine.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		Writer writer = null;
		try {
			writer = new FileWriter(file);
			m.marshal(stateMachine, writer);
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
}
