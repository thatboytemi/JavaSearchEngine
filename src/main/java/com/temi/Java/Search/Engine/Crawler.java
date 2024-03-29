package com.temi.Java.Search.Engine;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static Indexer indexer;
    Crawler(Indexer indexer){
        this.indexer = indexer;
    }
    public static void crawl(String url, int depth, HashSet<String> visited){
        if (depth <=2){
            Document doc = request(url, visited);
            if (doc!=null){
                int width = 0;
                for(Element link:doc.select("a[href]")){
                    if(width>3) break;
                    String next = link.absUrl("href");
                    if(!visited.contains(next)){
                        crawl(next, depth++, visited);
                    }
                    width++;
                }
                executorService.submit(() -> indexer.index(doc));
            }
        }
    }

    public static Document request(String url, HashSet<String> visited) {
        try{
            Connection conn = Jsoup.connect(url).userAgent("mozilla/17.0");
            Document doc = conn.get();
            if(conn.response().statusCode()==200){
                System.out.println(doc.title());
                System.out.println("Link: "+doc.baseUri());
                visited.add(url);
                return doc;
            }
            return null;
        }
        catch (IOException e){
            return null;
        }

    }
}
