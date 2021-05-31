import java.util.Scanner;
import java.util.UUID;

public class User {
    private String name;
    private String password;
    private UUID id;

    public User(String name, String password){
        this.name = name;
        this.password = password;
        id = UUID.randomUUID();

    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
