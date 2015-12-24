package core.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 06/12/2015.
 */
public class BillTextParser {
	public BillTextParser() {
	}

	public List<ParsedBillItem> parse(List<String> billLines) {
		List<ParsedBillItem> parsedBillItems = new ArrayList<>();
		for (String billLine : billLines) {
			ParsedBillItem parsedBillItem = parseItem(billLine);
			if (parsedBillItem != null) {
				parsedBillItems.add(parsedBillItem);
			}
		}
		return parsedBillItems;
	}

	private ParsedBillItem parseItem(String billLine) {
		ParsedBillItem parsedBillItem = null;
		String[] billParts = billLine.split("\\s+");

		String name = extractName(billParts);
		String[] numericalParts = removeName(billParts);
		if (numericalParts.length > 0) {
			parsedBillItem = new ParsedBillItem();
			parsedBillItem.setName(name);
			findAndSetNumericalValues(parsedBillItem, numericalParts);
		}

		return parsedBillItem;
	}

	private void findAndSetNumericalValues(ParsedBillItem parsedBillItem, String[] numericalParts) {
		List<Float> numbers = parseNumbers(numericalParts);

		if (numbers.size() == 1) {
			parsedBillItem.setPrice(numbers.get(0));
			parsedBillItem.setTotal(parsedBillItem.getPrice());
			parsedBillItem.setQuantity(1);
		} else if (numbers.size() == 2) {
			handleTwoNumbers(parsedBillItem, numbers);
		} else if (numbers.size() == 3) {
			handleThreeNumbers(parsedBillItem, numbers);
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

	private void handleThreeNumbers(ParsedBillItem parsedBillItem, List<Float> numbers) {
		float total, price;
		int quantity;

		float a = numbers.get(0);
		float b = numbers.get(1);
		float c = numbers.get(2);

		if (firstQuantityMiddlePriceLastTotal(a, b, c, parsedBillItem)) {

		} else if (firstQuantityMiddlePriceLastTotal(a, c, b, parsedBillItem)) {

		} else if (firstQuantityMiddlePriceLastTotal(b, c, a, parsedBillItem)) {

		} else {
			parsedBillItem.setPrice(a);
			parsedBillItem.setTotal(b);
			parsedBillItem.setQuantity((int) c);
		}
	}

	private boolean firstQuantityMiddlePriceLastTotal(float a, float b, float c, ParsedBillItem parsedBillItem) {
		boolean found = false;
		if (a * b == c) {
			parsedBillItem.setTotal(c);

			if (a < b) {
				parsedBillItem.setQuantity((int) a);
				parsedBillItem.setPrice(b);
			} else {
				parsedBillItem.setQuantity((int) b);
				parsedBillItem.setPrice(a);
			}

			found = true;
		}

		return found;
	}

	private void handleTwoNumbers(ParsedBillItem parsedBillItem, List<Float> numbers) {
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

		parsedBillItem.setPrice(price);
		parsedBillItem.setTotal(total);
		parsedBillItem.setQuantity(quantity);
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