package android.support.percent;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PercentLayoutHelper {
    private final ViewGroup mHost;

    public PercentLayoutHelper(ViewGroup host) {
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(ViewGroup.LayoutParams params,
            TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {

        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);
        int i = 0;
        for (int N = this.mHost.getChildCount(); i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if ((params instanceof PercentLayoutParams)) {
                PercentLayoutInfo info = ((PercentLayoutParams) params)
                        .getPercentLayoutInfo();

                if (info != null)
                    if ((params instanceof ViewGroup.MarginLayoutParams)) {
                        info.fillMarginLayoutParams(
                                (ViewGroup.MarginLayoutParams) params,
                                widthHint, heightHint);
                    } else
                        info.fillLayoutParams(params, widthHint, heightHint);
            }
        }
    }

    public static PercentLayoutInfo getPercentLayoutInfo(Context context,
            AttributeSet attrs) {
        PercentLayoutInfo info = null;
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.PercentLayout_Layout);
        float value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_widthPercent, 1, 1,
                -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.widthPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_heightPercent, 1, 1,
                -1.0F);
        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.heightPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginPercent, 1, 1,
                -1.0F);
        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.leftMarginPercent = value;
            info.topMarginPercent = value;
            info.rightMarginPercent = value;
            info.bottomMarginPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginLeftPercent, 1,
                1, -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.leftMarginPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginTopPercent, 1, 1,
                -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.topMarginPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginRightPercent, 1,
                1, -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.rightMarginPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginBottomPercent, 1,
                1, -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.bottomMarginPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1,
                1, -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.startMarginPercent = value;
        }
        value = array.getFraction(
                R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1,
                -1.0F);

        if (value != -1.0F) {
            info = info != null ? info : new PercentLayoutInfo();
            info.endMarginPercent = value;
        }
        array.recycle();
        return info;
    }

    public void restoreOriginalParams() {
        int i = 0;
        for (int N = this.mHost.getChildCount(); i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if ((params instanceof PercentLayoutParams)) {
                PercentLayoutInfo info = ((PercentLayoutParams) params)
                        .getPercentLayoutInfo();

                if (info != null)
                    if ((params instanceof ViewGroup.MarginLayoutParams))
                        info.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams) params);
                    else
                        info.restoreLayoutParams(params);
            }
        }
    }

    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        int i = 0;
        for (int N = this.mHost.getChildCount(); i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if ((params instanceof PercentLayoutParams)) {
                PercentLayoutInfo info = ((PercentLayoutParams) params)
                        .getPercentLayoutInfo();

                if (info != null) {
                    if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view,
            PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredWidthAndState(view)
                & ViewCompat.MEASURED_STATE_MASK;
        return (state == ViewCompat.MEASURED_STATE_TOO_SMALL)
                && (info.widthPercent >= 0.0F)
                && (info.mPreservedParams.width == ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view,
            PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredHeightAndState(view)
                & ViewCompat.MEASURED_STATE_MASK;
        return (state == ViewCompat.MEASURED_STATE_TOO_SMALL)
                && (info.heightPercent >= 0.0F)
                && (info.mPreservedParams.height == ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static abstract interface PercentLayoutParams {
        public abstract PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo();
    }

    public static class PercentLayoutInfo {
        public float widthPercent;
        public float heightPercent;
        public float leftMarginPercent;
        public float topMarginPercent;
        public float rightMarginPercent;
        public float bottomMarginPercent;
        public float startMarginPercent;
        public float endMarginPercent;
        final ViewGroup.MarginLayoutParams mPreservedParams;

        public PercentLayoutInfo() {
            this.widthPercent = -1.0F;
            this.heightPercent = -1.0F;
            this.leftMarginPercent = -1.0F;
            this.topMarginPercent = -1.0F;
            this.rightMarginPercent = -1.0F;
            this.bottomMarginPercent = -1.0F;
            this.startMarginPercent = -1.0F;
            this.endMarginPercent = -1.0F;
            this.mPreservedParams = new ViewGroup.MarginLayoutParams(0, 0);
        }

        public void fillLayoutParams(ViewGroup.LayoutParams params,
                int widthHint, int heightHint) {
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;

            if (this.widthPercent >= 0.0F) {
                params.width = ((int) (widthHint * this.widthPercent));
            }
            if (this.heightPercent >= 0.0F) {
                params.height = ((int) (heightHint * this.heightPercent));
            }
        }

        public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams params,
                int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);

            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams,
                    MarginLayoutParamsCompat.getMarginStart(params));

            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams,
                    MarginLayoutParamsCompat.getMarginEnd(params));

            if (this.leftMarginPercent >= 0.0F) {
                params.leftMargin = ((int) (widthHint * this.leftMarginPercent));
            }
            if (this.topMarginPercent >= 0.0F) {
                params.topMargin = ((int) (heightHint * this.topMarginPercent));
            }
            if (this.rightMarginPercent >= 0.0F) {
                params.rightMargin = ((int) (widthHint * this.rightMarginPercent));
            }
            if (this.bottomMarginPercent >= 0.0F) {
                params.bottomMargin = ((int) (heightHint * this.bottomMarginPercent));
            }
            if (this.startMarginPercent >= 0.0F) {
                MarginLayoutParamsCompat.setMarginStart(params,
                        (int) (widthHint * this.startMarginPercent));
            }

            if (this.endMarginPercent >= 0.0F) {
                MarginLayoutParamsCompat.setMarginEnd(params,
                        (int) (widthHint * this.endMarginPercent));
            }

        }

        public String toString() {
            return String
                    .format(Locale.getDefault(),
                            "PercentLayoutInformation width: %f height %f, margins (%f, %f,  %f, %f, %f, %f)",
                            new Object[] { Float.valueOf(this.widthPercent),
                                    Float.valueOf(this.heightPercent),
                                    Float.valueOf(this.leftMarginPercent),
                                    Float.valueOf(this.topMarginPercent),
                                    Float.valueOf(this.rightMarginPercent),
                                    Float.valueOf(this.bottomMarginPercent),
                                    Float.valueOf(this.startMarginPercent),
                                    Float.valueOf(this.endMarginPercent) });
        }

        public void restoreMarginLayoutParams(
                ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params,
                    MarginLayoutParamsCompat
                            .getMarginStart(this.mPreservedParams));

            MarginLayoutParamsCompat.setMarginEnd(params,
                    MarginLayoutParamsCompat
                            .getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(ViewGroup.LayoutParams params) {
            params.width = this.mPreservedParams.width;
            params.height = this.mPreservedParams.height;
        }
    }
}