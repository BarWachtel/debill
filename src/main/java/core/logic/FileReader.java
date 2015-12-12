package core.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 06/12/2015.
 */
public class FileReader {
	public static void main(String[] args) throws IOException {
		FileReader fileReader = new FileReader();
		String fileName = "txt/bill_crop.txt";

		readFile(fileName);
	}

	private static void readFile(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
		List<String> billLines = new ArrayList<>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			billLines.add(line);
		}

		BillParser billParser = new BillParser();
		List<BillItem> billItems = billParser.parse(billLines);
		for (BillItem billItem : billItems) {
			System.out.println(billItem.toString());
		}
	}
}
