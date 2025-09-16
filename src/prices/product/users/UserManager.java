package prices.product.users;

import prices.Price;
import prices.product.InvalidReference;
import prices.product.Tradable;
import prices.product.TradableDTO;

import java.util.ArrayList;
import java.util.TreeMap;

public final class UserManager {
    private static UserManager instance = null;
    private TreeMap<String, User> users;

    private UserManager(){
        users = new TreeMap<>();


    }

    public static  UserManager getInstance() throws DataValidationException{
        if (instance == null){
            instance = new UserManager();

        }

        return instance;

    }



    public  void init(String[] usersIn) throws DataValidationException{
        if(usersIn == null){
            throw new DataValidationException("User cannot be null");

        }
        for (String userId : usersIn) {
            if(userId!=null && !users.containsKey(userId)){

                users.put(userId, new User(userId));
            }

        }

    }
    public User getUser(String userId) throws InvalidReference {
        //return users.get(userId);
        if (!users.containsKey(userId)) {
            throw new InvalidReference("User not found");
        }
        return users.get(userId);

    }
    public void updateTradable(String userId, TradableDTO o) throws DataValidationException{
        if (userId == null || o == null){
            throw new DataValidationException("Invalid userId");
        }
        User user = users.get(userId);
        if(user == null){
            throw new DataValidationException("User doens't exist");
        }
        user.updateTradable(o);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(User user: users.values()){
            result.append(user.toString());
        }
        return result.toString();
//        StringBuilder sb = new StringBuilder();
//
//
//        for (User user : users.values()) {
//            sb.append("User Id: ").append(users).append("\n");
//
//            for (TradableDTO tradable : user.getTradables().values()) {
//                sb.append("Product: ").append(tradable.product()).append(", ")
//                        .append("Price: $").append(String.format("%.2f", tradable.price())).append(", ")
//                        .append("OriginalVolume: ").append(tradable.originalVolume()).append(", ")
//                        .append("RemainingVolume: ").append(tradable.remainingVolume()).append(", ")
//                        .append("CancelledVolume: ").append(tradable.cancelledVolume()).append(", ")
//                        .append("FilledVolume: ").append(tradable.filledVolume()).append(", ")
//                        .append("User: ").append(tradable.user()).append(", ")
//                        .append("Side: ").append(tradable.side()).append(", ")
//                        .append("Id: ").append(tradable.tradableId()).append("\n");
//            }
//        }
//
//        return sb.toString();
    }


}
