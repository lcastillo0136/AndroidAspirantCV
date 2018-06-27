package Adapters;

import android.content.Context;
import android.text.Html;
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

public class ExperienceAdapter extends BaseAdapter {
	private List<ExperienceItem> listExperience = new ArrayList<>();
	private Context context;
	
	public ExperienceAdapter(Context activity_context) {
		this.context = activity_context;
	}
	
	@Override
	public int getCount() {
		return listExperience.size();
	}
	
	@Override
	public ExperienceItem getItem(int position) {
		return listExperience.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExperienceItem c = getItem(position);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		
		convertView = inflater.inflate(R.layout.experience_item, null);
		
		TextView lblPeriod = convertView.findViewById(R.id.lblExperiencePeriod);
		TextView lblName = convertView.findViewById(R.id.lblExperienceTitle);
		TextView lblCompany = convertView.findViewById(R.id.lblExperienceCompany);
		TextView lblDescription = convertView.findViewById(R.id.lblExperienceCompanyDesc);
		
		lblPeriod.setText(c.period);
		lblName.setText(c.name);
		lblCompany.setText(c.company);
		lblDescription.setText(Html.fromHtml(c.description));
		
		return convertView;
	}
	
	
	public void add(String name, String period, String company, String description) {
		ExperienceItem item = new ExperienceItem(name, period, company, description);
		listExperience.add(item);
	}
	
	
	public void refresh() {
		this.notifyDataSetChanged();
		ListView list = ((MainActivity)this.context).findViewById(R.id.list_experience);
		if(list != null) {
			((MainActivity) this.context).getTotalHeightofListView(list);
		}
	}
	
	public class ExperienceItem {
		public String name;
		public String period;
		public String company;
		public String description;
		
		public ExperienceItem(String name, String period, String company, String description) {
			this.name = name;
			this.period = period;
			this.description = description;
			this.company = company;
		}
	}
}
