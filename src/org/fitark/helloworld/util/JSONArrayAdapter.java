package org.fitark.helloworld.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SimpleAdapter;

public class JSONArrayAdapter extends SimpleAdapter {

	private static Bitmap bmp;

	public JSONArrayAdapter(Context context, JSONArray jsonArray, int resource,
			String[] from, int[] to) {
		super(context, getListFromJsonArray(jsonArray), resource, from, to);
	}

	static ArrayList<Map<String, Object>> list;

	public static List<Map<String, Object>> getListFromJsonArray(
			JSONArray jsonArray) {
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		// fill the list
		for (int i = 0; i < jsonArray.length(); i++) {
			map = new HashMap<String, Object>();
			try {
				JSONObject jo = (JSONObject) jsonArray.get(i);
				// fill map
				Iterator iter = jo.keys();
				while (iter.hasNext()) {
					String currentKey = (String) iter.next();
					if (currentKey.equals("photo")) {
						map.put(currentKey,
								getBitmap(jo.getString(currentKey).toString(),
										i));
					} else {
						map.put(currentKey, jo.getString(currentKey));
					}
				}
				// add map to list
				list.add(map);
			} catch (JSONException e) {
				Log.e("JSON", e.getLocalizedMessage());
			}

		}
		return list;
	}

	// 获取网络图片资源，返回类型是Bitmap，用于设置在ListView中
	private static Bitmap getBitmap(String httpUrl, final int i) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Map<String, Object> m = (HashMap<String, Object>) list.get(msg
						.getData().getInt("i"));
				m.put("photo", bmp);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				bmp = null;
				// ListView中获取网络图片
				try {
					URL url = new URL(
							"https://www.google.com.hk/images/srpr/logo11w.png");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					InputStream is = conn.getInputStream();
					bmp = BitmapFactory.decodeStream(is);
					Bundle data = new Bundle();
					data.putInt("i", i);
					Message msg = new Message();
					msg.setData(data);
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return bmp;
	}
}
