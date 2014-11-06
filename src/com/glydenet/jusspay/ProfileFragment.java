package com.glydenet.jusspay;

import java.util.concurrent.ExecutionException;

import com.glydenet.jusspay.connection.HttpImageUploader;
import com.glydenet.jusspay.connection.HttpManager;
import com.glydenet.jusspay.connection.RequestPackage;
import com.glydenet.jusspay.model.ServerMessage;
import com.glydenet.jusspay.model.User;
import com.glydenet.jusspay.parsers.Parser;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

	private static final int PICK_IMAGE = 1;

	// Declare variables for widget data
	private ProgressBar progressBar, progressBar2;
	private Button uploadButton, saveButton;
	private ImageView imgView;
	private EditText textBox_firstName, textBox_lastName, textBox_username,
			textBox_phone;

	private MainActivity parentActivity;
	private User user;
	private Bitmap bitmap;
	private String filePath;
	private String imageName;

	public ProfileFragment(User user) {
		this.user = user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// retain this fragment
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		CharSequence mTitle = getActivity().getTitle();
		parentActivity = (MainActivity) getActivity();
		getActivity().setTitle(mTitle + " Profile");

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);

		/**
		 * Initialize widget views
		 */

		textBox_firstName = (EditText) rootView
				.findViewById(R.id.editText_firstName_profile);
		textBox_lastName = (EditText) rootView
				.findViewById(R.id.editText_lastName_profile);
		textBox_username = (EditText) rootView
				.findViewById(R.id.editText_username_profile);
		textBox_phone = (EditText) rootView
				.findViewById(R.id.editText_phone_profile);
		progressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBar_profile);
		progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar_profile2);

		// Hide progress bar
		progressBar.setVisibility(View.INVISIBLE);
		progressBar2.setVisibility(View.INVISIBLE);

		uploadButton = (Button) rootView
				.findViewById(R.id.button_image_profile);
		saveButton = (Button) rootView.findViewById(R.id.button_save_profile);
		imgView = (ImageView) rootView.findViewById(R.id.imageView_profile);

		/**
		 * setup event listeners
		 */

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				user.setFirstName(textBox_firstName.getText().toString());
				user.setLastName(textBox_lastName.getText().toString());
				user.setUserName(textBox_username.getText().toString());
				user.setPhone(textBox_phone.getText().toString());
				
				
				SaveTask saveTask = new SaveTask();
				saveTask.execute(makeRequestPackage());
			}
		});

		uploadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (parentActivity.isOnline()) {
					if (filePath == null) {
						// Openup a gallery browser
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								1);

						// Toggle Button Text
						uploadButton.setText("Upload");
					}

					if (filePath != null) {
						HttpImageUploader uploader = new HttpImageUploader(
								progressBar);
						try {

							imageName = uploader.execute(filePath).get();
							user.setPortrait(imageName);
							Toast.makeText(parentActivity, "Uploading Image",
									Toast.LENGTH_LONG).show();
							filePath = null;
							uploadButton.setText("Browse");

						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}

				} else {
					Toast.makeText(parentActivity,
							"Network Connection Unavailable", Toast.LENGTH_LONG)
							.show();
				}

			}

		});

		loadValues();

		parentActivity.setupUI(rootView);
		return rootView;
	}

	private void loadValues() {
		textBox_firstName.setText(user.getFirstName());
		textBox_lastName.setText(user.getLastName());
		textBox_username.setText(user.getUserName());
		textBox_phone.setText(user.getPhone());

	}

	// To handle when an image is selected from the browser
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == MainActivity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				filePath = null;

				try {
					// OI FILE Manager
					String filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					String selectedImagePath = getPath(selectedImageUri);

					if (selectedImagePath != null) {
						filePath = selectedImagePath;
					} else if (filemanagerstring != null) {
						filePath = filemanagerstring;
					} else {
						Toast.makeText(parentActivity.getApplicationContext(),
								"Unknown path", Toast.LENGTH_LONG).show();
						Log.e("Bitmap", "Unknown path");
					}

					if (filePath != null) {
						decodeFile(filePath);
					} else {
						bitmap = null;
					}
				} catch (Exception e) {
					Toast.makeText(parentActivity.getApplicationContext(),
							"Internal error", Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
			break;
		default:
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri,
				projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		imgView.setImageBitmap(bitmap);

	}

	private RequestPackage makeRequestPackage() {

		RequestPackage p = new RequestPackage();

		p.setUri(HttpManager.BASE_URL + "/user");
		p.setMethod("PUT");
		p.setParam("email", user.getEmail());
		p.setParam("username", user.getUserName());
		p.setParam("first_name", user.getFirstName());
		p.setParam("last_name", user.getLastName());
		p.setParam("phone", user.getPhone());
		p.setParam("portrait", user.getPortrait());

		return p;

	}
	
	private class SaveTask extends
			AsyncTask<RequestPackage, String, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar2.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(RequestPackage... packets) {
			// Post user data over the Network in the background
			String feedBack = HttpManager.sendRequest(packets[0]);
			// publishProgress();
			return feedBack;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... values) {
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

			// Hide the progress bar
			progressBar2.setVisibility(View.INVISIBLE);

			// Get message from the server
			ServerMessage msg = Parser.parseServerMessage(result);

			if (msg.isError()) {
				Toast.makeText(parentActivity, msg.getMessage(),
						Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(parentActivity, msg.getMessage(),
						Toast.LENGTH_LONG).show();
				
			}

		}

	}

}
