package crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SnapshotDownloader {
    public static void start(final String location) {

        String URL = "http://web.archive.org/web/20081214203623/http://store.steampowered.com/hwsurvey/videocard/";

        int i = 1;
        log.info("Downloading snapshots...");
        while (!URL.isEmpty()) {
            final String date = URL.split("\\/")[4];
            final String prettyDate = date.substring(6, 8) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
            log.info("{}: {} -> {}", i++, prettyDate, URL);
            Document doc = null;
            try {
                doc = Jsoup.connect(URL).get();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            final Document parse = Jsoup.parse(doc.html());
            final File f = new File(location + File.separator + date + ".html");
            try {
                FileUtils.writeStringToFile(f, doc.html(), StandardCharsets.UTF_8);
            } catch (final IOException e) {
                e.printStackTrace();
            }

            final Element elem = parse.select("img[alt=Next capture]").get(0).parent();
            if (URL.equals(elem.attr("href"))) {
                final Element element = parse.select("td[class=f]").get(0);
                URL = element.child(0).attr("href");
            } else {
                URL = elem.attr("href");
            }
        }
        log.info("Snapshot download complete!");
    }

}
