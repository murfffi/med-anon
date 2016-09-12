package doctools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;

public class I2B2StaxParser /* extends I2B2CollectionParser */{

	private static final XMLInputFactory factory = XMLInputFactory
			.newInstance();

	public static void main(String[] args) throws IOException, XMLStreamException {
		File sourceXml = new File(
				"c:/worktemp/deid_surrogate_train_all_version2.xml");
		findErrors(sourceXml);
	}

	private static void findErrors(File sourceXml)
			throws FileNotFoundException, XMLStreamException {
		Reader is = new FileReader(sourceXml);
		try {
			XMLEventReader inputXml = factory.createXMLEventReader(is);
			boolean inPhi = false;
			while (inputXml.hasNext()) {
				XMLEvent event = inputXml.nextEvent();
				StartElement se = event.isStartElement() ? event
						.asStartElement() : null;
						if (se != null) {
							if (inPhi) {
								throw new XMLStreamException("Start tag in PHI",
										se.getLocation());
							}
							if (se.getName().getLocalPart() == "PHI") {
								inPhi = true;
							}
						}

						if (event.isEndElement() && event.asEndElement().getName().getLocalPart() == "PHI") {
							inPhi = false;
						}
			}
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
}
