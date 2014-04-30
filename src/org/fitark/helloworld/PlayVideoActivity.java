package org.fitark.helloworld;

import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Toast;

public class PlayVideoActivity extends ActionBarActivity {

	static String TAG = "PlayVideoActivity";
	String src = "http://fitark.org:7500/files/50c9c8594ad9423891e7282a5379c50d.flv";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_play_video);
		if (check()) {
			final WebView mWebFlash = (WebView) findViewById(R.id.video_web_view);
			WebSettings settings = mWebFlash.getSettings();
			settings.setJavaScriptEnabled(true);
			settings.setAllowFileAccess(true);
			settings.setPluginState(PluginState.ON);
			mWebFlash.setWebChromeClient(new WebChromeClient());
			mWebFlash.addJavascriptInterface(new CallJava(), "CallJava");
			mWebFlash.setBackgroundColor(0);
			String html = "<embed width=\"100%\" height=400 id=\"fmovie\" src=\"http://fitark.org:8082/playback.swf?src=\"" + src + "\" type=\"application/x-shockwave-flash\">";
			mWebFlash.loadDataWithBaseURL(null, html, "text/html", "utf-8",
					null);
			mWebFlash.getSettings().setLayoutAlgorithm(
					LayoutAlgorithm.SINGLE_COLUMN);
			mWebFlash.getSettings().setJavaScriptEnabled(true); // 设置支持Javascript
			mWebFlash.requestFocus();
			;
			// mWebFlash
			// .loadUrl("file:///android_asset/player.html?src=http://fitark.org:7500/files/50c9c8594ad9423891e7282a5379c50d.flv");
			mWebFlash.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 调用javascript的函数get4Android(str)

					mWebFlash.loadUrl("javascript:setVideoSrc(" + src + ")");
				};
			});
		} else {
			Toast.makeText(this, "xxxxxxxxxx", 50);
		}
	}

	// 检查浏览器是否支持swf
	private boolean check() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

	private final class CallJava {
		public String getVideoSrc() {
			return src;
		}

		public void consoleFlashProgress(float progressSize) {
			Log.v(TAG, "yyy");
		}

		public void FlashLoaded() {
			Log.v(TAG, "xxx");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_video, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_play_video,
					container, false);
			return rootView;
		}
	}

}
