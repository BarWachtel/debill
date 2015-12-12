package core.logic;

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
			BillItem billItem = parseItem(billLine);
			if (billItem != null) {
				billItems.add(billItem);
			}
		}
		return billItems;
	}

	private BillItem parseItem(String billLine) {
		BillItem billItem = null;
		String[] billParts = billLine.split("\\s+");

		String name = extractName(billParts);
		String[] numericalParts = removeName(billParts);
		if (numericalParts.length > 0) {
			billItem  = new BillItem();
			billItem.setName(name);
			findAndSetNumericalValues(billItem, numericalParts);
		}

		return billItem;
	}

	private void findAndSetNumericalValues(BillItem billItem, String[] numericalParts) {
		List<Float> numbers = parseNumbers(numericalParts);

		if (numbers.size() == 1) {
			billItem.setPrice(numbers.get(0));
			billItem.setTotal(billItem.getPrice());
			billItem.setQuantity(1);
		} else if (numbers.size() == 2) {
			handleTwoNumbers(billItem, numbers);
		} else if (numbers.size() == 3) {
			handleThreeNumbers(billItem, numbers);
		}
	}

	private List<Float> parseNumbers(String[] numericalParts) {
		List<Float> numbers = new ArrayList<>();
		for (String numericalPart : numericalParts) {
			try {
				numbers.add(Float.parseFloat(numericalPart));
			} catch (NumberFormatException e) {
				numbers.add(Float.parseFloat(replaceCharactersWithDigits(numericalPart)));
			}
		}
		return numbers;
	}

	private String replaceCharactersWithDigits(String numericalPart) {
		StringBuilder sb = new StringBuilder();
		for (Character ch : numericalPart.toLowerCase().toCharArray()) {
			if (!Character.isDigit(ch) && ch != '.') {
				sb.append(replaceCharWithDigit(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private char replaceCharWithDigit(Character ch) {
		// All characters were converted to lowercase
		switch (ch) {
		case 'i':
		case 'l':
			return '1';
		case 'o':
		case 'd':
		case 'q':
			return '0';
		case 't':
			return '7';
		case '&':
		case 'h':
		case 'k':
			return '4';
		case 'b':
		case 's':
			return '8';
		}
		return '0';
	}

	private void handleThreeNumbers(BillItem billItem, List<Float> numbers) {
		float total, price;
		int quantity;

		float a = numbers.get(0);
		float b = numbers.get(1);
		float c = numbers.get(2);

		if (firstQuantityMiddlePriceLastTotal(a, b, c, billItem)) {

		} else if (firstQuantityMiddlePriceLastTotal(a, c, b, billItem)) {

		} else if (firstQuantityMiddlePriceLastTotal(b, c, a, billItem)) {

		} else {
			billItem.setPrice(a);
			billItem.setTotal(b);
			billItem.setQuantity((int) c);
		}
	}

	private boolean firstQuantityMiddlePriceLastTotal(float a, float b, float c, BillItem billItem) {
		boolean found = false;
		if (a * b == c) {
			billItem.setTotal(c);

			if (a < b) {
				billItem.setQuantity((int) a);
				billItem.setPrice(b);
			} else {
				billItem.setQuantity((int) b);
				billItem.setPrice(a);
			}

			found = true;
		}

		return found;
	}

	private void handleTwoNumbers(BillItem billItem, List<Float> numbers) {
		float total, price;
		int quantity;

		float a = numbers.get(0);
		float b = numbers.get(1);

		if (a == 0 || b == 0) {
			quantity = (int) Math.max(a, b);
			total = price = 0;
		}
		else if (secondIsQuantity(a, b)) {
			quantity = (int) b;
			total = a;
			price = total / quantity;
		} else if (secondIsQuantity(b, a)) {
			quantity = (int) a;
			total = b;
			price = total / quantity;
		} else {
			if (a != b) {
				// The smaller number is actually a float!
				total = Math.max(a, b);
				price = Math.min(a, b);
				quantity = (int) (total / price);
			} else {
				total = price = a;
				quantity = 1;
			}
		}

		billItem.setPrice(price);
		billItem.setTotal(total);
		billItem.setQuantity(quantity);
	}

	private boolean secondIsQuantity(float a, float b) {
		if (a > b && Math.abs(b) == b) {
			return true;
		}
		return false;
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