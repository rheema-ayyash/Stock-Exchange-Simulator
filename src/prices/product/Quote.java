package prices.product;

import prices.Price;

public class Quote {
   private final String user;
   private final String product;
   private final QuoteSide buySide;
   private final QuoteSide sellSide;

   public Quote (String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) throws InvalidReference {
       validateUser(userName);
       validateProduct(symbol);

       this.user = userName;
       this.product = symbol;
       this.buySide = new QuoteSide(userName, symbol, buyPrice, buyVolume, BookSide.BUY);
       this.sellSide = new QuoteSide(userName, symbol, sellPrice, sellVolume, BookSide.SELL);

   }

    private void validateUser(String user) throws InvalidReference{
        if(user == null || user.length() !=3 ){
            throw new InvalidReference("Invalid user code.");

        }
        for(int i = 0; i<user.length(); i++) {
            char ch = user.charAt(i);
            if (!Character.isLetter(ch)) {
                throw new InvalidReference("Invalid user code");
            }
        }

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
    public QuoteSide getQuoteSide( BookSide sideIn) throws InvalidReference{
       if(sideIn == BookSide.BUY){
           return buySide;

       }else if (sideIn == BookSide.SELL){
           return sellSide;
       }else{
           throw new InvalidReference("Invalid BookSide Value");
       }
    }
    public String getSymbol(){
       return product;
    }
    public String getUser(){
       return user;
    }
}
