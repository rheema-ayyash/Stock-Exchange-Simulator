package prices;

import java.util.HashMap;
import java.util.Map;

public abstract class PriceFactory {

    private  static final Map<Integer, Price>  newPrices = new HashMap<>();

    public static Price makePrice(int value) {
        Price price = newPrices.get(value);
        if(price == null){
            price = new Price(value);
            newPrices.put(value, price);
        }
        return price;

    }

    public static Price makePrice(String stringValueIn) throws InvalidPriceException {
        if (stringValueIn == null || stringValueIn.isEmpty() ) {
            throw new InvalidPriceException("Invalid price String value:");
        }

        //String str = "";  // stores valid numeric string
        boolean isNeg = false; //tracks if price is negative
        boolean isDecimal = false; // checks if there is a decimal point
        int isDollar = 0; //stores dollar
        int isCents = 0; // stores cents
        int count = 0; // tracks the number of decimal places for cents

        for (int i = 0; i < stringValueIn.length(); i++) { // iterates through each char
            char ch = stringValueIn.charAt(i);  // gets the current char

            if (ch == ' ' || ch == '$' || ch == ',') { //ignores spaces, dollar signs and commas
                continue;
            }
            if (ch == '-') { // makes sure the negative sign is only at the start after the dollar sign
                if (i != 1) {
                    throw new InvalidPriceException("Invalid price String value: " + stringValueIn);

                }
                isNeg  = true; //marks as negative

            } else if (ch == '.') {
                if (isDecimal) {
                    throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
                }
                isDecimal = true; //there is a decimal point
            } else if (Character.isDigit(ch)) {// checks for numerical characters
                int digit = Integer.parseInt(String.valueOf(ch)); // converts char into an int using parseInt
                //str += ch; // adds digit to the numerical string
                if (isDecimal) { // if there is decimal point it can only be 2 digits
                    if (count <2) {
                        isCents = isCents * 10 + digit; // converts dollar amount then add the cents
                        count++;


                    } else {
                        throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
                    }
                } else {
                    isDollar = isDollar * 10 + digit; // converts the dollar amount then adds the cents
                }


            } else {
                throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
            }
        }
       if (isDecimal && count == 1) { // if its not only 2 decimal places it is invalid
            throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
        }
        int totalCents = (isDollar * 100) + isCents; // converts string into a valid dollar
        if (isNeg) {
            totalCents = -totalCents; // if its negative it adds the neg sign
        }
        return makePrice(totalCents);
    }// returns converted price
}

            //if (str.charAt(i) == '$'&& str.charAt(i) == ',') {
                //str = (' ');
                // }else if(String.format(("$%,.2f" /100.0)){
                // i++;

//            } else {
//                throw new InvalidPriceException("Invalid Prices.Price");
//            }
//            //return String.format(("$%,.2f"));
//        }
//        return null;












