package core.parse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
		billLine = clearInhumanCharacters(billLine);
		String[] billParts = billLine.split("\\s+");

		NameExtracted nameExtracted = extractName(billParts);
		String[] partsWithoutName = nameExtracted.getPartsWithoutName();
		if (partsWithoutName != null && partsWithoutName.length > 0) {
			parsedBillItem = new ParsedBillItem();
			parsedBillItem.setName(nameExtracted.getName());
			findAndSetNumericalValues(parsedBillItem, partsWithoutName);
		}

		return parsedBillItem;
	}

	String clearInhumanCharacters(String str) {
		return str.replaceAll("[^a-zA-Z0-9.,;: ]", "");
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
		for (int i = 0, len = numericalParts.length; i < len; i++) {
			String numericalPart = numericalParts[i];
			try {
				if (i == len - 1 && hasAtleastFourDigits(numericalPart)) {
					numericalPart = numericalPart.substring(0, numericalPart.length() - 2);
				}
				numbers.add(Float.parseFloat(numericalPart));
			} catch (NumberFormatException e) {
				if (!numericalPart.isEmpty()) {
					numbers.add(Float.parseFloat(replaceCharactersWithDigits(numericalPart)));
				}
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

		} else if (c == 0 && a >= 0 && b >= 0) {
			handleTwoNumbers(parsedBillItem, numbers);
		}
		else {
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
		} else if (secondIsQuantity(a, b)) {
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
			if (!isMostlyCharacters(part)) {
				numericalParts.add(part);
			}
		}
		return numericalParts.toArray(new String[numericalParts.size()]);
	}

	private NameExtracted extractName(String[] billParts) {
		StringBuilder sb = new StringBuilder();
		boolean reachedNamePart = false;
		int startOfName, endOfName;
		startOfName = endOfName = -1;

		for (int i = 0; i < billParts.length; i++) {
			if (isMostlyCharacters(billParts[i]) || (reachedNamePart && (i - billParts.length > 2)) || (reachedNamePart && hasAtleastFourDigits(
					billParts[billParts.length - 1]) && i < (billParts.length - 1))) {
				if (!reachedNamePart) {
					startOfName = i;
					reachedNamePart = true;
				}
				sb.append(billParts[i]).append(' ');
			} else {
				if (reachedNamePart) {
					endOfName = i;
					break;
				}
			}

		}

		NameExtracted nameExtracted = new NameExtracted();
		nameExtracted.setName(sb.toString().trim());
		nameExtracted.setOriginalParts(billParts);
		nameExtracted.setStartOfName(startOfName);
		nameExtracted.setEndOfName(endOfName);

		return nameExtracted;
	}

	private boolean hasAtleastFourDigits(String billPart) {
		return getNumDigitsInPart(billPart) >= 4;
	}

	private boolean isMostlyCharacters(String part) {
		int numDigitsInPart = getNumDigitsInPart(part);
		// might have decimal parts as Ol, OI etc.
		return numDigitsInPart < (part.length() - numDigitsInPart);
	}

	private int getNumDigitsInPart(String part) {
		int numDigitsInPart = 0;
		for (Character ch : part.toCharArray()) {
			if (Character.isDigit(ch)) {
				numDigitsInPart++;
			}
		}
		return numDigitsInPart;
	}

	private class NameExtracted {
		String[] originalParts;
		int startOfName, endOfName;
		String name;

		public String[] getOriginalParts() {
			return originalParts;
		}

		public String[] getPartsWithoutName() {
			String[] result = null;

			if (originalParts.length > 0 && startOfName >= 0 && endOfName >= startOfName) {
				RangeRemoveSupportArrayList<String> numericalParts = new RangeRemoveSupportArrayList<>(originalParts.length);
				numericalParts.addAll(Arrays.asList(originalParts));
				numericalParts.removeRange(startOfName, endOfName);
				result = numericalParts.toArray(new String[numericalParts.size()]);
			}

			return result;
		}

		public void setOriginalParts(String[] originalParts) {
			this.originalParts = originalParts;
		}

		public void setStartOfName(int startOfName) {
			this.startOfName = startOfName;
		}

		public void setEndOfName(int endOfName) {
			this.endOfName = endOfName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public class RangeRemoveSupportArrayList<E> extends ArrayList<E> {
		public RangeRemoveSupportArrayList(int length) {
			super(length);
		}

		public void removeRange(int fromIndex, int toIndex) {
			super.removeRange(fromIndex, toIndex);
		}

	}
}