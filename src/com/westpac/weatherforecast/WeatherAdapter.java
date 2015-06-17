package com.westpac.weatherforecast;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//FactsAdapter class corresponds to Listview Adapter for populating the JSON data
public class WeatherAdapter extends ArrayAdapter<Weather> {
	ArrayList<Weather> factList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public WeatherAdapter(Context context, int resource,
			ArrayList<Weather> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		factList = objects;
	}

	// population JSON data into the view
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
			holder.tvName = (TextView) v.findViewById(R.id.tvName);

			holder.tvDescription = (TextView) v
					.findViewById(R.id.tvDescriptionn);
			holder.tvTemperature = (TextView) v.findViewById(R.id.tvTemp);
			// holder.tvTemp = (TextView)v.findViewById(R.id.tvTemp);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.imageview.setImageResource(R.drawable.ic_launcher);
		new DownloadImageTask(holder.imageview).execute(factList.get(position)
				.getImage());
		holder.tvName.setText(factList.get(position).getName());
		holder.tvDescription.setText(factList.get(position).getDescription());
		holder.tvTemperature.setInputType(factList.get(position).getTemperature());
		return v;

	}

	static class ViewHolder {
		public ImageView imageview;
		public TextView tvName;
		public TextView tvDescription;
		public TextView tvTemperature;

	}

	// Decoding the bitmap of the image from the url in the imageHref
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				//Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}

	}
}
