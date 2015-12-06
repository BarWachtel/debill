package core.ocr;

import generalutils.ocrsdk.Client;
import generalutils.ocrsdk.ProcessingSettings;
import generalutils.ocrsdk.Task;

import java.util.Vector;

/**
 * Created by user on 06/12/2015.
 */
public class OcrClient {
	private static Client restClient;

	static {
		restClient = new Client();
		// replace with 'https://cloud.ocrsdk.com' to enable secure connection
		restClient.serverUrl = "http://cloud.ocrsdk.com";
		restClient.applicationId = ClientSettings.APPLICATION_ID;
		restClient.password = ClientSettings.PASSWORD;
	}

	public static void main(String[] args) throws Exception {
		String imgName = "bill_he.png";
		String outputTextFile = "bill_he.txt";

		restClient = new Client();
		// replace with 'https://cloud.ocrsdk.com' to enable secure connection
		restClient.serverUrl = "http://cloud.ocrsdk.com";
		restClient.applicationId = ClientSettings.APPLICATION_ID;
		restClient.password = ClientSettings.PASSWORD;

		Vector<String> ocrArgs = new Vector<>();
//		ocrArgs.add("recognize");
		ocrArgs.add("--lang=hebrew,english");
		ocrArgs.add(imgName);
		ocrArgs.add(outputTextFile);

		performRecognition(ocrArgs);
	}

//	public String imageToText(String srcFile, String outputFile) {
//
//	}

	private static void performRecognition(Vector<String> argList)
			throws Exception {
		String language = ArgOptions.extractRecognitionLanguage(argList);
		String outputPath = argList.lastElement();
		argList.remove(argList.size() - 1);
		// argList now contains list of source images to process

		ProcessingSettings.OutputFormat outputFormat = outputFormatByFileExt(outputPath);

		ProcessingSettings settings = new ProcessingSettings();
		settings.setLanguage(language);
		settings.setOutputFormat(outputFormat);

		Task task = null;
		if (argList.size() == 1) {
			System.out.println("Uploading file..");
			task = restClient.processImage(argList.elementAt(0), settings);

		} else if (argList.size() > 1) {

			// Upload images via submitImage and start recognition with
			// processDocument
			for (int i = 0; i < argList.size(); i++) {
				System.out.println(String.format("Uploading image %d/%d..",
						i + 1, argList.size()));
				String taskId = null;
				if (task != null) {
					taskId = task.Id;
				}

				Task result = restClient.submitImage(argList.elementAt(i),
						taskId);
				if (task == null) {
					task = result;
				}
			}
			task = restClient.processDocument(task.Id, settings);

		} else {
			System.out.println("No files to process.");
			return;
		}

		waitAndDownloadResult(task, outputPath);
	}

	/**
	 * Extract output format from extension of output file.
	 */
	private static ProcessingSettings.OutputFormat outputFormatByFileExt(
			String filePath) {
		int extIndex = filePath.lastIndexOf('.');
		if (extIndex < 0) {
			System.out
					.println("No file extension specified. Plain text will be used as output format.");
			return ProcessingSettings.OutputFormat.txt;
		}
		String ext = filePath.substring(extIndex).toLowerCase();
		if (ext.equals(".txt")) {
			return ProcessingSettings.OutputFormat.txt;
		} else if (ext.equals(".xml")) {
			return ProcessingSettings.OutputFormat.xml;
		} else if (ext.equals(".pdf")) {
			return ProcessingSettings.OutputFormat.pdfSearchable;
		} else if (ext.equals(".docx")) {
			return ProcessingSettings.OutputFormat.docx;
		} else if (ext.equals(".rtf")) {
			return ProcessingSettings.OutputFormat.rtf;
		} else {
			System.out
					.println("Unknown output extension. Plain text will be used.");
			return ProcessingSettings.OutputFormat.txt;
		}
	}

	private static void waitAndDownloadResult(Task task, String outputPath)
			throws Exception {
		task = waitForCompletion(task);

		if (task.Status == Task.TaskStatus.Completed) {
			System.out.println("Downloading..");
			restClient.downloadResult(task, outputPath);
			System.out.println("Ready");
		} else if (task.Status == Task.TaskStatus.NotEnoughCredits) {
			System.out.println("Not enough credits to process document. "
					+ "Please add more pages to your application's account.");
		} else {
			System.out.println("Task failed");
		}

	}

	private static Task waitForCompletion(Task task) throws Exception {
		// Note: it's recommended that your application waits
		// at least 2 seconds before making the first getTaskStatus request
		// and also between such requests for the same task.
		// Making requests more often will not improve your application performance.
		// Note: if your application queues several files and waits for them
		// it's recommended that you use listFinishedTasks instead (which is described
		// at http://ocrsdk.com/documentation/apireference/listFinishedTasks/).
		while (task.isTaskActive()) {

			Thread.sleep(750);
			System.out.println("Waiting..");
			task = restClient.getTaskStatus(task.Id);
		}
		return task;
	}
}

