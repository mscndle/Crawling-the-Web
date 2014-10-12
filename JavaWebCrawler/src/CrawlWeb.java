import com.javafx.tools.doclets.internal.toolkit.util.DocFinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.Stack;


// Crawling the web in java
public class CrawlWeb {

    static String url;
    static int crawlSize;

    public static void main(String[] args) throws IOException {

////        Not handling incorrect inputs for now
//        System.out.println("Enter URL to begin crawling");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        url = br.readLine();
//
//        System.out.println("Enter total number of URLs to crawl");
//        crawlSize = Integer.parseInt(br.readLine());

//        String url = "http://www.google.com";
//
//        String page = getHtml(url);
//        String page = getHtmlUsingJsoup(url);

//        Set<String> pageLinks = getAllLinks(url);
//        for (String s : pageLinks) {
//            System.out.println(s);
//        }

        url = "https://www.google.com";
        crawlSize = 1000;

        Set<String> crawledLinks = crawlWeb(url, crawlSize);

        for (String link : crawledLinks) {
            System.out.println(link);
        }
    }


    //
    //  Using standard java libraries to read html contents of a page
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

    //
    //  Crawls the web starting from origin url in a DFS manner using a stack
    //
    static Set<String> crawlWeb(String origin, int limit) {
        Set<String> crawled = new HashSet<String>();    //maintains a list of unique urls crawled
        Stack<String> toCrawl = new Stack<String>();
        Set<String> links;
        String url;

        toCrawl.push(origin);

        while (!toCrawl.isEmpty() || crawled.size() < limit) {
            url = toCrawl.pop();

            if (!crawled.contains(url)) {   //crawl if not already crawled

                // To crawl - grab the links in that page add it to the stack
                links = getAllLinks(url);

                // Should probably check for duplicates here as well
                for (String link : links) {
                    if (!crawled.contains(link)) {
                        toCrawl.push(link);
                    }
                }

                crawled.add(url);
                System.out.println("crawled: " + url);
            }
        }

        return crawled;
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
            doc = Jsoup.connect(url).timeout(3*1000).get();
            elements = doc.select("a");
        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("attempting to connect to: " + url);
        }

        for (Element a : elements) {
            attribute = a.attr("href");

            //handle cases with http and https missing
            //for now ignore the url that doesn't have http/https
            if (attribute.length() >= 5 && (attribute.substring(0,4).contains("http") ||
                    attribute.substring(0,5).contains("https"))) {

                pageLinks.add(attribute);
//                System.out.println("added url : " + attribute);
            }

        }

        return pageLinks;
    }






}
