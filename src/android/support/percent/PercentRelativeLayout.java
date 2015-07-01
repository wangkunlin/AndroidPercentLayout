package android.support.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class PercentRelativeLayout extends RelativeLayout {
    private final PercentLayoutHelper mHelper;

    public PercentRelativeLayout(Context context) {
        super(context);
        mHelper = new PercentLayoutHelper(this);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = new PercentLayoutHelper(this);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mHelper.handleMeasuredStateTooSmall())
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mHelper.restoreOriginalParams();
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams
            implements PercentLayoutHelper.PercentLayoutParams {
        private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(
                    c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            return this.mPercentLayoutInfo;
        }

        protected void setBaseAttributes(TypedArray a, int widthAttr,
                int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr,
                    heightAttr);
        }
    }
}