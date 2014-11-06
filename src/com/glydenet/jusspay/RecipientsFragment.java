package com.glydenet.jusspay;

import java.util.List;

import com.glydenet.jusspay.adapter.RecipientsAdapter;
import com.glydenet.jusspay.connection.HttpManager;
import com.glydenet.jusspay.connection.RequestPackage;
import com.glydenet.jusspay.model.ServerMessage;
import com.glydenet.jusspay.model.User;
import com.glydenet.jusspay.parsers.Parser;
import com.glydenet.jusspay.parsers.UserJSONParser;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link HomeFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link RecipientsFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class RecipientsFragment extends Fragment {

	// Declare view variables

	private User user;
	private List<User> recipientsList;

	public static String ARG_SECTION_NUMBER = "Section";
	public static int ALL_RECIPIENTS = 1;
	public static int PEOPLE_RECIPIENTS = 2;
	public static int BUSINESS_RECIPIENTS = 3;

	private MainActivity parentActivity;

	private OnFragmentInteractionListener mListener;

	private ProgressBar progressBar;
	private GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		CharSequence mTitle = getActivity().getTitle();
		parentActivity = (MainActivity) getActivity();
		getActivity().setTitle(" My Recipients");

		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_recipients,
				container, false);

		/**
		 * Initialize widget views
		 */
		progressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBar_recipients);
		gridView = (GridView) rootView.findViewById(R.id.gridViewRecipients);

		// Hide the progress bar
		progressBar.setVisibility(View.INVISIBLE);

		if (parentActivity.isAuthenticated()) {
			this.user = parentActivity.authenticatedUser;
		}
		if (parentActivity.isOnline()) {
			getRecipients();
		} else {
			Toast.makeText(parentActivity, "Network Connection Unavailable",
					Toast.LENGTH_LONG).show();
		}

		 parentActivity.setupUI(rootView);
		return rootView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	private void getRecipients() {
		// construct request package
		RequestPackage requestPacket = makeRequestPackage();
		FetchRecipientsTask recipientsTask = new FetchRecipientsTask();
		recipientsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				requestPacket);
	}

	private RequestPackage makeRequestPackage() {

		RequestPackage p = new RequestPackage();

		p.setUri(HttpManager.BASE_URL + "/recipients");
		p.setMethod("GET");
		p.setParam("user", user.getEmail());
		// p.setParam("password", passwordTextView.getText().toString());

		return p;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	private void updateDisplay() {
		RecipientsAdapter adapter = new RecipientsAdapter(parentActivity,
				R.layout.item_recipients, recipientsList);
		gridView.setAdapter(adapter);
	}

	private class FetchRecipientsTask extends
			AsyncTask<RequestPackage, String, List<User>> {

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
		protected List<User> doInBackground(RequestPackage... packets) {
			// Post user data over the Network in the background
			String feedBack = HttpManager.sendRequest(packets[0]);
			// publishProgress();
			
			recipientsList = UserJSONParser.parseFeed(feedBack);
			return recipientsList;
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
		protected void onPostExecute(List<User> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// Hide the progress bar
			progressBar.setVisibility(View.INVISIBLE);
			
			recipientsList = result;

			if (recipientsList != null) {

				updateDisplay();

			} else {
				Toast.makeText(parentActivity, "Recipients creating failed",
						Toast.LENGTH_LONG).show();
			}

		}

	}

}
