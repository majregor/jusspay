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

public class LoginFragment extends Fragment {

	private ProgressBar progressBar;
	private Button loginButton;
	private TextView emailTextView;
	private TextView passwordTextView;

	private MainActivity parentActivity;

	public LoginFragment() {
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
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		CharSequence mTitle = getActivity().getTitle();
		parentActivity = (MainActivity) getActivity();
		getActivity().setTitle(mTitle + " Login");

		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);

		/**
		 * Initialize widget views
		 */
		progressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBar_login);
		loginButton = (Button) rootView.findViewById(R.id.buttonLogin);
		emailTextView = (TextView) rootView.findViewById(R.id.textFieldEmail);
		passwordTextView = (TextView) rootView
				.findViewById(R.id.textFieldPassword);

		// Hide the progress bar
		progressBar.setVisibility(View.INVISIBLE);

		/**
		 * setup event listeners
		 */
		setEventListeners();

		parentActivity.setupUI(rootView);
		return rootView;
	}

	private void setEventListeners() {

		// Event listener for login button
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (parentActivity.isOnline()) {
					loginUser();
				} else {
					Toast.makeText(parentActivity,
							"Network Connection Unavailable", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	private void loginUser() {
		// construct request package
		RequestPackage requestPacket = makeRequestPackage();
		LoginTask loginTask = new LoginTask();
		loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				requestPacket);
	}

	private RequestPackage makeRequestPackage() {

		RequestPackage p = new RequestPackage();

		p.setUri(HttpManager.BASE_URL + "/login");
		p.setMethod("POST");
		p.setParam("email", emailTextView.getText().toString());
		p.setParam("password", passwordTextView.getText().toString());

		return p;

	}

	private class LoginTask extends AsyncTask<RequestPackage, String, String> {

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
				Toast.makeText(parentActivity, msg.getMessage(), Toast.LENGTH_LONG)
						.show();
			} else {

				parentActivity.authenticatedUser = Parser.parseUser(result);
				if (parentActivity.authenticatedUser != null) {
					parentActivity.setAuthenticated(true);
					Toast.makeText(parentActivity, msg.getMessage(), Toast.LENGTH_LONG)
							.show();
					parentActivity.switchDrawerMenu(true);
					parentActivity.displayView(MainActivity.HOME);

				} else {
					Toast.makeText(parentActivity, "User Object Creation failed",
							Toast.LENGTH_LONG).show();
				}

			}

		}

	}
}
