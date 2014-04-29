package org.fitark.helloworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fitark.helloworld.util.JSONArrayAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;

public class ContactManagerActivity extends Activity {
	static final String TAG = "ContactManagerActivity";
	private ListView mContactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_contact_manager);

		mContactList = (ListView) findViewById(R.id.contact_list);
		populateContactList();
	}

	Runnable getDataThread = new Runnable() {
		@Override
		public void run() {
			getData();
		}
	};

	private void populateContactList() {
		new Thread(getDataThread).start();
	}

	private void getData() {
		String result;
		BufferedReader reader = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet req = new HttpGet();
			req.setURI(new URI("http://fitark.org:9000/patients.json"));
			HttpResponse res = client.execute(req);
			reader = new BufferedReader(new InputStreamReader(res.getEntity()
					.getContent()));
			StringBuffer strBuffer = new StringBuffer("");
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			JSONObject json = new JSONObject(result);
			JSONArray jsonArray = json.getJSONArray("patients");
			final JSONArray ja = jsonArray;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String[] from = new String[] { "name", "home_address",
							"photo" };
					int[] to = new int[] { R.id.name, R.id.home_address,
							R.id.photo };
					JSONArrayAdapter adapter = new JSONArrayAdapter(
							ContactManagerActivity.this, ja,
							R.layout.contact_list, from, to);
					adapter.setViewBinder(new ViewBinder() {

						@Override
						public boolean setViewValue(View view, Object data,
								String textRepresentation) {
							if (view instanceof ImageView & data instanceof Bitmap) {
								ImageView iv = (ImageView) view;
								iv.setImageBitmap((Bitmap) data);
								return true;
							}
							return false;
						}
					});
					mContactList.setAdapter(adapter);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_contact_manager,
					container, false);
			return rootView;
		}
	}

}
