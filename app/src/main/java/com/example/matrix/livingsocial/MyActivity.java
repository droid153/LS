package com.example.matrix.livingsocial;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrix.livingsocial.util.MyParcelable;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MyActivity extends Activity {

    private double mLat = 40.74;
    private double mLong = -73.98;

//    public static final String SERVER_URL_LS = "http://monocle.livingsocial.com/v2/deals?coords=40.74,-73.98&api-key=8551A250FEB245E5836CDB902C163A6C";
    public static final String SERVER_URL_LS = "http://monocle.livingsocial.com/v2/deals?coords=40.74,-73.98&api-key=8551A250FEB245E5836CDB902C163A6C";

//    String[] mTitle = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public static ArrayList<String> mMerchantName = new ArrayList<String>();
    public static ArrayList<String> mShortTitle = new ArrayList<String>();
    public static ArrayList<String> mOfferEnd = new ArrayList<String>();
    public static ArrayList<String> mPrice = new ArrayList<String>();
    public static ArrayList<String> mSavings = new ArrayList<String>();
    public static ArrayList<String> mDiscounts = new ArrayList<String>();
    public static ArrayList<String> mImageUrl = new ArrayList<String>();
    ListView mListview;
    TextView mTextview;
    ImageView mImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mListview = (ListView) findViewById(R.id.listView1);
        mTextview = (TextView) findViewById(R.id.merchantName);
        mTextview = (TextView) findViewById(R.id.shortTitle);
        mImageview = (ImageView) findViewById(R.id.image);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                String item = ((TextView) v.findViewById(R.id.merchantName)).getText().toString();
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();

                Intent detailsIntent = new Intent(MyActivity.this, Activity_DealDetails.class);
                detailsIntent.putExtra("dataMerchantName",mMerchantName.get(arg2));
                detailsIntent.putExtra("dataShortTitle",mShortTitle.get(arg2));
                detailsIntent.putExtra("dataOfferEnd",mOfferEnd.get(arg2));
                detailsIntent.putExtra("dataPrice",mPrice.get(arg2));
                detailsIntent.putExtra("dataImageUrl",mImageUrl.get(arg2));
                detailsIntent.putExtra("dataSavings",mSavings.get(arg2));
                detailsIntent.putExtra("dataDiscount",mDiscounts.get(arg2));

                startActivity(detailsIntent);
            }
        });

        DownloadData dd = new DownloadData();
        dd.execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        ActionBar actionBar = getActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#dd8833")));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;

            case R.id.ic_action_search:
//            Toast.makeText(getApplicationContext(), "Clicked Search", Toast.LENGTH_SHORT).show();
                Intent searchIntent = new Intent(this, ActionBarSearch.class);
                startActivity(searchIntent);
                break;

            case R.id.choose_city:
                //            Toast.makeText(getApplicationContext(), "Clicked Search", Toast.LENGTH_SHORT).show();




                Intent chooseCityIntent = new Intent(this, ChooseCity.class);
                chooseCityIntent.putExtra("PARCEL", new MyParcelable("1", "ABHI", "3"));
                startActivity(chooseCityIntent);
                break;

        }

        if (id == R.id.ic_action_search) {

        }


        return super.onOptionsItemSelected(item);
    }


    private class DownloadData extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject;

        @Override
        protected JSONObject doInBackground(Void... arg0) {

            try {

                DefaultHttpClient defaultClient = new DefaultHttpClient();
                HttpGet httpGetRequest = new HttpGet(SERVER_URL_LS);
//                Log.d("PARAM:", String.valueOf(httpGetRequest.getParams()));
                HttpResponse httpResponse = defaultClient.execute(httpGetRequest);

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF_8"));
                String json = reader.readLine();

                jsonObject = new JSONObject(json);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return jsonObject;
        }

        protected void onPostExecute(JSONObject result) {

            grouponParser(result);

            ListLayoutAdapter adapter = new ListLayoutAdapter(MyActivity.this, mMerchantName, mImageUrl, mShortTitle, mPrice, mOfferEnd, mDiscounts);
            mListview.setAdapter(adapter);

        }    //onPostExecute

        private void grouponParser(JSONObject result) {
            JSONArray dealsArray;
            JSONObject oneElemInDealsArray;
            JSONArray optionsArray;
            JSONObject dealTypeInside;
            JSONArray imagesArray;
            JSONObject imagesInside;

            try {
                dealsArray = result.getJSONArray("deals");

                for (int i = 0; i < dealsArray.length(); i++) {
                    oneElemInDealsArray = dealsArray.getJSONObject(i);

                    // Parsing individual node in "deals" array //
                    mShortTitle.add(i, oneElemInDealsArray.getString("short_title"));
                    mMerchantName.add(i, oneElemInDealsArray.getString("merchant_name"));
                    mOfferEnd.add(i, dateEdit(oneElemInDealsArray.getString("offer_ends_at")));

                    // Parsing individual nodes in "options" array //
                    optionsArray = oneElemInDealsArray.getJSONArray("options");
                    for (int j = 0; j < optionsArray.length(); j++) {
                        dealTypeInside = optionsArray.getJSONObject(j);

                        mPrice.add(i, dealTypeInside.getString("price"));
                        mSavings.add(i,dealTypeInside.getString("savings"));
                        mDiscounts.add(i,dealTypeInside.getString("discount"));
                    }

                    // Parsing individual nodes in "images" array //
                    imagesArray = oneElemInDealsArray.getJSONArray("images");
                    for (int j = 0; j < imagesArray.length(); j++) {
                        imagesInside = imagesArray.getJSONObject(j);

                        mImageUrl.add(i, imagesInside.getString("size700"));
                        //Log.d("ImageURL:", imagesInside.getString("size700"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String dateEdit(String timestamp) throws JSONException {

            StringBuilder sb = new StringBuilder(timestamp);
            for(int i=0;i<9;i++)
                sb.deleteCharAt(10);
            String temp = sb.deleteCharAt(10).toString();
//            Log.d("DATE_TIME",temp);

            return temp;
        }
    }

    class ListLayoutAdapter extends ArrayAdapter<String> {
        Context mContext;
        ArrayList<String> mMerchantName;
        ArrayList<String> mDescArray;
        ArrayList<String> mOfferEndDate;
        ArrayList<String> mPrice;
        ArrayList<String> mDiscount;
        ArrayList<String> mImageUrl;

        public ListLayoutAdapter(Context context,
                                 ArrayList<String> merchant_name,
                                 ArrayList<String> imageUrl,
                                 ArrayList<String> description,
                                 ArrayList<String> price,
                                 ArrayList<String> offerEndDate,
                                 ArrayList<String> discount) {
            super(context, R.layout.layout_image_title_description, R.id.merchantName, description);

            this.mContext = context;
            this.mMerchantName = merchant_name;
            this.mDescArray = description;
            this.mOfferEndDate = offerEndDate;
            this.mPrice = price;
            this.mImageUrl = imageUrl;
            this.mDiscount = discount;
        }

        // Optimize further to avoid findViewById() call. Its expensive too. So use view holder class //
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            ViewHolder holder = null;

            if (row == null) {
                LayoutInflater rowInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = rowInflator.inflate(R.layout.layout_image_title_description, parent, false);

                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            holder.merchantText.setText(mMerchantName.get(position));
            holder.shortTitleText.setText(mShortTitle.get(position));
            holder.priceText.setText("$" + mPrice.get(position));
            holder.offerEndText.setText(mOfferEnd.get(position));
            holder.discountText.setText(mDiscounts.get(position) + "% OFF");

            holder.imageUrlText.setImageResource(R.drawable.ic_launcher);
            new DownloadImageTask(holder.imageUrlText).execute(mImageUrl.get(position));

            return row;
        }

        class ViewHolder {
            TextView merchantText;
            TextView shortTitleText;
//            TextView dealTypeText;
            TextView priceText;
            TextView offerEndText;
            TextView discountText;
            ImageView imageUrlText;

            public ViewHolder(View row) {
                merchantText = (TextView) row.findViewById(R.id.merchantName);  Log.d("CONSTRUCTOR", merchantText.toString());
                shortTitleText = (TextView) row.findViewById(R.id.shortTitle);
                offerEndText = (TextView) row.findViewById(R.id.offerEnd);
                discountText = (TextView) row.findViewById(R.id.discount);
                priceText = (TextView) row.findViewById(R.id.price);
                imageUrlText = (ImageView) row.findViewById(R.id.image);
            }
        }
    }

}