package io.github.wulkanowy.api;

public class NotLoggedInErrorException extends VulcanException {

    public NotLoggedInErrorException(String message) {
        super(message);
    }

    public NotLoggedInErrorException(String message, Exception e) {
        super(message, e);
    }
}
