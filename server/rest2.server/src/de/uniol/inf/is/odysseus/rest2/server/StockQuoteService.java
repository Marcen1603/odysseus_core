package de.uniol.inf.is.odysseus.rest2.server;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.exception.DuplicateSymbolException;
import de.uniol.inf.is.odysseus.rest2.server.exception.SymbolNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/stockquote")
public class StockQuoteService implements Microservice {

    private static final Logger log = LoggerFactory.getLogger(StockQuoteService.class);

    private Map<String, Stock> stockQuotes = new HashMap<>();

    /**
     * Add initial stocks IBM, GOOG, AMZN.
     */
    public StockQuoteService() {
        stockQuotes.put("IBM", new Stock("IBM", "International Business Machines", 149.62, 150.78, 149.18));
        stockQuotes.put("GOOG", new Stock("GOOG", "Alphabet Inc.", 652.30, 657.81, 643.15));
        stockQuotes.put("AMZN", new Stock("AMZN", "Amazon.com", 548.90, 553.20, 543.10));
    }

    @Activate
    protected void activate(BundleContext bundleContext) {
        // Nothing to do
    }

    @Deactivate
    protected void deactivate(BundleContext bundleContext) {
        // Nothing to do
    }


    /**
     * Retrieve a stock for a given symbol.
     * http://localhost:8080/stockquote/IBM
     *
     * @param symbol Stock symbol will be taken from the path parameter.
     * @return
     */
    @GET
    @Path("/{symbol}")
    @Produces({"application/json", "text/xml"})
    @ApiOperation(
            value = "Return stock quote corresponding to the symbol",
            notes = "Returns HTTP 404 if the symbol is not found")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid stock item found"),
            @ApiResponse(code = 404, message = "Stock item not found")})
    public Response getQuote(@PathParam("symbol") String symbol) throws SymbolNotFoundException {
        Stock stock = stockQuotes.get(symbol);
        if (stock == null) {
            throw new SymbolNotFoundException("Symbol " + symbol + " not found");
        }
        return Response.status(Response.Status.OK).entity(stock).build();
    }

    /**
     * Add a new stock.
     * curl -v -X POST -H "Content-Type:application/json" \
     * -d '{"symbol":"BAR","name": "Bar Inc.", \
     * "last":149.62,"low":150.78,"high":149.18,
     * "createdByHost":"10.100.1.192"}' \
     * http://localhost:8080/stockquote
     *
     * @param stock Stock object will be created from the request Json body.
     */
    @POST
    @Consumes("application/json")
    @ApiOperation(
            value = "Add a stock item",
            notes = "Add a valid stock item")
    public void addStock(@ApiParam(value = "Stock object", required = true) Stock stock)
            throws DuplicateSymbolException {
        String symbol = stock.getSymbol();
        if (stockQuotes.containsKey(symbol)) {
            throw new DuplicateSymbolException("Symbol " + symbol + " already exists");
        }
        stockQuotes.put(symbol, stock);
    }

    /**
     * Retrieve all stocks.
     * http://localhost:8080/stockquote/all
     *
     * @return All stocks will be sent to the client as Json/xml
     * according to the Accept header of the request.
     */
    @GET
    @Path("/all")
    @Produces({"application/json", "text/xml"})
    @ApiOperation(
            value = "Get all stocks",
            notes = "Returns all stock items",
            response = Stocks.class,
            responseContainer = "List")
    public Stocks getAllStocks(@Context Request request) {
        request.getHeaders().getRequestHeaders().entrySet().forEach(entry -> log.info(entry.getKey() + "=" + entry
                .getValue()));
        String securityToken = (String) request.getSession().getAttribute("session");
        ISession session = SessionManagement.instance.login(securityToken);
        log.info("Found session "+session.getUser().getName());
        
        if (ExecutorServiceBinding.getExecutor() != null) {
        	IDataDictionaryWritable dd = ExecutorServiceBinding.getExecutor().getDataDictionary(session);
        	log.info("access to dd "+dd.getDatatypeNames());
        }
        
        return new Stocks(stockQuotes.values());
    }

    @Override
    public String toString() {
        return "StockQuoteService-OSGi-bundle";
}
}