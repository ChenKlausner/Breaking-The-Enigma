package dto;

public class SpecificationBuilder {
    private int possibleAmountOfRotors;
    private int amountOfRotorsInUse;
    private int amountOfReflectors;
    private int amountOfMassages;

    public void setPossibleAmountOfRotors(int possibleAmountOfRotors) {
        this.possibleAmountOfRotors = possibleAmountOfRotors;
    }
    public void setAmountOfRotorsInUse(int amountOfRotorsInUse) {
        this.amountOfRotorsInUse = amountOfRotorsInUse;
    }

    public void setAmountOfReflectors(int amountOfReflectors) {
        this.amountOfReflectors = amountOfReflectors;
    }

    public void setAmountOfMassages(int amountOfMassages) {
        this.amountOfMassages = amountOfMassages;
    }

    public Specification build() {
        Specification specification = new Specification(this.possibleAmountOfRotors, this.amountOfRotorsInUse, this.amountOfReflectors, this.amountOfMassages);
        return specification;
    }
}
