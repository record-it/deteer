package pl.recordit.deteer.controll;

import java.util.Optional;

public interface Feedback {
    boolean isSuccess();
    boolean isError();
    default Optional<Error> toError(){
        return this instanceof Error ? Optional.of((Error) this) : Optional.empty();
    }
    default Optional<Success> toSuccess(){
        return this instanceof Success ? Optional.of((Success) this) : Optional.empty();
    }

    static Feedback ofError(String message){
        return Error.of(message);
    }

    static Feedback ofSuccess(){
        return Success.SUCCESS;
    }
}
