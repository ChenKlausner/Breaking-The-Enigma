package dto;

public class MachineDetailsDto {
    private int rotorCount;

    private String alphabet;

    private int reflectorsAmount;

    public MachineDetailsDto(int rotorCount, String alphabet, int reflectorsAmount) {
        this.rotorCount = rotorCount;
        this.alphabet = alphabet;
        this.reflectorsAmount = reflectorsAmount;
    }

    public int getRotorCount() {
        return rotorCount;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getReflectorsAmount() {
        return reflectorsAmount;
    }
}
