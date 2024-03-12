package app.android.heartrate.phoneapp.activities.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.shawnlin.numberpicker.NumberPicker;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import app.android.heartrate.phoneapp.AdAdmob;
import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import app.android.heartrate.phoneapp.utils.EUGeneralClass;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddProfileActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_CODE = 1002;
    public static Activity activity_add_profile;
    public static Bitmap selected_user_image;
    private final int IMAGE_COMPRESSION = 80;
    SQLiteHealthTracker SQLite_health_tracker;
    private final String TAG = "AddProfileActivity :";

    ArrayAdapter<String> adapter_spinner_gender;
    ArrayList<String> arrayListGender = new ArrayList<>(Arrays.asList(AppConstants.hr_gender_male, AppConstants.hr_gender_female, AppConstants.hr_gender_child));

    private final int bitmapMaxHeight = 200;
    private final int bitmapMaxWidth = 200;
    File cropDirectory;
    String crop_save_location = "";
    String currentPhotoPath = "";
    EditText et_name;
    EditText et_weight;
    String heightUnit = "";
    String height_cm = "";
    String height_feet = "";
    String imagePath = "";
    CircleImageView img_profile_photo;
   
    LinearLayout lin_select_date;
    Context mContext;
    Animation push_animation;

    RelativeLayout rel_get_height;
    RelativeLayout rel_pick_photo;
    RelativeLayout rel_save;
    File saveDirectory;
    private int save_entry_day;
    private int save_entry_month;
    private int save_entry_year;
    String save_location = "";
    String selected_unit = "cm";
    private final boolean setBitmapMaxWidthHeight = false;
    Spinner spinner_gender;
    TextView txt_birth_date;
    TextView txt_btn_save;
    TextView txt_height;
    TextView txt_height_label;
    String user_gender = AppConstants.hr_gender_male;
    String user_photo_path = "";

    private void setResultCancelled() {
    }


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
        setContentView(R.layout.activity_add_profile);
        
        this.mContext = this;
        activity_add_profile = this;
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.view_push);
        setUpActionBar();
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
        this.img_profile_photo = findViewById(R.id.add_profile_img_user_photo);
        this.rel_pick_photo = findViewById(R.id.add_profile_rel_pick_photo);
        this.et_name = findViewById(R.id.add_profile_et_name);
        this.et_weight = findViewById(R.id.add_profile_et_weight);
        this.rel_get_height = findViewById(R.id.add_profile_rel_get_height);
        this.txt_height = findViewById(R.id.add_profile_txt_height);
        this.txt_height_label = findViewById(R.id.add_profile_txt_height_label);
        this.txt_birth_date = findViewById(R.id.add_profile_txt_birth_date);
        this.lin_select_date = findViewById(R.id.add_profile_lin_birth_date);
        this.spinner_gender = findViewById(R.id.add_profile_spinner_gender);
        this.rel_save = findViewById(R.id.add_profile_rel_save);
        this.txt_btn_save = findViewById(R.id.add_profile_txt_save);
        CreateFolders();
        SetGenderSpinner();
        if (AppConstants.is_profile_edit_mode) {
            SetProfileData();
            this.txt_btn_save.setText(getResources().getString(R.string.lbl_update));
        } else {
            if (this.user_gender.equalsIgnoreCase(AppConstants.hr_gender_male)) {
                this.spinner_gender.setSelection(0);
            } else if (this.user_gender.equalsIgnoreCase(AppConstants.hr_gender_female)) {
                this.spinner_gender.setSelection(1);
            } else if (this.user_gender.equalsIgnoreCase(AppConstants.hr_gender_child)) {
                this.spinner_gender.setSelection(2);
            }
            this.txt_btn_save.setText(getResources().getString(R.string.lbl_save));
        }
        this.rel_pick_photo.setOnClickListener(new View.OnClickListener() {
            

            public void onClick(View view) {
                view.startAnimation(AddProfileActivity.this.push_animation);
                AddProfileActivity.this.PickImage();
            }
        });
        this.lin_select_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddProfileActivity.this.hideSoftKeyboard();
                Calendar instance = Calendar.getInstance();
                AddProfileActivity.this.save_entry_year = instance.get(1);
                AddProfileActivity.this.save_entry_month = instance.get(2);
                AddProfileActivity.this.save_entry_day = instance.get(5);

                new DatePickerDialog(AddProfileActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            AddProfileActivity.this.txt_birth_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse((i3 + "/" + (i2 + 1) + "/" + i).trim())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, AddProfileActivity.this.save_entry_year, AddProfileActivity.this.save_entry_month, AddProfileActivity.this.save_entry_day).show();
            }
        });
        this.spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                AddProfileActivity addProfileActivity = AddProfileActivity.this;
                addProfileActivity.user_gender = addProfileActivity.spinner_gender.getSelectedItem().toString();
                AddProfileActivity.this.spinner_gender.setSelection(AddProfileActivity.this.spinner_gender.getSelectedItemPosition());
                String str = AddProfileActivity.this.TAG;
                Log.e(str, "Selected Gender :- " + AddProfileActivity.this.user_gender);
            }
        });
        this.rel_get_height.setOnClickListener(new View.OnClickListener() {
            

            public void onClick(View view) {
                AddProfileActivity.this.SelectHeightDialog();
            }
        });
        this.rel_save.setOnClickListener(new View.OnClickListener() {
            

            public void onClick(View view) {
                AddProfileActivity.this.SaveProcess();
            }
        });
    }

    private void SetGenderSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, this.arrayListGender);
        this.adapter_spinner_gender = arrayAdapter;
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        this.spinner_gender.setAdapter(this.adapter_spinner_gender);
    }

    private void CreateFolders() {
        String str = Environment.DIRECTORY_DCIM + File.separator + AppConstants.MY_FOLDER_NAME + File.separator + AppConstants.PROFILE_FOLDER_NAME;
        this.save_location = str;
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(str);
        this.saveDirectory = externalStoragePublicDirectory;
        if (!externalStoragePublicDirectory.exists()) {
            this.saveDirectory.mkdirs();
        }
        String str2 = Environment.DIRECTORY_DCIM + File.separator + AppConstants.MY_FOLDER_NAME + File.separator + AppConstants.CROP_FOLDER_NAME;
        this.crop_save_location = str2;
        File externalStoragePublicDirectory2 = Environment.getExternalStoragePublicDirectory(str2);
        this.cropDirectory = externalStoragePublicDirectory2;
        if (!externalStoragePublicDirectory2.exists()) {
            this.cropDirectory.mkdirs();
        }
    }


    private void PickImage() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.addCategory("android.intent.category.OPENABLE");
        if (Build.VERSION.SDK_INT >= 19) {
            intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"image/jpeg", "image/png"});
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1002);
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 69) {
            if (i == 96) {
                UCrop.getError(intent);
                setResultCancelled();
            } else if (i == 1002 && i2 == -1 && intent != null) {
                openCropActivity(intent.getData(), Uri.fromFile(getImageFile()));
            }
        } else if (i2 == -1) {
            HandleUCropResult(intent);
        } else {
            setResultCancelled();
        }
    }


    private File getImageFile() {
        File file = null;
        try {
            file = File.createTempFile("Crop_" + System.currentTimeMillis(), ".jpg", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), AppConstants.MY_FOLDER_NAME + File.separator + AppConstants.CROP_FOLDER_NAME));
            String sb = "file:" +
                    file.getAbsolutePath();
            this.currentPhotoPath = sb;
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }

    @SuppressLint("Range")
    private void imageFromGallery(Intent intent) {
        try {
            Uri data = intent.getData();
            String[] strArr = {"_data"};
            Cursor query = getContentResolver().query(data, strArr, null, null, null);
            query.moveToFirst();
            this.imagePath = query.getString(query.getColumnIndex(strArr[0]));
            query.close();
        } catch (OutOfMemoryError unused) {
            EUGeneralClass.ShowErrorToast(this, "Image Can't load.Try Again!!");
        }
    }

    private void openCropActivity(Uri uri, Uri uri2) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this.mContext, R.color.tool_bar_color));
        options.setStatusBarColor(ContextCompat.getColor(this.mContext, R.color.tool_bar_color_dark));
        options.setToolbarWidgetColor(ContextCompat.getColor(this.mContext, R.color.white));
        options.setActiveWidgetColor(ContextCompat.getColor(this.mContext, R.color.white));
        options.setActiveWidgetColor(ContextCompat.getColor(this.mContext, R.color.white));
        options.setFreeStyleCropEnabled(true);
        UCrop.of(uri, uri2).withMaxResultSize(16843039, 16843040).withAspectRatio(5.0f, 5.0f).withOptions(options).start(this);
    }

    private void HandleUCropResult(Intent intent) {
        if (intent == null) {
            setResultCancelled();
            return;
        }
        try {
            Uri output = UCrop.getOutput(intent);
            String uri = output.toString();
            selected_user_image = null;
            selected_user_image = BitmapFactory.decodeFile(uri);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), output);
            selected_user_image = bitmap;
            if (bitmap != null) {
                this.img_profile_photo.setImageBitmap(bitmap);
            } else {
                Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_user);
                selected_user_image = decodeResource;
                this.img_profile_photo.setImageBitmap(decodeResource);
            }
            this.user_photo_path = uri.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void SetProfileData() {
        if (AppConstants.selected_profile_data != null) {
            String trim = AppConstants.selected_profile_data.user_name.trim();
            String trim2 = AppConstants.selected_profile_data.user_birth_date.trim();
            String trim3 = String.valueOf(AppConstants.selected_profile_data.user_weight).trim();
            this.heightUnit = String.valueOf(AppConstants.selected_profile_data.user_height_unit);
            this.height_feet = String.valueOf(AppConstants.selected_profile_data.user_height_feet);
            this.height_cm = String.valueOf(AppConstants.selected_profile_data.user_height);
            this.user_gender = AppConstants.selected_profile_data.user_gender.trim();
            this.et_name.setText(trim);
            this.txt_birth_date.setText(trim2);
            this.et_weight.setText(trim3);
            if (this.heightUnit.equalsIgnoreCase(AppConstants.HEIGHT_FEET)) {
                this.txt_height_label.setText(this.mContext.getResources().getString(R.string.lbl_height_feet));
                this.txt_height.setText(this.height_feet);
            } else if (this.heightUnit.equalsIgnoreCase(AppConstants.HEIGHT_CM)) {
                this.txt_height_label.setText(this.mContext.getResources().getString(R.string.lbl_height_cm));
                this.txt_height.setText(this.height_cm);
            }
            if (this.user_gender.equalsIgnoreCase(AppConstants.hr_gender_male)) {
                this.spinner_gender.setSelection(0);
            } else if (this.user_gender.equalsIgnoreCase(AppConstants.hr_gender_female)) {
                this.spinner_gender.setSelection(1);
            } else if (this.user_gender.equalsIgnoreCase(AppConstants.hr_gender_child)) {
                this.spinner_gender.setSelection(2);
            }
            this.user_photo_path = AppConstants.selected_profile_data.user_photo_path.trim();
            Bitmap bitmap = AppConstants.selected_profile_data.bmp_user_photo;
            selected_user_image = bitmap;
            if (bitmap != null) {
                this.img_profile_photo.setImageBitmap(bitmap);
                return;
            }
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_user);
            selected_user_image = decodeResource;
            this.img_profile_photo.setImageBitmap(decodeResource);
            return;
        }
        EUGeneralClass.ShowErrorToast(this, "Something went wrong!");
    }


    private void SelectHeightDialog() {
        final Dialog dialog = new Dialog(this.mContext, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_get_height);
        RelativeLayout relativeLayout = dialog.findViewById(R.id.dialog_height_rel_done);
        RelativeLayout relativeLayout2 = dialog.findViewById(R.id.dialog_height_rel_cancel);
        TextView textView = dialog.findViewById(R.id.dialog_height_txt_header);
        final Spinner spinner = dialog.findViewById(R.id.dialog_height_spinner_units);
        String[] stringArray = getResources().getStringArray(R.array.height_units);
        if (stringArray.length > 0) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dialog_spinner_item, stringArray);
            arrayAdapter.setDropDownViewResource(R.layout.dialog_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(0);
            this.selected_unit = spinner.getSelectedItem().toString();
        }
        final RelativeLayout relativeLayout3 = dialog.findViewById(R.id.dialog_height_rel_picker_cm);
        relativeLayout3.setVisibility(View.VISIBLE);
        final RelativeLayout relativeLayout4 = dialog.findViewById(R.id.dialog_height_rel_picker_feet);
        relativeLayout3.setVisibility(View.GONE);
        final NumberPicker numberPicker = dialog.findViewById(R.id.dialog_height_nm_picker_cm);
        numberPicker.setMaxValue(272);
        numberPicker.setMinValue(30);
        numberPicker.setValue(188);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFadingEdgeEnabled(true);
        numberPicker.setScrollerEnabled(true);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setAccessibilityDescriptionEnabled(true);
        final NumberPicker numberPicker2 = dialog.findViewById(R.id.dialog_height_nm_picker_feet);
        numberPicker2.setMaxValue(8);
        numberPicker2.setMinValue(1);
        numberPicker2.setValue(5);
        numberPicker2.setWrapSelectorWheel(false);
        numberPicker2.setFadingEdgeEnabled(true);
        numberPicker2.setScrollerEnabled(true);
        numberPicker2.setWrapSelectorWheel(true);
        numberPicker2.setAccessibilityDescriptionEnabled(true);
        final NumberPicker numberPicker3 = dialog.findViewById(R.id.dialog_height_nm_picker_inch);
        numberPicker3.setMaxValue(11);
        numberPicker3.setMinValue(0);
        numberPicker3.setValue(0);
        numberPicker3.setWrapSelectorWheel(false);
        numberPicker3.setFadingEdgeEnabled(true);
        numberPicker3.setScrollerEnabled(true);
        numberPicker3.setWrapSelectorWheel(true);
        numberPicker3.setAccessibilityDescriptionEnabled(true);
        if (AppConstants.is_profile_edit_mode) {
            if (this.heightUnit.equalsIgnoreCase(AppConstants.HEIGHT_FEET)) {
                spinner.setSelection(1);
                relativeLayout3.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(this.height_feet)) {
                    if (this.height_feet.contains("'")) {
                        String str = this.height_feet;
                        String substring = str.substring(0, str.indexOf("'"));
                        if (!TextUtils.isEmpty(substring)) {
                            numberPicker2.setValue(Integer.parseInt(substring.trim()));
                        }
                    }
                    if (this.height_feet.contains("\"")) {
                        String str2 = this.height_feet;
                        String substring2 = str2.substring(str2.indexOf("'") + 1, this.height_feet.indexOf("\""));
                        if (!TextUtils.isEmpty(substring2)) {
                            numberPicker3.setValue(Integer.parseInt(substring2.trim()));
                        }
                    }
                }
            } else if (this.heightUnit.equalsIgnoreCase(AppConstants.HEIGHT_CM)) {
                spinner.setSelection(0);
                relativeLayout3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(this.height_cm)) {
                    numberPicker.setValue(Integer.parseInt(this.height_cm));
                }
            }
        }
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
            }
        });
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
            }
        });
        numberPicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
            }
        });
        textView.setText("Height");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                AddProfileActivity.this.selected_unit = spinner.getSelectedItem().toString();
                int selectedItemPosition = spinner.getSelectedItemPosition();
                spinner.setSelection(selectedItemPosition);
                if (selectedItemPosition == 0) {
                    relativeLayout3.setVisibility(View.VISIBLE);
                    relativeLayout4.setVisibility(View.GONE);
                } else if (selectedItemPosition == 1) {
                    relativeLayout3.setVisibility(View.GONE);
                    relativeLayout4.setVisibility(View.VISIBLE);
                }
                String str = AddProfileActivity.this.TAG;
                Log.e(str, "Selected Height Unit :- " + AddProfileActivity.this.selected_unit);
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            

            public void onClick(View view) {
                try {
                    if (AddProfileActivity.this.selected_unit.equalsIgnoreCase(AddProfileActivity.this.getResources().getString(R.string.lbl_feet_inches))) {
                        String str = numberPicker2.getValue() + "' " + numberPicker3.getValue() + "\"";
                        AddProfileActivity.this.txt_height.setText(str);
                        String FeetToCentimeter = AppConstants.FeetToCentimeter(str);
                        Log.e(AddProfileActivity.this.TAG, "Feet To CM :- " + FeetToCentimeter);
                    } else if (AddProfileActivity.this.selected_unit.equalsIgnoreCase(AddProfileActivity.this.getResources().getString(R.string.lbl_centimeter))) {
                        int value = numberPicker.getValue();
                        AddProfileActivity.this.txt_height.setText(value + " cm");
                        String CentimeterToFeet = AppConstants.CentimeterToFeet(String.valueOf(value));
                        Log.e(AddProfileActivity.this.TAG, "CM To Feet :- " + CentimeterToFeet);
                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void SaveProcess() {
        TextView textView;
        boolean z;
        int i;
        String str;
        String trim = this.et_name.getText().toString().trim();
        String trim2 = this.txt_birth_date.getText().toString().trim();
        String trim3 = this.et_weight.getText().toString().trim();
        String trim4 = this.txt_height.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            this.et_name.setError(AppConstants.error_field_require);
            textView = this.et_name;
            z = true;
        } else {
            z = false;
            textView = null;
        }
        if (TextUtils.isEmpty(trim2)) {
            this.txt_birth_date.setError(AppConstants.error_field_require);
            textView = this.txt_birth_date;
            z = true;
        }
        if (TextUtils.isEmpty(trim3)) {
            this.et_weight.setError(AppConstants.error_field_require);
            textView = this.et_weight;
            z = true;
        }
        if (TextUtils.isEmpty(trim4)) {
            this.txt_height.setError(AppConstants.error_field_require);
            textView = this.txt_height;
            trim4 = "";
            str = trim4;
            z = true;
            i = 30;
        }
        else if (trim4.contains("'")) {
            str = AppConstants.HEIGHT_FEET.trim();
            i = (int) Float.parseFloat(AppConstants.FeetToCentimeter(trim4));
        }
        else {
            str = AppConstants.HEIGHT_CM.trim();
            String replaceAll = trim4.replaceAll("cm", "");
            i = Integer.parseInt(replaceAll.trim());
            trim4 = AppConstants.CentimeterToFeet(replaceAll);
        }
        if (z) {
            textView.requestFocus();
            return;
        }
        UserProfileData userProfileData = new UserProfileData();
        if (!AppConstants.is_profile_edit_mode) {
            userProfileData.user_id = SQLiteHealthTracker.GetUserProfileLatestRowID() + 1;
            userProfileData.user_name = trim.trim();
            userProfileData.user_birth_date = trim2.trim();
            userProfileData.user_gender = this.user_gender.trim();
            userProfileData.user_height_unit = str;
            userProfileData.user_height_feet = trim4;
            userProfileData.user_height = i;
            userProfileData.user_weight = Integer.parseInt(trim3);
            userProfileData.bmp_user_photo = selected_user_image;
            userProfileData.user_photo_path = this.user_photo_path;
            if (!this.SQLite_health_tracker.CheckProfileNameExist(trim)) {
                this.SQLite_health_tracker.InsertUserProfileData(userProfileData);
                EUGeneralClass.ShowSuccessToast(this, "Profile Data saved successfully!");
                onBackPressed();
                return;
            }
            EUGeneralClass.ShowErrorToast(this, "Profile name already exist! Enter different name.");
            this.et_name.setText("");
            this.et_name.requestFocus();
            return;
        }
        int i2 = AppConstants.selected_profile_data.row_id;
        int i3 = AppConstants.selected_profile_data.user_id;
        userProfileData.row_id = i2;
        userProfileData.user_id = i3;
        userProfileData.user_name = trim.trim();
        userProfileData.user_birth_date = trim2.trim();
        userProfileData.user_gender = this.user_gender.trim();
        userProfileData.user_height_unit = str;
        userProfileData.user_height_feet = trim4;
        userProfileData.user_height = i;
        userProfileData.user_weight = Integer.parseInt(trim3);
        userProfileData.bmp_user_photo = selected_user_image;
        userProfileData.user_photo_path = this.user_photo_path;
        this.SQLite_health_tracker.UpdateUserProfileData(i2, i3, userProfileData);
        EUGeneralClass.ShowSuccessToast(this, "Profile Data updated successfully!");
        onBackPressed();
    }

    public String getURLForResource(int i) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + i).toString();
    }

    private void setUpActionBar() {
        ((TextView) findViewById(R.id.toolbar_txt_title_1)).setText(getResources().getString(R.string.lbl_header_add_new));
        ((TextView) findViewById(R.id.toolbar_txt_title_2)).setText(getResources().getString(R.string.lbl_header_profile));
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
