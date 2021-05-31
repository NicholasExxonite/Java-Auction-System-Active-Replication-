import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * The interface class. Contains methods used by implementation.
 */
public interface auctionsystemInterface  extends  java.rmi.Remote{

//    public int createAuction(int startprice, int acceptprice, String desc, String user) throws RemoteException;
//    void displayAuctions();
 //   public void displayAuctions() throws RemoteException;
    public int createAuction(Auction auction) throws Exception;
    public HashMap<Integer, Auction> getAuctions() throws RemoteException;
    public String DisplayAuctions() throws Exception;
    public String auctionBid(int amount, int auc_id, String bidderName)throws Exception;
//    public boolean closeAuction(int auctionId, String user) throws RemoteException;
    public  String closeAuction(int aucId, String userName) throws Exception;
//    public void buyerBid(int auctionId, int bid, String bidderName) throws RemoteException;
    public String register(String username, String password) throws Exception;

    public boolean login(String username, String password) throws Exception;
//    public boolean registerAccount(String username, String password) throws RemoteException;
//
//    public boolean loginAttempt(String username, String password) throws RemoteException;
//
//    public String getWinnerName(int auctionId) throws RemoteException;

//    public SecretKey getKey() throws RemoteException;

//    public Cipher getCipher() throws  RemoteException;

//    public String encryptUser(String id, SecretKey k) throws RemoteException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException;

//    public boolean decrpytUser(String encryptedString, String user) throws RemoteException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

//    public HashMap<String, UUID> getUserIds() throws RemoteException;
//
//    public HashMap<String, String> getCredentials() throws RemoteException;
}
