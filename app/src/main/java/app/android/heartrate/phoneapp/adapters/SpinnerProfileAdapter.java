package app.android.heartrate.phoneapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.android.heartrate.phoneapp.R;
import app.android.heartrate.phoneapp.model.classes.UserProfileData;
import de.hdodenhof.circleimageview.CircleImageView;

public class SpinnerProfileAdapter extends ArrayAdapter<String> {
    ArrayList<UserProfileData> array_data;
    Context mContext;

    public SpinnerProfileAdapter(Context context, ArrayList<UserProfileData> arrayList) {
        super(context, R.layout.custom_spinner_list);
        this.mContext = context;
        this.array_data = arrayList;
    }

    public int getCount() {
        return this.array_data.size();
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        SpinnerViewHolder spinnerViewHolder = new SpinnerViewHolder();
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_spinner_dropdown_list, viewGroup, false);
            spinnerViewHolder.img_user_photo = view.findViewById(R.id.spinner_img_user);
            spinnerViewHolder.txt_user_name = view.findViewById(R.id.spinner_txt_name);
            spinnerViewHolder.txt_user_gender = view.findViewById(R.id.spinner_txt_gender);
            spinnerViewHolder.txt_user_height = view.findViewById(R.id.spinner_txt_height);
            view.setTag(spinnerViewHolder);
        } else {
            spinnerViewHolder = (SpinnerViewHolder) view.getTag();
        }
        UserProfileData userProfileData = this.array_data.get(i);
        String trim = userProfileData.user_name.trim();
        String trim2 = userProfileData.user_gender.trim();
        String valueOf = String.valueOf(userProfileData.user_height);
        spinnerViewHolder.img_user_photo.setImageBitmap(userProfileData.bmp_user_photo);
        spinnerViewHolder.txt_user_name.setText(trim);
        spinnerViewHolder.txt_user_gender.setText(trim2);
        spinnerViewHolder.txt_user_height.setText(valueOf);
        return view;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        SpinnerViewHolder spinnerViewHolder = new SpinnerViewHolder();
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_spinner_list, viewGroup, false);
            spinnerViewHolder.img_user_photo = view.findViewById(R.id.spinner_img_user);
            spinnerViewHolder.txt_user_name = view.findViewById(R.id.spinner_txt_name);
            spinnerViewHolder.txt_user_gender = view.findViewById(R.id.spinner_txt_gender);
            spinnerViewHolder.txt_user_height = view.findViewById(R.id.spinner_txt_height);
            view.setTag(spinnerViewHolder);
        } else {
            spinnerViewHolder = (SpinnerViewHolder) view.getTag();
        }
        UserProfileData userProfileData = this.array_data.get(i);
        String trim = userProfileData.user_name.trim();
        String trim2 = userProfileData.user_gender.trim();
        String valueOf = String.valueOf(userProfileData.user_height);
        spinnerViewHolder.img_user_photo.setImageBitmap(userProfileData.bmp_user_photo);
        spinnerViewHolder.txt_user_name.setText(trim);
        spinnerViewHolder.txt_user_gender.setText(trim2);
        spinnerViewHolder.txt_user_height.setText(valueOf);
        return view;
    }

    private static class SpinnerViewHolder {
        CircleImageView img_user_photo;
        TextView txt_user_gender;
        TextView txt_user_height;
        TextView txt_user_name;

        private SpinnerViewHolder() {
        }
    }
}
