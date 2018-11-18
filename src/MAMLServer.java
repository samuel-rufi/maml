import java.util.HashMap;

import static spark.Spark.get;

public class MAMLServer {

    private static HashMap<String, MAML> m = new HashMap<>();

    public static void main(String[] args) {

        get("/load/:address", (request, response) -> {

            try {
                String address = request.params(":address");
                m.put(request.ip(), new MAML(address, ""));
                return "{\"status\": \"success\"}" + request.ip();
            } catch (Exception e) {
                return "{\"status\": \"failure\"}";
            }

        });

        get("/read", (request, response) -> {

            MAML maml = m.get(request.ip());
            if (m == null)
                return "{\"status\": \"failure\"}";

            Message message = maml.read();
            if(message == null)
                return "{\"status\": \"failure\"}";

            return message;

        });

    }

}
