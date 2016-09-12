package medanon;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {

		File inputFile = args.length > 0 ? new File(args[0]) : new File(
				"../samples/sample.txt");
		URL inputUrl = inputFile.toURI().toURL();
		File outputFile = new File(inputFile.getParentFile(),
				inputFile.getName() + ".anon.txt");

		GateUtil.initGate();

		try (Anonymizer anon = new AnonymizerFactory()
		.createDefaultAnonymizer()) {
			anon.anonymize(inputUrl.toString(), outputFile.toString());
			System.out.println("Output is in " + outputFile.getCanonicalPath());
		}
	}

}
