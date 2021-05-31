import java.io.Serializable;
import java.util.UUID;

/**
 * Auction class.
 * This class represents a single auction.
 */
public class Auction implements Serializable{
    private String itemDesc;
    private int auctionId;
    private int startPrice, acceptablePrice, currentBid;
    private String creatorName;
    private String currentBidUser;
    private UUID userID;

    public Auction(int startPrice, int acceptPrice, String desc, String creator )
    {

        currentBidUser = "empty";
        currentBid = 0;
        this.startPrice = startPrice;
        this.acceptablePrice = acceptPrice;
        this.itemDesc = desc;
        this.creatorName = creator;

    }

    public int getAcceptablePrice() {
        return acceptablePrice;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public int getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(int currentBid) {
        this.currentBid = currentBid;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCurrentBidUser() {
        return currentBidUser;
    }

    public void setCurrentBidUser(String currentBidUser) {
        this.currentBidUser = currentBidUser;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
