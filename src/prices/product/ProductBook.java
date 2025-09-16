package prices.product;

import prices.*;
import prices.product.users.DataValidationException;
import prices.product.users.publications.CurrentMarketPublisher;
import prices.product.users.publications.CurrentMarketTracker;

public class ProductBook {
    private final String product;
    private final ProductBookSide buySide;
    private final ProductBookSide sellSide;

    public ProductBook(String product) throws InvalidReference, DataValidationException {
        validateProduct(product);
        this.product = product;
        this.buySide =  new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }
    private void validateProduct (String product) throws InvalidReference {
        if (product == null || product.length() < 1 || product.length() > 5) {
            throw new InvalidReference("Invalid Product symbol");

        }
        for (int i = 0; i < product.length(); i++) {
            char ch = product.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '.') {
                throw new InvalidReference("Invalid Product symbol");
            }

        }
    }
    public String getProduct(){
        return product;
    }

    public ProductBookSide getBuySide(){
        return buySide;
    }

    public ProductBookSide getSellSide(){
        return sellSide;
    }
    public TradableDTO add(Tradable t) throws InvalidPriceException, InvalidReference, DataValidationException {
        //System.out.println("**ADD: " + t);
        if(t == null){
            throw new InvalidPriceException("Cannot be null");
        }
        ProductBookSide side;
        //TradableDTO dto;
        if(t.getSide() !=  null && t.getSide() == BookSide.BUY){
            side = buySide;
            //dto = side.add(t);
        }else{
            side = sellSide;
        }
        //ProductBookSide side = t.getSide();
        TradableDTO dto = side.add(t);

        tryTrade();
        updateMarket();
        return dto;

    }
    public TradableDTO[] add(Quote qte) throws InvalidReference, InvalidPriceException, DataValidationException{
        if(qte == null){
            throw new InvalidReference("Cannot be null");
        }
        removeQuotesForUser(qte.getUser());
        TradableDTO buyDTO = buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sellDTO = sellSide.add(qte.getQuoteSide(BookSide.SELL));

        tryTrade();
        return new TradableDTO[] {buyDTO, sellDTO};


    }

    public TradableDTO cancel(BookSide side, String orderId) throws InvalidReference, DataValidationException, InvalidPriceException {
        if(orderId == null || orderId.isEmpty()){
            throw new InvalidReference("OrderId is Invalid");
        }
        ProductBookSide bookSide;
        if(side == BookSide.BUY){
            bookSide = buySide;
        }else if(side == BookSide.SELL){
            bookSide = sellSide;
        }else{
            throw new InvalidReference(("Invalid Book"));

        }
        TradableDTO dto = bookSide.cancel(orderId);
        //updateMarket();
        if (dto == null){
            throw new InvalidReference(" Cannot be null");
        }
        updateMarket();


        return dto;

    }
    public TradableDTO[] removeQuotesForUser(String userName) throws InvalidReference, DataValidationException, InvalidPriceException {
        if(userName == null){
            throw new InvalidReference("UserName cannot be null");
        }
        TradableDTO removeBuy = buySide.removeQuotesForUser(userName);
        TradableDTO removeSell = sellSide.removeQuotesForUser(userName);
        updateMarket();
        return new TradableDTO[] {removeBuy, removeSell};


    }
    public void tryTrade() throws InvalidPriceException, InvalidReference, DataValidationException {
        while(true) {
            Price topBuy = buySide.topOfBookPrice();
            Price topSell = sellSide.topOfBookPrice();

            if (topBuy == null || topSell == null) {
                return;
            }
            if (topSell.greaterThan(topBuy)) {
                return;

            }
            int totalToTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());

            if (totalToTrade <= 0) {
                return;
            }
            buySide.tradeOut(topSell, totalToTrade);
            sellSide.tradeOut(topBuy, totalToTrade);
        }
//            Price topBuy = buySide.topOfBookPrice();
//            Price topSell = sellSide.topOfBookPrice();
//
//            if (topBuy == null || topSell == null) {
//                return;
//            }
//
//
//        // Calculate totalToTrade as the max of buy & sell top volumes
//        int totalToTrade = Math.max(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
//
//
//        if (totalToTrade <= 0) {
//            return;
//        }
//
//        //  Check price conditions; if topSell > topBuy, no trade happens
//        if (topSell.greaterThan(topBuy)) {
//            return;
//        }
//
//
//        int toTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
//
//
//        sellSide.tradeOut(topBuy, toTrade); // Sell side trades at buy price
//        buySide.tradeOut(topSell, toTrade); // Buy side trades at sell price



//        Price topBuy = buySide.topOfBookPrice();
//        Price topSell = sellSide.topOfBookPrice();
//
//        if (topBuy == null || topSell == null) {
//            return;
//        }
//        int totalToTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
//
//        if (totalToTrade > 0) {
//            //int toTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
//
//            buySide.tradeOut(topSell, totalToTrade);
//            sellSide.tradeOut(topBuy, totalToTrade);
//
//        }
    }

    public String getTopOfBookString(BookSide side){
        Price price;
        int volume;

        if(side == BookSide.BUY){
            price = buySide.topOfBookPrice();
            volume = buySide.topOfBookVolume();
            //if(price != null &&  price.getCents() > 0 && volume > 0){
            if (price == null){
                return "Top of BUY book: $0.00 x 0";

                //return ("Top of BUY book: " + price + " x " + volume);
            }
            return ("Top of BUY book: " + price + " x " + volume);
        }else {
            price = sellSide.topOfBookPrice();
            volume = sellSide.topOfBookVolume();
            //if(price !=null && price.getCents() > 0 && volume > 0){
            if(price == null){
                return "Top of SELL book: $0.00 x 0";
                //return ("Top of SELL book: " + price + " x " + volume);
            }
            return ("Top of SELL book: " + price + " x " + volume);
        }
        //return "no top";
    }
    private void updateMarket() throws InvalidPriceException {
        Price topBuyPrice = buySide.topOfBookPrice();
        int topBuyVolume = buySide.topOfBookVolume();
        Price topSellPrice = sellSide.topOfBookPrice();
        int topSellVolume = sellSide.topOfBookVolume();

        CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, topBuyVolume, topSellPrice, topSellVolume);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product Book: ").append(product).append("\n");
        sb.append(buySide.toString());
        sb.append(sellSide.toString());
        return sb.toString();
//        StringBuilder sb = new StringBuilder();
//        sb.append("Product: ").append(product).append("\n");
//        sb.append("Side: BUY\n");
//        sb.append(buySide.toString().isEmpty() ? "\t<Empty>\n" : buySide.toString()).append("\n");
//        sb.append("Side: SELL\n");
//        sb.append(sellSide.toString().isEmpty() ? "\t<Empty>" : sellSide.toString());
//        return sb.toString();
    }
//        StringBuilder sb = new StringBuilder();
//        sb.append("Product: ").append(product).append("\n");
//        sb.append("Side: BUY\n");
//        sb.append(buySide.toString().isEmpty() ? "\t<Empty>\n" : buySide.toString()).append("\n");
//        sb.append("Side: SELL\n");
//        sb.append(sellSide.toString().isEmpty() ? "\t<Empty>" : sellSide.toString());
//        return sb.toString();
//    }

}







