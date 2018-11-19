import java.util.HashMap;

import static spark.Spark.get;

public class MAMLServer {

    private static HashMap<String, MAML> m = new HashMap<>();

    public static void main(String[] args) {

        if(args.length >= 5) {
            MAML.protocol = args[0];
            MAML.host = args[1];
            MAML.port = args[2];
            MAML.depth = Integer.parseInt(args[3]);
            MAML.minWeightMagnitude = Integer.parseInt(args[4]);
        }

        get("/load/:address", (request, response) -> {
            String address = request.params(":address");
            m.put(request.ip(), new MAML(address));
            return "{\"status\": \"ok\"}";
        });

        get("/load/:address/:password", (request, response) -> {
            String address = request.params(":address");
            String password = request.params(":password");
            m.put(request.ip(), new MAML(address,password));
            return "{\"status\": \"ok\"}";
        });

        get("/read", (request, response) -> {

            MAML maml = m.get(request.ip());
            if (maml == null)
                return "{\"message\": \"null\"}";

            MessageResponse r = maml.read();
            if(r == null )
                return "{\"message\": \"null\"}";
            if(r.getMessage() == null)
                return "{\"message\": \"unable to read content\"}";
            return r.getMessage();

        });

    }

}
