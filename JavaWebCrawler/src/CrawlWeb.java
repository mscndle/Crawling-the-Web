import com.javafx.tools.doclets.internal.toolkit.util.DocFinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;




// Crawling the web in java
public class CrawlWeb {

    static String url;
    static int crawlSize;

    public static void main(String[] args) throws IOException {

        //Not handling incorrect inputs for now
//        System.out.println("Enter URL to begin crawling");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        url = br.readLine();
//
//        System.out.println("Enter total number of URLs to crawl");
//        crawlSize = Integer.parseInt(br.readLine());

        String url = "http://www.google.com";

//        String page = getHtml(url);
//        String page = getHtmlUsingJsoup(url);
//
        Set<String> pageLinks = getAllLinks(url);
        for (String s : pageLinks) {
            System.out.println(s);
        }

    }


    //
    //  Standard java way of 
    //
    static String getHtml(String url) {
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return sb.toString();
    }

    //
    //  Test function for Jsoup. Not really being used in the parse.
    //
    static String getHtmlUsingJsoup(String url) {
        String html;

        try {
            html = Jsoup.connect(url).get().html();
        } catch (IOException ioe) {
            html = "";    //(Yea I know)
            ioe.printStackTrace();
        }

        return html;
    }

    static void crawlWeb(String origin, int limit) {

        Set<String> crawled = new HashSet<String>();    //maintains a list of unique urls crawled
        Set<String> toCrawl = new HashSet<String>();

        toCrawl.add(origin);

        while (!toCrawl.isEmpty() || crawled.size() < limit) {


        }

    }

    //
    // Returns all HTML links found in a page
    // Jsoup reduces the need for another function get html
    // Getting page html and parsing links is all done here
    //
    static Set<String> getAllLinks(String url) {
        Set<String> pageLinks = new HashSet<String>();
        Elements elements = new Elements();
        Document doc;
        String attribute;

        try {
            doc = Jsoup.connect(url).get();
            elements = doc.select("a");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        for (Element a : elements) {
            attribute = a.attr("href");

            //handle cases with http and https missing
            if (!attribute.contains("http") && !attribute.contains("https")) {
                attribute = url + attribute;
            }

            pageLinks.add(attribute);
        }

        return pageLinks;
    }






}
