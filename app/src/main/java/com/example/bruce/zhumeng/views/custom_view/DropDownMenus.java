package com.example.bruce.zhumeng.views.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;

import java.util.List;

/**
 * Created by zhang on 2016/4/14.
 */
public class DropDownMenus extends LinearLayout {

    //top menu layout
    private LinearLayout tabMenuView;
    //bottom container,concluding popupMenuViews and maskView
    private FrameLayout  contanierView;
    //menu parent layout
    private FrameLayout  popupMenuView;
    //
    private View         maskView;

    private int dividerColor        = 0xffcccccc;
    private int textSelectedColor   = 0xff890c85;
    private int textUnselectedColor = 0xff111111;
    private int maskViewColor       = 0x88888888;
    private int menuTextSize        = 14;
    private int selectedIcon;
    private int unselectedIcon;

    private int    currentTabPosition = -1;
    private String currentTabText     = "";

    public DropDownMenus(Context context) {
        super(context, null);
    }

    public DropDownMenus(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        //add custom attrs
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenus);
        dividerColor = a.getColor(R.styleable.DropDownMenus_ddmDividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenus_ddmTextSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenus_ddmTextUnselectedColor, textUnselectedColor);
        maskViewColor = a.getColor(R.styleable.DropDownMenus_ddmMaskViewColor, maskViewColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenus_ddmMenuTextSize, menuTextSize);
        selectedIcon = a.getResourceId(R.styleable.DropDownMenus_ddmSelectedIcon, selectedIcon);
        unselectedIcon = a.getResourceId(R.styleable.DropDownMenus_ddmUnselectedIcon, unselectedIcon);
        underlineColor = a.getColor(R.styleable.DropDownMenus_ddmUnderlineColor, underlineColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenus_ddmBackgroundColor, menuBackgroundColor);
        a.recycle();

        //init tabMenuView and add into
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //add underLine for tabMenuView
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //init container and add into
        contanierView = new FrameLayout(context);
        contanierView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout
                .LayoutParams.MATCH_PARENT));
        addView(contanierView, 2);

    }

    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View
            contentView) {
        Log.d("zhang", "tabTexts.size()=" + tabTexts.size());
        Log.d("zhang", "popupViews.size()=" + popupViews.size());
        if (tabTexts.size() != popupViews.size()) {

            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        contanierView.addView(contentView, 0);

        //init maskView
        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout
                .LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskViewColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        contanierView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuView = new FrameLayout(getContext());
        popupMenuView.setVisibility(GONE);
        contanierView.addView(popupMenuView, 2);


        //init popupMenuView;
        for (int i = 0; i < popupViews.size(); i++) {
            if (i == 0 || i == 2) {
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int dispalyHeight = dm.heightPixels;
                popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (
                        dispalyHeight) / 2));
            } else {
                popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            popupMenuView.addView(popupViews.get(i), i);
        }

    }

    private void addTab(@NonNull List<String> tabTexts, int i) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(unselectedIcon), null);
        tab.setText(tabTexts.get(i));
        tab.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
        //add click event
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(v);
            }
        });
        Log.d("zhang", "tabMenuView=" + tabMenuView);
        tabMenuView.addView(tab);
        //add divider
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }

    }

    private void switchMenu(View target) {
        System.out.println(currentTabPosition);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (currentTabPosition == i) {
                    closeMenu();
                } else {
                    if (currentTabPosition == -1) {
                        //menu is closing
                        popupMenuView.setVisibility(View.VISIBLE);
                        popupMenuView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_popupview_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_maskview_in));
                        popupMenuView.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuView.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    currentTabPosition = i;
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(selectedIcon), null);
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(unselectedIcon), null);
                popupMenuView.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (currentTabPosition != -1) {
            ((TextView) tabMenuView.getChildAt(currentTabPosition)).setText(text);
            currentTabText = text;
        } else {
            currentTabText = "不限";
        }
    }

    public String getTabText() {
        return currentTabText;

    }

    public void closeMenu() {
        if (currentTabPosition != -1) {
            ((TextView) tabMenuView.getChildAt(currentTabPosition)).setTextColor(textUnselectedColor);
            ((TextView) tabMenuView.getChildAt(currentTabPosition)).setCompoundDrawablesWithIntrinsicBounds(null,
                    null, getResources().getDrawable(unselectedIcon), null);
            popupMenuView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_popupview_out));
            popupMenuView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_maskview_out));
            maskView.setVisibility(GONE);
            currentTabPosition = -1;
        }
    }

    private int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

}
