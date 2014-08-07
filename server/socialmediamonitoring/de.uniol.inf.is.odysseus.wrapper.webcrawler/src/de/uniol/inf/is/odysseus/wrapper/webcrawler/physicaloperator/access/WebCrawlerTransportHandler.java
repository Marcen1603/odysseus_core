package de.uniol.inf.is.odysseus.wrapper.webcrawler.physicaloperator.access;

import java.util.ArrayList;
import java.util.Map;

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
		public static final String WEBSITES = "sites";
		public static final String FETCH = "fetch";
		
		public String depth;
		public String sites;
		public String fetch;
		
		ArrayList<String> crawledText = new ArrayList<>();
		ArrayList<String> liste = new ArrayList<>();
		
		public WebCrawlerTransportHandler() {
			super();
		}
		
		public WebCrawlerTransportHandler(final IProtocolHandler<?> protocolHandler, Map<String, String> options) {
			super(protocolHandler, options);
			init(options);
		}

		public void init(Map<String, String> options) {
			if (options.containsKey(DEPTH)) {
				depth = options.get(DEPTH);
			}
			
			if (options.containsKey(WEBSITES)) {
				sites = options.get(WEBSITES);
			}
				
			if (options.containsKey(FETCH)) {
				fetch = options.get(FETCH);
			}
		}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options)
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
		
		liste = initializeCrawler();
		
		if(liste != null)
			return true;
		
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getNext() {

		Tuple<?> tuple = new Tuple(2, false);
	
		tuple.setAttribute(0, sites);
		tuple.setAttribute(1,liste.toString());
		
		return tuple;
	}
	
	public ArrayList<String> initializeCrawler()
	{
		String crawlStorageFolder = System.getProperty("user.home");
        int numberOfCrawlers = 1;

     //   this.text.clear();       
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(Integer.parseInt(this.depth));
        config.setMaxPagesToFetch(Integer.parseInt(this.fetch));
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
        
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		controller.addSeed(this.sites);
		WebCrawler crawler = new WebCrawler();
		controller.start(WebCrawler.class, numberOfCrawlers);
								
		this.crawledText = crawler.getText();
		
		for(String str : crawledText){
			str.trim().equals("");
			str.trim().equals("\t");
			str.trim();
			System.out.println("TEXT: " + str);
		}
		
		controller.shutdown();
		
		return this.crawledText;
	}
}
