package de.uniol.inf.is.odysseus.wrapper.webcrawler.physicaloperator.access;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.webcrawler.util.WebCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class WebCrawlerTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> 
{
	
	//Names for the options
		public static final String NAME = "WebCrawler";
		public static final String DEPTH = "depth";
		public static final String WEBSITES = "site";
		public static final String FETCH = "fetch";
		
		public String depth;
		public String sites;
		public String fetch;
		
		private ArrayList<String> urls = new ArrayList<>();
		private ArrayList<String> crawledText = new ArrayList<>();
		
		private WebCrawler crawler; 
		private CrawlController controller = null;
		private CrawlConfig config;
		private PageFetcher pageFetcher;
		private RobotstxtConfig robotstxtConfig;
		private RobotstxtServer robotstxtServer;
		
		public WebCrawlerTransportHandler() {
			super();
		}
		
		public WebCrawlerTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
			super(protocolHandler, options);
			init(options);
		}

		public void init(OptionMap options) {
			if (options.containsKey(DEPTH)) {
				depth = options.get(DEPTH);
			}
			
			if (options.containsKey(WEBSITES)) {
				sites = options.get(WEBSITES);
				
				String[] delimit = sites.split("§");
				
				for(int i=0; i<delimit.length; i++)
					urls.add(delimit[i]);
			}
				
			if (options.containsKey(FETCH)) {
				fetch = options.get(FETCH);
			}
		}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options)
	{
		final WebCrawlerTransportHandler handler = new WebCrawlerTransportHandler(protocolHandler, options);
		return handler;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

	@Override
	public boolean hasNext() {
		
		initializeCrawler();
		
		if(this.crawledText != null)
			return true;
		
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getNext() {

		Tuple<?> tuple = new Tuple(2, false);
	
		tuple.setAttribute(0, sites);
		tuple.setAttribute(1,this.crawledText.toString());
		
		//this.crawledText.clear();
		
		return tuple;
	}
	
	public void initializeCrawler()
	{	
		String crawlStorageFolder = System.getProperty("user.home");
        int numberOfCrawlers =1;

        this.config = new CrawlConfig();
        this.config.setCrawlStorageFolder(crawlStorageFolder);
        this.config.setMaxDepthOfCrawling(Integer.parseInt(this.depth));
        this.config.setMaxPagesToFetch(Integer.parseInt(this.fetch));
        
        this.pageFetcher = new PageFetcher(this.config);
        this.robotstxtConfig = new RobotstxtConfig();
        this.robotstxtServer = new RobotstxtServer( this.robotstxtConfig,  this.pageFetcher);
       
        
		try {
			this.controller = new CrawlController( this.config,  this.pageFetcher,  this.robotstxtServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(String str : this.urls)
			this.controller.addSeed(str);
		
		this.crawler = new WebCrawler();
		this.controller.start(WebCrawler.class, numberOfCrawlers);
		
		this.crawledText.clear();
		this.crawledText = crawler.getText();
		
		trimCrawledText();
		
		this.controller.shutdown();
		this.controller.waitUntilFinish();
	}

	private void trimCrawledText() 
	{
		ArrayList<String> tempList = new ArrayList<String>();
		
		for(String str : this.crawledText)
		{
			StringBuffer textToTrim = new StringBuffer(str);
			
			int counter = 0;
			boolean check = true;
			
			while(counter<textToTrim.length())
			{
				if(textToTrim.charAt(counter) == '\n')
				{
					textToTrim.deleteCharAt(counter);
					check = false;
				}
				
				if(textToTrim.charAt(counter) == '\t')
				{
					textToTrim.deleteCharAt(counter);
					check = false;
				}
				
				if(textToTrim.charAt(counter) == '\r')
				{
					textToTrim.deleteCharAt(counter);
					check = false;
				}
			
				if(check)
					counter++;
				
				check = true;
			}	
			str = textToTrim.toString().trim();
			tempList.add(str);	
		}
		this.crawledText = tempList;
	}
}
