package langpendlaren.api.http;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.json.error.Error;

public class ErrorHandler {
    public static void sendErrorMessage(Context context, Exception exception, HttpStatus status) {
        exception.printStackTrace();
        Error error = new Error();
        error.setErrorMessage(exception.getClass().getSimpleName(), exception.getMessage());
        context.status(status);
        context.json(error);
    }
}
