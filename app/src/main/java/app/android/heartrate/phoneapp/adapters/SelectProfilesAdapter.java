package app.android.heartrate.phoneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.android.heartrate.phoneapp.R;

import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class SelectProfilesAdapter extends RecyclerView.Adapter<SelectProfilesAdapter.ProfileViewHolder> {
    private final List<UserProfileData> array_data;
    private final Context mContext;
    Animation push_animation;

    public abstract void onSelectProfileAdapterClickItem(int i, View view);

    public SelectProfilesAdapter(Context context, ArrayList<UserProfileData> arrayList) {
        this.array_data = arrayList;
        this.mContext = context;
        this.push_animation = AnimationUtils.loadAnimation(context, R.anim.view_push);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProfileViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_dialog_profiles, null));
    }

    public void onBindViewHolder(ProfileViewHolder profileViewHolder, @SuppressLint("RecyclerView") final int i) {
        UserProfileData userProfileData = this.array_data.get(i);
        String trim = userProfileData.user_name.trim();
        String trim2 = userProfileData.user_birth_date.trim();
        String trim3 = userProfileData.user_gender.trim();
        profileViewHolder.img_profile_photo.setImageBitmap(userProfileData.bmp_user_photo);
        profileViewHolder.txt_name.setText(trim);
        profileViewHolder.txt_dob.setText(trim2);
        profileViewHolder.txt_gender.setText(trim3);
        profileViewHolder.rel_main.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SelectProfilesAdapter.this.onSelectProfileAdapterClickItem(i, view);
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
        CircleImageView img_profile_photo;
        RelativeLayout rel_main;
        TextView txt_dob;
        TextView txt_gender;
        TextView txt_name;

        public ProfileViewHolder(View view) {
            super(view);
            this.rel_main = view.findViewById(R.id.row_dialog_profile_rel_main);
            this.img_profile_photo = view.findViewById(R.id.row_dialog_profile_img_user);
            this.txt_name = view.findViewById(R.id.row_dialog_profile_txt_name);
            this.txt_dob = view.findViewById(R.id.row_dialog_profile_txt_dob);
            this.txt_gender = view.findViewById(R.id.row_dialog_profile_txt_gender);
        }
    }
}
