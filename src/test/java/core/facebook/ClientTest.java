package core.facebook;

import junit.framework.TestCase;
import org.junit.Test;

public class ClientTest extends TestCase {
	@Test
	public void testConnectionToFacebook() {
		String tempAccessToken = "CAACEdEose0cBAKqaYHbWdc25FW7c0Fg18JWYaVMcEnaGSOsyPl11TPlqc6yEeUfztJmVHJGdWIZBffkfGH2X3CMEcRhH3SB0R1TfZC4FO2E3YFiiAUKaAepvE2NMlQAfZA1hx80L3jx1Q7PoU5oLvZBKH4cAdOOWUEIQhhnZAd4thXQn3nZB7lZALOgZAasp2VMagwVOKga9IAZDZD";
		Client client = new Client(tempAccessToken);
		client.stam();
	}
}