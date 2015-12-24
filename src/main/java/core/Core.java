package core;

import core.ocr.OcrClient;
import core.parse.BillItem;
import core.parse.BillParser;
import generalutils.FileUtils;
import generalutils.thread.ThreadLocalUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/12/2015.
 */
public class Core {
	public List<BillItem> imgToBillItems(File imgFile) {
		String imgFileName = FileUtils.saveBinaryFile(imgFile);
		List<String> billLines = OcrClient.getImgLines(imgFileName);

		BillParser billParser = new BillParser();
		List<BillItem> billItems = billParser.parse(billLines);

		// Lets see whats going on here
		for (BillItem billItem : billItems) {
			System.out.println(billItem.toString());
		}

		return billItems;
	}

	public static void main(String[] args) {
		String imgName = "img/bill_crop.png";
		File imgFile = FileUtils.loadFile(imgName);

		Core core = new Core();
		core.imgToBillItems(imgFile);
	}
}
