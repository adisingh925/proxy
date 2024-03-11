package app.android.heartrate.phoneapp.activities.record;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.utils.AppConstants;

public class BMIResultActivity extends AppCompatActivity {
    String bmiValue;
    ImageView img_arrow_moderately_obese;
    ImageView img_arrow_normal;
    ImageView img_arrow_overweight;
    ImageView img_arrow_severely;
    ImageView img_arrow_severely_obese;
    ImageView img_arrow_underweight;
    ImageView img_arrow_very_severely;
    ImageView img_arrow_very_severely_obese;
    ImageView img_share;
    Animation push_animation;
    String resultText;
    TextView txt_bmi_result;
    TextView txt_bmi_result_text;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.FullscreenAd(this);

    }

    private void SetView() {
        setContentView(R.layout.activity_bmi_result);

        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        setUpActionBar();
        this.img_arrow_very_severely = findViewById(R.id.bmi_result_img_very_severely);
        this.img_arrow_severely = findViewById(R.id.bmi_result_img_severely);
        this.img_arrow_underweight = findViewById(R.id.bmi_result_img_underweight);
        this.img_arrow_normal = findViewById(R.id.bmi_result_img_normal);
        this.img_arrow_overweight = findViewById(R.id.bmi_result_img_overweight);
        this.img_arrow_moderately_obese = findViewById(R.id.bmi_result_img_moderately_obese);
        this.img_arrow_severely_obese = findViewById(R.id.bmi_result_img_severely_obese);
        this.img_arrow_very_severely_obese = findViewById(R.id.bmi_result_img_very_severely_obese);
        this.img_share = findViewById(R.id.bmi_result_img_share);
        this.txt_bmi_result_text = findViewById(R.id.bmi_result_txt_result_text);
        this.txt_bmi_result = findViewById(R.id.bmi_result_txt_result);
        Intent intent = getIntent();
        this.resultText = intent.getStringExtra("ResultText");
        this.bmiValue = intent.getStringExtra("BMI");
        this.txt_bmi_result_text.setText(this.resultText);
        this.txt_bmi_result.setText(String.format(Locale.US, "%.2f", Float.valueOf(Float.parseFloat(this.bmiValue))));
        SetResultImage(this.resultText);
        this.img_share.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(BMIResultActivity.this.push_animation);
                String sb = "My BMI is " +
                        String.format(Locale.US, "%.2f", Float.valueOf(Float.parseFloat(BMIResultActivity.this.bmiValue))) +
                        " which is " +
                        BMIResultActivity.this.resultText + ".";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", sb);
                intent.setType("text/plain");
                BMIResultActivity.this.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }

    private void SetResultImage(String str) {
        if (str.equalsIgnoreCase(AppConstants.bmi_result_very_severely)) {
            this.img_arrow_very_severely.setVisibility(View.VISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_severely)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.VISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_underweight)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.VISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_normal)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.VISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_overweight)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.VISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_moderately_obese)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.VISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_severely_obese)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.VISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.INVISIBLE);
        } else if (str.equalsIgnoreCase(AppConstants.bmi_result_very_severely_obese)) {
            this.img_arrow_very_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_severely.setVisibility(View.INVISIBLE);
            this.img_arrow_underweight.setVisibility(View.INVISIBLE);
            this.img_arrow_normal.setVisibility(View.INVISIBLE);
            this.img_arrow_overweight.setVisibility(View.INVISIBLE);
            this.img_arrow_moderately_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_severely_obese.setVisibility(View.INVISIBLE);
            this.img_arrow_very_severely_obese.setVisibility(View.VISIBLE);
        }
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_bmi));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_result));
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                BMIResultActivity.this.onBackPressed();
            }
        });
        setSupportActionBar(findViewById(R.id.toolbar_actionbar));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayHomeAsUpEnabled(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BackScreen();
    }

    private void BackScreen() {
        finish();
        AppConstants.overridePendingTransitionExit(this);
    }
}
