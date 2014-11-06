package com.glydenet.jusspay.adapter;

import java.util.List;

import com.glydenet.jusspay.R;
import com.glydenet.jusspay.model.User;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecipientsAdapter extends ArrayAdapter<User>{
	
	private Context context;
	private List<User> recipientsList;

	public RecipientsAdapter (Context context, int resource, List<User> recipientsList){
		super(context, resource, recipientsList);
		this.context = context;
		this.recipientsList = recipientsList;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_recipients, parent, false);
		
		//Display photo and name of the recipient
		User recipient = recipientsList.get(position);
		TextView tv = (TextView) view.findViewById(R.id.textViewRecipients);
		tv.setText(recipient.getFirstName());
		
		return view;
	}
	
	
}
