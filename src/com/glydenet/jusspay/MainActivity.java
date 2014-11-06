package com.glydenet.jusspay;

import java.util.ArrayList;

import com.glydenet.jusspay.adapter.NavDrawerListAdapter;
import com.glydenet.jusspay.connection.RequestPackage;
import com.glydenet.jusspay.model.NavDrawerItem;
import com.glydenet.jusspay.model.User;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnFragmentInteractionListener, ActionBar.TabListener {

	/**
	 * Adding The Navigation Drawer, Steps will include: Creating a
	 * NavDrawerListAdapter instance and adding list items. Assigning the
	 * adapter to Navigation Drawer ListView Creating click event listener for
	 * list items Creating and displaying fragment activities on selecting list
	 * item.
	 */
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	/**
	 * constants to represent fragments
	 */
	// For Anonymous Nav Menu Items
	public static final int MAIN = 0;
	public static final int LOGIN = 1;
	public static final int SIGNUP = 2;
	public static final int ABOUT = 3;
	public static final int CONFIRM_EMAIL = 4;

	// For Authenticated Nav Menu Items
	public static final int HOME = 0;
	public static final int MY_ACCOUNT = 1;
	public static final int RECENT = 2;
	public static final int RECIPIENTS = 3;
	public static final int CREDIT = 4;
	public static final int FEES = 5;
	public static final int MESSAGES = 6;
	public static final int LOGOUT = 7;
	public static final int ABOUT_US = 8;
	

	// Variable to keep track of authentication
	private boolean authenticated = false;

	// Variable to hold the authenticated user
	public User authenticatedUser;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		/**
		 * Initialize views
		 */

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		if (isAuthenticated()) {
			navMenuTitles = getResources().getStringArray(
					R.array.nav_drawer_items_authenticated);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(
					R.array.nav_drawer_icons_authenticated);
		} else {
			navMenuTitles = getResources().getStringArray(
					R.array.nav_drawer_items_anonymous);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(
					R.array.nav_drawer_icons_anonymous);
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		int index = 0;
		for (String navTitle : navMenuTitles) {
			navDrawerItems.add(new NavDrawerItem(navTitle, navMenuIcons
					.getResourceId(index++, -1)));
		}

		// Recycle the typed array
		navMenuIcons.recycle();

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);


		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			if (isAuthenticated()) {
				displayView(HOME);
			} else {
				displayView(MAIN);
			}
		}

		// Set on click listener on the list
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// ActionBar actionBar = getActionBar();
		// ColorDrawable colorDrawable = new
		// ColorDrawable(Color.parseColor("#FFFFFF"));
		// actionBar.setBackgroundDrawable(colorDrawable);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressLint("InlinedApi")
	public void displayView(int position) {
		// update the main content by replacing fragments
		android.support.v4.app.Fragment fragment = null;
		
		//Remove all Navigation Tabs from if any
		if (getActionBar().getTabCount() > 0) {
			getActionBar().removeAllTabs();
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
		
		
		if (!isAuthenticated()) {
			// NOT AUTHENTICATED MENU ITEMS
			switch (position) {
			case MAIN:
				fragment = new MainFragment();
				break;
			case LOGIN:
				fragment = new LoginFragment();
				break;
			case SIGNUP:
				fragment = new SignupFragment();
				break;
			case CONFIRM_EMAIL:
				fragment = new ConfirmEmailFragment();
				break;
			}
			
		} else {

			// AUTHENTICATED MENU ITEMS OPTIONS
			switch (position) {
			case HOME:
				fragment = new HomeFragment(this.authenticatedUser);
				break;
			case RECIPIENTS:
				if (getActionBar().getTabCount() <= 0) {
					getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
					
					ActionBar actionBar = getActionBar();
					// for each of the sections in the app, add a tab to the action bar.
					actionBar.addTab(actionBar.newTab().setText(R.string.tab_title_all_recipients)
							.setTabListener(this));
					actionBar.addTab(actionBar.newTab().setText(R.string.tab_title_people_recipients)
							.setTabListener(this));
					actionBar.addTab(actionBar.newTab().setText(R.string.tab_title_business_recipients)
							.setTabListener(this));
				}
				break;
			case MY_ACCOUNT:
				fragment  = new AccountFragment(this.authenticatedUser);
				break;
			case 5:
				// fragment = new WhatsHotFragment();
				break;

			default:
				break;
			}
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).addToBackStack(null).commit();

			// don't act on fragments not in drawer list
			if (position != CONFIRM_EMAIL) {
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navMenuTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	public void resetPassword(View v) {
	}

	public void displaySignup(View v) {
		displayView(SIGNUP);
	}

	public void displayLogin(View v) {
		displayView(LOGIN);
	}

	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	public void setAuthenticated(boolean a) {
		this.authenticated = a;
	}

	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void onFragmentInteraction(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub

	}

	public void switchDrawerMenu(boolean auth) {
		// load slide menu items
		if (auth) {
			navMenuTitles = getResources().getStringArray(
					R.array.nav_drawer_items_authenticated);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(
					R.array.nav_drawer_icons_authenticated);
		} else {
			navMenuTitles = getResources().getStringArray(
					R.array.nav_drawer_items_anonymous);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(
					R.array.nav_drawer_icons_anonymous);
		}

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		int index = 0;
		for (String navTitle : navMenuTitles) {
			navDrawerItems.add(new NavDrawerItem(navTitle, navMenuIcons
					.getResourceId(index++, -1)));
		}

		// Recycle the typed array
		navMenuIcons.recycle();

		if (!adapter.isEmpty()) {
			adapter.removeAllItems();
		}
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
		// When the given tab is selected, show the tab contents in the
	    // container view.
		RecipientsFragment fragment = new RecipientsFragment();
	    Bundle args = new Bundle();
	    args.putInt(RecipientsFragment.ARG_SECTION_NUMBER,
	        tab.getPosition() + 1);
	    fragment.setArguments(args);
	    getSupportFragmentManager().beginTransaction()
	        .replace(R.id.frame_container, fragment).commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		

	}
	
	
	// Hide Soft Keyboard when user touches outside views that are not edit text.
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	public void setupUI(View view) {

	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {

	        view.setOnTouchListener(new OnTouchListener() {
	        	
	       
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					//hideSoftKeyboard(MainActivity.this);
					return false;
				}

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView);
	        }
	    }
	}
	
	
}
