---
title: Android基础 - 天气预报demo
date: 2016-05-11 09:37:46
tags:
---
### 天气实体类 Weather.java

``` bash
package com.lyp.myweatherdemo.bean;

public class Weather {

	private String currentCity 	= null;
	private String currentTemp 	= null;
	private String pm25			= null;
	private String date[] 		= new String[4];
	private String date_weather[]= new String[4];
	private String date_wind[] 	= new String[4];
	private String date_temp[] 	= new String[4];

	public Weather() {
		currentCity 	= "null";
		currentTemp 	= "null";
		pm25			= "null";
		date 			= new String[4];
		date_weather	= new String[4];
		date_wind 		= new String[4];
		date_temp 		= new String[4];
	}

	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getCurrentTemp() {
		return currentTemp;
	}
	public void setCurrentTemp(String currentTemp) {
		this.currentTemp = currentTemp;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String[] getDate() {
		return date;
	}
	public void setDate(String[] date) {
		this.date = date;
	}
	public String[] getDate_weather() {
		return date_weather;
	}
	public void setDate_weather(String[] date_weather) {
		this.date_weather = date_weather;
	}
	public String[] getDate_wind() {
		return date_wind;
	}
	public void setDate_wind(String[] date_wind) {
		this.date_wind = date_wind;
	}
	public String[] getDate_temp() {
		return date_temp;
	}
	public void setDate_temp(String[] date_temp) {
		this.date_temp = date_temp;
	}
}
```

### MainActivity.java
``` bash
package com.lyp.myweatherdemo;

import com.lyp.myweatherdemo.bean.Weather;

/**
 * 天气预报
 * @author LYP
 */
public class MainActivity extends Activity {

	private static final String MCODE = "BC:E8:12:E1:15:3A:59:12:94:52:FF:42:CB:62:DC:8A:C1:DC:66:DD;com.lyp.myweatherdemo";//百度API安全码
	private static final String AK = "rqD8Nr9xUUCNQWyC5Laj9E0R";

	private Button bt_urlConn;
	private TextView tv_show;
	private TextView tv_currentTemp;
	private TextView tv_currentCity;
	private TextView tv_weather;
	private TextView tv_pm25;
	private TextView tv_date[] = new TextView[4];

	private String city;
	private String url;
	private String jsonData;

	private Weather weather;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Weather w = (Weather) msg.obj;

			tv_show.setText(jsonData);
			tv_currentCity.setText(w.getCurrentCity());
			tv_currentTemp.setText(w.getCurrentTemp());
			tv_pm25.setText("PM2.5:" + w.getPm25());
			tv_weather.setText(w.getDate_weather()[0] + "  " + w.getDate_wind()[0]);
			tv_date[0].setText("今    天 | " + w.getDate_weather()[0] + " | " + w.getDate_temp()[0]);
			tv_date[1].setText("明    天 | " + w.getDate_weather()[1] + " | " + w.getDate_temp()[1]);
			tv_date[2].setText("后    天 | " + w.getDate_weather()[2] + " | " + w.getDate_temp()[2]);
			tv_date[3].setText("大后天 | " + w.getDate_weather()[3] + " | " + w.getDate_temp()[3]);
		}
	};
```
上面mHandler的作用：
通过重写handleMessage方法，接收子线程中通过网络请求得到的Weather实体，
给相应的控件设置数据。

``` bash
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();


		bt_urlConn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				new Thread(new Runnable() {
					public void run() {
						jsonData = backStringFromGet(url);
						Log.d("LOG", jsonData);

						try {
							JSONObject jsonObject = new JSONObject(jsonData);
							JSONArray JA_results = jsonObject.getJSONArray("results");

							JSONObject JO_results1 = JA_results.getJSONObject(0);
							weather.setCurrentCity(JO_results1.getString("currentCity"));
							weather.setPm25(JO_results1.getString("pm25"));

							JSONArray JA_weatherData = JO_results1.getJSONArray("weather_data");
							String date[] 		= new String[4];
							String date_weather[]= new String[4];
							String date_wind[] 	= new String[4];
							String date_temp[] 	= new String[4];
							for (int i = 0; i < JA_weatherData.length(); i++) {
								JSONObject item = JA_weatherData.getJSONObject(i);
								date[i] = item.getString("date");
								date_weather[i] = item.getString("weather");
								date_temp[i] = item.getString("temperature");
								date_wind[i] = item.getString("wind");
							}
							weather.setDate(date);
							weather.setDate_weather(date_weather);
							weather.setDate_temp(date_temp);
							weather.setDate_wind(date_wind);
							weather.setCurrentTemp(date[0].substring(date[0].indexOf("：")+1, date[0].length()-1));

						} catch (JSONException e) {
							e.printStackTrace();
						}

						Message message = new Message();
						message.obj = weather;
						mHandler.sendMessage(message);
					};
				}).start();
			}
		});
	}

    public static String backStringFromGet(String url) {
		String result = null;
		try {
			URL httpURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpURL.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			if(conn.getResponseCode() == 200){
				BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
				String str;
				StringBuffer sb = new StringBuffer();
				while((str=reader.readLine()) != null){
					sb.append(str);
				}
				result = sb.toString();
				reader.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

```

上面当点击按钮时，开启一个子线程，获取网络数据。

在子线程的run()方法中，通过封装好的backStringFromGet(url)函数得到json字符串数据，
然后通过JSONObject和JSONArray类对json进行解析，并存储在一个Weather实体中。

由于安卓不能在子线程中对UI进行更新操作，所以通过new Message()创建一个消息对象，
设置它的obj为之前的Weather实体，利用Handler的sendMessage方法把实体传到主线程中。

其余代码如下:


```bash
	private void initData() {
		try {
			this.city = URLEncoder.encode("大连", "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//百度API
		this.url = "http://api.map.baidu.com/telematics/v3/weather"
				+ "?location="+this.city
				+ "&mcode="+MCODE
				+ "&output=json"
				+ "&ak="+AK;

		this.weather = new Weather();
	}

	private void initView() {
		bt_urlConn = (Button) findViewById(R.id.bt_urlConn);
		tv_show = (TextView) findViewById(R.id.tv_show);
		tv_currentTemp = (TextView) findViewById(R.id.tv_currentTemp);
		tv_currentCity = (TextView) findViewById(R.id.tv_currentCity);
		tv_weather = (TextView) findViewById(R.id.tv_weather);
		tv_pm25	= (TextView) findViewById(R.id.tv_pm25);
		tv_date[0] = (TextView) findViewById(R.id.tv_date1);
		tv_date[1] = (TextView) findViewById(R.id.tv_date2);
		tv_date[2] = (TextView) findViewById(R.id.tv_date3);
		tv_date[3] = (TextView) findViewById(R.id.tv_date4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_quit) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
```
