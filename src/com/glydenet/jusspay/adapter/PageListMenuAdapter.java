package com.glydenet.jusspay.adapter;

import com.glydenet.jusspay.R;
import com.glydenet.jusspay.model.ListMenuItem;
 
import java.util.ArrayList;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PageListMenuAdapter extends BaseAdapter{

	private Context context;
    private ArrayList<ListMenuItem> listMenuItems;
     
    public PageListMenuAdapter(Context context, ArrayList<ListMenuItem> listMenuItems){
        this.context = context;
        this.listMenuItems = listMenuItems;
    }
 
    @Override
    public int getCount() {
        return listMenuItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return listMenuItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void removeAllItems(){
    	if(!listMenuItems.isEmpty()){
    		this.listMenuItems.clear();
    	}
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_menu_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
          
        imgIcon.setImageResource(listMenuItems.get(position).getIcon());        
        txtTitle.setText(listMenuItems.get(position).getTitle());
         
        return convertView;
    }
 
}
