package medanon;

import java.io.Closeable;

public interface Anonymizer extends Closeable {

	void anonymize(String inputUrl, String outputPath)
			throws AnonymizationException;

	String anonymizeString(String text) throws AnonymizationException;

}
