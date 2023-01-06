package langpendlaren.api.http;

/**
 * Holds an error message being sent instead when something goes wrong.
 */
public class ErrorMessage {
    /**
     * The reason the error happened.
     */
    private String reason;
    /**
     * The stacktrace message.
     */
    private String stacktrace;

    /**
     * Getter method for marshalling.
     * @return The reason for the error.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Setter method for marshalling.
     * @param reason The reason for the error.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Getter method for marshalling.
     * @return The stacktrace message.
     */
    public String getStacktrace() {
        return stacktrace;
    }

    /**
     * Setter method for marshalling.
     * @param stacktrace The stacktrace message.
     */
    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
}
