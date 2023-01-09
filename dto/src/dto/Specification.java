package dto;

import java.util.*;

public class Specification {
    private int possibleAmountOfRotors;
    private int amountOfRotorsInUse;
    private int amountOfReflectors;
    private int amountOfMassages;

    public Specification(int possibleAmountOfRotors, int amountOfRotorsInUse, int amountOfReflectors, int amountOfMassages) {
        this.possibleAmountOfRotors = possibleAmountOfRotors;
        this.amountOfRotorsInUse = amountOfRotorsInUse;
        this.amountOfReflectors = amountOfReflectors;
        this.amountOfMassages = amountOfMassages;
    }

    public int getPossibleAmountOfRotors() {
        return possibleAmountOfRotors;
    }

    public int getAmountOfRotorsInUse() {
        return amountOfRotorsInUse;
    }

    public int getAmountOfReflectors() {
        return amountOfReflectors;
    }

    public int getAmountOfMassages() {
        return amountOfMassages;
    }
}
