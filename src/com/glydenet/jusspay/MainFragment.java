package com.glydenet.jusspay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
	
	private MainActivity parentActivity;
	
	public MainFragment(){}
	public MainFragment(MainActivity parentActivity){
		this.parentActivity = parentActivity;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
          
        parentActivity.setupUI(rootView);
        return rootView;
    }
    

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 * Helps us grab the parent activity on attach and pass it into the local 
	 * parent activity variable
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.parentActivity = (MainActivity) activity; 
	}
}
