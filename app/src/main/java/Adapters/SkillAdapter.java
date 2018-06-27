package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.staff.profileapplication.MainActivity;
import com.example.staff.profileapplication.R;

import java.util.ArrayList;
import java.util.List;


public class SkillAdapter extends BaseAdapter {
	private List<SkillItem> listSkills = new ArrayList<>();
	private Context context;
	
	public SkillAdapter(Context activity_context) {
		this.context = activity_context;
	}
	
	@Override
	public int getCount() {
		return listSkills.size();
	}
	
	@Override
	public SkillItem getItem(int position) {
		return listSkills.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint("InflateParams")
	public View getView(int pos, View v, ViewGroup arg2) {
		SkillItem c = getItem(pos);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		
		v = inflater.inflate(R.layout.skill_item, null);
		
		TextView lblSkillName = (TextView)v.findViewById(R.id.txt_skill_name);
		TextView lblSkillPercentBar = (TextView)v.findViewById(R.id.skill_percent_bar);
		TextView lblSkillValue = (TextView)v.findViewById(R.id.txt_skill_value);
		
		if (!c.isHeader) {
			((TextView)v.findViewById(R.id.skill_title_style)).setVisibility(View.INVISIBLE);
			lblSkillName.setVisibility(View.INVISIBLE);
			lblSkillName.setTextSize(0F);
		} else {
			lblSkillValue.setVisibility(View.INVISIBLE);
			lblSkillValue.setTextSize(0F);
			lblSkillPercentBar.setVisibility(View.INVISIBLE);
			lblSkillPercentBar.setTextSize(0F);
			
		}
		
		int percent_width = (c.value * 685) / 100;
		lblSkillName.setText(c.name);
		lblSkillValue.setText(c.name);
		lblSkillPercentBar.setWidth(percent_width);
		
		return v;
	}
	
	public void add(String name, int value, boolean isheader ) {
		SkillItem item = new SkillItem(name, value);
		item.isHeader = isheader;
		listSkills.add(item);
	}
	
	public void add(String name, int value) {
		this.add(name, value, false);
	}
	
	public void refresh() {
		this.notifyDataSetChanged();
		ListView list = ((MainActivity)this.context).findViewById(R.id.list_skills);
		if(list != null) {
			((MainActivity) this.context).getTotalHeightofListView(list);
		}
	}
	
	
	public class SkillItem {
		public String name;
		public int value;
		public boolean isHeader;
		
		public SkillItem(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
	
}