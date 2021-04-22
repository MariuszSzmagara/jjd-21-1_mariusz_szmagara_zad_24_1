package pl.javastart.homebudget.transaction.model;

public enum Type {
    INCOME("przychód"),
    EXPENSE("wydatek");
    private final String translationPl;

    Type(String translationPl) {
        this.translationPl = translationPl;
    }

}
