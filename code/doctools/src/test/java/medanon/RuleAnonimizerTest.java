package medanon;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RuleAnonimizerTest {

	private final File tempFile;

	public RuleAnonimizerTest() throws IOException {
		tempFile = File.createTempFile("medanon-unittest", ".txt");
	}

	@BeforeClass
	public static void init() {
		GateUtil.initGate();
	}

	@After
	public void cleanup() {
		tempFile.delete();
	}

	@Test
	public void testCreation() throws IOException {
		new RuleAnonimizer().close();
	}

	@Test
	public void testAnonymize() throws IOException, AnonymizationException {
		try (RuleAnonimizer anon = new RuleAnonimizer()) {
			URL inputUrl = getClass().getResource("/sample.txt");
			Assert.assertNotNull(inputUrl);
			anon.anonymize(inputUrl.toString(), tempFile.toString());
			System.out.println("Output is in " + tempFile.getCanonicalPath());
			System.out.println(FileUtils.readFileToString(tempFile));
		}
	}

}
