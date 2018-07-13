package com.example.staff.profileapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.provider.Settings.Secure;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.widget.IconButton;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.ExperienceAdapter;
import Adapters.SkillAdapter;
import DAO.DAOBase;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends DAOBase {
	private MainActivity _that;
	private String android_id;
	private JSONArray skills;
	private SkillAdapter adpSkill;
	private ExperienceAdapter adpExperience;
	private ProgressDialog progress;
	
	private int current_skill;
	private boolean _tabClicked = false;
	
	private IconButton btnSend;
	private CircleImageView imageProfile;
	private ImageView profileBg;
	private TextView userName;
	private TextView skillLabel;
	private TextView currentJob;
	private TextView mobileLabel;
	private TextView emailLabel;
	private TextView dobLabel;
	private TextView urlLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		progress = new ProgressDialog(this);
		progress.setTitle("Loading...");
		progress.setMessage("Please wait.");
		progress.setCancelable(false);
		progress.show();
		
		if (mWifi.isConnected() == false && mMobile.isConnected() == false) {
			Toast nowifitoast = Toast.makeText(this,"No wifi",Toast.LENGTH_LONG);
			nowifitoast.setGravity(Gravity.BOTTOM,0,0);
			nowifitoast.show();
			progress.cancel();
		}
		
		
		
		setContentView(R.layout.activity_main);
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				                      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				                      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				                      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				                      | View.SYSTEM_UI_FLAG_FULLSCREEN
				                      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		
		getWindow().getDecorView().setSystemUiVisibility(uiOptions);
		
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}
		
		this._that = this;
		
		
		
		Iconify.with(new FontAwesomeModule())
				.with(new EntypoModule())
				.with(new MaterialModule())
				.with(new SimpleLineIconsModule())
				.with(new IoniconsModule());
		
		android_id = Secure.getString(this.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		
		btnSend = findViewById(R.id.edit);
		imageProfile = findViewById(R.id.profile);
		profileBg = findViewById(R.id.header_cover_image);
		userName = findViewById(R.id.name);
		skillLabel = findViewById(R.id.designation);
		currentJob = findViewById(R.id.location);
		mobileLabel = findViewById(R.id.mobileNumber);
		emailLabel = findViewById(R.id.email);
		dobLabel = findViewById(R.id.dob);
		urlLabel = findViewById(R.id.lblURLValue);
		
		
		ListView list = findViewById(R.id.list_skills);
		adpSkill = new SkillAdapter(this);
		list.setAdapter(adpSkill);
		
		ListView liste = findViewById(R.id.list_experience);
		adpExperience = new ExperienceAdapter(this);
		liste.setAdapter(adpExperience);
		
		int permissionCheck = ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.CALL_PHONE);
		
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.CALL_PHONE  }, 0);
		}
		
		View.OnClickListener callListener = new View.OnClickListener() {
			@Override
			public  void  onClick(View v) {
				Uri uri = Uri.parse("tel:".concat(String.valueOf(mobileLabel.getText())));
				Intent intent = new Intent(Intent.ACTION_CALL, uri);
				if (ContextCompat.checkSelfPermission(_that, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
					startActivity(intent);
				}
			}
		};
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		
		urlLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(urlLabel.getText())));
				//startActivity(browserIntent);
			}
		});
		
		mobileLabel.setOnClickListener(callListener);
		((IconTextView)findViewById(R.id.phoneIcon)).setOnClickListener(callListener);
		((IconTextView)findViewById(R.id.phonereplyIcon)).setOnClickListener(callListener);
		
		btnSend.setText("{fa-heart}");
		((IconTextView)findViewById(R.id.phoneIcon)).setText("{fa-phone 32px}");
		((IconTextView)findViewById(R.id.phonereplyIcon)).setText("{fa-reply 22px}");
		current_skill = 0;
		LoadUserContent();
		
		Picasso.with(this).load("https://luiscastillo.onix-software.com/assets/images/photo.png").into(imageProfile);
		Picasso.with(this).load("https://luiscastillo.onix-software.com/assets/images/bg.png").into(profileBg);
	}
	
	private void LoadUserContent() {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dvi", android_id);
		_that.Request("https://luiscastillo.onix-software.com/js/user-content.js", params,  new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					userName.setText(response.getString("first").concat(" ").concat(response.getString("last")) );
					skills = response.getJSONArray("skillsCarousel");
					currentJob.setText(response.getString("company"));
					mobileLabel.setText(response.getJSONObject("user").getString("phone"));
					emailLabel.setText(response.getJSONObject("user").getString("email"));
					dobLabel.setText(response.getJSONObject("user").getString("age"));
					urlLabel.setText("https://luiscastillo.onix-software.com");
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date newDate = format.parse(String.valueOf(dobLabel.getText()));
					
					format = new SimpleDateFormat("MMM dd,yyyy");
					dobLabel.setText(format.format(newDate));
					
					JSONArray skills_list = response.getJSONObject("user").getJSONArray("skills");
					
					for (int isk=0;isk<skills_list.length()-1;isk++) {
						JSONObject objskill = skills_list.getJSONObject(isk);
						JSONArray objSkillsValues = objskill.getJSONArray("values");
						adpSkill.add(objskill.getString("name"), 0, true);
						
						for (int s =0;s<objSkillsValues.length()-1;s++) {
							adpSkill.add(objSkillsValues.getJSONObject(s).getString("name"), objSkillsValues.getJSONObject(s).getInt("value"));
						}
					}
					
					JSONArray experience_list = response.getJSONObject("user").getJSONArray("experience");
					
					for(int iex=0;iex<experience_list.length()-1;iex++) {
						JSONObject objexperience = experience_list.getJSONObject(iex);
						adpExperience.add(objexperience.getString("title"), objexperience.getString("year"), objexperience.getString("location"), objexperience.getString("description") );
					}
					
					adpSkill.refresh();
					adpExperience.refresh();
					skillCarousel(skills.getString(0));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				progress.cancel();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				adpSkill.add("No Connection", 0, true);
				
				adpSkill.refresh();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
      | View.SYSTEM_UI_FLAG_FULLSCREEN
      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		
		getWindow().getDecorView().setSystemUiVisibility(uiOptions);
	}
	
	private void skillCarousel(String nextSkill) {
		skillLabel.setText(nextSkill);
		new android.os.Handler().postDelayed(
			new Runnable() {
				public void run() {
					_that.current_skill++;
					try {
						if (_that.current_skill >= _that.skills.length()-1) {
							_that.current_skill = 0;
						}
						_that.skillCarousel(_that.skills.getString(_that.current_skill));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			},
			4000);
	}
	
	public static void getTotalHeightofListView(ListView listView) {
		
		ListAdapter myListAdapter = listView.getAdapter();
		if (myListAdapter == null) {
			//do nothing return null
			return;
		}
		//set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, listView);
			listItem.measure(0, 0);
			
			totalHeight += listItem.getMeasuredHeight() + listItem.getPaddingBottom() + listItem.getPaddingTop();
		}
		//setting listview item in adapter
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (myListAdapter.getCount() - 1)) + listView.getPaddingBottom() + listView.getPaddingTop();;
		listView.setLayoutParams(params);
		// print height of adapter on log
	}
	
}







































