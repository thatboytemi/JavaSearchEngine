package com.temi.Java.Search.Engine;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@RestController
public class Controller {
    private static final Indexer indexer = new Indexer();
    private static final Crawler crawler = new Crawler(indexer);
    private static final HashSet<String> visited = new HashSet<>();
    @GetMapping("/crawl")
    public String crawl(@RequestParam String url){
        crawler.crawl(url, 1, visited);
        return "Page successfully indexed";
    }
    @GetMapping("/search")
    public String search(@RequestParam String query){
        String [] terms = query.split(" ");
        ArrayList<Document> documents = new ArrayList<>();
        for(String term:terms){
            documents.addAll((Collection<? extends Document>) indexer.search(term));
        }
        final String[] res = {""};
        documents.forEach((doc)->{
            res[0] += doc.title() +" : "+ doc.baseUri()+"\n";
        });
        if(res[0].length()==0){
            return "Nothing found :(";
        }
        return res[0];
    }
}
