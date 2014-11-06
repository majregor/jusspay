package com.glydenet.jusspay;

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
 * must implement the {@link ConfirmEmailFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ConfirmEmailFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class ConfirmEmailFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private Button signupBtn;
	private MainActivity parentActivity;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ConfirmEmailFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ConfirmEmailFragment newInstance(String param1, String param2) {
		ConfirmEmailFragment fragment = new ConfirmEmailFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ConfirmEmailFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		CharSequence mTitle = getActivity().getTitle();
    	getActivity().setTitle(mTitle+" Confirm Email");
    	
        View rootView = inflater.inflate(R.layout.fragment_signup_confirm, container, false);
         
        signupBtn = (Button) rootView.findViewById(R.id.btnSignup_openmail);
        
        /**
         * Setup Event Listeners and Handlers
         */
        
        signupBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				parentActivity.displayView(MainActivity.LOGIN);
			}
		});
        
        parentActivity.setupUI(rootView);
        return rootView;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
			parentActivity = (MainActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

}
