package medanon;

import java.io.IOException;

public class AnonymizerFactory {

	public Anonymizer createDefaultAnonymizer() throws IOException {
		return new RuleAnonimizer();
	}
}
