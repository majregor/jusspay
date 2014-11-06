package com.glydenet.jusspay.connection;

import java.io.IOException;
import java.net.URL;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class HttpManager {

	public static final String BASE_URL =  "http://www.jusspay.com/jusspay"; //"http://192.168.1.101/jusspay";
	public static final String PHOTO_BASE_URL = BASE_URL + "/photos";
	
	
	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	public static String sendRequest(RequestPackage urlPackage) {

		String rString = null;
		String uri = urlPackage.getUri();

		try {
		
			OkHttpClient client = new OkHttpClient();
			Request request=null;

			if (urlPackage.getMethod().equals("GET")) {
				uri += "?" + urlPackage.getEncodedParams();
				URL url = new URL(uri);
				request = doGetRequest(url);
			}else if(urlPackage.getMethod().equals("POST")){
				URL url = new URL(uri);
				String json = urlPackage.getJSONEncodedParams();
				RequestBody body = RequestBody.create(JSON, json);
				request = doPostRequest(url, body);
			}
			else if(urlPackage.getMethod().equals("PUT")){
				URL url = new URL(uri);
				String json = urlPackage.getJSONEncodedParams();
				RequestBody body = RequestBody.create(JSON, json);
				request = doPutRequest(url, body);
			}

			
			Response response = client.newCall(request).execute();
			rString = response.body().string();

			return rString;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally {
		}
	}

	private static Request doGetRequest(URL url) {
		Request request = new Request.Builder().url(url).build();

		return request;
	}

	private static Request doPostRequest(URL url, RequestBody body) {
	
		Request request = new Request.Builder().url(url).post(body).build();
		
		return request;
	}
	
	private static Request doPutRequest(URL url, RequestBody body) {
		
		Request request = new Request.Builder().url(url).put(body).build();
		
		return request;
	}

}
