package prices.product.users;

import prices.Price;
import prices.product.InvalidReference;
import prices.product.Tradable;
import prices.product.TradableDTO;
import prices.product.users.publications.CurrentMarketObserver;
import prices.product.users.publications.CurrentMarketSide;


import java.util.ArrayList;
import java.util.HashMap;

public class User implements CurrentMarketObserver {
    private final String userId;
    private final HashMap<String, TradableDTO> tradables;
    private final HashMap<String, CurrentMarketSide[]> currentMarkets;

    public User(String userId) throws DataValidationException{
        validateUser(userId);

        this.userId = userId;
        this.tradables = new HashMap<>();
        this.currentMarkets = new HashMap<>();

    }
    private void validateUser(String userId) throws DataValidationException{
        if(userId == null || userId.length() !=3 ){
            throw new DataValidationException("Invalid user code.");

        }
        for(int i = 0; i<userId.length(); i++){
            char ch = userId.charAt(i);
            if(!Character.isLetter(ch)){
                throw new DataValidationException("Invalid user code");
            }
        }
    }

    public void updateTradable(TradableDTO o) throws DataValidationException{
        if(o == null){
            return;
        }
        tradables.put(o.getId(),o);

    }
    public String getUserId(){
        return userId;
    }


    public HashMap<String, TradableDTO> getTradables() {
        return new HashMap<>(tradables);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Id: ").append(userId).append("\n");

        for (TradableDTO tradable : tradables.values()) {
            sb.append("\tProduct: ").append(tradable.product()).append(", ")
                    .append("Price: ").append((tradable.price())).append(", ")
                    .append("OriginalVolume: ").append(tradable.originalVolume()).append(", ")
                    .append("RemainingVolume: ").append(tradable.remainingVolume()).append(", ")
                    .append("CancelledVolume: ").append(tradable.cancelledVolume()).append(", ")
                    .append("FilledVolume: ").append(tradable.filledVolume()).append(", ")
                    .append("User: ").append(tradable.user()).append(", ")
                    .append("Side: ").append(tradable.side()).append(", ")
                    .append("Id: ").append(tradable.tradableId()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        currentMarkets.put(symbol, new CurrentMarketSide[] {buySide, sellSide});

    }

    public String getCurrentMarkets(){
        StringBuilder sb = new StringBuilder();
        for(String symbol : currentMarkets.keySet()){
            CurrentMarketSide[] market = currentMarkets.get(symbol);
            sb.append(symbol).append("  ").append(market[0]).append(" - ").append(market[1]).append("\n");
        }
        return sb.toString();
    }

}
