package com.example.parser.service.pda;

import com.example.parser.model.pda.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PdaParser {
    private static final String LESSON_URI = "https://www.youtube.com/watch?v=Bw26a9tW7G8";
    private static final String PARSING_PAGE_URL = "https://4pda.to/";
    private static final String KEY = "itemprop";
    private static final String VALUE = "url";

    public void parse() throws IOException {
        log.info(LESSON_URI);
        openUriAndUseElementsByAttributeValue(true);
    }

    @SneakyThrows
    private void openUriAndUseElementsByAttributeValue(boolean prnDocument)  {
        /**
         *
         */
        List<Post> postsOfPda = new ArrayList<>();
        log.info("Подключение к главной странице " + PARSING_PAGE_URL);
        Document document = Jsoup.connect(PARSING_PAGE_URL).get();

        Elements postTitleElement = document.getElementsByAttributeValue(KEY, VALUE);

        if (prnDocument) {
//            log.info(document.title());
//            log.info(document);
            log.info(postTitleElement);
        }
        for (Element element : postTitleElement) {
            Post post = new Post();
            final String detLink = element.attr("href");
            post.setDetailsLink(element.attr("href"));
            post.setTitle(element.attr("title"));
            Document detailsDoc = Jsoup.connect(post.getDetailsLink()).get();
            log.info("Подключение к странице деталей поста " + detLink);
            try {
                Element authorNameElement = detailsDoc.getElementsByClass("name").first().child(0);
                post.setAuthor(authorNameElement.text());
                post.setAuthorDetailsLink(authorNameElement.attr("href"));
                post.setDateOfCreated(detailsDoc.getElementsByClass("date").first().text());
            } catch (NullPointerException e) {
                post.setAuthor("<<<<< Автор не определился");
                post.setAuthorDetailsLink("<<<<<< Ссылки нет");
            }
            postsOfPda.add(post);
        }
//        postsOfPda.forEach(System.out::println);
    }
}
