package doctools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class I2B2Corpus {

	private static final Logger log = Logger.getLogger(I2B2Corpus.class);

	private final File location;
	private final I2B2CollectionParser i2b2Parser;

	public I2B2Corpus(File location, I2B2CollectionParser i2b2Parser)
			throws IOException {
		location = location.getCanonicalFile();
		if (!location.exists()) {
			throw new IllegalArgumentException(location + " doesn't exist");
		}
		this.location = location;
		this.i2b2Parser = i2b2Parser;
		log.info("Initialized I2B2Corpus for " + this.location);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FormatException
	 */
	public static void main(String[] args) throws IOException, FormatException {
		File corpusLocation = new File(args[0]);
		File destination = new File(args[1]);
		new I2B2Corpus(corpusLocation, new I2B2DomParser())
				.extractTextFiles(destination);

	}

	public void extractTextFiles(File destination) throws IOException,
	FormatException {
		for (File sourceFile : getSourceFiles()) {
			i2b2Parser.extractTextFiles(sourceFile, destination);
		}

	}

	private Collection<File> getSourceFiles() {
		if (location.isFile()) {
			return Arrays.asList(location);
		} else {
			return FileUtils
					.listFiles(location, new String[] { "xml" }, false /* recursive */);
		}
	}

}
