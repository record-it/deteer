package pl.recordit.deteer.entity;

import pl.recordit.deteer.service.CommentService;

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
