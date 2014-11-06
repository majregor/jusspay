package com.glydenet.jusspay;

import com.glydenet.jusspay.connection.HttpManager;
import com.glydenet.jusspay.connection.RequestPackage;
import com.glydenet.jusspay.model.ServerMessage;
import com.glydenet.jusspay.parsers.Parser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SignupFragment extends Fragment {

	private TextView txtFirstName, txtLastName, txtPhone, txtEmail,
			txtPassword, txtPIN, txtUserName;
	private ProgressBar progressBar;
	private Button signUpButton;
	private MainActivity parentActivity;

	public SignupFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		CharSequence mTitle = getActivity().getTitle();
		parentActivity = (MainActivity) getActivity();
		getActivity().setTitle(mTitle + " Sign Up");

		View rootView = inflater.inflate(R.layout.fragment_signup, container,
				false);

		/**
		 * Initialize widget views
		 */
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		// Hide progress bar
		progressBar.setVisibility(View.INVISIBLE);

		signUpButton = (Button) rootView.findViewById(R.id.btnSignup_signup);
		txtFirstName = (TextView) rootView
				.findViewById(R.id.textFieldFirstName_signup);
		txtLastName = (TextView) rootView
				.findViewById(R.id.textFieldLastName_signup);
		txtPhone = (TextView) rootView.findViewById(R.id.textFieldPhone_signup);
		txtEmail = (TextView) rootView.findViewById(R.id.textFieldEmail_signup);
		txtPassword = (TextView) rootView
				.findViewById(R.id.textFieldPassword_signup);
		txtPIN = (TextView) rootView.findViewById(R.id.textFieldPIN_signup);
		txtUserName = (TextView) rootView
				.findViewById(R.id.textFieldUserName_signup);

		/**
		 * setup event listeners
		 */

		signUpButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (parentActivity.isOnline()) {
					registerUser();
				} else {
					Toast.makeText(parentActivity,
							"Network Connection Unavailable", Toast.LENGTH_LONG)
							.show();
				}

			}

		});
		
		 parentActivity.setupUI(rootView);
		return rootView;
	}

	private void registerUser() {
		// construct request package
		RequestPackage requestPacket = makeRequestPackage();
		RegisterTask registerTask = new RegisterTask();
		registerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				requestPacket);
	}

	private RequestPackage makeRequestPackage() {

		RequestPackage p = new RequestPackage();

		p.setUri(HttpManager.BASE_URL + "/register");
		p.setMethod("POST");
		p.setParam("username", txtUserName.getText().toString());
		p.setParam("email", txtEmail.getText().toString());
		p.setParam("password", txtPassword.getText().toString());
		p.setParam("first_name", txtFirstName.getText().toString());
		p.setParam("last_name", txtLastName.getText().toString());
		p.setParam("pin", txtPIN.getText().toString());
		p.setParam("phone", txtPhone.getText().toString());

		return p;

	}

	private class RegisterTask extends
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
			progressBar.setVisibility(View.VISIBLE);
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
			progressBar.setVisibility(View.INVISIBLE);

			// Get message from the server
			ServerMessage msg = Parser.parseServerMessage(result);

			
			if (msg.isError()) {
				Toast.makeText(parentActivity, msg.getMessage(),
						Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(parentActivity, msg.getMessage(),
						Toast.LENGTH_LONG).show();
				parentActivity.displayView(MainActivity.CONFIRM_EMAIL);
			}
			

		}

	}
}
