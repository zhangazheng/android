package org.fitark.helloworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
			// TODO Auto-generated method stub
			getData();
		}
	};

	private void populateContactList() {
		new Thread(getDataThread).start();
		// SimpleAdapter adapter = new SimpleAdapter(this, getData(),
		// R.layout.contact_list, new String[] { "name", "home_address",
		// "photo" }, new int[] { R.id.name, R.id.home_address,
		// R.id.img });
		// mContactList.setAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		String result = null;
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
			Log.v(TAG, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
