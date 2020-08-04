package pl.recordit.deteer.controll;

public final class Error implements Feedback{
    private final String message;

    private Error(){
        this.message = "";
    }

    private Error(String message) {
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isError() {
        return true;
    }

    public String getErrorMessage() {
        return message;
    }

    public static Error of(String message){
        return new Error(message != null ? message : "Unknown error!");
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                '}';
    }
}
