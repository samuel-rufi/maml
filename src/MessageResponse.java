public class MessageResponse {

    private String address;
    private Message message;

    public MessageResponse(String address, Message message) {
        this.address = address;
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public Message getMessage() {
        return message;
    }

}
