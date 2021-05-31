import javax.crypto.SecretKey;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.UUID;

public class BuyerClient{
    private int clientId;
    private String curBuyerName, curBuyerPas;
    private Boolean isLogged;
    private UUID userid;
    private SecretKey key;
    public BuyerClient() {
        /**
         * BuyerClient constructor. Initializes registry and the interface.
         */

        try{
            Registry registry = LocateRegistry.getRegistry(null);

            auctionsystemInterface ai = (auctionsystemInterface) Naming.lookup("auctionsystemInterface");


            System.out.println("Welcome to the buyer client! ");
            while (true)
            {
                System.out.println("Please type 'l' to log in" +
                        ", 'r' to register or type 'g' to continue as a guest");


                Scanner s = new Scanner(System.in);
                String cred = s.nextLine();

                if(cred.equals("l"))
                {
                    if(tryLogin(ai))
                    {
                        break;
                    }

                }
                else if (cred.equals("r"))
                {
                    tryRegister(ai);

                }
                else if(cred.equals("g"))
                {
                    System.out.println("You have decided to continue as a guest. You can only view" +
                            "current auctions.");
                    setLogged(false);
                    break;
                }
                else
                {
                    System.out.println("Unknown command. Try again.");
                }
            }

            System.out.println("Buyer client.");
            System.out.println("To exit the client type: e");
            System.out.println("To display current auctions type: d");
            System.out.println("To bid on an auction type: b");

            while (true)
            {
                Scanner s = new Scanner(System.in);
                String text = s.nextLine();

                if(text.equals("d"))
                {
                    displayAuctions(ai);

                }
                else if(text.equals("b"))
                {
                    makeBid(ai);

                }
                else if (text.equals("e"))
                {
                    System.out.println("You have exited the client.");
                    break;
                }

            }
        }catch (Exception ex){
            System.out.println("Seller client exception: " + ex.toString());
            ex.printStackTrace();
        }


    }

    /**
     * Method called when user tries to bid on an auction.
     * It checks if the input arguments from the user are correct.
     * If this auction exit and the bid is more than the current bid
     * The user makes a bid. The bid and name of users are saved as Auction variables.
     * @param ai
     * @throws RemoteException
     */
    public void makeBid(auctionsystemInterface ai) throws RemoteException
    {
        System.out.println("Please type in the id of the auction and the your bid separated" +
                "by space as integer numbers(example: 3 500 , where 3 is id and 500 the bid");
        Scanner sc = new Scanner(System.in);
        String[] inputArray = sc.nextLine().split(" ");

        try{
//            ai.buyerBid(Integer.parseInt(inputArray[0]), Integer.parseInt(inputArray[1]), curBuyerName);
            String output = ai.auctionBid(Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[0]), curBuyerName);
            System.out.println(output);
        }
        catch (NumberFormatException ex)
        {
            System.out.println("You can input only integers for the two values.");
        }
        catch (NullPointerException nullex)
        {
            System.out.println("There is no auction with this id.");
        }
        catch (ArrayIndexOutOfBoundsException aex)
        {
            System.out.println("You need to input both the id and the price of bid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * his method attempts to log a registered user into the client.
     * It saves user input as current name and password.
     * We get the UUID of the user and encrypt it with the generated secret key.
     * The encrypted message is sent to the server to be decrypted and compared.
     *
     * If the userID is correct then we proceed to log in the user.
     * @param ai
     * @return
     * @throws RemoteException
     */
    public boolean tryLogin(auctionsystemInterface ai) throws RemoteException
    {
//
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter username: ");
        curBuyerName = sc.next();

        System.out.println("Please enter password: ");
        curBuyerPas = sc.next();

        try {
            boolean succesful = ai.login(curBuyerName,curBuyerPas);

            if(succesful)
            {
                System.out.println("You have successfully logged in " + curBuyerName);
                return true;
            }
            else
            {
                System.out.println("Login failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method takes user input as password and username.
     * It calls the registerAccount method from Implementation and passes the name and password to create a new account.
     * @param ai
     * @throws RemoteException
     */
    public void tryRegister(auctionsystemInterface ai) throws RemoteException
    {
        System.out.println("Please enter username: ");
        Scanner sc = new Scanner(System.in);
        curBuyerName = sc.next();

        System.out.println("Please enter password: ");
        curBuyerPas = sc.next();

        try {
            System.out.println(ai.register(curBuyerName, curBuyerPas));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ai.registerAccount(curBuyerName,  curBuyerPas);
//        System.out.println("Your account has been successfully created, " + curBuyerName + " Please log in.");
//                    setLogged(true);
//                    break;
    }

    /**
     * Method for displaying ongoing auctions.
     * @param ai
     * @throws RemoteException
     */
    public void displayAuctions(auctionsystemInterface ai) throws RemoteException
    {
//        HashMap<Integer, Auction> auctions;
//        auctions = ai.getAuctions();
//
//        for (Auction a : auctions.values())
//        {
//            System.out.println("-----------------------------------------------------------------");
//            System.out.println("ID: " + a.getAuctionId() +"| START PRICE: " + a.getStartPrice());
//            System.out.println("DESCRIPTION: " + a.getItemDesc());
//            System.out.println("CUR HIGHEST BID: " + a.getCurrentBid());
//            System.out.println("-----------------------------------------------------------------");
//        }
        try {
            System.out.println(ai.DisplayAuctions());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BuyerClient buyer = new BuyerClient();
    }

    public void setLogged(Boolean logged) {
        isLogged = logged;
    }

    public Boolean getLogged() {
        return isLogged;
    }
}
