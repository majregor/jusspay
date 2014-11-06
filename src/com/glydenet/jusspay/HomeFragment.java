package com.glydenet.jusspay;

import com.glydenet.jusspay.model.User;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link HomeFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link HomeFragment#newInstance} factory method to create an instance of this
 * fragment.
 * 
 */
public class HomeFragment extends Fragment {


	// Declare view variables
	private Button btnAddCredit, btnSendPay, btnRecent;
	private TextView txtName, txtPhone, txtBalanceValue;
	private CircularImageView userPortrait;
	
	private User user;
	
	private MainActivity parentActivity;

	private OnFragmentInteractionListener mListener;
	
	public HomeFragment(){
		super();
	}

	public HomeFragment(User user) {
		// Required empty public constructor
		this.user = user;
	}

	/* (non-Javadoc)
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
		getActivity().setTitle(mTitle + " Home");
		
		// Inflate the layout for this fragment
		View rootView= inflater.inflate(R.layout.fragment_home, container, false);
		
		/**
		 * Initialize widget views
		 */
		btnAddCredit 	= (Button) rootView.findViewById(R.id.button_add_credit_home);
		btnSendPay 		= (Button) rootView.findViewById(R.id.button_send_home);
		btnRecent 		= (Button) rootView.findViewById(R.id.button_recent_home);
		
		txtName			= (TextView) rootView.findViewById(R.id.text_name_home); 
		txtPhone		= (TextView) rootView.findViewById(R.id.text_phone_home);  
		txtBalanceValue = (TextView) rootView.findViewById(R.id.text_bal_value_home); 
		
		userPortrait	= (CircularImageView) rootView.findViewById(R.id.user_portrait_home);
		
		loadValues();
		
		 parentActivity.setupUI(rootView);
		return rootView;
	}
	
	private void loadValues(){
		// load values for text views
		txtName.setText(user.getFirstName() + " " + user.getLastName());
		txtPhone.setText(user.getEmail());
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

}
