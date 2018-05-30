package com.example.mumumu.gitdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mumu on 2018/5/29.
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float width = 0;
        float height = 0;
        float childWidth;
        float childheight;
        float lineWidth = 0;
        float lineHeight = 0;
        float maxLineWidth = 0;

        //测量孩子
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        View child;
        MarginLayoutParams layoutParams;
        for (int i = 0; i < getChildCount(); i++) {
            //获取子view
            child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childheight = child.getMeasuredHeight();
            layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //不允许有子view宽度超过父布局
            if (child.getMeasuredWidth() > widthSize) {
                throw new IllegalArgumentException("高度不能大于父布局");
            }
            //当行宽和子view宽度大于widthSize的时候就是换行的时候，宽高 行宽 行高 都要做处理
            if (lineWidth + childWidth + layoutParams.leftMargin + layoutParams.rightMargin > widthSize - getPaddingLeft() - getPaddingRight()) {
                //换行后FlowLayout宽度就为测量的宽度（简化）
                width = widthSize;
                //换行后高度加上 上一行高度 （注意也就是说height没有算最后一行的高度）
                height += lineHeight;
                //重置行高
                lineHeight = childheight;
                //重置行宽
                lineWidth = childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            } else {//不换行
                //行高取最大值
                lineHeight = Math.max(lineHeight, childheight);
                //行宽+=上当前子view
                lineWidth += childWidth + layoutParams.leftMargin + layoutParams.rightMargin;

                //最大行宽等取最大行宽与当前行宽取大值 （考虑到只有单行的情况下，FlowLayout的宽度应该是等于单行宽的）
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                //单行的时候 width ！= widthSize;所以width为0，与maxLineWidth取最大值，还是widthSize，
                // 不是单行的时候width == widthSize，所以取最大值没问题
                width = Math.max(width, maxLineWidth);
            }
        }

        //height只是在换行的时候加上了上一行的高度，所以for遍历完成后，需要加上最后一行的高度
        height += lineHeight;

        //最后根据测量模式来决定使用哪个高度
        width = widthMode == MeasureSpec.EXACTLY ? widthSize : width+getPaddingLeft()+getPaddingRight();
        height = heightMode == MeasureSpec.EXACTLY ? heightSize : height+getPaddingTop()+getPaddingBottom();
        //设置最终的宽和高
        setMeasuredDimension((int) width, (int) height);

    }


    float lineWidth = 0;   //x值   换行重置，不换行+=
    float lineHeight = 0;   //y值  换行重置，不换行取大值
    float totalHeight = 0;  //总高度，换行加
    //布局只和当前行的宽度，和总的行高有关
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child;
        float childWidth;
        float childheight;
        MarginLayoutParams layoutParams;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childheight = child.getMeasuredHeight();
            layoutParams = (MarginLayoutParams) child.getLayoutParams();
            if (lineWidth + child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) {

                //重置该view的的x值，此时的linewidth不算当前view宽度
                lineWidth = layoutParams.leftMargin;
                //重置该view的的Y值，也就是当前行的y坐标，也就是以往的总高度
                totalHeight += lineHeight;
                //布局前需要重置行宽和行的总高度
                layoutChild(child, lineWidth, totalHeight, lineWidth + childWidth, totalHeight + childheight);

                //布局完之后重新计算行的宽和高
                lineWidth = childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
                //重置当前行高，
                lineHeight = childheight;
            } else {
                layoutChild(child, lineWidth + layoutParams.leftMargin, totalHeight, lineWidth + layoutParams.leftMargin + childWidth, totalHeight + childheight);

                //布局之后需要重新计算行的宽高
                lineWidth += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                lineHeight = Math.max(lineHeight, childheight);
            }
        }
    }


    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    private void layoutChild(View child, float l, float t, float r, float b) {
        l += getPaddingLeft();
        r += getPaddingLeft();
        t += getPaddingTop();
        b += getPaddingTop();
        child.layout((int) l, (int) t, (int) r, (int) b);
    }


}