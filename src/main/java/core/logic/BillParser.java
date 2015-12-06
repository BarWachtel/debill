package core.logic;

import database.entity.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 06/12/2015.
 */
public class BillParser {
	public BillParser() {
	}

	public List<BillItem> parse(List<String> billLines) {
		List<BillItem> billItems = new ArrayList<>();
		for (String billLine : billLines) {
			billItems.add(parseItem(billLine));
		}
		return billItems;
	}

	private BillItem parseItem(String billLine) {
		BillItem billItem = new BillItem();
		String[] billParts = billLine.split("\\s+");

		String name = extractName(billParts);
		String[] numericalParts = removeName(billParts);

		billItem.setName(name);
		return billItem;
	}

	private String[] removeName(String[] billParts) {
		List<String> numericalParts = new ArrayList<>();
		for (String part : billParts) {
			if (!isPartOfName(part)) {
				numericalParts.add(part);
			}
		}
		return numericalParts.toArray(new String[numericalParts.size()]);
	}

	private String extractName(String[] billParts) {
		StringBuilder sb = new StringBuilder();
		boolean reachedNamePart = false;
		for (String part : billParts) {
			if (isPartOfName(part)) {
				reachedNamePart = true;
				sb.append(part).append(' ');
			} else {
				if (reachedNamePart) {
					break;
				}
			}
		}

		return sb.toString().trim();
	}

	private boolean isPartOfName(String part) {
		int numDigitsInPart = 0;
		for (Character ch : part.toCharArray()) {
			if (Character.isDigit(ch)) {
				numDigitsInPart++;
			}
		}
		// might have decimal parts as Ol, OI etc.
		return numDigitsInPart < (part.length() - numDigitsInPart);
	}
}