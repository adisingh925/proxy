package app.android.heartrate.phoneapp.activities.record;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.CholesterolDataAdapter;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.ProfileData;
import app.android.heartrate.phoneapp.model.classes.CholesterolData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sharedpreferences.SharedPreferences;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

public class CholesterolDataActivity extends AppCompatActivity {
    SQLiteHealthTracker SQLite_health_tracker;

    CholesterolDataAdapter adapter_cholesterol_data;
    ArrayList<CholesterolData> array_cholesterol_data = new ArrayList<>();


    boolean is_user_interact = false;
    Context mContext;
    Animation push_animation;
    RecyclerView recycler_cholesterol;

    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    TextView txt_no_data;
    TextView spinner_txt_name;

    private SharedPreferences sharedPreferencesUtils;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();


        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_cholesterol_data_list);

        this.mContext = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        sharedPreferencesUtils = SharedPreferences.INSTANCE;
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.cholesterol_spinner_profiles);
        this.recycler_cholesterol = findViewById(R.id.cholesterol_rv_data);
        this.spinner_txt_name = findViewById(R.id.spinner_txt_name);
        this.recycler_cholesterol.setLayoutManager(new LinearLayoutManager(this));
        this.recycler_cholesterol.setItemAnimator(new DefaultItemAnimator());
        TextView textView = findViewById(R.id.txt_no_data);
        this.txt_no_data = textView;
        textView.setVisibility(View.GONE);
        SetProfileSpinner();
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        this.is_user_interact = true;
    }

    private void SetProfileSpinner() {
        ProfileData profileData = SQLite_health_tracker.GetUserProfileData();
        String name = profileData.getFirstName() +" "+ profileData.getLastName();
        spinner_txt_name.setText(name);
    }


    private void SetCholesterolDataList() {
        this.array_cholesterol_data.clear();
        ArrayList<CholesterolData> arrayList = (ArrayList) this.SQLite_health_tracker.GetCholesterolDataByUserID(this.sharedPreferencesUtils.getUserId());
        this.array_cholesterol_data = arrayList;
        if (arrayList.size() > 0) {
            this.txt_no_data.setVisibility(View.GONE);
            CholesterolDataAdapter r0 = new CholesterolDataAdapter(this, this.array_cholesterol_data) {


                @Override
                public void onCholesterolAdapterClickItem(int i, View view) {
                    if (view.getId() == R.id.row_cholesterol_rel_edit) {
                        AppConstants.selected_cholesterol_data = CholesterolDataActivity.this.array_cholesterol_data.get(i);
                        AppConstants.is_cholesterol_edit_mode = true;
                        CholesterolDataActivity.this.AddCholesterolScreen();
                    }
                    if (view.getId() == R.id.row_cholesterol_rel_delete) {
                        CholesterolDataActivity.this.ConformDeleteDialog(CholesterolDataActivity.this.array_cholesterol_data.get(i).row_id, CholesterolDataActivity.this.array_cholesterol_data.get(i).user_id);
                    }
                }
            };
            this.adapter_cholesterol_data = r0;
            this.recycler_cholesterol.setAdapter(r0);
            return;
        }
        this.txt_no_data.setVisibility(View.VISIBLE);
    }


    private void ConformDeleteDialog(final int i, int i2) {
        final Dialog dialog = new Dialog(this, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_rate);
        Button button = dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button button2 = dialog.findViewById(R.id.dialog_conform_btn_no);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_header)).setText("Delete");
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_message)).setText("Do you want to delete selected data?");
        button.setText("Delete");
        button2.setText("Cancel");
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                try {
                    CholesterolDataActivity.this.SQLite_health_tracker.deleteCholesterolByID(i);
                    EUGeneralClass.ShowSuccessToast(CholesterolDataActivity.this, AppConstants.data_deleted_messages);
                    CholesterolDataActivity.this.SetCholesterolDataList();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void AddCholesterolScreen() {
        startActivity(new Intent(this, AddCholesterolActivity.class));
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_cholesterol));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_data));
        findViewById(R.id.tool_bar_rel_add_user).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AppConstants.is_cholesterol_edit_mode = false;
                CholesterolDataActivity.this.AddCholesterolScreen();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                CholesterolDataActivity.this.onBackPressed();
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
        SetCholesterolDataList();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    private void BackScreen() {
        AppConstants.is_tools_interstitial_show = true;

        finish();
        AppConstants.overridePendingTransitionExit(this);
    }
}
