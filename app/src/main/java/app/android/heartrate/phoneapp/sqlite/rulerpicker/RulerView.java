package app.android.heartrate.phoneapp.sqlite.rulerpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;



public final class RulerView extends View {
    private int mIndicatorColor = -1;
    private int mIndicatorInterval = 14;
    private Paint mIndicatorPaint;
    private float mIndicatorWidthPx = 4.0f;
    private int mLongIndicatorHeight = 0;
    private float mLongIndicatorHeightRatio = 0.6f;
    private int mMaxValue = 100;
    private int mMinValue = 0;
    private int mShortIndicatorHeight = 0;
    private float mShortIndicatorHeightRatio = 0.4f;
    private int mTextColor = -1;
    private Paint mTextPaint;
    private int mTextSize = 36;
    private int mViewHeight;

    public RulerView(Context context) {
        super(context);
        parseAttr(null);
    }

    public RulerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        parseAttr(attributeSet);
    }

    public RulerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        parseAttr(attributeSet);
    }

    public RulerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        parseAttr(attributeSet);
    }

    private void parseAttr(AttributeSet attributeSet) {

        refreshPaint();
    }

    private void refreshPaint() {
        Paint paint = new Paint(1);
        this.mIndicatorPaint = paint;
        paint.setColor(this.mIndicatorColor);
        this.mIndicatorPaint.setStrokeWidth(this.mIndicatorWidthPx);
        this.mIndicatorPaint.setStyle(Paint.Style.STROKE);
        Paint paint2 = new Paint(1);
        this.mTextPaint = paint2;
        paint2.setColor(this.mTextColor);
        this.mTextPaint.setTextSize((float) this.mTextSize);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        invalidate();
        requestLayout();
    }

    
    public void onDraw(Canvas canvas) {
        for (int i = 1; i < this.mMaxValue - this.mMinValue; i++) {
            if (i % 5 == 0) {
                drawLongIndicator(canvas, i);
                drawValueText(canvas, i);
            } else {
                drawSmallIndicator(canvas, i);
            }
        }
        drawSmallIndicator(canvas, 0);
        drawSmallIndicator(canvas, getWidth());
        super.onDraw(canvas);
    }

    
    public void onMeasure(int i, int i2) {
        this.mViewHeight = MeasureSpec.getSize(i2);
        int i3 = ((this.mMaxValue - this.mMinValue) - 1) * this.mIndicatorInterval;
        updateIndicatorHeight(this.mLongIndicatorHeightRatio, this.mShortIndicatorHeightRatio);
        setMeasuredDimension(i3, this.mViewHeight);
    }

    private void updateIndicatorHeight(float f, float f2) {
        int i = this.mViewHeight;
        this.mLongIndicatorHeight = (int) (((float) i) * f);
        this.mShortIndicatorHeight = (int) (((float) i) * f2);
    }

    private void drawSmallIndicator(Canvas canvas, int i) {
        int i2 = this.mIndicatorInterval;
        canvas.drawLine((float) (i2 * i), 0.0f, (float) (i2 * i), (float) this.mShortIndicatorHeight, this.mIndicatorPaint);
    }

    private void drawLongIndicator(Canvas canvas, int i) {
        int i2 = this.mIndicatorInterval;
        canvas.drawLine((float) (i2 * i), 0.0f, (float) (i2 * i), (float) this.mLongIndicatorHeight, this.mIndicatorPaint);
    }

    private void drawValueText(Canvas canvas, int i) {
        canvas.drawText(String.valueOf(this.mMinValue + i), (float) (this.mIndicatorInterval * i), ((float) this.mLongIndicatorHeight) + this.mTextPaint.getTextSize(), this.mTextPaint);
    }

    
    public int getTextColor() {
        return this.mIndicatorColor;
    }

    
    public void setTextColor(int i) {
        this.mTextColor = i;
        refreshPaint();
    }

    
    public float getTextSize() {
        return (float) this.mTextSize;
    }

    
    public void setTextSize(int i) {
        this.mTextSize = RulerViewUtils.sp2px(getContext(), (float) i);
        refreshPaint();
    }

    
    public int getIndicatorColor() {
        return this.mIndicatorColor;
    }

    
    public void setIndicatorColor(int i) {
        this.mIndicatorColor = i;
        refreshPaint();
    }

    
    public float getIndicatorWidth() {
        return this.mIndicatorWidthPx;
    }

    
    public void setIndicatorWidth(int i) {
        this.mIndicatorWidthPx = (float) i;
        refreshPaint();
    }

    
    public int getMinValue() {
        return this.mMinValue;
    }

    
    public int getMaxValue() {
        return this.mMaxValue;
    }

    
    public void setValueRange(int i, int i2) {
        this.mMinValue = i;
        this.mMaxValue = i2;
        invalidate();
    }

    
    public int getIndicatorIntervalWidth() {
        return this.mIndicatorInterval;
    }

    
    public void setIndicatorIntervalDistance(int i) {
        if (i > 0) {
            this.mIndicatorInterval = i;
            invalidate();
            return;
        }
        throw new IllegalArgumentException("Interval cannot be negative or zero.");
    }

    
    public float getLongIndicatorHeightRatio() {
        return this.mLongIndicatorHeightRatio;
    }

    
    public float getShortIndicatorHeightRatio() {
        return this.mShortIndicatorHeightRatio;
    }

    
    public void setIndicatorHeight(float f, float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Sort indicator height must be between 0 to 1.");
        } else if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Long indicator height must be between 0 to 1.");
        } else if (f2 <= f) {
            this.mLongIndicatorHeightRatio = f;
            this.mShortIndicatorHeightRatio = f2;
            updateIndicatorHeight(f, f2);
            invalidate();
        } else {
            throw new IllegalArgumentException("Long indicator height cannot be less than sort indicator height.");
        }
    }
}
