package com.glydenet.jusspay.parsers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.json.JSONException;
import org.json.JSONObject;

import com.glydenet.jusspay.model.ServerMessage;
import com.glydenet.jusspay.model.User;

public class Parser {

	/**
	 * Returns JSON string from model object
	 * 
	 * @param user
	 * @return
	 */

	public static String serialize(Object obj) {
		JSONObject jsonObj = new JSONObject();

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				// skip over static fields and continue to next declared
				continue;
			}

			try {

				field.setAccessible(true);
				jsonObj.put(field.getName(), field.get(obj));

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				continue;
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
		}

		return jsonObj.toString();
	}

	public static ServerMessage parseServerMessage(String jsonString) {

		JSONObject obj;
		try {
			boolean error;
			obj = new JSONObject(jsonString);

			if (obj.getString("error").equals("false")) {
				error = false;
			} else {
				error = true;
			}
			ServerMessage msg = new ServerMessage(error,
					obj.getString("message"));

			return msg;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			ServerMessage msg = new ServerMessage(true,
					jsonString);
			e.printStackTrace();
			return msg;
		}
	}

	public static User parseUser(String jsonString) {
		User user = null;
		JSONObject obj;

		if (jsonString != null) {
			try {
				obj = new JSONObject(jsonString);

				// Get the returned values from the response into variables.
				String firstName = obj.getString("first_name");
				String lastName = obj.getString("last_name");
				String apiKey = obj.getString("apiKey");
				String email = obj.getString("email");
				String userName = obj.getString("user_name");
				String dob = obj.getString("dob");
				byte status = Byte.valueOf(obj.getString("status"));
				String created = obj.getString("created");
				String modified = obj.getString("modified");
				String portrait = obj.getString("portrait");
				String phone	= obj.getString("phone");

				user = new User(firstName, lastName, apiKey, email, userName, phone,
						dob, status, created, modified);
				user.setPortrait(portrait);

			} catch (JSONException je) {
				je.printStackTrace();
			}
		}
		return user;
	}
}
