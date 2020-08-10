package pl.recordit.deteer.entity;

public enum CommentStatus {
    REGISTERED("Zgłoszono"),
    CONSIDERED("W trakcie rozważania"),
    APPROVED("Zaaprobowano i wprowadzono"),
    REJECTED("Odrzucono");

    private String polishName;

    CommentStatus(String polishName){
        this.polishName = polishName;
    }

    @Override
    public String toString(){
        return this.polishName;
    }
}
