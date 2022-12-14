package langpendlaren.api.http;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.json.error.Error;

public class ErrorHandler {
    /**
     * Short hand for internal server error.
     * @param context Context handler that sends the reply to the client.
     * @param exception Exception thrown.
     */
    public static void sendErrorMessage(Context context, Exception exception) {
        exception.printStackTrace();
        sendErrorMessage(context, exception, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates error message that is sent to the client.
     * @param context Context handler that sends the reply to the client.
     * @param exception Exception thrown.
     * @param message Message explaining the error in more detail.
     * @param status HTTP status code.
     */
    public static void sendErrorMessage(Context context, Exception exception, String message, HttpStatus status) {
        exception.printStackTrace();
        Error error = new Error();
        error.setErrorMessage(exception.getClass().getSimpleName(), message);
        context.status(status);
        context.json(error);

    }
}
