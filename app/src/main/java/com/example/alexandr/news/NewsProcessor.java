package com.example.alexandr.news;

import java.io.IOException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class NewsProcessor {
    private final static NewsLoader newsLoader = new NewsLoader();

    public static boolean processUpdate(NewsIntentService service) throws IOException {
        final Storage storage = Storage.getInstance(service);
        String category = storage.loadCurrentTopic();
        if ( category==null || category.isEmpty())
            category = Topics.AUTO;
        News news = newsLoader.loadNews(category);
        if (news == null)
            return false;
        storage.saveNews(news);
        return true;
    }
}
