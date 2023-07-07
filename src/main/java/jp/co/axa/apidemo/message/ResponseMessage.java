package jp.co.axa.apidemo.message;

import java.beans.ConstructorProperties;

public class ResponseMessage {

    private String message;

    @ConstructorProperties({"message"})
    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage() {
        this.message = message;
    }
}
