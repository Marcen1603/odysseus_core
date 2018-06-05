package de.uniol.inf.is.odysseus.wrapper.webcrawler.util;

//import java.io.File;
import java.util.ArrayList;
//import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class WebCrawler extends edu.uci.ics.crawler4j.crawler.WebCrawler {
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
        + "|png|tiff?|mid|mp2|mp3|mp4"
        + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
        + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static ArrayList<String> savedText = new ArrayList<>();
	HtmlParseData htmlParseData;	
	
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		
		
		System.out.println("--------ANFANG---------------");
	 	System.out.println("URL:" + url.getURL());
		System.out.println("Depth:" + url.getDepth());
		System.out.println("Domain:" + url.getDomain());
		System.out.println("ParentURL:" + url.getParentUrl());
		System.out.println("Path" + url.getPath());
		System.out.println("--------ENDE---------------"); 
		
		return !FILTERS.matcher(href).matches() && href.startsWith(url.getURL());
	}
	
	@SuppressWarnings("unused")
	@Override
	public void visit(Page page) {          
		String url = page.getWebURL().getURL();
		//System.out.println("URL: " + url);
	
	if (page.getParseData() instanceof HtmlParseData) {
			this.htmlParseData = (HtmlParseData) page.getParseData();
			String text = null;				
		
			text = htmlParseData.getText();
			savedText.add(text);
			String html = htmlParseData.getHtml();
			java.util.List<WebURL> links = htmlParseData.getOutgoingUrls();
			
		//	System.out.println("Text: " + text);
			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}
	}

	public HtmlParseData getParseData()
	{
		return this.htmlParseData;
	}
	
	public ArrayList<String> getText()
	{
		return savedText;
	}
	
	public void deleteText()
	{
		savedText.clear();
	}
	
	@Override
	public Object getMyLocalData() {
		return super.getMyLocalData();
	}
}
