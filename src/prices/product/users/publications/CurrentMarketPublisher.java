package prices.product.users.publications;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentMarketPublisher {
    private static CurrentMarketPublisher instance;
    private final HashMap<String, ArrayList<CurrentMarketObserver>> filters;

    private CurrentMarketPublisher(){
        filters = new HashMap<>();
    }

    public static CurrentMarketPublisher getInstance(){
        if(instance == null){
            instance = new CurrentMarketPublisher();
        }
        return instance;
    }
    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo){
        if(!filters.containsKey(symbol)){
            filters.put(symbol, new ArrayList<>());
        }
        filters.get(symbol).add(cmo);

    }
    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo){
        ArrayList<CurrentMarketObserver> observers = filters.get(symbol);
        if(observers !=null){
            observers.remove(cmo);
            if(observers.isEmpty()){
                filters.remove(symbol);
            }

        }


    }
    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide){
        if(!filters.containsKey(symbol)){
            return;
        }
        for (CurrentMarketObserver observer: filters.get(symbol)){
            observer.updateCurrentMarket(symbol,buySide,sellSide);
        }


    }


}
