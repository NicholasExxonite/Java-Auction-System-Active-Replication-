import org.jgroups.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BackendServer implements Receiver {
    private HashMap<Integer, Auction> auctions;
    JChannel channel;
    static int id =1;
//    String name = System.getProperty("user.name", "n/a");
    String name = ("backendServer " + id);
    String smdata;
    final List<String> state= new LinkedList<>();





    @Override
    public void viewAccepted(View newView) {
        System.out.println("** view: " + newView);

    }

    @Override
    public void suspect(Address address) {

    }

    @Override
    public void block() {

    }

    @Override
    public void unblock() {

    }

    @Override
    public void receive(Message msg) {
//        String line=msg.getSrc() + ": " + msg.getObject();
        String line[] = msg.getObject().toString().split(" ");
        if (line[0].equals("s")) {
            int startprice = Integer.parseInt(line[1]);
            System.out.println(this.name + ":" + startprice);
        }
//        smdata=line;
//        System.out.println(this.name + ":" +line);
//        System.out.println(msg);

//        synchronized(state) {
//            state.add(line);
//        }

    }


    public String getSmdata() {
        return smdata;
    }

    @Override
    public void getState(OutputStream output) throws Exception {
//        synchronized(state) {
//            Util.objectToStream(state, new DataOutputStream(output));
//        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
//        List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
//        synchronized (state) {
//            state.clear();
//            state.addAll(list);
//        }
//        System.out.println("received state (" + list.size() + " messages in chat history):");
//        for (String str : list) {
//            System.out.println(str);
//        }

    }
    public void start() throws Exception {
        channel= new JChannel();
        channel.setName(name);
        channel.setReceiver(this);
        channel.connect("AuctionServer");
//        channel.getState(null, 10000);
//        eventLoop();
        //channel.close();
        System.out.println("backend server started!");
        if (channel.isConnected())
            System.out.println("Channel is connected");

        id++;
    }

    public static void main(String[] args) throws Exception {
        new BackendServer().start();
    }
}

