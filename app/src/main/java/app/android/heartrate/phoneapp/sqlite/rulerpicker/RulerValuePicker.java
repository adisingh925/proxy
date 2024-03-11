package app.android.heartrate.phoneapp.sqlite.rulerpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

public final class RulerValuePicker extends FrameLayout implements ObservableHorizontalScrollView.ScrollChangedListener {
    private ObservableHorizontalScrollView mHorizontalScrollView;
    private View mLeftSpacer;
    private RulerValuePickerListener mListener;
    private int mNotchColor = -1;
    private Paint mNotchPaint;
    private Path mNotchPath;
    private View mRightSpacer;
    private RulerView mRulerView;

    public RulerValuePicker(Context context) {
        super(context);
        init(null);
    }

    public RulerValuePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public RulerValuePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }


    private void init(AttributeSet attributeSet) {
        addChildViews();

        this.mNotchPaint = new Paint();
        prepareNotchPaint();
        this.mNotchPath = new Path();
    }

    private void prepareNotchPaint() {
        this.mNotchPaint.setColor(this.mNotchColor);
        this.mNotchPaint.setStrokeWidth(5.0f);
        this.mNotchPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void addChildViews() {
        ObservableHorizontalScrollView observableHorizontalScrollView = new ObservableHorizontalScrollView(getContext(), this);
        this.mHorizontalScrollView = observableHorizontalScrollView;
        observableHorizontalScrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout linearLayout = new LinearLayout(getContext());
        View view = new View(getContext());
        this.mLeftSpacer = view;
        linearLayout.addView(view);
        RulerView rulerView = new RulerView(getContext());
        this.mRulerView = rulerView;
        linearLayout.addView(rulerView);
        View view2 = new View(getContext());
        this.mRightSpacer = view2;
        linearLayout.addView(view2);
        this.mHorizontalScrollView.removeAllViews();
        this.mHorizontalScrollView.addView(linearLayout);
        removeAllViews();
        addView(this.mHorizontalScrollView);
    }

    
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.mNotchPath, this.mNotchPaint);
    }

    
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            int width = getWidth();
            ViewGroup.LayoutParams layoutParams = this.mLeftSpacer.getLayoutParams();
            int i5 = width / 2;
            layoutParams.width = i5;
            this.mLeftSpacer.setLayoutParams(layoutParams);
            ViewGroup.LayoutParams layoutParams2 = this.mRightSpacer.getLayoutParams();
            layoutParams2.width = i5;
            this.mRightSpacer.setLayoutParams(layoutParams2);
            calculateNotchPath();
            invalidate();
        }
    }

    private void calculateNotchPath() {
        this.mNotchPath.reset();
        this.mNotchPath.moveTo((float) ((getWidth() / 2) - 30), 0.0f);
        this.mNotchPath.lineTo((float) (getWidth() / 2), 40.0f);
        this.mNotchPath.lineTo((float) ((getWidth() / 2) + 30), 0.0f);
    }

    public void selectValue(final int i) {
        this.mHorizontalScrollView.postDelayed(new Runnable() {
            

            public void run() {
                int i = 0;
                int i2;
                int i3;
                if (i < RulerValuePicker.this.mRulerView.getMinValue()) {
                    i = 0;
                } else {
                    if (i > RulerValuePicker.this.mRulerView.getMaxValue()) {
                        i3 = RulerValuePicker.this.mRulerView.getMaxValue();
                        i2 = RulerValuePicker.this.mRulerView.getMinValue();
                    } else {
                        i3 = i;
                        i2 = RulerValuePicker.this.mRulerView.getMinValue();
                    }
                    i = i3 - i2;
                }
                RulerValuePicker.this.mHorizontalScrollView.smoothScrollTo(i * RulerValuePicker.this.mRulerView.getIndicatorIntervalWidth(), 0);
            }
        }, 400);
    }

    public int getCurrentValue() {
        int minValue = this.mRulerView.getMinValue() + (this.mHorizontalScrollView.getScrollX() / this.mRulerView.getIndicatorIntervalWidth());
        if (minValue > this.mRulerView.getMaxValue()) {
            return this.mRulerView.getMaxValue();
        }
        return minValue < this.mRulerView.getMinValue() ? this.mRulerView.getMinValue() : minValue;
    }

    @Override
    public void onScrollChanged() {
        RulerValuePickerListener rulerValuePickerListener = this.mListener;
        if (rulerValuePickerListener != null) {
            rulerValuePickerListener.onIntermediateValueChange(getCurrentValue());
        }
    }

    @Override
    public void onScrollStopped() {
        makeOffsetCorrection(this.mRulerView.getIndicatorIntervalWidth());
        RulerValuePickerListener rulerValuePickerListener = this.mListener;
        if (rulerValuePickerListener != null) {
            rulerValuePickerListener.onValueChange(getCurrentValue());
        }
    }

    private void makeOffsetCorrection(int i) {
        int scrollX = this.mHorizontalScrollView.getScrollX() % i;
        if (scrollX < i / 2) {
            this.mHorizontalScrollView.scrollBy(-scrollX, 0);
        } else {
            this.mHorizontalScrollView.scrollBy(i - scrollX, 0);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.value = getCurrentValue();
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        selectValue(savedState.value);
    }

    public void setNotchColorRes(int i) {
        setNotchColor(ContextCompat.getColor(getContext(), i));
    }

    public int getNotchColor() {
        return this.mNotchColor;
    }

    public void setNotchColor(int i) {
        this.mNotchColor = i;
        prepareNotchPaint();
        invalidate();
    }

    public int getTextColor() {
        return this.mRulerView.getTextColor();
    }

    public void setTextColor(int i) {
        this.mRulerView.setTextColor(i);
    }

    public void setTextColorRes(int i) {
        setTextColor(ContextCompat.getColor(getContext(), i));
    }

    public float getTextSize() {
        return this.mRulerView.getTextSize();
    }

    public void setTextSize(int i) {
        this.mRulerView.setTextSize(i);
    }

    public void setTextSizeRes(int i) {
        setTextSize((int) getContext().getResources().getDimension(i));
    }

    public int getIndicatorColor() {
        return this.mRulerView.getIndicatorColor();
    }

    public void setIndicatorColor(int i) {
        this.mRulerView.setIndicatorColor(i);
    }

    public void setIndicatorColorRes(int i) {
        setIndicatorColor(ContextCompat.getColor(getContext(), i));
    }

    public float getIndicatorWidth() {
        return this.mRulerView.getIndicatorWidth();
    }

    public void setIndicatorWidth(int i) {
        this.mRulerView.setIndicatorWidth(i);
    }

    public void setIndicatorWidthRes(int i) {
        setIndicatorWidth(getContext().getResources().getDimensionPixelSize(i));
    }

    public int getMinValue() {
        return this.mRulerView.getMinValue();
    }

    public int getMaxValue() {
        return this.mRulerView.getMaxValue();
    }

    public void setMinMaxValue(int i, int i2) {
        this.mRulerView.setValueRange(i, i2);
        invalidate();
        selectValue(i);
    }

    public int getIndicatorIntervalWidth() {
        return this.mRulerView.getIndicatorIntervalWidth();
    }

    public void setIndicatorIntervalDistance(int i) {
        this.mRulerView.setIndicatorIntervalDistance(i);
    }

    public float getLongIndicatorHeightRatio() {
        return this.mRulerView.getLongIndicatorHeightRatio();
    }

    public float getShortIndicatorHeightRatio() {
        return this.mRulerView.getShortIndicatorHeightRatio();
    }

    public void setIndicatorHeight(float f, float f2) {
        this.mRulerView.setIndicatorHeight(f, f2);
    }

    public void setValuePickerListener(RulerValuePickerListener rulerValuePickerListener) {
        this.mListener = rulerValuePickerListener;
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            

            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private int value;

        SavedState(Parcelable parcelable) {
            super(parcelable);
            this.value = 0;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.value = 0;
            this.value = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.value);
        }
    }
}
