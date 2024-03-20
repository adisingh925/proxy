package app.android.heartrate.phoneapp.activities.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.activities.record.blood_count.BloodCountDataActivity;
import app.android.heartrate.phoneapp.activities.record.blood_pressure.BloodPressureDataActivity;
import app.android.heartrate.phoneapp.activities.record.blood_sugar.BloodSugarDataActivity;
import app.android.heartrate.phoneapp.activities.record.bmi.BMIDataActivity;
import app.android.heartrate.phoneapp.activities.record.body_temp.BodyTempDataActivity;
import app.android.heartrate.phoneapp.activities.record.cholestrol.CholesterolDataActivity;
import app.android.heartrate.phoneapp.activities.record.heart_rate.HeartRateDataActivity;
import app.android.heartrate.phoneapp.utils.AppConstants;


public class TrackerToolsActivity extends AppCompatActivity {


    Context mContext;
    Animation push_animation;

    RelativeLayout rel_blood_count;
    RelativeLayout rel_blood_pressure;
    RelativeLayout rel_blood_sugar;
    RelativeLayout rel_bmi;
    RelativeLayout rel_body_temp;
    RelativeLayout rel_cholesterol;
    RelativeLayout rel_heart_rate;
    RelativeLayout rel_medicines;
    RelativeLayout rel_weight;
    TextView spinner_txt_name;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();


        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_tracker_tools);

        this.mContext = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        this.rel_blood_count = findViewById(R.id.tracker_tools_rel_blood_count);
        this.rel_blood_pressure = findViewById(R.id.tracker_tools_rel_bp);
        this.rel_blood_sugar = findViewById(R.id.tracker_tools_rel_blood_sugar);
        this.rel_bmi = findViewById(R.id.tracker_tools_rel_bmi);
        this.rel_body_temp = findViewById(R.id.tracker_tools_rel_body_temp);
        this.rel_cholesterol = findViewById(R.id.tracker_tools_rel_cholesterol);
        this.rel_heart_rate = findViewById(R.id.tracker_tools_rel_heart_rate);
        this.rel_weight = findViewById(R.id.tracker_tools_rel_weight);
        this.rel_medicines = findViewById(R.id.tracker_tools_rel_medicine);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        this.rel_blood_count.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.BloodCountScreen();
            }
        });
        this.rel_blood_pressure.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.BloodPressureScreen();
            }
        });
        this.rel_blood_sugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.BloodSugarScreen();
            }
        });
        this.rel_bmi.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.BMIScreen();
            }
        });
        this.rel_body_temp.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.BodyTempScreen();
            }
        });
        this.rel_cholesterol.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.CholesterolScreen();
            }
        });
        this.rel_heart_rate.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.HeartRateScreen();
            }
        });
        this.rel_weight.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.WeightScreen();
            }
        });
        this.rel_medicines.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(TrackerToolsActivity.this.push_animation);
                TrackerToolsActivity.this.MedicinesScreen();
            }
        });
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_lets_track));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_health));
        setSupportActionBar(findViewById(R.id.toolbar_actionbar));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onResume() {
        super.onResume();


    }


//


//


    @Override
    public void onBackPressed() {
        super.onBackPressed();


//


    }


//


//


//


    private void BackScreen() {

        finish();
        AppConstants.overridePendingTransitionExit(this);
    }


    private void BloodCountScreen() {
        startActivity(new Intent(this, BloodCountDataActivity.class));
    }


    private void BloodPressureScreen() {
        startActivity(new Intent(this, BloodPressureDataActivity.class));
    }


    private void BloodSugarScreen() {
        startActivity(new Intent(this, BloodSugarDataActivity.class));
    }


    private void BMIScreen() {
        startActivity(new Intent(this, BMIDataActivity.class));
    }


    private void BodyTempScreen() {
        startActivity(new Intent(this, BodyTempDataActivity.class));
    }


    private void CholesterolScreen() {
        startActivity(new Intent(this, CholesterolDataActivity.class));
    }


    private void HeartRateScreen() {
        startActivity(new Intent(this, HeartRateDataActivity.class));
    }


    private void WeightScreen() {
        startActivity(new Intent(this, WeightDataActivity.class));
    }


    private void MedicinesScreen() {
        startActivity(new Intent(this, MedicineDataActivity.class));
    }
}
