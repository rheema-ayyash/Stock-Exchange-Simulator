package prices.product;

import prices.Price;

public interface Tradable {
    String getId();
    int getRemainingVolume();
    void setCancelledVolume(int newVol) throws InvalidReference;
    int getCancelledVolume();
    void setRemainingVolume(int newVol) throws InvalidReference;
    TradableDTO makeTradableDTO();
    Price getPrice();
    void setFilledVolume(int newVol) throws InvalidReference;
    int getFilledVolume();
    BookSide getSide();
    String getUser();
    String getProduct();
    int getOriginalVolume();


}
