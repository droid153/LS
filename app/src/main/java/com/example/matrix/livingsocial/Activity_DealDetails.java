package com.example.matrix.livingsocial;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by matrix on 10/5/2014.
 */
public class Activity_DealDetails extends Activity {

    ImageView imageDetailView;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String merchant = null;
        String shortTitle = null;
        String offerEnd = null;
        String price = null;
        String imageUrl = null;
        String savings = null;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_deal_details);

        if(getIntent() != null){
            merchant = getIntent().getStringExtra("dataMerchantName");
            shortTitle = getIntent().getStringExtra("dataShortTitle");
            offerEnd = getIntent().getStringExtra("dataOfferEnd");
            price = getIntent().getStringExtra("dataPrice");
            imageUrl = getIntent().getStringExtra("dataImageUrl");
            savings = getIntent().getStringExtra("dataSavings");


            ((TextView) findViewById(R.id.textMerchantDetail)).setText(merchant);
            ((TextView) findViewById(R.id.textShortTitleDetail)).setText(shortTitle);
            ((TextView) findViewById(R.id.textPriceDetail)).setText("$" + price);
            ((TextView) findViewById(R.id.textOfferEndDetail)).setText("HURRY!!! Offer Ends " + offerEnd);
            ((TextView) findViewById(R.id.textSavingsDetail)).setText("$" + savings + " Off original price");
            ((ImageView)findViewById(R.id.imageDetails)).setImageResource(R.drawable.ic_launcher);
//            new DownloadImageDetail(imageDetailView).execute(imageUrl);

        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);

        ActionBar actionBar = getActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#dd8833")));

        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }


    public class DownloadImageDetail extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageDetail(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

}
