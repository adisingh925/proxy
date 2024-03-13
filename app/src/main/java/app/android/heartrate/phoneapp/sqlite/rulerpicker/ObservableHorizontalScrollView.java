package app.android.heartrate.phoneapp.sqlite.rulerpicker;

import android.content.Context;
import android.widget.HorizontalScrollView;


public final class ObservableHorizontalScrollView extends HorizontalScrollView {
    private static final long NEW_CHECK_DURATION = 100;
    private final ScrollChangedListener mScrollChangedListener;
    private long mLastScrollUpdateMills = -1;
    private final Runnable mScrollerTask = new Runnable() {


        public void run() {
            if (System.currentTimeMillis() - ObservableHorizontalScrollView.this.mLastScrollUpdateMills > ObservableHorizontalScrollView.NEW_CHECK_DURATION) {
                ObservableHorizontalScrollView.this.mLastScrollUpdateMills = -1;
                ObservableHorizontalScrollView.this.mScrollChangedListener.onScrollStopped();
                return;
            }
            ObservableHorizontalScrollView.this.postDelayed(this, ObservableHorizontalScrollView.NEW_CHECK_DURATION);
        }
    };


    public ObservableHorizontalScrollView(Context context, ScrollChangedListener scrollChangedListener) {
        super(context);
        this.mScrollChangedListener = scrollChangedListener;
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        ScrollChangedListener scrollChangedListener = this.mScrollChangedListener;
        if (scrollChangedListener != null) {
            scrollChangedListener.onScrollChanged();
            if (this.mLastScrollUpdateMills == -1) {
                postDelayed(this.mScrollerTask, NEW_CHECK_DURATION);
            }
            this.mLastScrollUpdateMills = System.currentTimeMillis();
        }
    }


    public interface ScrollChangedListener {
        void onScrollChanged();

        void onScrollStopped();
    }
}
