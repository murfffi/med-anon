package medanon;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.corpora.DocumentContentImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Anonymizer based on rules defined in a GATE Pipeline
 * 
 * @author mnozchev
 */
public class RuleAnonimizer implements Anonymizer {

	private static final Logger log = Logger.getLogger(RuleAnonimizer.class);

	private static final String appLocation = System.getProperty("app.loc",
			"c:/sources/med-anon/gate/pipelines/anon_v1.gapp");

	private final CorpusController app;

	public RuleAnonimizer() throws IOException {
		this(new File(appLocation).getCanonicalFile());
	}

	public RuleAnonimizer(File appFile) throws IOException {
		if (!appFile.isFile()) {
			throw new IllegalArgumentException(appFile + " is not a file.");
		}
		log.info(String.format("Loading pipeline %s ...", appFile));
		try {
			app = GateUtil.loadApplication(appFile);
		} catch (GateException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void anonymize(String inputUrl, String outputPath)
			throws AnonymizationException {
		URL input;
		try {
			input = new URL(inputUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		Document doc;
		try {
			doc = Factory.newDocument(input);
		} catch (ResourceInstantiationException e) {
			throw new IllegalArgumentException(e);
		}

		String anonymizedText;
		try {
			annotate(doc);
			anonymizedText = getReplacedAnonData(doc);
		} finally {
			doc.cleanup();
		}

		try {
			FileUtils.write(new File(outputPath), anonymizedText);
		} catch (IOException e) {
			throw new AnonymizationException("Could not save output file.", e);
		}
	}

	@Override
	public String anonymizeString(String text) throws AnonymizationException {
		Document doc;
		try {
			doc = Factory.newDocument(text);
		} catch (ResourceInstantiationException e) {
			throw new IllegalArgumentException(e);
		}

		try {
			annotate(doc);
			return getReplacedAnonData(doc);
		} finally {
			doc.cleanup();
		}
	}

	private String getReplacedAnonData(Document doc) {
		AnnotationSet privateDataFields = doc.getAnnotations().get("PHI");
		if (privateDataFields != null) {
			for (Annotation ann : privateDataFields) {
				replaceAnonField(doc, ann);
			}
		} else {
			log.warn("null PHI ann set.");
		}

		String docContent = doc.getContent().toString();
		docContent.replace("\n", System.getProperty("line.separator"));
		return docContent;
	}

	private void replaceAnonField(Document doc, Annotation ann) {
		String privateFieldType = (String) ann.getFeatures().get("type");
		privateFieldType = privateFieldType != null ? privateFieldType : "PRIVATE";
		DocumentContent replacement = new DocumentContentImpl(("[["
				+ privateFieldType + "]]").toUpperCase());
		try {
			doc.edit(ann.getStartNode().getOffset(), ann.getEndNode()
					.getOffset(), replacement);
		} catch (InvalidOffsetException e) {
			throw new RuntimeException("Unexpected Gate exception.", e);
		}
	}

	private void annotate(Document doc) throws AnonymizationException {
		Corpus corpus;
		try {
			corpus = Factory.newCorpus("corpus");
		} catch (ResourceInstantiationException e) {
			throw new IllegalStateException("Unexpected exception in GATE.", e);
		}

		corpus.add(doc);

		app.setCorpus(corpus);
		try {
			app.execute();
		} catch (ExecutionException e) {
			throw new AnonymizationException(e);
		}
	}

	@Override
	public void close() {
		app.cleanup();
	}

}
