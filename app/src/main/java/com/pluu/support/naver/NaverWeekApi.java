package com.pluu.support.naver;

import android.content.Context;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.item.Status;
import com.pluu.webtoon.item.WebToonInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 네이버 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NaverWeekApi extends AbstractWeekApi {

    private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일", "완결"};

    private static final String URL = "http://m.comic.naver.com/webtoon/weekday.nhn";
    private final String[] URL_VALUE = {"mon", "tue", "wed", "thu", "fri", "sat", "sun", "fin"};

    private int currentPos;

    public NaverWeekApi(Context context) {
        super(context, TITLE);
    }

    @Override
    public ServiceConst.NAV_ITEM getNaviItem() {
        return ServiceConst.NAV_ITEM.NAVER;
    }

    @Override
    public List<WebToonInfo> parseMain(int position) {
        currentPos = position;

        ArrayList<WebToonInfo> list = new ArrayList<>();

        String response;
        try {
            response = requestApi();
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }

        Document doc = Jsoup.parse(response);
        Elements links = doc.select("#pageList a");
        Pattern pattern = Pattern.compile("(?<=titleId=)\\d+");
        for (Element a : links) {
            Matcher matcher = pattern.matcher(a.attr("href"));
            if (!matcher.find()) {
                continue;
            }

            WebToonInfo item = new WebToonInfo(matcher.group());
            item.setTitle(a.select(".toon_name").text());
            item.setImage(a.select("img").first().attr("src"));

            if (!a.select(".aside_info .ico_up").isEmpty()) {
                // 최근 업데이트
                item.setStatus(Status.UPDATE);
            } else if (!a.select(".aside_info .ico_break").isEmpty()) {
                // 휴재
                item.setStatus(Status.BREAK);
            }
            item.setIsAdult(!a.select(".ico_adult2").isEmpty());
            item.setWriter(a.select(".sub_info").text());
			item.setRate(Const.getRateNameByRate(a.select(".txt_score").text()));
            item.setUpdateDate(a.select("span[class=if1]").text());
            list.add(item);
        }

        return list;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public String getUrl() {
        return URL;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        map.put("week", URL_VALUE[currentPos]);
        return map;
    }
}
