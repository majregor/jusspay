package com.glydenet.jusspay.parsers;


import java.util.ArrayList;
import java.util.List;
import org.json.*;

import com.glydenet.jusspay.model.User;

public class UserJSONParser {

	public static List<User> parseFeed(String content) {

		JSONArray ar;
		List<User> usersList = new ArrayList<>();

		try {

			ar = new JSONArray(content);

			// Iterate through the JSONArray
			for (int i = 0; i < ar.length(); i++) {
				JSONObject obj = ar.getJSONObject(i);
				User user = new User(obj.getString("first_name"),
						obj.getString("last_name"), obj.getString("email"), 
						obj.getString("user_name"), obj.getString("phone"));
				user.setApiKey(obj.getString("apiKey"));
				user.setDob(obj.getString("dob"));
				user.setStatus((byte) obj.getInt("status"));
				
				usersList.add(user);
			}
			
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usersList;
	}

	public static User parseUserString(String content) {
		 
		JSONObject obj;
		try {
			obj = new JSONObject(content);

			User user = new User(obj.getString("firstName"),
					obj.getString("lastName"), obj.getString("email"),
					obj.getString("password"), obj.getString("userName"), obj.getString("phone"));
			user.setApiKey(obj.getString("apiKey"));
			user.setDob(obj.getString("dob"));
			user.setStatus((byte) obj.getInt("status"));

			return user;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
