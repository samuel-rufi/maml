import java.util.HashMap;

import static spark.Spark.get;

public class MAMLServer {

    private static HashMap<String, MAML> m = new HashMap<>();

    public static void main(String[] args) {

        get("/load/:address", (request, response) -> {
            String address = request.params(":address");
            m.put(request.ip(), new MAML(address));
            return "{\"status\": \"success\"}";
        });

        get("/load/:address/:password", (request, response) -> {
            String address = request.params(":address");
            String password = request.params(":password");
            m.put(request.ip(), new MAML(address,password));
            return "{\"status\": \"success\"}";
        });

        get("/read", (request, response) -> {

            MAML maml = m.get(request.ip());
            if (maml == null)
                return "{\"message\": \"null\"}";

            Message message = maml.read();
            if(message == null)
                return "{\"message\": \"null\"}";

            return message;

        });

    }

}
