package prices.product;

import prices.InvalidPriceException;
import prices.Price;
import prices.product.users.DataValidationException;
import prices.product.users.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class ProductBookSide {
    private final BookSide side;
    private final TreeMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side) throws InvalidReference, DataValidationException {
        if (side == null) {
            throw new InvalidReference("Side cannot be null");

        }
        this.side = side;
        if (side == BookSide.BUY) {
            this.bookEntries = new TreeMap<>(Comparator.reverseOrder());
        } else {
            this.bookEntries = new TreeMap<>();
        }
    }

    public BookSide getSide() {
        return side;
    }

    public TradableDTO add(Tradable o) throws InvalidReference, DataValidationException {
        if (o == null) {
            throw new InvalidReference("Cannot be null");
        }
        Price price = o.getPrice();
        if (!bookEntries.containsKey(price)) {
            bookEntries.put(price, new ArrayList<>());
        }
        bookEntries.get(price).add(o);
        //System.out.println("**ADD: " + o.toString());

        TradableDTO addedDTO = o.makeTradableDTO();
        UserManager.getInstance().updateTradable(o.getUser(), addedDTO);

        return addedDTO;
    }

    public TradableDTO cancel(String tradableId) throws InvalidReference, DataValidationException {
        Price[] prices = bookEntries.keySet().toArray(new Price[0]);
        for (int p = 0; p < prices.length; p++) {
            Price price = prices[p];
            ArrayList<Tradable> tradables = bookEntries.get(price);

            for (int i = 0; i < tradables.size(); i++) {
                Tradable t = tradables.get(i);

                if (t.getId().equals(tradableId)) {
                   // System.out.println("**CANCEL: " + t);

                    t.setCancelledVolume(t.getRemainingVolume());
                    t.setRemainingVolume(0);
                    tradables.remove(i);

                    if (tradables.isEmpty()) {
                        bookEntries.remove(price);
                    }
                    TradableDTO cancelledDTO = t.makeTradableDTO();
                    UserManager.getInstance().updateTradable(t.getUser(), cancelledDTO);
                    return cancelledDTO;
                }
            }
        }

        return null;
    }

    public TradableDTO removeQuotesForUser(String userName) throws InvalidReference, DataValidationException {
        Price[] prices = bookEntries.keySet().toArray(new Price[0]);
        for (int p = 0; p < prices.length; p++) {
            Price price = prices[p];
            ArrayList<Tradable> tradables = bookEntries.get(price);

            for (int i = 0; i < tradables.size(); i++) {
                Tradable t = tradables.get(i);
                if (t.getUser().equals(userName)) {
                    TradableDTO removedTQuote = cancel(t.getId());
                    if (tradables.isEmpty()) {
                        bookEntries.remove(price);
                    }
                    UserManager.getInstance().updateTradable(t.getUser(), removedTQuote);
                    return removedTQuote;
                }

            }
        }
        return null;
    }

    public Price topOfBookPrice() {
        if (bookEntries.isEmpty()) {
            return null;
        }
        return bookEntries.firstKey();

    }

    public int topOfBookVolume() {
        if (bookEntries.isEmpty()) {
            return 0;
        }
        Price topPrice = bookEntries.firstKey();

        ArrayList<Tradable> tradables = bookEntries.get(topPrice);

        int totalVolume = 0;

        for (int i = 0; i < tradables.size(); i++) {
            totalVolume += tradables.get(i).getRemainingVolume();
        }
        return totalVolume;

//        if(side == BookSide.BUY){
//            topPrice = bookEntries.lastKey();
//
//        }else if (side == BookSide.SELL){
//            topPrice = bookEntries.firstKey();
//        }else{
//            return 0;
//        }
//        ArrayList<Tradable> tradables = bookEntries.get(topPrice);
//        int totalVolume = 0;
//
//        for(int i = 0; i < tradables.size(); i++){
//            totalVolume +=tradables.get(i).getRemainingVolume();
//
//        }
//
    }

    public void tradeOut(Price price, int volToTrade) throws InvalidPriceException, InvalidReference, DataValidationException {


        Price topPrice = topOfBookPrice();

        if (topPrice == null || price.greaterThan(price)) {
            return;
        }
        ArrayList<Tradable> atPrice = bookEntries.get(topPrice);
        int totalVolAtPrice = 0;
        for (Tradable o : atPrice) {
            totalVolAtPrice += o.getRemainingVolume();
        }

        if (volToTrade >= totalVolAtPrice) {
            // enough to trade out all at price
            for (Tradable t : atPrice) {
                int rv = t.getRemainingVolume();
                t.setFilledVolume(t.getOriginalVolume());
                t.setRemainingVolume(0);
                System.out.println("\tFULL FILL: (" + t.getSide() + " " + rv + ") " + t);
                TradableDTO orderDTO = t.makeTradableDTO();
                UserManager.getInstance().updateTradable(t.getUser(), orderDTO);
            }
            bookEntries.remove(topPrice);
        } else {
            // not enough to trade out all at price
            int remainder = volToTrade;
            for (Tradable t : atPrice) {
                double ratio = (double) t.getRemainingVolume() / totalVolAtPrice;
                int toTrade = (int) Math.ceil(volToTrade * ratio);

                toTrade = Math.min(toTrade, remainder);
                t.setFilledVolume(t.getFilledVolume() + toTrade);
                t.setRemainingVolume(t.getRemainingVolume() - toTrade);
                System.out.println("\tPARTIAL FILL: (" + t.getSide() + " " + toTrade + ") " + t);
                remainder -= toTrade;
                TradableDTO orderDTO = t.makeTradableDTO();

                UserManager.getInstance().updateTradable(t.getUser(), orderDTO);
            }
        }

    }



//    public void tradeOut(Price price, int vol) throws InvalidReference {
//
//        int remainingVol = vol;
//
//        while(!bookEntries.isEmpty() && remainingVol>0) {
//            Price topPrice = topOfBookPrice();
//
//            if (topPrice == null || (side == BookSide.BUY && topPrice.compareTo(price) < 0) ||
//                    (side == BookSide.SELL && topPrice.compareTo(price) > 0)) {
//                return;
//
//            }
//            ArrayList<Tradable> tradablesAtPrice = bookEntries.get(topPrice);
//            int totalVolAtPrice = 0;
//            //tradablesAtPrice.sort((t1, t2) -> Integer.compare(t1.getRemainingVolume(), t2.getRemainingVolume()));
//
//
//            for (int i = 0; i < tradablesAtPrice.size(); i++) {
//
//                totalVolAtPrice += tradablesAtPrice.get(i).getRemainingVolume();
//
//
//            }
//
//            if (remainingVol >= totalVolAtPrice) {
//                for (int i = 0; i < tradablesAtPrice.size()  ; i++) {
//                    Tradable t = tradablesAtPrice.get(i);
//                    t.setFilledVolume(t.getOriginalVolume());
//                    t.setRemainingVolume(0);
//
//
//                    System.out.println("FULL FILL: (" + side + " " + t.getOriginalVolume() + ") " + t);
//
//
//                }
//
//                bookEntries.remove(topPrice);
//                remainingVol -= totalVolAtPrice;
//
//
//            } else {
//                double ratio = (double) remainingVol / totalVolAtPrice;
//                for (int i =0; i < tradablesAtPrice.size() ; i++) {
//                    Tradable t = tradablesAtPrice.get(i);
//                    int toTrade = (int) Math.ceil(ratio * t.getRemainingVolume());
//                    toTrade = Math.min(toTrade, t.getRemainingVolume());
//                    t.setFilledVolume(t.getFilledVolume() + toTrade);
//                    t.setRemainingVolume(t.getRemainingVolume() - toTrade);
//
//                    if(toTrade == t.getRemainingVolume()){
//                        System.out.println("FULL FILL: (" + side + " " + t.getRemainingVolume() + ") " + t);
//                    } else {
//                        System.out.println("PARTIAL FILL: (" + side + " " + toTrade + ") " + t);
//                    }
//
//
//                    //System.out.println("PARTIAL FILL: (" + side + " " + t.getFilledVolume() + ") " + t);
//                    remainingVol -= toTrade;
//                    if (remainingVol <= 0) {
//                        break;
//                    }
//
//                }
//                //Remove fully filled tradables
//                for (int i = 0; i < tradablesAtPrice.size(); i++) {
//                    if (tradablesAtPrice.get(i).getRemainingVolume() == 0) {
//                        tradablesAtPrice.remove(i);
//                        i--; // Adjust the index after removal
//                    }
//                }
//
//                // If no tradables left at this price, remove the price level
//                if (tradablesAtPrice.isEmpty()) {
//                    bookEntries.remove(topPrice);
//                }
//            }
//        }
//
//
//    }

    @Override
    public String toString() {
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Side: ").append(side).append("\n");

            if (bookEntries.isEmpty()) {
                sb.append("\t<Empty>\n");
            } else {
                for (Price price : bookEntries.keySet()) {
                    sb.append("\t").append(price).append(":\n");

                    ArrayList<Tradable> tradables = bookEntries.get(price);
                    for (Tradable tradable : tradables) {
                        sb.append("\t\t").append(tradable.toString()).append("\n");
                    }
                }
            }

            return sb.toString();
        }


//        StringBuilder sb = new StringBuilder();
//
//        if (bookEntries.isEmpty()) {
//            sb.append("\t<Empty>\n");
//        } else {
//            for (Price price : bookEntries.keySet()) {
//                sb.append("Price: ").append(price).append("\n");
//
//                // Iterate over all tradables at this price
//                for (Tradable tradable : bookEntries.get(price)) {
//                    // Customize the format to match your expected output
//                    String side = tradable.getSide() == BookSide.BUY ? "BUY" : "SELL";
//                    sb.append("\t").append(side)
//                            .append(" order: TGT at ").append(price)
//                            .append(", Orig Vol: ").append(tradable.getOriginalVolume())
//                            .append(", Rem Vol: ").append(tradable.getRemainingVolume())
//                            .append(", Fill Vol: ").append(tradable.getFilledVolume())
//                            .append(", CXL Vol: ").append(tradable.getCancelledVolume())
//                            .append(", ID: ").append(tradable.getId())  // Ensure the ID is formatted as expected
//                            .append("\n");
//                }
//            }
//        }
//        return sb.toString();
    }

//        StringBuilder sb = new StringBuilder();
//        if (bookEntries.isEmpty()) {
//            sb.append("\t<Empty>\n");
//        } else {
//            for (Price price : bookEntries.keySet()) {
//                sb.append("Price: ").append(price).append("\n");
//                for (Tradable tradable : bookEntries.get(price)) {
//                    sb.append("\t").append(tradable).append("\n");
//                }
//            }
//        }
//        return sb.toString();
//    }
}




