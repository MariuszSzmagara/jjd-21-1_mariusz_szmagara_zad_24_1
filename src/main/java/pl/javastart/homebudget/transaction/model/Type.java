package pl.javastart.homebudget.transaction.model;

public enum Type {
    INCOME("przychód"),
    EXPENSE("wydatek");
    private final String description;

    Type(String description) {
        this.description = description;
    }

}
