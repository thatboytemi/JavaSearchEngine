# Java Search Engine
Developed an application using Java and the Spring Boot framework. The application makes use of the Standford NLP library (to tokenize the body of web pages and search terms) and the JSoup library (for web scraping). Upon running the app, users can provide URLs of webpages they want indexed and then users can search for terms and be provided with a list of webpages that contain these terms. The application does this by exposing 2 api endpoints: /search and /crawl.
## How to run:
First ensure that you that you have added the Stanford NLP library as a dependency: https://stanfordnlp.github.io/CoreNLP/

Also ensure that you have added the JSoup JAR as a dependency: https://jsoup.org/

Run the file called "JavaSearchEngineApplication.java".

Then either in your web browser or preferably in an application like Postman:
1) to index a website: type "http://localhost:8080/crawl?url={url of site you want crawled}"
2) to search for something: type "http://localhost:8080/search?query={url of site you want crawled}"

Crawling takes time, give it a minute or so.
When searching, expected response is a list of urls corresponding to the search terms.
