package org.fitark.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "xx";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		Button startContactManageBtn = (Button) findViewById(R.id.contactManageActivity);
		startContactManageBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startContactManageActivity();
			}
		});
		((Button) findViewById(R.id.showPDF))
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						showPDF();
					}

				});
		((Button) findViewById(R.id.showImage))
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						showImage();
					}

				});

		// 设置是否开启左上按钮
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void showPDF() {
		Uri uri = Uri
				.parse("http://fitark.org:7500/files/93d75dc1269e49d593f04195a875d59d.pdf");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		WebView webview = new WebView(this);
		setContentView(webview);
	}

	private void showImage() {
		Uri uri = Uri
				.parse("http://fitark.org:7500/files/93d75dc1269e49d593f04195a875d59d.png");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		WebView webview = new WebView(this);
		setContentView(webview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_search:
			// openSearch();
			return true;
		case R.id.action_settings:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public void startBackupRestoreActivity(View view) {
		Intent intent = new Intent(this, BackupRestoreActivity.class);
		startActivity(intent);
	}

	protected void startContactManageActivity() {
		Intent intent = new Intent(this, ContactManagerActivity.class);
		startActivity(intent);

	}

	public void playVideo(View view) {
		Intent intent = new Intent(this, PlayVideoActivity.class);
		startActivity(intent);
	}
}
