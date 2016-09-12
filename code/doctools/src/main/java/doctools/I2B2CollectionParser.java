package doctools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public abstract class I2B2CollectionParser {

	public abstract void extractTextFiles(File source, File destination)
			throws IOException, FormatException;

	protected static String getPrefix(File sourceFile) {
		return FilenameUtils.getBaseName(sourceFile.getName());
	}
}
