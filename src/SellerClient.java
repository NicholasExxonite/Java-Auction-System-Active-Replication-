import javax.crypto.SecretKey;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class SellerClient {
    private Boolean isLogged = false;
    private String curSellerName;
    private String curSellerPas;
    private UUID userid;
    private SecretKey key;

    public static void main(String[] args)
    {
        SellerClient sc = new SellerClient();

    }
    /**
     * Class constructor. Initializes the registry and interface.
     */
    private SellerClient() {
        try{
            Registry registry = LocateRegistry.getRegistry(null);

            auctionsystemInterface ai = (auctionsystemInterface) Naming.lookup("auctionsystemInterface");

            //Login/register

            System.out.println("Welcome to the seller client.");
            while(true)
            {

                System.out.println("Please type 'l' to log in" +
                        ", 'r' to register or type 'g' to continue as a guest");

                Scanner s = new Scanner(System.in);
                String cred = s.nextLine();

                if(cred.equals("l"))
                {

                    if (tryLogging(ai))
                    {
//                        isLogged = true;
                        break;
                    }


                }
                else if (cred.equals("r"))
                {
                    tryRegistering(ai);

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

            System.out.println("\n" + "Welcome to the seller client!" + "\n");
            System.out.println("To create an auction type: a");
            System.out.println("To exit the client type: e");
            System.out.println("To display current auctions type: d");
            System.out.println("To close an auction type: c");


            while(true)
            {
                Scanner sc = new Scanner(System.in);
                String text = sc.nextLine();

                if(text.equals("a"))
                {
                    createAuction(ai, sc);

                }
                else if(text.equals("e"))
                {
                    System.out.println("Exited!");
                    return;
                }
                else if(text.equals("d"))
                {
                    displayOngoingAuctions(ai);

                }
                else if (text.equals("c"))
                {
                    try {
                        closeAuction(ai);
                    }catch (InputMismatchException ex)
                    {
                        System.out.println("You need to input auction ID as a number");
                    }
                }
                else
                {
                    System.out.println("No such command!");

                }
            }
        }catch (Exception ex)
        {
            System.out.println("Seller client exception: " + ex.toString());
           ex.printStackTrace();
        }
    }


    /**
     * This method attempts to log a registered user into the client.
     * It saves user input as current name and password.
     * We get the UUID of the user and encrypt it with the generated secret key.
     * The encrypted message is sent to the server to be decrypted and compared.
     *
     * If the userID is correct then we proceed to log in the user.
     *
     * @param ai
     * @return
     * @throws RemoteException
     */
    public Boolean tryLogging(auctionsystemInterface ai) throws RemoteException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter username: ");
        curSellerName = sc.next();

        System.out.println("Please enter password: ");
        curSellerPas = sc.next();

        try {
            boolean succesful = ai.login(curSellerName,curSellerPas);

            if(succesful)
            {
                System.out.println("You have successfully logged in " + curSellerName);
                isLogged = true;
                return true;
            }
            else
            {
                System.out.println("Login failed.");
//                isLogged = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method is called when trying to close an auction.
     * Checks if it's a logged user that tries the command.
     * Then it checks if this is the user that created the auction.
     * If yes it closes it and checks if the highest bid met the acceptable price.
     * @param ai
     * @throws RemoteException
     */
    public void closeAuction(auctionsystemInterface ai) throws Exception {
        if(isLogged)
        {
            System.out.println("Note that you can close only auctions that you have started!");
            System.out.println("Please input the id of the auction you would like to close.");
            Scanner scr = new Scanner(System.in);
            int t = scr.nextInt();
            String output = ai.closeAuction(t, curSellerName);
            System.out.println(output);

//            try{
//                String winnerName = ai.getWinnerName(t);
//                int curbid = ai.getAuctions().get(t).getCurrentBid();
//                int acpprice = ai.getAuctions().get(t).getAcceptablePrice();
//                if(ai.closeAuction(t, curSellerName))
//                {
//                    if(curbid >= acpprice)
//                    {
//                        System.out.println("Auction with id: " + t + " Has been closed!");
//                        System.out.println("Winner of the auction is: " + winnerName);
//                    }
//
//                    else
//                        System.out.println("Auction closed. Minimum acceptable price was not met.");
//                }
//                else System.out.println("Failed. You are not the one that started the auction.");
//            }catch (NullPointerException ex)
//            {
//                System.out.println("There is no bid with this id");
//            }
        }
        else System.out.println("You need to be logged in to close auctions");
    }

    /**
     * A method for displaying the current auctions.
     * @param ai
     * @throws RemoteException
     */
    public void displayOngoingAuctions(auctionsystemInterface ai) throws RemoteException{
        HashMap<Integer, Auction> auctions;
        auctions = ai.getAuctions();

//        for (Auction a : auctions.values())
//        {
//            System.out.println("-----------------------------------------------------------------");
//            System.out.println("ID: " + a.getAuctionId() +"| START PRICE: " + a.getStartPrice());
//            System.out.println("DESCRIPTION: " + a.getItemDesc());
//            System.out.println("CUR HIGHEST BID: " + a.getCurrentBid());
//            if(a.getCreatorName().equals(curSellerName))
//            {
//                System.out.println("ACCEPTABLE PRICE: " + a.getAcceptablePrice());
//            }
//            System.out.println("-----------------------------------------------------------------");
//        }

        try {
            System.out.println(ai.DisplayAuctions());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for creating an auction.
     * Checks if the input arguments are correct. If yes it creates an auction.
     * @param ai
     * @param sc
     * @throws RemoteException
     */
    public void createAuction(auctionsystemInterface ai, Scanner sc) throws RemoteException{
        if(isLogged)
        {
            Scanner auctionsc = new Scanner(System.in);
            System.out.println("You chose to create an auction! Please input the needed details.");

            System.out.println("Description of auction: ");
            String desc = auctionsc.nextLine();

            System.out.println("Starting price(as integer number): ");
            try {
                int startPrice = sc.nextInt();
                System.out.println("Acceptable price(as integer number): ");
                int acceptPrice = sc.nextInt();


//                ai.createAuction(startPrice, acceptPrice, desc, curSellerName);
                Auction auction = new Auction(startPrice, acceptPrice, desc, curSellerName);
                ai.createAuction(auction);
                System.out.println("You have successfully created an auction.");
            }catch (Exception ex)
            {
                //ex.printStackTrace();
                System.out.println("Please enter an integer");
            }

        }
        else System.out.println("You need to log in to create an auction.");
    }

    /**
     * This method takes user input as password and username.
     * It calls the registerAccount method from Implementation and passes the name and password to create a new account.
     * @param ai - the auctionsystem Interface
     * @throws RemoteException
     * @throws NoSuchAlgorithmException
     */
    public void tryRegistering(auctionsystemInterface ai) throws RemoteException, NoSuchAlgorithmException {

        System.out.println("Please enter username: ");
        Scanner sc = new Scanner(System.in);
        curSellerName = sc.next();

        System.out.println("Please enter password: ");
        curSellerPas = sc.next();

        try {
            System.out.println(ai.register(curSellerName, curSellerPas));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(ai.registerAccount(curSellerName,  curSellerPas))
//        {
//            System.out.println("Your account has been successfully created, " + curSellerName + " Please log in.");
////                    setLogged(true);
////                    break;
//        }
//        else System.out.println("Account with this username already exists.");
    }

    public Boolean getLogged() {
        return isLogged;
    }

    public void setLogged(Boolean logged) {
        isLogged = logged;
    }
}
