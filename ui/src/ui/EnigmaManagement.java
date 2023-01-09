package ui;

import dto.InputProcess;
import dto.MachineHistory;
import dto.CodeConfiguration;
import dto.Specification;
import engine.Engine;
import engine.EngineUtility;
import myException.*;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnigmaManagement {
    EngineUtility engine = new Engine();
    boolean machineDataLoad = false;

    public void run() {
        boolean exit = false;
        do {
            printMenu();
            int inputOption = getValidOperationInput();
            if (inputOption != 10 && inputOption != 1 && inputOption != 9 && !machineDataLoad) {
                System.out.println("You should load machine date first! please choose option 1 in the menu." + System.lineSeparator());
                continue;
            }
            if ((inputOption >= 5 && inputOption <= 7) && engine.getOriginalCodeConfig() == null && machineDataLoad) {
                System.out.println("You should initialize code configuration first! please choose option 3/4 in the menu." + System.lineSeparator());
                continue;
            }
            try {
                switch (inputOption) {
                    case 1:
                        readMachineData();
                        break;
                    case 2:
                        Specification specification = engine.getMachineSpecification();
                        printMachineSpecification(specification);
                        break;
                    case 3:
                        CodeConfiguration settings = getStartUpSettings();
                        if (settings != null) {
                            engine.setMachineSettingsManually(settings);
                            System.out.println("Code configuration setup - Done! --- " + getDescriptionOfCurrentSettings(engine.getOriginalCodeConfig()) + System.lineSeparator());
                        }
                        break;
                    case 4:
                        CodeConfiguration startupSettings = engine.setMachineSettingsAutomatically();
                        System.out.println("Code configuration setup - Done! --- " + getDescriptionOfCurrentSettings(startupSettings) + System.lineSeparator());
                        break;
                    case 5:
                        String input = getValidInputToProcess();
                        if (input != null) {
                            String output = engine.encryptOrDecryptInput(input);
                            System.out.println("Processed output:  " + output + System.lineSeparator());
                        }
                        break;
                    case 6:
                        engine.setMachineToStartSettings();
                        System.out.println("Initialize machine to original code configuration - Done!" + System.lineSeparator());
                        break;
                    case 7:
                        MachineHistory machineHistory = engine.getMachineHistoryAndStatistics();
                        printMachineHistoryAndStatistics(machineHistory);
                        break;
                    case 8:
                        saveEngineToFile();
                        break;
                    case 9:
                        loadEngineFromFile();
                        break;
                    case 10:
                        exit = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!exit);
    }

    private String getFullPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter full file path :");
        String path = scanner.nextLine();
        return path;
    }

    private void readMachineData() {
        boolean validPath = false;
        try {
            String path = getFullPath();
            engine.loadMachineData(path);
            validPath = true;
            System.out.println("File loaded successfully." + System.lineSeparator());
            machineDataLoad = true;
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println("File with the specified pathname does not exist!");
        } catch (SuffixIsNotXmlException e) {
            System.out.println(e.getMessage());
        } catch (AlphabetNotEvenLengthException e) {
            System.out.println(e.getMessage());
        } catch (InvalidRotorCountException e) {
            System.out.println(e.getMessage());
        } catch (NotEnoughRotorsException e) {
            System.out.println(e.getMessage());
        } catch (RotorHasDoubleMappingException e) {
            System.out.println(e.getMessage());
        } catch (IdIsNotUniqueException e) {
            System.out.println(e.getMessage());
        } catch (IdIsNotOrderInSequenceFromOneException e) {
            System.out.println(e.getMessage());
        } catch (NotchIsOutOfRangeException e) {
            System.out.println(e.getMessage());
        } catch (CharMapToItselfException e) {
            System.out.println(e.getMessage());
        } catch (ReflrctorsIdOrderIsNotSequenceOfRomanException e) {
            System.out.println(e.getMessage());
        }
        if (!validPath) {
            System.out.println("Failed Loading file!" + System.lineSeparator());
        }
    }

    private int getValidOperationInput() {
        Scanner scanner = new Scanner(System.in);
        String errorMsg = "Invalid input. Please enter option number between 1 to 10.";
        boolean validInput = false;
        int input;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println(errorMsg);
                scanner.next();
            }
            input = scanner.nextInt();
            if (input >= 1 && input <= 10) {
                validInput = true;
            } else {
                System.out.println(errorMsg);
            }
        } while (!validInput);

        return input;
    }

    private List<Integer> getRotorsListInput() {
        Scanner scanner = new Scanner(System.in);
        List<Integer> rotors = new ArrayList<>();
        int rotorCount = engine.getMachineRotorCount();
        System.out.println("# Setting up rotors: #");
        System.out.println("Please enter a sequence of " + rotorCount + " rotor id's you would like to use separated with , (example- id,id,id) :");
        String input = scanner.nextLine();
        String parts[] = input.split(",");
        for (int i = parts.length - 1; i >= 0; i--) {
            rotors.add(Integer.parseInt(parts[i]));
        }
        engine.checkRotorListInput(rotors);
        System.out.println("Rotors setting up in place - Done!" + System.lineSeparator());
        return rotors;
    }

    private List<Character> getRotorsStartPosition() {
        Scanner scanner = new Scanner(System.in);
        List<Character> positions = new ArrayList<>();
        int rotorCount = engine.getMachineRotorCount();
        System.out.println("# Setting up rotors start positions: #");
        System.out.println("Please enter a sequence of " + rotorCount + " characters represent the start positions of the rotors. (Machine's Alphabet :" + engine.getMachineAlphabet() + ")");
        String input = scanner.nextLine();
        input = input.toUpperCase();
        engine.checkRotorsStartPositions(input);
        for (int i = input.length() - 1; i >= 0; i--) {
            positions.add(input.charAt(i));
        }
        System.out.println("Rotors start positions setup - Done!" + System.lineSeparator());
        return positions;
    }

    private Map<Character, Character> getPlugInUse() {
        Scanner scanner = new Scanner(System.in);
        Map<Character, Character> plugsInUse = new HashMap<>();
        System.out.println("# Plug board setup: #");
        System.out.println("Please enter pairs of connected plugs in sequence from the following alphabet :" + engine.getMachineAlphabet());
        String pair = scanner.nextLine();
        if (pair.isEmpty()) {
            return plugsInUse;
        }
        if (pair.length() % 2 != 0) {
            System.out.println("Invalid input! The sequence of connected plugs IS NOT in even length!");
            return null;
        }
        if (!engine.isStringContainOnlyLettersFromAlphabet(pair.toUpperCase())) {
            System.out.println("Invalid input! There are some characters not from the alphabet.");
            return null;
        }
        for (int i = 0; i < pair.length() - 1; i = i + 2) {
            char inputChar = pair.toUpperCase().charAt(i);
            char outputChar = pair.toUpperCase().charAt(i + 1);
            if (inputChar == outputChar) {
                System.out.println("Invalid input! Plug " + inputChar + " connected to itself.");
                return null;
            }
            if (engine.isPairAlreadyExist(plugsInUse, inputChar, outputChar)) {
                System.out.println("Invalid input! " + inputChar + " or " + outputChar + " already connected to another plug.");
                return null;
            }
            plugsInUse.put(inputChar, outputChar);
        }
        System.out.println("Plug board setup - Done!" + System.lineSeparator());
        return plugsInUse;
    }

    private CodeConfiguration getStartUpSettings() {
        CodeConfiguration startupSettings = null;
        boolean validCodeConfiguration = false;
        try {
            List<Integer> rotors = getRotorsListInput();
            List<Character> positions = getRotorsStartPosition();
            LinkedHashMap<Integer, Character> rotorsInUse = getRotorsToUse(rotors, positions);
            String reflectorId = getReflectorInput();
            Map<Character, Character> plugs = null;
            if (reflectorId != null) {
                plugs = getPlugInUse();
            }
            if (reflectorId != null && plugs != null) {
                startupSettings = new CodeConfiguration(rotorsInUse, null, reflectorId, plugs);
                validCodeConfiguration = true;
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Invalid input! There is no , that separates the rotors id!");
        } catch (NumberFormatException e) {
            System.out.println("The input contain invalid rotor id number / Invalid pattern of the input!");
        } catch (WrongNumberOfRotorsException e) {
            System.out.println(e.getMessage());
        } catch (IdIsNotUniqueException e) {
            System.out.println(e.getMessage());
        } catch (InvalidRotorIdException e) {
            System.out.println(e.getMessage());
        } catch (CharacterNotFromAlphabetException e) {
            System.out.println(e.getMessage());
        } catch (InvalidStartupPositionLenException e) {
            System.out.println(e.getMessage());
        }
        if (!validCodeConfiguration) {
            System.out.println("Failed setting code configuration!" + System.lineSeparator());
        }
        return startupSettings;
    }

    private String getReflectorInput() {
        Scanner scanner = new Scanner(System.in);
        int numOfOptions = engine.getMachineReflectorList().size();
        String errorMsg = "Invalid input! you should enter number between 1 to " + (numOfOptions) + ".";
        Map<Integer, String> roman = new HashMap<Integer, String>() {{
            put(1, "I");
            put(2, "II");
            put(3, "III");
            put(4, "IV");
            put(5, "V");
        }};
        int input;
        printReflectorOptions();
        if (!scanner.hasNextInt()) {
            System.out.println(errorMsg);
            scanner.next();
            return null;
        }
        input = scanner.nextInt();
        if (input >= 1 && input <= numOfOptions) {
            System.out.println("Reflector setup - Done!" + System.lineSeparator());
        } else {
            System.out.println(errorMsg);
            return null;
        }
        return roman.get(input);
    }

    private void printReflectorOptions() {
        System.out.println("# Reflector setup: #");
        System.out.println("Please choose one reflector from the following:");
        IntStream.range(0, engine.getMachineReflectorList().size()).forEachOrdered(i -> System.out.printf("%d.%s%n", i + 1, engine.getReflectorId(i)));
    }

    private LinkedHashMap<Integer, Character> getRotorsToUse(List<Integer> rotorsList, List<Character> startPosition) {
        LinkedHashMap<Integer, Character> rotorsToUse = IntStream.range(0, rotorsList.size()).boxed().collect(Collectors.toMap(rotorsList::get, startPosition::get, (a, b) -> b, LinkedHashMap::new));
        return rotorsToUse;
    }

    private String getValidInputToProcess() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter input you would like to process:");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            System.out.println("Empty input. Operation failed." + System.lineSeparator());
            return null;
        }
        if (!engine.isStringContainOnlyLettersFromAlphabet(input.toUpperCase())) {
            System.out.println("Invalid input! There might be some characters that are not part of the alphabet.");
            System.out.println("Failed processing input!" + System.lineSeparator());
            return null;
        }
        return input.toUpperCase();
    }

    private void printMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("Please choose one of the following operations:").append(System.lineSeparator())
                .append("1. Load machine data from xml file.").append(System.lineSeparator())
                .append("2. Show machine's Specification.").append(System.lineSeparator())
                .append("3. Choose code configuration Manually.").append(System.lineSeparator())
                .append("4. Choose code configuration automatically.").append(System.lineSeparator())
                .append("5. Encrypt/Decrypt.").append(System.lineSeparator())
                .append("6. Initialize machine to original code configuration.").append(System.lineSeparator())
                .append("7. Show activity history and statistics of current machine.").append(System.lineSeparator())
                .append("8. Save current machine's state to file.").append(System.lineSeparator())
                .append("9. Load machine from file.").append(System.lineSeparator())
                .append("10. Exit.").append(System.lineSeparator());
        System.out.println(menu);
    }

    private void printMachineHistoryAndStatistics(MachineHistory machineHistory) {
        System.out.println("Machine history and statistics: ");
        for (Map.Entry<CodeConfiguration, List<InputProcess>> entry : machineHistory.getHistoryAndStats().entrySet()) {
            CodeConfiguration currSettings = entry.getKey();
            System.out.println(getDescriptionOfCurrentSettings(currSettings));
            List<InputProcess> currProcessList = entry.getValue();
            int i = 1;
            for (InputProcess process : currProcessList) {
                String s = i + ". " + "<" + process.getInput() + "> --> <" + process.getOutput() + "> (" + process.getTime() + " nano-seconds)" + System.lineSeparator();
                System.out.println(s);
                i++;
            }
        }
        System.out.println(System.lineSeparator());
    }

    private String getDescriptionOfCurrentSettings(CodeConfiguration codeConfiguration) {
        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();
        StringBuilder str3 = new StringBuilder();
        Set<Integer> set = codeConfiguration.getRotorsInUse().keySet();
        Iterator<Integer> itr = set.iterator();
        List<Integer> alKeys = new ArrayList<Integer>(codeConfiguration.getRotorsInUse().keySet());
        Collections.reverse(alKeys);
        str1.append("<");
        str2.append("<");
        for (Integer strKey : alKeys) {
            if (alKeys.get(0) == strKey) {
                str1.append(strKey);
            } else {
                str1.append(",").append(strKey);
            }
            str2.append(codeConfiguration.getRotorsInUse().get(strKey));
            str2.append("(" + codeConfiguration.getNotchDistanceFromWindow().get(strKey) + ")");
        }
        str1.append(">");
        str2.append(">");
        str1.append(str2).append("<" + codeConfiguration.getReflectorId() + ">");
        if (codeConfiguration.getPlugsInUse().size() != 0) {
            str3.append("<");
            codeConfiguration.getPlugsInUse().forEach((k, v) -> str3.append(k).append("|").append(v).append(","));
            str3.deleteCharAt(str3.length() - 1);
            str3.append(">");
        }
        str1.append(str3);
        return str1.toString();
    }

    private void printMachineSpecification(Specification sp) {
        StringBuilder str = new StringBuilder();
        str.append("Machine's Specification:").append(System.lineSeparator());
        str.append("1. Rotor in use/Possible amount of rotors: ").append(sp.getAmountOfRotorsInUse()).append("/").append(sp.getPossibleAmountOfRotors()).append(System.lineSeparator());
        str.append("2. Amount of reflectors: ").append(sp.getAmountOfReflectors()).append(System.lineSeparator());
        str.append("3. Amount of massages that were encrypt/decrypt in the machine: ").append(sp.getAmountOfMassages()).append(System.lineSeparator());
        if (engine.getOriginalCodeConfig() != null) {
            str.append("4. Description of original code configuration: ").append(getDescriptionOfCurrentSettings(engine.getOriginalCodeConfig())).append(System.lineSeparator());
            str.append("5. Description of current code configuration: ").append(getDescriptionOfCurrentSettings(engine.getCurrentCodeConfig())).append(System.lineSeparator());
        }
        System.out.println(str);
    }

    private String getFileNameFullPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter full file path *including* file name and *without* file extension:");
        String path = scanner.nextLine();
        return path;
    }

    private void saveEngineToFile() {
        String fileName = getFileNameFullPath();
        try {
            engine.saveCurrentMachineToFile(fileName);
            System.out.println("Machine's current state saved successfully." + System.lineSeparator());
        } catch (IOException ex) {
            System.out.println("Failed saving machine's state to file." + System.lineSeparator());
        }
    }

    private void loadEngineFromFile() {
        String fileToDeserialize = getFileNameFullPath();
        try {
            Engine tmpEngine = engine.loadMachineFromFile(fileToDeserialize);
            if (tmpEngine != null) {
                engine = tmpEngine;
                machineDataLoad = true;
                System.out.println("Machine has been successfully loaded from file." + System.lineSeparator());
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Failed loading machine from file." + System.lineSeparator());
        }
    }
}
