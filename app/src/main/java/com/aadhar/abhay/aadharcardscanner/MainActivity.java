package com.aadhar.abhay.aadharcardscanner;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.vision.barcode.Barcode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity  {
    private AdView mAdView;
    CardView cardView,c2;
    TextView uidtext,nametext,dobtext,addtext,gntext,result,posttext,statetxt;
    String uid,name,dob,add,gn,post,state;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    AnimatorSet animationSet;
    XmlPullParserFactory pullParserFactory;

    Handler handler = new Handler();

    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
         uidtext=(TextView)findViewById(R.id.textView1);
         nametext=(TextView)findViewById(R.id.textView2);
         dobtext=(TextView)findViewById(R.id.textView3);
         gntext=(TextView)findViewById(R.id.textView4);

        animationSet = new AnimatorSet();
        statetxt=(TextView)findViewById(R.id.textView14);
       result=(TextView)findViewById(R.id.result);
        cardView = (CardView) findViewById(R.id.cardView2);
        c2 = (CardView) findViewById(R.id.cardView);
       c2.setVisibility(View.GONE);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-4855672100917117/9769702189");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");

                result.post(new Runnable() {
                    @Override
                    public void run() {
                        String strings = barcode.displayValue;
                        try {
                            pullParserFactory = XmlPullParserFactory.newInstance();
                            XmlPullParser parser = pullParserFactory.newPullParser();
                            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                            parser.setInput(new StringReader(strings));

                            int eventType = parser.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_DOCUMENT) {


                                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                                    // extract data from tag
                                    //uid
                                    uid = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                                    //name

                                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                                    //gender
                                    gn = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                                    // year of birth
                                    dob= parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                                    // care of
                                   // careOf = parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR);
                                    // village Tehsil
                                   add = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                                    // Post Office
                                    /*postOffice = parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR);
                                    // district
                                    district = parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);*/
                                    // state
                                    state = parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                                    // Post Code
                                    post = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);

                                } else if (eventType == XmlPullParser.END_TAG) {


                                } else if (eventType == XmlPullParser.TEXT) {


                                }
                                // update eventType
                                eventType = parser.next();
                            }

                              c2.setVisibility(View.VISIBLE);

                            ObjectAnimator card_y = ObjectAnimator.ofFloat(c2, View.TRANSLATION_X, 70);
                            card_y.setDuration(2500);
                            card_y.setRepeatMode(ValueAnimator.REVERSE);
                            card_y.setRepeatCount(ValueAnimator.INFINITE);
                            card_y.setInterpolator(new LinearInterpolator());
                            animationSet.playTogether(card_y);
                            animationSet.start();

                              if(gn.equals("M"))
                              {
                                  gntext.setText("GANDER:_MALE");
                              }else {
                                  gntext.setText("GANDER:_FEMALE");
                              }

                            uidtext.setText("UID:_"+uid);
                            nametext.setText("NAME:_"+name);
                            dobtext.setText("DOB:_"+dob);

                            statetxt.setText("ADDRESS:_"+state+"  "+add+"  "+post);

                            handler.postDelayed(runnable,3*1000);

                        }


                        catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }


    }
       Runnable runnable=new Runnable() {
           @Override
           public void run() {
               if (mInterstitialAd.isLoaded()) {
                   mInterstitialAd.show();
               } else {
                   Log.d("TAG", "The interstitial wasn't loaded yet.");
               }

           }
       };

}
