package app.android.heartrate.phoneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker;
import app.android.heartrate.phoneapp.utils.AppConstants;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileViewHolder> {
    private final List<UserProfileData> array_data;
    private final Context mContext;
    SQLiteHealthTracker SQLite_health_tracker;
    Animation push_animation;

    public ProfilesAdapter(Context context, ArrayList<UserProfileData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
        SQLiteHealthTracker sQLiteHealthTracker = new SQLiteHealthTracker(this.mContext);
        this.SQLite_health_tracker = sQLiteHealthTracker;
        sQLiteHealthTracker.openToWrite();
    }

    public abstract void onProfileAdapterClickItem(int i, View view);

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProfileViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_profiles, null));
    }

    public void onBindViewHolder(ProfileViewHolder profileViewHolder, @SuppressLint("RecyclerView") final int i) {
        UserProfileData userProfileData = this.array_data.get(i);
        String trim = userProfileData.user_name.trim();
        String trim2 = userProfileData.user_birth_date.trim();
        String trim3 = userProfileData.user_gender.trim();
        Bitmap bitmap = userProfileData.bmp_user_photo;
        String trim4 = userProfileData.user_height_unit.trim();
        String trim5 = userProfileData.user_height_feet.trim();
        String valueOf = String.valueOf(userProfileData.user_height);
        profileViewHolder.img_profile_photo.setImageBitmap(bitmap);
        profileViewHolder.txt_name.setText(trim);
        profileViewHolder.txt_dob.setText(trim2);
        profileViewHolder.txt_gender.setText(trim3);
        if (trim4.equalsIgnoreCase(AppConstants.HEIGHT_FEET)) {
            profileViewHolder.txt_height.setText(trim5);
        } else if (trim4.equalsIgnoreCase(AppConstants.HEIGHT_CM)) {
            profileViewHolder.txt_height.setText(valueOf);
        }
        if (trim3.equalsIgnoreCase(AppConstants.hr_gender_male)) {
            profileViewHolder.img_gender.setImageResource(R.drawable.ic_male_sign_select);
        } else if (trim3.equalsIgnoreCase(AppConstants.hr_gender_female)) {
            profileViewHolder.img_gender.setImageResource(R.drawable.ic_female_sign_select);
        }
        profileViewHolder.rel_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ProfilesAdapter.this.onProfileAdapterClickItem(i, view);
            }
        });
        profileViewHolder.rel_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ProfilesAdapter.this.onProfileAdapterClickItem(i, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.array_data.size();
    }

    @Override
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView img_gender;
        CircleImageView img_profile_photo;
        RelativeLayout rel_delete;
        RelativeLayout rel_edit;
        RelativeLayout rel_main;
        TextView txt_dob;
        TextView txt_gender;
        TextView txt_height;
        TextView txt_name;

        public ProfileViewHolder(View view) {
            super(view);
            this.rel_main = view.findViewById(R.id.row_profile_rel_main);
            this.img_profile_photo = view.findViewById(R.id.row_profile_img_user);
            this.txt_name = view.findViewById(R.id.row_profile_txt_name);
            this.txt_dob = view.findViewById(R.id.row_profile_txt_dob);
            this.img_gender = view.findViewById(R.id.row_profile_img_gender);
            this.txt_gender = view.findViewById(R.id.row_profile_txt_gender);
            this.txt_height = view.findViewById(R.id.row_profile_txt_height);
            this.rel_edit = view.findViewById(R.id.row_profile_rel_edit);
            this.rel_delete = view.findViewById(R.id.row_profile_rel_delete);
        }
    }
}
