package doctools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class I2B2DomParser extends I2B2CollectionParser {

	private static final DocumentBuilderFactory dbf = DocumentBuilderFactory
			.newInstance();
	private static final TransformerFactory tf = TransformerFactory
			.newInstance();

	private static final XPath xpath = XPathFactory.newInstance().newXPath();
	private static final Logger log = Logger.getLogger(I2B2DomParser.class);
	private final DocumentBuilder xmlParser;

	public I2B2DomParser() {
		try {
			this.xmlParser = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException("Unexpected XML setup error", e);
		}
	}

	@Override
	public void extractTextFiles(File sourceFile, File destination)
			throws IOException, FormatException {
		InputStream is = new FileInputStream(sourceFile);
		try {
			Document doc = xmlParser.parse(is);
			NodeList textNodes = (NodeList) xpath.evaluate("//TEXT", doc,
					XPathConstants.NODESET);
			for (int i = 0; i < textNodes.getLength(); ++i) {
				extractTextFile(textNodes.item(i), getPrefix(sourceFile),
						destination);
			}
		} catch (SAXException e) {
			throw new FormatException("Bad XML in " + sourceFile, e);
		} catch (XPathExpressionException e) {
			throw new FormatException("XPath failed in " + sourceFile, e);
		} finally {
			IOUtils.closeQuietly(is);
		}

	}

	private void extractTextFile(Node textNode, String prefix, File destination)
			throws IOException {
		String text = textNode.getTextContent();
		Node recordNode = textNode.getParentNode();
		String recordId = getIdOfRecord(recordNode);
		if (recordNode.getNodeName() != "RECORD" || recordId == null) {
			log.warn(String.format("Unexpected parent node %s of text %s",
					recordNode.getNodeName(), Utils.summary(text)));
			return;
		}

		File destFile = new File(destination, String.format("%s__%s.xml",
				prefix, recordId));
		Transformer serializer;
		try {
			serializer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new IllegalStateException("Unexpected XML setup error.", e);
		}
		serializer.setParameter(OutputKeys.METHOD, "html");
		Writer out = new FileWriter(destFile);
		try {
			serializer
			.transform(new DOMSource(textNode), new StreamResult(out));
		} catch (TransformerException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
		// FileUtils.writeStringToFile(destFile, text);
	}

	private String getIdOfRecord(Node recordNode) {
		Node attrNode = recordNode.getAttributes().getNamedItem("ID");
		return attrNode == null ? null : attrNode.getNodeValue();
	}

}
