package com.glydenet.jusspay;

import java.util.ArrayList;

import com.glydenet.jusspay.adapter.NavDrawerListAdapter;
import com.glydenet.jusspay.adapter.PageListMenuAdapter;
import com.glydenet.jusspay.model.ListMenuItem;
import com.glydenet.jusspay.model.NavDrawerItem;
import com.glydenet.jusspay.model.User;

import android.app.Activity;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link AccountFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AccountFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AccountFragment extends Fragment {

	// Constants
	public static final int PROFILE = 0;
	public static final int HISTORY = 1;
	public static final int MESSAGES = 2;
	public static final int SUPPORT = 3;
	public static final int SETTINGS = 4;
	public static final int LOGOUT = 5;

	// Declare view variables
	private Button btnAddCredit, btnSendPay, btnRecent;
	private TextView txtName, txtPhone, txtBalanceValue;
	private CircularImageView userPortrait;
	private ListView listMenu;

	// Menu items
	private String[] menuTitles;
	private TypedArray menuIcons;
	private ArrayList<ListMenuItem> listMenuItems;
	private PageListMenuAdapter pageListMenuAdapter;

	private User user;

	protected MainActivity parentActivity;

	private OnFragmentInteractionListener mListener;

	public AccountFragment(User user) {
		// Required public constructor
		this.user = user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		CharSequence mTitle = getActivity().getTitle();
		parentActivity = (MainActivity) getActivity();
		getActivity().setTitle(mTitle + " My Account");

		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_account, container,
				false);

		// Construct menu items in list

		menuTitles = getResources().getStringArray(
				R.array.list_menu_items_account);
		// menu icons from resources
		menuIcons = getResources().obtainTypedArray(
				R.array.list_menu_icons_account);

		listMenu = (ListView) rootView.findViewById(R.id.list_menu_account);

		listMenuItems = new ArrayList<ListMenuItem>();
		// Populate the listMenu item list
		int index = 0;
		for (String menuTitle : menuTitles) {
			listMenuItems.add(new ListMenuItem(menuTitle, menuIcons
					.getResourceId(index++, -1)));
		}
		// Recycle the typed array
		menuIcons.recycle();

		// setting the nav drawer list adapter
		pageListMenuAdapter = new PageListMenuAdapter(
				parentActivity.getApplicationContext(), listMenuItems);
		listMenu.setAdapter(pageListMenuAdapter);

		// Set on click listener on the list menu
		listMenu.setOnItemClickListener(new ListMenuClickListener(parentActivity));

		/**
		 * Initialize widget views
		 */
		btnAddCredit = (Button) rootView
				.findViewById(R.id.button_add_credit_account);
		btnSendPay = (Button) rootView.findViewById(R.id.button_send_account);
		btnRecent = (Button) rootView.findViewById(R.id.button_recent_account);

		txtName = (TextView) rootView.findViewById(R.id.text_name_account);
		txtPhone = (TextView) rootView.findViewById(R.id.text_phone_account);
		txtBalanceValue = (TextView) rootView
				.findViewById(R.id.text_bal_value_account);

		userPortrait = (CircularImageView) rootView
				.findViewById(R.id.user_portrait_account);

		loadValues();

		 parentActivity.setupUI(rootView);
		return rootView;
	}

	private void loadValues() {
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

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		android.support.v4.app.Fragment fragment = null;

		switch (position) {
		case PROFILE:
			fragment = new ProfileFragment(this.user);
			break;
		case HISTORY:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = this.parentActivity
					.getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();

			this.parentActivity.setTitle(menuTitles[position]);
			// don't act on fragments not in drawer list
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	/**
	 * List Meny item click listener
	 * */
	private class ListMenuClickListener implements ListView.OnItemClickListener {
		private MainActivity parentActivity;

		public ListMenuClickListener(MainActivity parentActivity) {
			super();
			this.parentActivity = parentActivity;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

}
