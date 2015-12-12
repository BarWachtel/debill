package core.ocr;

/**
 * Created by user on 12/12/2015.
 */
public enum Language {
	HEBREW ("hebrew"),
	ENGLISH ("english");

	String value;

	Language(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
