package langpendlaren.api.http.json.error;

/**
 * Holds an error message being sent instead when something goes wrong.
 */
public class Error {
    private ErrorMessage errorMessage;

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(String reason, String stacktrace) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setError(reason);
        errorMessage.setMessage(stacktrace);
        this.errorMessage = errorMessage;
    }


    static class ErrorMessage {

        /**
         * The reason the error happened.
         */
        private String error;
        /**
         * The stacktrace message.
         */
        private String message;

        /**
         * Getter method for marshalling.
         * @return The reason for the error.
         */
        public String getError() {
            return error;
        }

        /**
         * Setter method for marshalling.
         * @param error The reason for the error.
         */
        public void setError(String error) {
            this.error = error;
        }

        /**
         * Getter method for marshalling.
         * @return The stacktrace message.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Setter method for marshalling.
         * @param message The stacktrace message.
         */
        public void setMessage(String message) {
        this.message = message;
    }
    }
}
