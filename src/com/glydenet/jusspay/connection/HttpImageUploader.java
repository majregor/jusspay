package com.glydenet.jusspay.connection;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

//Uploader class
public class HttpImageUploader extends AsyncTask<String, Void, String> {

	private ProgressBar progressBar = null;

	public HttpImageUploader(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressBar.setVisibility(View.VISIBLE);
	}

	protected String doInBackground(String... path) {

		String outPut = null;

		for (String sdPath : path) {

			Bitmap bitmapOrg = BitmapFactory.decodeFile(sdPath);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();

			// Resize the image
			double width = bitmapOrg.getWidth();
			double height = bitmapOrg.getHeight();
			double ratio = 200 / width;
			int newheight = (int) (ratio * height);

			System.out.println("———-width" + width);
			System.out.println("———-height" + height);

			bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 200, newheight,
					true);

			// Here you can define .PNG as well
			bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, bao);
			byte[] ba = bao.toByteArray();
			String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

			System.out.println("uploading image now ——–" + ba1);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("image", ba1));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(HttpManager.BASE_URL
						+ "/imageUpload.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				// print responce
				outPut = EntityUtils.toString(entity);
				Log.i("GET RESPONSE—-", outPut);

				// is = entity.getContent();
				Log.e("log_tag ******", "good connection");

				bitmapOrg.recycle();

			} catch (Exception e) {
				Log.e("log_tag ******",
						"Error in http connection " + e.toString());
			}
		}
		return outPut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// System.out.println(result);
		progressBar.setVisibility(View.INVISIBLE);

	}

}