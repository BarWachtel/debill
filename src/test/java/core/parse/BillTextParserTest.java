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
	public void testParseArcadia() throws Exception {
		List<String> billItems = getAcadaiaBill();
		parseBill(billItems);
	}

	@Test
	public void testParseOyster() throws Exception {
		List<String> billItems = getOysterBill();
		parseBill(billItems);
	}

	private void parseBill(List<String> billItems) {
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

		acadiaBill.add("ן»¿1 Luchador           7.00");
		acadiaBill.add("1 To Tell the Truth  500");
		acadiaBill.add("1 Muskoka            7.00");
		acadiaBill.add("1 Cornbread          800");
		acadiaBill.add("1 Foie Gras          1700");
		acadiaBill.add("1 Gumbo 50C          1200");
		acadiaBill.add("1 Pimpfish           24 00");
		acadiaBill.add("1 Steel head         23 00");
		acadiaBill.add("1 Carrot             9 00");
		acadiaBill.add("1 Rice Pudding       10.00");

		return acadiaBill;
	}

	public List<String> getOysterBill() {
		List<String> oysterBill = new LinkedList<>();

//		oysterBill.add("ן»¿Oysters            6.00");
//		oysterBill.add("1 Swlts            12.00");
//		oysterBill.add("1 Duck Terrine     14.00");
//		oysterBill.add("1    Prlx Dessert  0.00");
//		oysterBill.add("1 AG Malbec        25.00");
//		oysterBill.add("1 Soup             8.00");
//		oysterBill.add("1 TagliateUe       16.00");
		oysterBill.add("1    04S Brcwnie   9.00");

		return oysterBill;
	}
}