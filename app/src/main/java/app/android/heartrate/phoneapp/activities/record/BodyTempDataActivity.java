package app.android.heartrate.phoneapp.activities.record;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.adapters.BodyTempDataAdapter;
import app.android.heartrate.phoneapp.adapters.SpinnerProfileAdapter;
import app.android.heartrate.phoneapp.model.classes.BodyTempData;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;

import java.util.ArrayList;

public class BodyTempDataActivity extends AppCompatActivity {
    public static String[] array_celsius_range;
    public static String[] array_fahrenheit_range;
    public static String[] array_pulse_range;
    public static Activity body_temp_list_activity;
    SQLiteHealthTracker SQLite_health_tracker;

    int[] arrayProfileIds;
    String[] arrayProfileNames;
    ArrayList<UserProfileData> array_profiles = new ArrayList<>();
    ArrayList<BodyTempData> array_temp_data = new ArrayList<>();

    BodyTempDataAdapter bodyTempAdapter;
    BodyTempDataTask body_temp_Data_task;
    String current_profile_name;
    int current_user_id;
    private final Handler data_handler = new Handler(Looper.getMainLooper()) {


        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                BodyTempDataActivity.this.recycler_temp_data.setLayoutManager(new LinearLayoutManager(BodyTempDataActivity.this));
                BodyTempDataActivity.this.recycler_temp_data.setItemAnimator(new DefaultItemAnimator());
                if (BodyTempDataActivity.this.array_temp_data.size() > 0) {
                    BodyTempDataActivity.this.lbl_no_data.setVisibility(View.GONE);
                    BodyTempDataActivity bodyTempDataActivity = BodyTempDataActivity.this;
                    bodyTempDataActivity.bodyTempAdapter = new BodyTempDataAdapter(bodyTempDataActivity.array_temp_data, BodyTempDataActivity.this) {


                        @Override
                        public void onBodyTempAdapterClickItem(int i, View view) {
                            if (view.getId() == R.id.bt_row_rel_delete) {
                                BodyTempDataActivity.this.ConformDeleteDialog(BodyTempDataActivity.this.array_temp_data.get(i).row_id);
                            }
                            if (view.getId() == R.id.bt_row_rel_edit) {
                                AppConstants.is_body_temp_edit_mode = true;
                                AppConstants.selected_body_temp_data = BodyTempDataActivity.this.array_temp_data.get(i);
                                BodyTempDataActivity.this.AddBodyTempScreen();
                            }
                        }
                    };
                    BodyTempDataActivity.this.recycler_temp_data.setAdapter(BodyTempDataActivity.this.bodyTempAdapter);
                } else {
                    BodyTempDataActivity.this.lbl_no_data.setVisibility(View.VISIBLE);
                }
                BodyTempDataActivity.this.DismissProgressBar();
            } else if (i == 99) {
                BodyTempDataActivity.this.DismissProgressBar();
            }
        }
    };

    boolean is_user_interact = false;
    TextView lbl_no_data;
    LottieAnimationView lottie_circular_loading;
    RecyclerView recycler_temp_data;

    SpinnerProfileAdapter spinner_profile_adapter;
    Spinner spinner_profiles;


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
        setContentView(R.layout.activity_body_temp_list);

        body_temp_list_activity = this;
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.lottie_circular_loading = findViewById(R.id.animation_view);
        TextView textView = findViewById(R.id.lbl_no_data);
        this.lbl_no_data = textView;
        textView.setVisibility(View.GONE);
        this.spinner_profiles = findViewById(R.id.bt_spinner_profiles);
        this.recycler_temp_data = findViewById(R.id.bt_rv_data);
        SetProfileSpinner();
    }


    private void AddBodyTempScreen() {
        startActivity(new Intent(this, AddBodyTempActivity.class));
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
                if (BodyTempDataActivity.this.is_user_interact) {
                    BodyTempDataActivity bodyTempDataActivity = BodyTempDataActivity.this;
                    bodyTempDataActivity.current_user_id = bodyTempDataActivity.arrayProfileIds[i];
                    BodyTempDataActivity bodyTempDataActivity2 = BodyTempDataActivity.this;
                    bodyTempDataActivity2.current_profile_name = bodyTempDataActivity2.arrayProfileNames[i].trim();
                    BodyTempDataActivity.this.SetBodyTempDataList();
                }
            }
        });
    }


    private void SetBodyTempDataList() {
        BodyTempDataTask bodyTempDataTask = new BodyTempDataTask();
        this.body_temp_Data_task = bodyTempDataTask;
        bodyTempDataTask.execute();
    }


    public class BodyTempDataTask extends AsyncTask<Void, Void, Void> {
        private BodyTempDataTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            BodyTempDataActivity.this.ShowProgressBar();
        }

        public Void doInBackground(Void... voidArr) {
            try {
                BodyTempDataActivity.array_celsius_range = new String[91];
                BodyTempDataActivity.array_fahrenheit_range = new String[91];
                BodyTempDataActivity.array_pulse_range = new String[ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION];
                for (int i = 0; i < BodyTempDataActivity.array_celsius_range.length; i++) {
                    float f = ((float) ((i) + 330)) / 10.0f;
                    BodyTempDataActivity.array_celsius_range[i] = String.valueOf(f);
                    BodyTempDataActivity.array_fahrenheit_range[i] = String.valueOf(((float) Math.round(((f * 1.8f) + 32.0f) * 10.0f)) / 10.0f);
                }
                int i2 = 0;
                while (i2 < BodyTempDataActivity.array_pulse_range.length) {
                    int i3 = i2 + 1;
                    BodyTempDataActivity.array_pulse_range[i2] = String.valueOf(i3);
                    i2 = i3;
                }
                BodyTempDataActivity.this.array_temp_data.clear();
                BodyTempDataActivity bodyTempDataActivity = BodyTempDataActivity.this;
                bodyTempDataActivity.array_temp_data = (ArrayList) bodyTempDataActivity.SQLite_health_tracker.GetTemperatureDataByUserID(BodyTempDataActivity.this.current_user_id);
                BodyTempDataActivity.this.data_handler.sendMessage(BodyTempDataActivity.this.data_handler.obtainMessage(0));
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                BodyTempDataActivity.this.data_handler.sendMessage(BodyTempDataActivity.this.data_handler.obtainMessage(99));
                return null;
            }
        }

        public void onPostExecute(Void r1) {
            super.onPostExecute(null);
        }
    }

    public void ConformDeleteDialog(final int i) {
        final Dialog dialog = new Dialog(this, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_rate);
        Button button = dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button button2 = dialog.findViewById(R.id.dialog_conform_btn_no);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_header)).setText("Delete");
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_message)).setText("Do you want to delete this data?");
        button.setText("Delete");
        button2.setText("Cancel");
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                try {
                    BodyTempDataActivity.this.SQLite_health_tracker.deleteDataByID(i);
                    EUGeneralClass.ShowSuccessToast(BodyTempDataActivity.this, AppConstants.data_deleted_messages);
                    BodyTempDataActivity.this.SetBodyTempDataList();
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

    public void ShowProgressBar() {
        LottieAnimationView lottieAnimationView = this.lottie_circular_loading;
        if (lottieAnimationView != null) {
            lottieAnimationView.setVisibility(View.VISIBLE);
        }
    }

    public void DismissProgressBar() {
        LottieAnimationView lottieAnimationView = this.lottie_circular_loading;
        if (lottieAnimationView != null) {
            lottieAnimationView.setVisibility(View.GONE);
        }
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_body));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_temp_data));
        findViewById(R.id.tool_bar_rel_add_user).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AppConstants.is_body_temp_edit_mode = false;
                BodyTempDataActivity.this.AddBodyTempScreen();
            }
        });
        findViewById(R.id.toolbar_rel_back).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                BodyTempDataActivity.this.onBackPressed();
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
        int selectedItemPosition = this.spinner_profiles.getSelectedItemPosition();
        this.current_user_id = this.arrayProfileIds[selectedItemPosition];
        this.current_profile_name = this.arrayProfileNames[selectedItemPosition].trim();
        SetBodyTempDataList();


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
