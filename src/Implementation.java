import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Implementation extends java.rmi.server.UnicastRemoteObject implements auctionsystemInterface {
    JChannel channel;
//    private HashMap<Integer, Auction> auctions;
//    private HashMap<String, String> credentials;
    private HashMap<String, UUID> userIds;

    private static int uniqueId = 0;

    private RequestOptions options;
    private RpcDispatcher disp;
    Address address;
    int numbServers;

//    private SecretKey key;
//    private Cipher cipher;
    //private String testId = "sd2415";

    /**
     * Implementation class constructor.
     *
     * We assume that keys have been previously generate a random key and store it for trying the functionality
     * @throws RemoteException
     */
    protected Implementation(JChannel channel) throws RemoteException {

        super();

        disp = new RpcDispatcher(channel, this);
        options= new RequestOptions(ResponseMode.GET_ALL, 1000);
        address= channel.getAddress();


//        auctions = new HashMap<>();
//        credentials = new HashMap<>();
        userIds = new HashMap<>();


    }


    @Override
    public int createAuction(Auction auction) throws Exception
    {

        uniqueId++;
//        RspList rsp_list;
//        rsp_list =
        disp.callRemoteMethods(null, "CreateAuction",
            new Object[]{auction, uniqueId},
            new Class[]{Auction.class, int.class}, options);

//        List results = rsp_list.getResults();
        System.out.println("Debug check: createAuction from implementation.java");
        return auction.getAuctionId();
    }

    @Override
    public HashMap<Integer, Auction> getAuctions() throws RemoteException {
        return null;
    }


    public String DisplayAuctions() throws Exception{
        RspList rspList =disp.callRemoteMethods(null, "DisplayAuctions", new Object[]{},
                new Class[]{}, options);

//        System.out.println("Debug check: DisplayAuctions from implementation.java");

        List results = rspList.getResults();
        return (String)results.get(0);
    }


    public String closeAuction(int aucId, String userName) throws Exception
    {
        RspList rspList = disp.callRemoteMethods(null, "closeAuction",
                new Object[]{aucId, userName},
                new Class[]{int.class, String.class}, options);

        List l_server_response = rspList.getResults();
        return (String) l_server_response.get(0);

    }

    public String auctionBid(int amount, int auc_id, String bidderName)throws Exception
    {
        RspList rspList = disp.callRemoteMethods(null, "auctionBid",
                new Object[]{amount, auc_id, bidderName},
                new Class[]{int.class, int.class, String.class}, options);

        List l_server_response = rspList.getResults();
        return (String) l_server_response.get(0);

    }

    public synchronized String register(String username, String password) throws Exception
    {
        RspList rspList =disp.callRemoteMethods(null, "register",
                new Object[]{username, password}, new Class[]{String.class, String.class},
                options);

        List l_server_response = rspList.getResults();
        return (String) l_server_response.get(0);
    }
    public boolean login(String username, String password) throws Exception
    {
        RspList rspList = disp.callRemoteMethods(null, "login",
                new Object[]{username, password}, new Class[]{String.class, String.class},
                options);

        List serv_response = rspList.getResults();
        return (Boolean) serv_response.get(0);
    }


}
