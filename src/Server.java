import org.jgroups.JChannel;

import java.rmi.Naming;
import java.rmi.RemoteException;


public class Server extends Implementation{
    private static JChannel channel;

    protected Server(JChannel channel) throws RemoteException {
        super(channel);
    }

    public static void main(String args[])
    {
        try{
            channel = new JChannel();
            channel.connect("backendServer");

            Implementation obj = new Implementation(channel);

            Naming.bind("auctionsystemInterface", obj);

        }catch (Exception ex)
        {
            System.out.println("Server exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
