package pl.recordit.deteer.controll;

public final class Success implements Feedback{
    protected final static Success SUCCESS = new Success();
    private Success(){
    }
    @Override
    public boolean isSuccess() {
        return true;
    }
    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String toString() {
        return "Success";
    }
}
