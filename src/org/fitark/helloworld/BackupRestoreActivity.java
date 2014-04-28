package org.fitark.helloworld;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.app.Fragment;
import android.app.backup.BackupManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

public class BackupRestoreActivity extends Activity {

	static final String TAG = "BackupRestoreActivity";
	static final String FILE_NAME = "saved_data";
	static final Object[] sDataLock = new Object[0];

	RadioGroup fav_color_group;
	CheckBox hobby_basketball;
	CheckBox hobby_football;
	CheckBox hobby_sing;
	File mDataFile;
	BackupManager bckManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_backup_restore);
		fav_color_group = (RadioGroup) findViewById(R.id.fav_color_group);
		hobby_basketball = (CheckBox) findViewById(R.id.hobby_basketball);
		hobby_football = (CheckBox) findViewById(R.id.hobby_football);
		hobby_sing = (CheckBox) findViewById(R.id.hobby_sing);
		mDataFile = new File(getFilesDir(), BackupRestoreActivity.FILE_NAME);
		bckManager = new BackupManager(this);

		populateUI();

	}
	void populateUI() {
		RandomAccessFile file;

		int whichFav = R.id.fav_blue;
		boolean basketball = false;
		boolean football = false;
		boolean sing = false;

		synchronized (BackupRestoreActivity.sDataLock) {
			boolean exists = mDataFile.exists();
			try {
				file = new RandomAccessFile(mDataFile, "rw");
				if (exists) {
					Log.v(TAG, "文件已经存在");
					whichFav = file.readInt();
					basketball = file.readBoolean();
					football = file.readBoolean();
					sing = file.readBoolean();
					Log.v(TAG, "最喜欢的颜色是：" + whichFav + " 爱好 篮球：" + basketball
							+ " 足球：" + football + " 唱歌：" + sing);
				} else {
					// The default values were configured above: write them
					// to the newly-created file.
					Log.v(TAG, "创建默认文件");
					writeDataToFileLocked(file, basketball, football, sing,
							whichFav);
					//
					// // We also need to perform an initial backup; ask for one
					bckManager.dataChanged();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		fav_color_group.check(whichFav);
		hobby_basketball.setChecked(basketball);
		hobby_football.setChecked(football);
		hobby_sing.setChecked(sing);
		fav_color_group
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						Log.v(TAG, "新值被选中:" + checkedId);
						recordNewUIState();
					}
				});
		CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.v(TAG, "新值被选中:" + buttonView + isChecked);
				recordNewUIState();
			}
		};
		hobby_basketball.setOnCheckedChangeListener(checkListener);
		hobby_football.setOnCheckedChangeListener(checkListener);
		hobby_sing.setOnCheckedChangeListener(checkListener);
	}

	void recordNewUIState() {
		int whichFav = fav_color_group.getCheckedRadioButtonId();
		boolean basketball = hobby_basketball.isChecked();
		boolean football = hobby_football.isChecked();
		boolean sing = hobby_sing.isChecked();
		try {
			RandomAccessFile file = new RandomAccessFile(mDataFile, "rw");
			synchronized (BackupRestoreActivity.sDataLock) {
				writeDataToFileLocked(file, basketball, football, sing,
						whichFav);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		bckManager.dataChanged();
	}

	void writeDataToFileLocked(RandomAccessFile file, boolean basketball,
			boolean football, boolean sing, int whichFav) throws IOException {
		file.setLength(0L);
		file.writeInt(whichFav);
		file.writeBoolean(basketball);
		file.writeBoolean(football);
		file.writeBoolean(sing);
		Log.v(TAG, "写入最新值 最喜欢的颜色是：" + whichFav + " 爱好 篮球：" + basketball
				+ " 足球：" + football + " 唱歌：" + sing);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.backup_restore, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_backup_restore,
					container, false);
			return rootView;
		}
	}

}
