package prices.product.users.publications;

import prices.InvalidPriceException;
import prices.Price;
import prices.PriceFactory;
import prices.product.ProductBookSide;
import prices.PriceFactory;

public class CurrentMarketTracker {
    private static CurrentMarketTracker instance;
    private CurrentMarketTracker(){

    }
    public static CurrentMarketTracker getInstance(){
        if (instance == null){
            instance = new CurrentMarketTracker();
        }
        return instance;
    }

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidPriceException {
        //Price marketWidth;
        if(buyPrice == null){
            //buyPrice = new Price(0);
            buyPrice = new Price(0);
            buyVolume =0;
            //marketWidth = sellPrice.subtract(buyPrice);

        }if (sellPrice == null){
            sellPrice = new Price(0);
            sellVolume = 0;
            //marketWidth = new Price(0);
        }
        Price marketWidth = new Price(0);
        if(buyPrice.getCents() > 0 && sellPrice.getCents()>0){
            marketWidth = sellPrice.subtract(buyPrice);
        }


        CurrentMarketSide buySide = new CurrentMarketSide (buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice,sellVolume);

        System.out.println("*********** Current Market ***********");
        System.out.printf("* %-6s %s - %s [%s]%n", symbol, buySide, sellSide, marketWidth);
        System.out.println("**************************************");

        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buySide, sellSide);


    }


}
