public class MessageResponse {

    private String address;
    private Message message;
    private boolean isTrusted;

    public MessageResponse(String address, Message message, boolean isTrusted) {
        this.address = address;
        this.message = message;
        this.isTrusted = isTrusted;
    }

    public String getAddress() {
        return address;
    }

    public Message getMessage() {
        return message;
    }

    public boolean isTrusted() { return isTrusted; };

}
