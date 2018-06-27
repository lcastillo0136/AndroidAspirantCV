package DAO;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DAOBase extends AppCompatActivity {
	private String url = "";
	private Context current_context;
	
  protected int RequestMethod;
	
	public DAOBase() {
		RequestMethod = Request.Method.POST;
		
	}
	
	protected RequestQueue Request(String url, HashMap<String, String> params, Response.Listener<JSONObject> response_function, Response.ErrorListener response_error) {
		RequestQueue queue = Volley.newRequestQueue(this);
		
		// Request a string response from the provided URL.
		JsonObjectRequest stringRequest = new JsonObjectRequest(this.RequestMethod, url, new JSONObject(params), response_function, response_error);
		
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
		
		return queue;
	}
	
	protected RequestQueue Request(String url, HashMap<String, String> params, Response.Listener<JSONObject> response_function) {
		return this.Request(url, params, response_function, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			
			}
		});
	}
	
	protected RequestQueue Request(String url, HashMap<String, String> params) {
		return this.Request(url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
			
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			
			}
		});
	}
	
	protected RequestQueue Request(String url) {
		return this.Request(url, new HashMap<String, String>(), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
			
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			
			}
		});
	}
}
