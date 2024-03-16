package app.android.heartrate.phoneapp.sqlite.rulerpicker;

import android.content.Context;


public final class RulerViewUtils {
    RulerViewUtils() {
    }

    static int sp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
