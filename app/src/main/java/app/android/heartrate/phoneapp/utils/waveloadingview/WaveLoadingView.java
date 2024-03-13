package app.android.heartrate.phoneapp.utils.waveloadingview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;


public class WaveLoadingView extends View {
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_AMPLITUDE_VALUE = 50.0f;
    private static final float DEFAULT_BORDER_WIDTH = 0.0f;
    private static final int DEFAULT_ROUND_RECTANGLE_X_AND_Y = 30;
    private static final int DEFAULT_STROKE_COLOR = 0;
    private static final float DEFAULT_TITLE_BOTTOM_SIZE = 18.0f;
    private static final float DEFAULT_TITLE_CENTER_SIZE = 22.0f;
    private static final int DEFAULT_TITLE_COLOR = Color.parseColor("#212121");
    private static final float DEFAULT_TITLE_STROKE_WIDTH = 0.0f;
    private static final float DEFAULT_TITLE_TOP_SIZE = 18.0f;
    private static final int DEFAULT_TRIANGLE_DIRECTION = TriangleDirection.NORTH.ordinal();
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final int DEFAULT_WAVE_BACKGROUND_COLOR = Color.parseColor("#00000000");
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#212121");
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final int DEFAULT_WAVE_PROGRESS_VALUE = 50;
    private static final int DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE.ordinal();
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    private Bitmap bitmapBuffer;
    private float mAmplitudeRatio;
    private AnimatorSet mAnimatorSet;
     Paint mBorderPaint;
    private String mBottomTitle;
    private Paint mBottomTitlePaint;
    private Paint mBottomTitleStrokePaint;
    private int mCanvasHeight;
    private int mCanvasSize;
    private int mCanvasWidth;
    private String mCenterTitle;
    private Paint mCenterTitlePaint;
    private Paint mCenterTitleStrokePaint;
    private Context mContext;
    private float mDefaultWaterLevel;
    private boolean mIsRoundRectangle;
    private int mProgressValue;
    private int mRoundRectangleXY;
    private Matrix mShaderMatrix;
    private int mShapeType;
    private String mTopTitle;
    private Paint mTopTitlePaint;
    private Paint mTopTitleStrokePaint;
    private int mTriangleDirection;
    private final float mWaterLevelRatio;
    private int mWaveBgColor;
    private Paint mWaveBgPaint;
    private int mWaveColor;
    private Paint mWavePaint;
    private BitmapShader mWaveShader;
    private final float mWaveShiftRatio;
    private ObjectAnimator waveShiftAnim;

    public enum ShapeType {
        TRIANGLE,
        CIRCLE,
        SQUARE,
        RECTANGLE
    }

    public enum TriangleDirection {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public WaveLoadingView(Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WaveLoadingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mWaterLevelRatio = 1.0f;
        this.mWaveShiftRatio = 0.0f;
        this.mProgressValue = 50;
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.mContext = context;
        this.mShaderMatrix = new Matrix();
        Paint paint = new Paint();
        this.mWavePaint = paint;
        paint.setAntiAlias(true);
        Paint paint2 = new Paint();
        this.mWaveBgPaint = paint2;
        paint2.setAntiAlias(true);
        initAnimation();

    }

    public void onDraw(Canvas canvas) {
        this.mCanvasSize = canvas.getWidth();
        if (canvas.getHeight() < this.mCanvasSize) {
            this.mCanvasSize = canvas.getHeight();
        }
        if (this.mWaveShader != null) {
            if (this.mWavePaint.getShader() == null) {
                this.mWavePaint.setShader(this.mWaveShader);
            }
            this.mShaderMatrix.setScale(1.0f, this.mAmplitudeRatio / 0.1f, 0.0f, this.mDefaultWaterLevel);
            this.mShaderMatrix.postTranslate(this.mWaveShiftRatio * ((float) getWidth()), (0.5f - this.mWaterLevelRatio) * ((float) getHeight()));
            this.mWaveShader.setLocalMatrix(this.mShaderMatrix);
            float strokeWidth = this.mBorderPaint.getStrokeWidth();
            int i = this.mShapeType;
            if (i == 0) {
                Path equilateralTriangle = getEquilateralTriangle(new Point(0, getHeight()), getWidth(), getHeight(), this.mTriangleDirection);
                canvas.drawPath(equilateralTriangle, this.mWaveBgPaint);
                canvas.drawPath(equilateralTriangle, this.mWavePaint);
            } else if (i == 1) {
                if (strokeWidth > 0.0f) {
                    canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, ((((float) getWidth()) - strokeWidth) / 2.0f) - 1.0f, this.mBorderPaint);
                }
                float width = (((float) getWidth()) / 2.0f) - strokeWidth;
                canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, width, this.mWaveBgPaint);
                canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, width, this.mWavePaint);
            } else if (i == 2) {
                if (strokeWidth > 0.0f) {
                    float f = strokeWidth / 2.0f;
                    canvas.drawRect(f, f, (((float) getWidth()) - f) - 0.5f, (((float) getHeight()) - f) - 0.5f, this.mBorderPaint);
                }
                canvas.drawRect(strokeWidth, strokeWidth, ((float) getWidth()) - strokeWidth, ((float) getHeight()) - strokeWidth, this.mWaveBgPaint);
                canvas.drawRect(strokeWidth, strokeWidth, ((float) getWidth()) - strokeWidth, ((float) getHeight()) - strokeWidth, this.mWavePaint);
            } else if (i == 3) {
                if (this.mIsRoundRectangle) {
                    if (strokeWidth > 0.0f) {
                        float f2 = strokeWidth / 2.0f;
                        RectF rectF = new RectF(f2, f2, (((float) getWidth()) - f2) - 0.5f, (((float) getHeight()) - f2) - 0.5f);
                        int i2 = this.mRoundRectangleXY;
                        canvas.drawRoundRect(rectF, (float) i2, (float) i2, this.mWaveBgPaint);
                        int i3 = this.mRoundRectangleXY;
                        canvas.drawRoundRect(rectF, (float) i3, (float) i3, this.mWavePaint);
                    } else {
                        RectF rectF2 = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
                        int i4 = this.mRoundRectangleXY;
                        canvas.drawRoundRect(rectF2, (float) i4, (float) i4, this.mWaveBgPaint);
                        int i5 = this.mRoundRectangleXY;
                        canvas.drawRoundRect(rectF2, (float) i5, (float) i5, this.mWavePaint);
                    }
                } else if (strokeWidth > 0.0f) {
                    float f3 = strokeWidth / 2.0f;
                    canvas.drawRect(f3, f3, (((float) getWidth()) - f3) - 0.5f, (((float) getHeight()) - f3) - 0.5f, this.mWaveBgPaint);
                    canvas.drawRect(f3, f3, (((float) getWidth()) - f3) - 0.5f, (((float) getHeight()) - f3) - 0.5f, this.mWavePaint);
                } else {
                    canvas.drawRect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mWaveBgPaint);
                    canvas.drawRect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mWavePaint);
                }
            }
            if (!TextUtils.isEmpty(this.mTopTitle)) {
                float measureText = this.mTopTitlePaint.measureText(this.mTopTitle);
                canvas.drawText(this.mTopTitle, (((float) getWidth()) - measureText) / 2.0f, ((float) (getHeight() * 2)) / 10.0f, this.mTopTitleStrokePaint);
                canvas.drawText(this.mTopTitle, (((float) getWidth()) - measureText) / 2.0f, ((float) (getHeight() * 2)) / 10.0f, this.mTopTitlePaint);
            }
            if (!TextUtils.isEmpty(this.mCenterTitle)) {



            }
            if (!TextUtils.isEmpty(this.mBottomTitle)) {
                float measureText3 = this.mBottomTitlePaint.measureText(this.mBottomTitle);
                canvas.drawText(this.mBottomTitle, (((float) getWidth()) - measureText3) / 2.0f, (((float) (getHeight() * 8)) / 10.0f) - ((this.mBottomTitleStrokePaint.descent() + this.mBottomTitleStrokePaint.ascent()) / 2.0f), this.mBottomTitleStrokePaint);
                canvas.drawText(this.mBottomTitle, (((float) getWidth()) - measureText3) / 2.0f, (((float) (getHeight() * 8)) / 10.0f) - ((this.mBottomTitlePaint.descent() + this.mBottomTitlePaint.ascent()) / 2.0f), this.mBottomTitlePaint);
                return;
            }
            return;
        }
        this.mWavePaint.setShader(null);
    }

    
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (getShapeType() == 3) {
            this.mCanvasWidth = i;
            this.mCanvasHeight = i2;
        } else {
            this.mCanvasSize = i;
            if (i2 < i) {
                this.mCanvasSize = i2;
            }
        }
        updateWaveShader();
    }

    private void updateWaveShader() {
        if (this.bitmapBuffer == null || haveBoundsChanged()) {
            Bitmap bitmap = this.bitmapBuffer;
            if (bitmap != null) {
                bitmap.recycle();
            }
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            if (measuredWidth > 0 && measuredHeight > 0) {
                double d = 6.283185307179586d / ((double) measuredWidth);
                float f = (float) measuredHeight;
                float f2 = 0.1f * f;
                this.mDefaultWaterLevel = f * 0.5f;
                float f3 = (float) measuredWidth;
                Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint();
                paint.setStrokeWidth(2.0f);
                paint.setAntiAlias(true);
                int i = measuredWidth + 1;
                int i2 = measuredHeight + 1;
                float[] fArr = new float[i];
                paint.setColor(adjustAlpha(this.mWaveColor, 0.3f));
                int i3 = 0;
                while (i3 < i) {
                    float sin = (float) (((double) this.mDefaultWaterLevel) + (((double) f2) * Math.sin(((double) i3) * d)));
                    float f4 = (float) i3;
                    canvas.drawLine(f4, sin, f4, (float) i2, paint);
                    fArr[i3] = sin;
                    i3++;
                    fArr = fArr;
                    d = d;
                }
                paint.setColor(this.mWaveColor);
                int i4 = (int) (f3 / 4.0f);
                for (int i5 = 0; i5 < i; i5++) {
                    float f5 = (float) i5;
                    canvas.drawLine(f5, fArr[(i5 + i4) % i], f5, (float) i2, paint);
                }
                BitmapShader bitmapShader = new BitmapShader(createBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
                this.mWaveShader = bitmapShader;
                this.mWavePaint.setShader(bitmapShader);
            }
        }
    }

    private boolean haveBoundsChanged() {
        return getMeasuredWidth() != this.bitmapBuffer.getWidth() || getMeasuredHeight() != this.bitmapBuffer.getHeight();
    }

    
    public void onMeasure(int i, int i2) {
        int measureWidth = measureWidth(i);
        int measureHeight = measureHeight(i2);
        if (getShapeType() == 3) {
            setMeasuredDimension(measureWidth, measureHeight);
            return;
        }
        if (measureWidth >= measureHeight) {
            measureWidth = measureHeight;
        }
        setMeasuredDimension(measureWidth, measureWidth);
    }

    private int measureWidth(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        return (mode == 1073741824 || mode == Integer.MIN_VALUE) ? size : this.mCanvasWidth;
    }

    private int measureHeight(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (!(mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            size = this.mCanvasHeight;
        }
        return size + 2;
    }













    public int getShapeType() {
        return this.mShapeType;
    }




    public void setProgressValue(int i) {
        this.mProgressValue = i;
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "waterLevelRatio", this.mWaterLevelRatio, ((float) i) / 100.0f);
        ofFloat.setDuration(1000L);
        ofFloat.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat);
        animatorSet.start();
    }






    public void setCenterTitle(String str) {
        this.mCenterTitle = str;
    }




    public void startAnimation() {
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null) {
            animatorSet.start();
        }
    }



    public void cancelAnimation() {
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }





    private void initAnimation() {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0.0f, 1.0f);
        this.waveShiftAnim = ofFloat;
        ofFloat.setRepeatCount(-1);
        this.waveShiftAnim.setDuration(1000L);
        this.waveShiftAnim.setInterpolator(new LinearInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimatorSet = animatorSet;
        animatorSet.play(this.waveShiftAnim);
    }

    
    public void onAttachedToWindow() {
        startAnimation();
        super.onAttachedToWindow();
    }

    
    public void onDetachedFromWindow() {
        cancelAnimation();
        super.onDetachedFromWindow();
    }

    private int adjustAlpha(int i, float f) {
        return Color.argb(Math.round(((float) Color.alpha(i)) * f), Color.red(i), Color.green(i), Color.blue(i));
    }

    private int sp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    private int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private Path getEquilateralTriangle(Point point, int i, int i2, int i3) {
        Point point2;
        Point point3;
        Point point4 = null;
        if (i3 == 0) {
            point4 = new Point(point.x + i, point.y);
            int i4 = point.x + (i / 2);
            double d = i2;
            point3 = new Point(i4, (int) (d - ((Math.sqrt(3.0d) / 2.0d) * d)));
        } else if (i3 == 1) {
            point4 = new Point(point.x, point.y - i2);
            point3 = new Point(point.x + i, point.y - i2);
            point.x += i / 2;
            point.y = (int) ((Math.sqrt(3.0d) / 2.0d) * ((double) i2));
        } else {
            if (i3 == 2) {
                point4 = new Point(point.x, point.y - i2);
                point2 = new Point((int) ((Math.sqrt(3.0d) / 2.0d) * ((double) i)), point.y / 2);
            } else if (i3 == 3) {
                point4 = new Point(point.x + i, point.y - i2);
                point2 = new Point(point.x + i, point.y);
                double d2 = i;
                point.x = (int) (d2 - ((Math.sqrt(3.0d) / 2.0d) * d2));
                point.y /= 2;
            } else {
                point2 = null;
            }
            Path path = new Path();
            path.moveTo((float) point.x, (float) point.y);
            path.lineTo((float) point4.x, (float) point4.y);
            path.lineTo((float) point2.x, (float) point2.y);
            return path;
        }
        point2 = point3;
        Path path2 = new Path();
        path2.moveTo((float) point.x, (float) point.y);
        path2.lineTo((float) point4.x, (float) point4.y);
        path2.lineTo((float) point2.x, (float) point2.y);
        return path2;
    }
}
