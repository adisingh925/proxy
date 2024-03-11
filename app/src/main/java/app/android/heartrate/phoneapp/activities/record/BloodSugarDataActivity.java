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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.BloodSugarDataAdapter;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.BloodSugarData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

import java.util.ArrayList;

public class BloodSugarDataActivity extends AppCompatActivity {
    SQLiteHealthTracker SQLite_health_tracker;

    BloodSugarDataAdapter adapter_bs_data;
    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<BloodSugarData> array_bs_data = new ArrayList<>();
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();
    String current_profile_name;
    int current_user_id;
    ImageView img_status_info;

    boolean is_user_interact = false;
    Context mContext;
    Animation push_animation;
    RecyclerView recycler_blood_sugar;

    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;
    TextView txt_no_data;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetView();

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.FullscreenAd(this);
        adAdmob.BannerAd(findViewById(R.id.banner), this);
        AppConstants.overridePendingTransitionEnter(this);
    }

    private void SetView() {
        setContentView(R.layout.activity_bs_data_list);
        
        this.mContext = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.spinner_profiles = findViewById(R.id.bs_spinner_profiles);
        this.recycler_blood_sugar = findViewById(R.id.bs_rv_data);
        this.recycler_blood_sugar.setLayoutManager(new LinearLayoutManager(this));
        this.recycler_blood_sugar.setItemAnimator(new DefaultItemAnimator());
        TextView textView = findViewById(R.id.txt_no_data);
        this.txt_no_data = textView;
        textView.setVisibility(View.GONE);
        SetProfileSpinner();
        ImageView imageView = findViewById(R.id.bs_img_status_info);
        this.img_status_info = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                view.startAnimation(BloodSugarDataActivity.this.push_animation);
                BloodSugarDataActivity.this.StatusInfoDialog();
            }
        });
    }


    private void StatusInfoDialog() {
        final Dialog dialog = new Dialog(this, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_bs_status_info);
        Button button = dialog.findViewById(R.id.dialog_bs_status_btn_ok);
        button.setText("OK");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        this.is_user_interact = true;
    }

    private void SetProfileSpinner() {
        this.array_profiles.clear();
        ArrayList<UserProfileData> arrayList = (ArrayList) this.SQLite_health_tracker.GetUserProfileData();
        this.array_profiles = arrayList;
        if (arrayList.size() > 0) {
            this.arrayProfileIds = new int[this.array_profiles.size()];
            this.arrayProfileNames = new String[this.array_profiles.size()];
            for (int i = 0; i < this.array_profiles.size(); i++) {
                this.arrayProfileIds[i] = this.array_profiles.get(i).user_id;
                this.arrayProfileNames[i] = this.array_profiles.get(i).user_name.trim();
            }
            SpinnerProfileAdapter spinnerProfileAdapter = new SpinnerProfileAdapter(this, this.array_profiles);
            this.spinner_profile_adapter = spinnerProfileAdapter;
            this.spinner_profiles.setAdapter(spinnerProfileAdapter);
        }
        this.spinner_profiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (BloodSugarDataActivity.this.is_user_interact) {
                    BloodSugarDataActivity bloodSugarDataActivity = BloodSugarDataActivity.this;
                    bloodSugarDataActivity.current_user_id = bloodSugarDataActivity.arrayProfileIds[i];
                    BloodSugarDataActivity bloodSugarDataActivity2 = BloodSugarDataActivity.this;
                    bloodSugarDataActivity2.current_profile_name = bloodSugarDataActivity2.arrayProfileNames[i].trim();
                    Log.e("selected Profile :", "ID :" + BloodSugarDataActivity.this.current_user_id + "\nName :" + BloodSugarDataActivity.this.current_profile_name);
                    BloodSugarDataActivity.this.SetBloodSugarList();
                }
            }
        });
    }


    private void SetBloodSugarList() {
        this.array_bs_data.clear();
        ArrayList<BloodSugarData> arrayList = (ArrayList) this.SQLite_health_tracker.GetBloodSugarDataByUserID(this.current_user_id);
        this.array_bs_data = arrayList;
        if (arrayList.size() > 0) {
            this.txt_no_data.setVisibility(View.GONE);
            BloodSugarDataAdapter r0 = new BloodSugarDataAdapter(this, this.array_bs_data) {


                @Override
                public void onBloodSugarAdapterClickItem(int i, View view) {
                    if (view.getId() == R.id.row_bs_rel_edit) {
                        AppConstants.selected_bs_data = BloodSugarDataActivity.this.array_bs_data.get(i);
                        AppConstants.is_bs_edit_mode = true;
                        BloodSugarDataActivity.this.AddBloodSugarScreen();
                    }
                    if (view.getId() == R.id.row_bs_rel_delete) {
                        BloodSugarDataActivity.this.ConformDeleteDialog(BloodSugarDataActivity.this.array_bs_data.get(i).row_id, BloodSugarDataActivity.this.array_bs_data.get(i).user_id);
                    }
                }
            };
            this.adapter_bs_data = r0;
            this.recycler_blood_sugar.setAdapter(r0);
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
                    BloodSugarDataActivity.this.SQLite_health_tracker.deleteBloodSugarByID(i);
                    EUGeneralClass.ShowSuccessToast(BloodSugarDataActivity.this, AppConstants.data_deleted_messages);
                    BloodSugarDataActivity.this.SetBloodSugarList();
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


    private void AddBloodSugarScreen() {
        startActivity(new Intent(this, AddBloodSugarActivity.class));
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_blood));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_sugar_data));
        findViewById(R.id.tool_bar_rel_add_user).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppConstants.is_bs_edit_mode = false;
                BloodSugarDataActivity.this.AddBloodSugarScreen();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BloodSugarDataActivity.this.onBackPressed();
            }
        });
        setSupportActionBar( findViewById(R.id.toolbar_actionbar));
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
        int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
        this.current_user_id = this.arrayProfileIds[selectedItemPosition];
        this.current_profile_name = this.arrayProfileNames[selectedItemPosition].trim();
        SetBloodSugarList();


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
