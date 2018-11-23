import org.json.JSONObject;

public class MessageResponse {

    private String address;
    private String nextAddress;
    private Message message;
    private boolean isTrusted;

    public MessageResponse(String address, String nextAddress, Message message, boolean isTrusted) {
        this.address = address;
        this.message = message;
        this.isTrusted = isTrusted;
        this.nextAddress = nextAddress;
    }

    public String getAddress() {
        return address;
    }

    public Message getMessage() {
        return message;
    }

    public boolean isTrusted() { return isTrusted; };

    @Override
    public String toString() {

        JSONObject o = null;

        if(message == null)
            o = new JSONObject("{\"message\": \"invalid content\"}");
        else
            o = new JSONObject(getMessage().toString());

        o = o.accumulate("address", address);
        o = o.accumulate("nextAddress", nextAddress);
        o = o.accumulate("isTrusted", isTrusted);

        return o.toString();
    }

}
