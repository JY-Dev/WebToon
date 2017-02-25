package com.pluu.support.impl;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.pluu.support.daum.DaumWeekApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoWeekApi;
import com.pluu.support.nate.NateWeekApi;
import com.pluu.support.naver.NaverWeekApi;
import com.pluu.support.olleh.OllehWeekApi;
import com.pluu.support.tstore.TStorerWeekApi;
import com.pluu.webtoon.item.WebToonInfo;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractWeekApi extends NetworkSupportApi {

    private final String[] CURRENT_TABS;

    protected AbstractWeekApi(Context context, String[] tabs) {
        super(context);
        this.CURRENT_TABS = tabs;
    }

    public abstract NAV_ITEM getNaviItem();

    public int getTitleColor(Context context) {
        return ContextCompat.getColor(context, getNaviItem().color);
    }

    public int getTitleColorDark(Context context) {
        return ContextCompat.getColor(context, getNaviItem().bgColor);
    }

    public int getWeeklyTabSize() {
        return CURRENT_TABS.length;
    }

    public String getWeeklyTabName(int position) {
        return CURRENT_TABS[position];
    }

    public int getTodayTabPosition() {
        return (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7;
    }

    public abstract List<WebToonInfo> parseMain(int position) throws Exception;

    public static AbstractWeekApi getApi(Context context, NAV_ITEM item) {
        switch (item) {
            case NAVER:
                return new NaverWeekApi(context);
            case DAUM:
                return new DaumWeekApi(context);
            case OLLEH:
                return new OllehWeekApi(context);
            case KAKAOPAGE:
                return new KakaoWeekApi(context);
            case NATE:
                return new NateWeekApi(context);
            case T_STORE:
                return new TStorerWeekApi(context);
            default:
                throw new Resources.NotFoundException("Not Found API");
        }
    }

}
