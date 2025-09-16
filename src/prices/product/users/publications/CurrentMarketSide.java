package prices.product.users.publications;

import prices.InvalidPriceException;
import prices.Price;
import prices.product.BookSide;
import prices.product.InvalidReference;
import prices.product.ProductBookSide;


public class CurrentMarketSide {
    private final Price price;
    private final int volume;

    public CurrentMarketSide(Price price, int volume) {
        this.price = price;
        this.volume = volume;

    }

    @Override
    public String toString() {
        return price + "x" + volume;
//        return "CurrentMarketSide{" +
//                "price=" + price +
//                ", volume=" + volume +
//                '}';
    }
    //    @Override
//    public String ToString(){
//        return price.toString() + "x" + volume;



}

