package medanon;

import gate.CorpusController;
import gate.Gate;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

public class GateUtil {

	private static final String GATE_HOME_PROP = "gate.home";

	public static void initGate() {
		if (System.getProperty(GATE_HOME_PROP) == null) {
			System.setProperty(GATE_HOME_PROP, "c:/gate71");
		}

		try {
			Gate.init();
		} catch (GateException e) {
			throw new RuntimeException("Gate failed to initialize.", e);
		}
	}

	public static CorpusController loadApplication(File appFile)
			throws GateException, IOException {
		return (CorpusController) PersistenceManager
				.loadObjectFromFile(appFile);
	}

}
