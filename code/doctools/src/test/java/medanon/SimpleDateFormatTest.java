package medanon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class SimpleDateFormatTest {

	private final SimpleDateFormat partialFormat = new SimpleDateFormat("MM/dd");

	@Test
	public void testPartialDateParsing() throws ParseException {
		testSame(false, "121/78");
		testSame(true, "12/13");
		testSame(true, "07/09");
	}

	private void testSame(boolean expectedSame, String dateString)
			throws ParseException {
		Date d = partialFormat.parse(dateString);
		String reformated = partialFormat.format(d);
		String msg = String.format("Expected %s but were %s, %s",
				expectedSame ? "same" : "different", dateString, reformated);
		Assert.assertEquals(msg, expectedSame, dateString.equals(reformated));
	}
}
