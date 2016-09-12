package doctools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Cleanup {

	private static final String[] EXTS = new String[] {"html", "htm"};
	private static final Logger log = Logger.getLogger(Cleanup.class);
	private static final String[][] MARKERS = {
		{ "<div class=\"story-feature", "<!-- / story-body -->" },
		{ "<h1 class=\"story-header", "<!-- / story-body -->" },
		{ "<a class=\"story\"", null },
	};

	public void cleanDocs(File sourceDir, File destDir) throws IOException {
		FileUtils.forceMkdir(destDir);
		for (File htmlFile : FileUtils.listFiles(sourceDir, EXTS, false)) {
			process(htmlFile, new File(destDir, htmlFile.getName()));
		}
	}

	private void process(File srcFile, File destFile) throws IOException {
		log.info("Processing " + srcFile);
		String contents = FileUtils.readFileToString(srcFile,
				Charset.forName("UTF-8"));

		contents = trimHtml(contents);
		contents = trimTrailingWhitespace(contents);
		FileUtils.write(destFile, contents, Charset.forName("UTF-8"));
		log.info("Saved " + destFile);
	}

	private String trimTrailingWhitespace(String contents) {
		StringBuilder sb = new StringBuilder(contents.length());
		for (String line : StringUtils.split(contents, '\n')) {
			line = StringUtils.stripEnd(line, null);
			if (!line.isEmpty()) {
				sb.append(line).append('\n');
			}
		}
		return sb.toString();
	}

	private String trimHtml(String contents) {
		String original = contents;
		for (String[] markerPair : MARKERS) {
			contents = trimContents(contents, markerPair[0], markerPair[1]);
		}

		int paras = StringUtils.countMatches(contents, "<p>");
		log.log(paras < 3 ? Level.WARN : Level.INFO, paras
				+ " paragraphs in result.");
		if (contents.length() == original.length()) {
			log.warn("Not trimmed.");
		}
		return contents;
	}

	private String trimContents(String contents, String start, String end) {
		int beginIndex = 0;
		if (start != null) {
			log.info("Begin marker " + start + " not found.");
			beginIndex = contents.indexOf(start);
			beginIndex = Math.max(0, beginIndex);
		}

		int endIndex = contents.length();
		if (end != null) {
			endIndex = contents.lastIndexOf(end);
			if (endIndex < 0) {
				log.info("End marker " + end + " not found.");
				endIndex = contents.length() - end.length();
			}
			endIndex += end.length();
		}

		return contents.substring(beginIndex, endIndex);
	}

	public static void main(String[] args) throws IOException {
		File inPath = new File("C:/worktemp/diplomna/raw_car_crash_docs");
		File outPath = new File(inPath.getParentFile(), "trimmed_docs");
		new Cleanup().cleanDocs(inPath, outPath);
	}

}
