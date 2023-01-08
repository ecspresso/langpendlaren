package langpendlaren.api.http;

import io.javalin.http.Context;
import langpendlaren.api.http.json.error.Error;

public class ErrorHandler {
    public static void sendErrorMessage(Context context, Exception exception) {
        exception.printStackTrace();
        Error error = new Error();
        error.setErrorMessage(exception.getClass().getSimpleName(), exception.getMessage());
        context.json(error);
    }
}
