package core.facebook;

import com.restfb.*;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;

import java.util.List;

/**
 * Created by user on 24/12/2015.
 */
public class Client {
//	private static final String PARAM_APP_SECRET_PROOF = "appsecret_proof";
//	private static final String APP_ID = "977010022371410";
//	private static final String APP_SECRET = "aa51dff9fe0e482f11318e913b51f71b";
//	private FacebookClient facebookClient;
//	private String accessToken;
//
//	public Client(String accessToken) {
//		this.accessToken = accessToken;
//		facebookClient = new DefaultFacebookClient(this.accessToken, APP_SECRET, Version.VERSION_2_5);
//	}
//
//	public void stam() {
//		Connection<User> friends = null;
//		try {
//			String proof = facebookClient.obtainAppSecretProof(accessToken, APP_SECRET);
//			User loginUser = facebookClient.fetchObject("me", User.class, Parameter.with(PARAM_APP_SECRET_PROOF, proof));
//			friends = facebookClient.fetchConnection("/me/friends", User.class);
//			List<User> friendsList = friends.getData();
//		} catch (FacebookException e) {
//			e.printStackTrace();
//		}
//
//	}
}
