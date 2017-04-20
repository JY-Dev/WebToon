package com.pluu.support.nate;

import android.content.Context;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.NAV_ITEM;
import com.pluu.webtoon.item.WebToonInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 네이트 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class NateWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private final String WEEKLY_URL = "http://m.comics.nate.com/main/index";

	public NateWeekApi(Context context) {
		super(context, TITLE);
	}

	@Override
	public NAV_ITEM getNaviItem() {
		return NAV_ITEM.NATE;
	}

	@Override
	public List<WebToonInfo> parseMain(int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

		Document doc = Jsoup.parse(response);
		Elements links = doc.select(".wkTypeAll_" + position);
		WebToonInfo item;
		Pattern pattern = Pattern.compile("(?<=btno=)\\d+");
		String href;
		for (Element a : links) {
			href = a.attr("href");
			Matcher matcher = pattern.matcher(href);
			if (!matcher.find()) {
				continue;
			}

			item = new WebToonInfo(matcher.group());
			item.setTitle(a.select(".wtl_title").text());
			item.setImage(a.select(".wtl_img img").first().attr("src"));
			item.setWriter(a.select(".wtl_author").text());
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
		return WEEKLY_URL;
	}
}
