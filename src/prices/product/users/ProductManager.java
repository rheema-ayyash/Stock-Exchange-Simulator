package prices.product.users;

import prices.InvalidPriceException;
import prices.product.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ProductManager {
    private static ProductManager instance = null;
    private HashMap<String, ProductBook> productBooks = new HashMap<>();

    private ProductManager(){
        productBooks = new HashMap<>();
    }
    public static ProductManager getInstance(){
        if(instance == null){
            instance = new ProductManager();
        }
        return instance;
    }

    private void validateProductSymbol(String symbol) throws DataValidationException {
        if (symbol == null || symbol.length() < 1 || symbol.length() > 5) {
            throw new DataValidationException("Invalid product symbol length.");
        }

        for (int i = 0; i < symbol.length(); i++) {
            char ch = symbol.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '.') {
                throw new DataValidationException("Invalid product symbol format.");
            }
        }
    }

    public void addProduct(String symbol) throws DataValidationException, InvalidReference {
        validateProductSymbol(symbol);



        //if (productBooks.containsKey(symbol)) {
        ProductBook newProductBook = new ProductBook(symbol);
        productBooks.put(symbol, newProductBook);
        //}
    }


        public ProductBook getProductBook(String symbol) throws DataValidationException{
        if (symbol ==  null || !productBooks.containsKey(symbol)){
            throw new DataValidationException("ProductBook doesn't exist");
        }
        return productBooks.get(symbol);
    }

     public String getRandomProduct()throws DataValidationException{
        if(productBooks.isEmpty()){
            throw new DataValidationException(("No books"));
        }
        List<String> keys = new ArrayList<>(productBooks.keySet());
        Random random = new Random();
        return keys.get(random.nextInt(keys.size()));

    }
    public TradableDTO addTradable(Tradable o) throws DataValidationException, InvalidPriceException, InvalidReference {
        if(o == null){
            throw new DataValidationException("Invalid tradable");
        }
        String symbol = o.getProduct();
        //ProductBook productBook = getProductBook(symbol);
//        if(!productBooks.containsKey(symbol)){
//            throw new DataValidationException(("Invalid Product"));
//        }
        //ProductBook productBook = productBooks.get(symbol);
        ProductBook productBook = getProductBook(symbol);
        TradableDTO tradableDTO = productBook.add(o);
        //productBook.tryTrade();

        //UserManager userManager = UserManager.getInstance();
        UserManager.getInstance().updateTradable(o.getUser(), tradableDTO);
        return tradableDTO;


    }
    public TradableDTO[] addQuote(Quote q) throws DataValidationException, InvalidReference, InvalidPriceException {
        if(q == null){
            throw new DataValidationException("Quote cannot be null");
        }
        String symbol = q.getSymbol();
        String user = q.getUser();

        ProductBook productBook = getProductBook(symbol);
        productBook.removeQuotesForUser(user);

        TradableDTO buyTDTO = productBook.add(q.getQuoteSide(BookSide.BUY));
        TradableDTO sellTDTO = productBook.add(q.getQuoteSide(BookSide.SELL));


       // return new TradableDTO[] {buyTDTO, sellTDTO};


        UserManager.getInstance().updateTradable(q.getUser(), buyTDTO); // Update the user's tradable
        UserManager.getInstance().updateTradable(q.getUser(), sellTDTO); // Update the user's tradable

        return new TradableDTO[] { buyTDTO, sellTDTO };


    }
    public TradableDTO cancel(TradableDTO o) throws DataValidationException, InvalidReference, InvalidPriceException {
        if(o==null){
            throw new DataValidationException("Cannot be null");
        }
        String symbol = o.product();
        ProductBook productBook = getProductBook(symbol);

//        TradableDTO cancelledDTO = productBook.cancel(o.side());
//        return cancelledDTO;

//        try{
//
//            productBook = getProductBook(symbol);
//        }catch (DataValidationException e){
//            throw new DataValidationException( "Failure to cancel");
//
//        }
        TradableDTO result = productBook.cancel(o.side(),o.tradableId());

        if(result == null){
            System.out.println("Failure to cancel");
            return null;
        }
        return result;


    }
    public TradableDTO[] cancelQuote(String symbol, String user) throws DataValidationException, InvalidReference, InvalidPriceException {
        if(symbol == null || user == null){
            throw new DataValidationException("Invalid data");
        }

        ProductBook productBook = getProductBook(symbol);
        if (productBook == null){
            throw new DataValidationException("Invalid data");
        }
        TradableDTO[] removed = productBook.removeQuotesForUser(user);

        return removed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ProductBook productBook : productBooks.values()) {
            sb.append("Product Book: ").append(productBook.getProduct()).append("\n");
            sb.append(productBook.toString());
        }
        return sb.toString();
    }



}
