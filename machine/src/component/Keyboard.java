package component;

import java.io.Serializable;

public class Keyboard implements Serializable {
    private final String alphabet;

    public Keyboard(String alphabet) {
        this.alphabet = alphabet;
    }

    public String getAlphabet(){
        return this.alphabet;
    }

    public int getPositionOfCharacter(char inputChar){
        return alphabet.indexOf(inputChar);
    }
}
