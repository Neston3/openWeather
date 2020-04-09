package com.maricajr.openweather;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.maricajr.openweather.Url_String.WEATHER_URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private Button mButton;
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    private boolean mShowingFragments = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeVolleyRequest();

        mViewPager = findViewById(R.id.viewPager);


    }

    private void makeVolleyRequest() {
        String URLline = WEATHER_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }


    public void parseData(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);

            JSONArray items = jsonObject.getJSONArray("list");
            mCardAdapter = new CardPagerAdapter();

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String name = item.getString("name");
                JSONObject temp = item.getJSONObject("main");
                String temp1 = temp.getString("temp");
                String humidity = temp.getString("humidity");
                String pressure = temp.getString("pressure");
                String weather_clouds = "";

                JSONObject wind_obj = item.getJSONObject("wind");
                String wind = wind_obj.getString("speed");

                JSONArray weathers = item.getJSONArray("weather");
                for (int j = 0; j < weathers.length(); j++) {
                    JSONObject item_weather = weathers.getJSONObject(j);
                    weather_clouds = item_weather.getString("description");
                }

                mCardAdapter.addCardItem(new CardItem(name, wind, humidity, pressure, weather_clouds, temp1));

            }

            mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                    dpToPixels(2, this));

            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
            mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mViewPager.setOffscreenPageLimit(3);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (!mShowingFragments) {
            mButton.setText("Views");
            mViewPager.setAdapter(mFragmentCardAdapter);
            mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        } else {
            mButton.setText("Fragments");
            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
        }

        mShowingFragments = !mShowingFragments;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCardShadowTransformer.enableScaling(isChecked);
        mFragmentCardShadowTransformer.enableScaling(isChecked);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}
