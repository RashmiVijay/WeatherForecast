package com.westpac.weatherforecast;
import java.io.IOException;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//imports for JSON parsing
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	ArrayList < Weather > factsList;
	// GPSTracker class
	GPSTrack gps;

	String WURL = "https://api.forecast.io/forecast/";
	String ACCESS_KEY = "f85e51f2fd361ce21c1042c8986e7c10/";
	double latLoc;
	double lngLoc;
	String LOCURL = WURL + ACCESS_KEY + latLoc + "," + lngLoc;
	String location;
	WeatherAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//	Config.context = this;
		GPSTrack loc = new GPSTrack(MainActivity.this);
		Location location = loc.getLocation();


		if (location != null) {
			latLoc = location.getLatitude();
			lngLoc = location.getLongitude();
		}

		
		factsList = new ArrayList < Weather > ();
		//Executing asynctask for recieving JSON data from the server

		//new JSONAsyncTask().execute("https://api.forecast.io/forecast/f85e51f2fd361ce21c1042c8986e7c10/37.8267,-122.423");
		new JSONAsyncTask().execute(LOCURL);

		ListView listview = (ListView) findViewById(R.id.list);
		adapter = new WeatherAdapter(getApplicationContext(), R.layout.row, factsList);

		listview.setAdapter(adapter);

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(listener);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView <? > arg0, View arg1, int position,
			long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), factsList.get(position).getName(), Toast.LENGTH_LONG).show();
			}
		});
	}


	class JSONAsyncTask extends AsyncTask < String, Void, Boolean > {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting to server");
			dialog.show();
			dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(String...urls) {
			try {


				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					String curTemp = "";
					//Pasring The JSON Array and the JSON object with the key value pairs
					JSONObject jsono = new JSONObject(data);
					JSONObject curObject = jsono.getJSONObject("currently");
				     if (curObject.has("time")) {
				    	 curTemp = curObject.getString("time") + " ";
				     }

				     if (curObject.has("summary")) {
				    	 curTemp += curObject.getString("summary") + " ";
				     }

				     if (curObject.has("temperature")) {
				    	 curTemp += curObject.getString("temperature");
				     }

				     curTemp = curTemp.trim();
					
						Weather fact = new Weather();

						fact.setName(curObject.getString("time")); // time object of JSON data (title:)
						fact.setDescription(curObject.getString("summary")); //summary object of JSON data(description:)
						fact.setTemperature(curObject.getInt("temperature"));
						factsList.add(fact);
					//}
					return true;
				}

				//------------------>>

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		//Failure case
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			adapter.notifyDataSetChanged();
			if (result == false) Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

		}
	}


	public OnClickListener listener = new OnClickListener() {@Override
		public void onClick(View arg0) {

			//Refresh view
			adapter.notifyDataSetChanged();

		}
	};




}