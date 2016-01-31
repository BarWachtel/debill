package core.parse;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 30/01/2016.
 */
public class BillTextParserTest extends TestCase {

	BillTextParser billTextParser = new BillTextParser();

	@Test
	public void testClearInhumanChars() {
		String badness = "ן»¿1 Luchador           7.00";
		String squeackyClean = billTextParser.clearInhumanCharacters(badness);
		Assert.assertEquals("1 Luchador           7.00", squeackyClean);
	}

	@Test
	public void testParse() throws Exception {
		List<String> billItems = getAcadaiaBill();
		for(String billItem : billItems) {
			System.out.println(billItem);
		}
		System.out.println();

		List<ParsedBillItem> parsedBillItems = billTextParser.parse(billItems);
		for (ParsedBillItem parsedBillItem : parsedBillItems) {
			System.out.println(parsedBillItem.toString());
		}
	}

	public List<String> getAcadaiaBill() {
		List<String> acadiaBill = new LinkedList<>();

//		acadiaBill.add("ן»¿1 Luchador           7.00");
//		acadiaBill.add("1 To Tell the Truth  500");
//		acadiaBill.add("1 Muskoka            7.00");
//		acadiaBill.add("1 Cornbread          800");
//		acadiaBill.add("1 Foie Gras          1700");
		acadiaBill.add("1 Gumbo 50C          1200");
		acadiaBill.add("1 Pimpfish           24 00");
		acadiaBill.add("1 Steel head         23 00");
		acadiaBill.add("1 Carrot             9 00");
		acadiaBill.add("1 Rice Pudding       10.00");

		return acadiaBill;
	}
}