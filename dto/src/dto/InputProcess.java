package dto;

import java.io.Serializable;

public class InputProcess implements Serializable {
    private String input;
    private String output;
    private Long time;

    public InputProcess(String input, String output, Long time) {
        this.input = input;
        this.output = output;
        this.time = time;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public Long getTime() {
        return time;
    }
}
