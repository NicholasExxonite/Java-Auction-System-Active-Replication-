import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.blocks.RspFilter;
import org.jgroups.util.RspList;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class replicaServer {
    private HashMap<Integer, Auction> auctions = new HashMap<>();
    private HashMap<String, String> credentials = new HashMap<>();
//    static int auctionId = 1;

    public static void main(String[] args) throws Exception {
        new replicaServer().start();
    }
    public void CreateAuction(Auction auc, int auctId)
    {
//        ArrayList<Auction> temp =new ArrayList<>();
//        temp.add(auc);

        auctions.put(auctId, auc);
        auctions.get(auctId).setAuctionId(auctId);

        System.out.println("New Auction created: ID: "+ auctions.get(auctId).getAuctionId() + " Description: " + auc.getItemDesc());
        System.out.println("Number of current auctions: " +auctions.size());
//        auctionId++;
    }
    public String register(String username, String password) throws InterruptedException
    {
        boolean free = true;
        for(String key : credentials.keySet())
        {
            if(key.equals(username))
            {
                free = false;
//                return "Account with username: " + username + " already exists!";
            }
        }
        if(free)
        {
            credentials.put(username, password);
            return "Account with username: "+ username + " has been created!";
        }
        else return "Account with username: " + username + " already exists!";
    }
    public Boolean login(String username, String password) throws RemoteException
    {
        for(Map.Entry e: credentials.entrySet())
        {
            if(username.equals(e.getKey()))
            {
                if (password.equals(e.getValue()))
                {
//                    System.out.println("Logged in successfully");
                    return true;
                }
//                System.out.println("Username and password don't match");
            }
        }
        return false;
    }

    public String DisplayAuctions() throws RemoteException
    {
        ArrayList<String> list = new ArrayList<>();

        for(Auction auc : auctions.values()){
            String desc = auc.getItemDesc();
            String startPrice = Integer.toString(auc.getStartPrice());
            String highestBid = Integer.toString(auc.getCurrentBid());
            String acceptPrice = Integer.toString(auc.getAcceptablePrice());
            String aid = Integer.toString(auc.getAuctionId());


            String toDisplay = "Auction ID: " + aid +" | Description: "+ desc +
                    "\nstartPrice: " + startPrice +" | Highest Bid: "+ highestBid + "\n";
            list.add(toDisplay);

//            System.out.println(toDisplay);
        }
        if(list.isEmpty())
        {
            return "There are currently no ongoing auctions.";
        }
        return String.join("", list);
    }
    public String auctionBid(int amount, int auc_id, String bidderName) throws RemoteException
    {
        for(Auction auc : auctions.values())
        {
            if(auc.getAuctionId() == auc_id)
            {
                if (amount > auc.getStartPrice() || amount > auc.getCurrentBid())
                {
                    auc.setCurrentBid(amount);
                    auc.setCurrentBidUser(bidderName);
                    return "Bid successful! You just bid :" + amount + "On bid with ID: " + auc.getAuctionId();
                }
                else return "Illegal bid. Bid must be greater than current bid price or starting price";
            }

        }
        return "Error...";
    }
    public String closeAuction(int aucId, String userName) throws RemoteException
    {
        if(!auctions.get(aucId).getCreatorName().equals(userName))
            return "You are not the one who started this auction.";

        if(auctions.get(aucId).getCurrentBid() <= auctions.get(aucId).getAcceptablePrice())
        {
            auctions.remove(aucId);
            return "Auction with id: " + aucId + " closed, but the acceptable price was not met";
        }
        else
        {
            String winner = auctions.get(aucId).getCurrentBidUser();
            auctions.remove(aucId);
            return "Auction with id: " + aucId + " closed. Winner: " + winner;
        }
    }



//
//            if(auc.getAuctionId().g == a)
//            {
//                if(!userName.equals(auc.getCreatorName()))
//                    return "You are not the one who started this auction.";
//                if(auc.getCurrentBid() <= auc.getAcceptablePrice())
//                {
//                    auctions.remove(auc.getAuctionId());
//                    return "Auction with id: " + auc.getAuctionId() + " closed, but the acceptable price was not met.";
//                }
//                else
//                {
//                    auctions.remove(auc.getAuctionId());
//                    return "Auction with id: " + auc.getAuctionId()+ " closed. Winner: " + auc.getCurrentBidUser();
//                }
//            }else return "No ongoing auction with this id";
//        return "Something went wrong.";
//    }
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void start() throws Exception
    {
        JChannel channel = new JChannel();
        channel.connect("backendServer");
        channel.setDiscardOwnMessages(true);
        RpcDispatcher disp = new RpcDispatcher(channel, this);
        Address ad =channel.getAddress();


        RequestOptions options = new RequestOptions(ResponseMode.GET_ALL, 30000, false,
                new RspFilter() {
                    int rec = 0;
                    @Override
                    public boolean isAcceptable(Object o, Address address) {
                        if(address == ad)
                            return false;

                        rec++;
                        return true;
                    }
                    @Override
                    public boolean needMoreResponses() {
                        if(rec<1)
                            return false;

                        return true;
                    }
                });

        //  get the State.
        RspList rsp_list = disp.callRemoteMethods(null, "getData", new Object[]{},
                new Class[]{}, options);

        List result=rsp_list.getResults();

        if(result.size() != 0)
        {
            HashMap server_Resp = (HashMap)result.get(0);
            Object auctionItem = server_Resp.get("tempAuctionItem");
            //Object profiles = server_Resp.get("tempProfiles");


            HashMap auct = (HashMap)auctionItem;

            auctions.putAll(auct);
        }

    }
    public void eventLoop()
    {
        while (true)
        {

        }
    }

    /**
     * Method used to get a dumb from other members of the cluster.
     * @return
     */
    public HashMap<String, Object> getData()
    {
        HashMap<String, Object> toSend = new HashMap<>();

        HashMap<Integer, Auction> tempAuctionItem =new HashMap<>();

        tempAuctionItem.putAll(auctions);

        toSend.put("tempAuctionItem",tempAuctionItem);

        return toSend;

    }

}
