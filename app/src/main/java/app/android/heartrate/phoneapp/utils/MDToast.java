package app.android.heartrate.phoneapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import app.android.heartrate.phoneapp.R;


public class MDToast extends Toast {
    public static int LENGTH_LONG = 1;
    public static int LENGTH_SHORT = 0;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_INFO = 0;
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_WARNING = 2;
    private final Context mContext;
    private int mType;
    private View mView;

    public MDToast(Context context) {
        super(context);
        this.mContext = context;
    }

    public static MDToast makeText(Context context, String str) {
        return makeText(context, str, LENGTH_SHORT, 0);
    }

    public static MDToast makeText(Context context, String str, int i) {
        return makeText(context, str, i, 0);
    }

    public static MDToast makeText(Context context, String str, int i, int i2) {
        MDToast mDToast = new MDToast(context);
        @SuppressLint("WrongConstant") View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.custom_toast_container, null);
        ImageView imageView = inflate.findViewById(R.id.icon);
        TextView textView = inflate.findViewById(R.id.text);
        if (i2 == 1) {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_exit_logo));
            if (Build.VERSION.SDK_INT >= 16) {
//                inflate.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_success_background));
            } else {
                inflate.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSuccess));
            }
        } else if (i2 == 2) {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_exit_logo));
            if (Build.VERSION.SDK_INT >= 16) {
//                inflate.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_exit_logo));
            } else {
                inflate.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWarning));
            }
        } else if (i2 != 3) {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_exit_logo));
            if (Build.VERSION.SDK_INT >= 16) {
//                inflate.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_info_background));
            } else {
                inflate.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSuccess));
            }
        } else {
//            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_exit_logo));
            if (Build.VERSION.SDK_INT >= 16) {
//                inflate.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_exit_logo));
            } else {
                inflate.setBackgroundColor(ContextCompat.getColor(context, R.color.colorError));
            }
        }

        textView.setText(str);
        mDToast.setDuration(i);
        mDToast.setView(inflate);
        mDToast.mView = inflate;
        mDToast.mType = i2;
        return mDToast;
    }

    @Override
    public void setText(int i) {
        setText(this.mContext.getString(i));
    }

    @Override
    public void setText(CharSequence charSequence) {
        View view = this.mView;
        if (view != null) {
            TextView textView = view.findViewById(R.id.text);
            if (textView != null) {
                textView.setText(charSequence);
                return;
            }
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        throw new RuntimeException("This Toast was not created with Toast.makeText()");
    }

    public void setIcon(int i) {
        setIcon(ContextCompat.getDrawable(this.mContext, i));
    }

    public void setIcon(Drawable drawable) {
        View view = this.mView;
        if (view != null) {
            ImageView imageView = view.findViewById(R.id.icon);
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
                return;
            }
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        throw new RuntimeException("This Toast was not created with Toast.makeText()");
    }

    public void setType(int i) {
        this.mType = i;
    }

    public int getType() {
        return this.mType;
    }
}
