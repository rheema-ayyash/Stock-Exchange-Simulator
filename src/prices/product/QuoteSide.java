package prices.product;

import prices.Price;

public class QuoteSide implements Tradable {
    private final String user;
    private final String product;
    private final Price price;
    private final int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private final BookSide side;
    private String id;


public QuoteSide( String user, String product, Price price, int volume, BookSide side) throws InvalidReference {
    validateUser(user);
    validateProduct(product);
    validatePrice(price);
    validateVolume(volume);
    validateSide(side);

    this.id = generateId(user,product, price);
    this.user = user;
    this.product = product;
    this.filledVolume = 0;
    this.cancelledVolume=0;
    this.remainingVolume = volume;
    this.price = price;
    this.originalVolume = volume;
    this.side = side;

}
    private void validateUser(String user) throws InvalidReference{
        if(user == null || user.length() !=3 ){
            throw new InvalidReference("Invalid user code.");

        }
        for(int i = 0; i<user.length(); i++){
            char ch = user.charAt(i);
            if(!Character.isLetter(ch)){
                throw new InvalidReference("Invalid user code");
            }
        }
    }
    private void validateProduct (String product) throws InvalidReference{
        if (product == null || product.length()<1 || product.length()>5){
            throw new InvalidReference("Invalid Product symbol");

        }
        for (int i =0; i < product.length();i++){
            char ch = product.charAt(i);
            if(!Character.isLetterOrDigit(ch) && ch!= '.'){
                throw new InvalidReference("Invalid Product symbol");
            }

        }
    }
    private void validatePrice (Price price) throws InvalidReference{
        if (price == null){
            throw new InvalidReference("Cannot be null");

        }
    }

    private void validateVolume(int volume) throws InvalidReference{
        if (volume <=0 || volume>= 10000){
            throw new InvalidReference("Volume out of range");
        }
    }
    private void validateSide(BookSide side) throws InvalidReference{
        if(side ==null){
            throw new InvalidReference("Cannot be null");
        }
    }
    private String generateId(String user, String product, Price price){
        return user + product + price + System.nanoTime();
    }
    @Override
    public String getUser(){
        return user;
    }
    @Override
    public String getProduct(){
        return product;
    }
    @Override
    public Price getPrice(){
        return price;
    }
    @Override

    public int getOriginalVolume(){
        return originalVolume;
    }
    @Override
    public int getRemainingVolume(){
        return remainingVolume;
    }
    @Override
    public int getFilledVolume(){
        return filledVolume;
    }
    @Override
    public int getCancelledVolume() {
        return cancelledVolume;
    }
    @Override
    public String getId(){
        return id;
    }
    @Override
    public BookSide getSide(){
        return side;
    }
    @Override
    public void setCancelledVolume(int newVol) throws InvalidReference{
        if(newVol < 0 || newVol > remainingVolume){
            throw new InvalidReference("Invalid range");

        }
        this.cancelledVolume = newVol;
    }

    public void setRemainingVolume(int newVol) throws InvalidReference{
        if(newVol <0 || newVol>originalVolume){
            throw new InvalidReference("Invalid range");

        }
        this.remainingVolume = newVol;
    }
    public void setFilledVolume (int newVol) throws InvalidReference{
        if( newVol < 0 || newVol > originalVolume){ //remainingVolume){
            throw new InvalidReference("invalid range");

        }
        this.filledVolume = newVol;

    }
    @Override
    public TradableDTO makeTradableDTO(){
        return new TradableDTO(user, product, price, originalVolume, remainingVolume, cancelledVolume, filledVolume, side, id);


    }

    @Override
    public String toString() {
        return String.format("%s %s side quote for %s: %s, Orig Vol: %d, Rem Vol: %d, Fill Vol: %d, CXL Vol: %d, ID: %s",
                user, side, product, price, originalVolume, remainingVolume, filledVolume, cancelledVolume, id);

//        return user + " " + side + " side quote for " + product +
//                " at " + price +
//                ", Orig Vol: " + originalVolume +
//                ", Rem Vol: " + remainingVolume +
//                ", Fill Vol: " + filledVolume +
//                ", CXL Vol: " + cancelledVolume +
//                ", ID: " + id;
    }
//        return "QuoteSide{" +
//                "user='" + user + '\'' +
//                ", product='" + product + '\'' +
//                ", price=" + price +
//                ", originalVolume=" + originalVolume +
//                ", remainingVolume=" + remainingVolume +
//                ", cancelledVolume=" + cancelledVolume +
//                ", filledVolume=" + filledVolume +
//                ", side=" + side +
//                ", id='" + id + '\'' +
//                '}';
//    }
    //    @Override
//    public String toString(){
//        return String.format("%s %s side quote for %s: %s, Orig Vol: %d, Rem Vol: %d, Fill Vol: %d, CXL Vol: %d, ID: %s",
//                user,               // USER
//                BookSide.BUY,       // SIDE (we assume a static side for example, could be BookSide.SELL or BookSide.BUY)
//                product,            // PRODUCT
//                price,              // PRICE
//                originalVolume,     // Original Volume
//                remainingVolume,    // Remaining Volume
//                filledVolume,       // Filled Volume
//                cancelledVolume,    // Cancelled Volume
//                id);
//    }
//


}
