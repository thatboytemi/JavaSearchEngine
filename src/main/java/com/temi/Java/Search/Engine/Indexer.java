package com.temi.Java.Search.Engine;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.StringUtils;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.stream.Collectors;

public class Indexer {
    private final static Map<String, Set<String>> IN_MEMORY_INDEX = new HashMap<>();
    private final static Map<String, Document> PAGE_MAP = new HashMap<>();

    private StanfordCoreNLP pipeline;
    public static Set<String> STOP_WORDS = Set.of("a", "an", "and", "are", "as", "at", "be", "but", "by",
            "for", "if", "in", "into", "is", "it",
            "no", "not", "of", "on", "or", "s", "such",
            "t", "that", "the", "their", "then", "there", "these",
            "they", "this", "to", "was", "will", "with");
    public Indexer() {
        setup();
    }

    private void setup() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }
    private List<String> tokenize(String corpus) {
        // convert the corpus to lowercase
        Annotation annotation = new Annotation(corpus.toLowerCase());
        pipeline.annotate(annotation);

        // Extract tokens and lemmata from the annotation
        List<CoreLabel> tokens = annotation.get(CoreAnnotations.TokensAnnotation.class);

        // Build a list to store the results
        List<String> tokenized = new ArrayList<>();

        // Append the lemmatized tokens to the list
        for (CoreLabel token : tokens) {
            String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
            tokenized.add(lemma);
        }

        // remove the stop words from the tokenized words
        return tokenized.stream().filter(token -> !invalidToken(token)).collect(Collectors.toList());
    }

    private boolean invalidToken(String token) {
        // if the token is a stop word or a punctuation / numbers or special characters don't consider this as a valid token
        return STOP_WORDS.contains(token) || StringUtils.matches(token, "[\\p{Punct}\\d]+");
    }

    public void generateInvertedIndex(Document document, List<String> tokens) {
        final String documentId = UUID.randomUUID().toString();
        tokens.forEach(token -> {
            if (!IN_MEMORY_INDEX.containsKey(token)) {
                IN_MEMORY_INDEX.put(token, new HashSet<>());
            }
            IN_MEMORY_INDEX.get(token).add(documentId);
        });
        PAGE_MAP.put(documentId, document);
    }
    public void index(Document document) {
        long start = System.currentTimeMillis();
        List<String> tokens = tokenize(document.body().text());
        generateInvertedIndex(document, tokens);
        long end = System.currentTimeMillis();
    }
    public ArrayList<Document> search(String terms){
        terms= terms.toLowerCase();
        List<String> tokenizedTerms = tokenize(terms);
        Set<String> docs = new HashSet<>();
        tokenizedTerms.forEach((val)->{
            Set<String> temp =IN_MEMORY_INDEX.get(val);
            if(temp!=null){
                docs.addAll(temp);
            }
        });
        ArrayList <Document> documents = new ArrayList<>();
        if (!docs.isEmpty()){
            docs.forEach((doc)->{
                documents.add(PAGE_MAP.get(doc));
            });
        }
       return documents;
    }
}


