package prices.product.users.publications;

public interface CurrentMarketObserver {
    void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide);

}
