package engine;

import component.Plugboard;
import dto.*;
import battlefield.Battlefield;
import myException.*;
import component.Keyboard;
import component.Reflector;
import component.Rotor;
import enigma.Enigma;
import ex3.jaxb.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Engine implements EngineUtility, Serializable {
    private final static String JAXB_XML_PACKAGE_NAME = "ex3.jaxb.schema.generated";
    private Enigma enigmaMachine;
    private MachineHistory history;
    private CodeConfiguration originalCodeConfig;
    private Set<String> dictionary;
    private Integer numOfAgents;
    private String excludeChars;
    private Battlefield battlefield;

    @Override
    public void loadMachineData(String fullFilePath) throws JAXBException, FileNotFoundException {
        CTEEnigma cteEnigma;
        Keyboard keyboard;
        List<Rotor> rotorsList;
        List<Reflector> reflectorsList;
        int rotorCount;
        checkSuffix(fullFilePath);
        try {
            InputStream inputStream = new FileInputStream(new File(fullFilePath));
            cteEnigma = deserializeFrom(inputStream);
            keyboard = getKeyboard(cteEnigma.getCTEMachine());
            checkValidRotorCount(cteEnigma.getCTEMachine());
            rotorCount = cteEnigma.getCTEMachine().getRotorsCount();
            checkIfNotchIsInRange(cteEnigma.getCTEMachine().getCTERotors(), keyboard.getAlphabet().length());
            rotorsList = createRotorsList(cteEnigma.getCTEMachine().getCTERotors());
            reflectorsList = createReflectorsList(cteEnigma.getCTEMachine().getCTEReflectors());
            /*numOfAgents = cteEnigma.getCTEDecipher().getAgents();
            checkIfValidNumOfAgents(numOfAgents);   // ----> needed for ex2*/
            excludeChars = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
            createMachineDictionary(cteEnigma.getCTEDecipher().getCTEDictionary());
            enigmaMachine = new Enigma(keyboard, rotorsList, reflectorsList, rotorCount);
            history = new MachineHistory();
            enigmaMachine.setMessageCounter(0);
            originalCodeConfig = null;
        } catch (JAXBException | FileNotFoundException e) {
            throw e;
        }
    }

    public void loadMachineDataWithInputStream(InputStream inputStream, String uBoatUserName) throws JAXBException {
        CTEEnigma cteEnigma;
        Keyboard keyboard;
        List<Rotor> rotorsList;
        List<Reflector> reflectorsList;
        int rotorCount;
        try {
            cteEnigma = deserializeFrom(inputStream);
            keyboard = getKeyboard(cteEnigma.getCTEMachine());
            checkValidRotorCount(cteEnigma.getCTEMachine());
            rotorCount = cteEnigma.getCTEMachine().getRotorsCount();
            checkIfNotchIsInRange(cteEnigma.getCTEMachine().getCTERotors(), keyboard.getAlphabet().length());
            rotorsList = createRotorsList(cteEnigma.getCTEMachine().getCTERotors());
            reflectorsList = createReflectorsList(cteEnigma.getCTEMachine().getCTEReflectors());
            excludeChars = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
            createMachineDictionary(cteEnigma.getCTEDecipher().getCTEDictionary());
            enigmaMachine = new Enigma(keyboard, rotorsList, reflectorsList, rotorCount);
            battlefield = new Battlefield(cteEnigma.getCTEBattlefield().getLevel(), cteEnigma.getCTEBattlefield().getBattleName(), cteEnigma.getCTEBattlefield().getAllies(), uBoatUserName);
            enigmaMachine.setMessageCounter(0);
            originalCodeConfig = null;
        } catch (JAXBException e) {
            throw e;
        }
    }

    @Override
    public Specification getMachineSpecification() {
        SpecificationBuilder spBuilder = new SpecificationBuilder();
        spBuilder.setPossibleAmountOfRotors(enigmaMachine.getRotorsList().size());
        spBuilder.setAmountOfRotorsInUse(enigmaMachine.getRotorCount());
        spBuilder.setAmountOfReflectors(enigmaMachine.getReflectorsList().size());
        spBuilder.setAmountOfMassages(enigmaMachine.getMessageCounter());
        Specification specification = spBuilder.build();
        return specification;
    }

    @Override
    public void setMachineSettingsManually(CodeConfiguration codeConfiguration) {
        setMachineRotorsInUse(codeConfiguration.getRotorsInUse());
        setMachineReflector(codeConfiguration.getReflectorId().trim());
        setMachinePlugboard(codeConfiguration.getPlugsInUse());
        enigmaMachine.setToStartPosition();
        CodeConfiguration code = new CodeConfiguration(codeConfiguration.getRotorsInUse(), getMapOfNotchDistanceFromWindow(), codeConfiguration.getReflectorId().trim(), codeConfiguration.getPlugsInUse());
        //addStartSettingsToHistory(code);
        originalCodeConfig = code;
    }

    @Override
    public CodeConfiguration setMachineSettingsAutomatically() {
        LinkedHashMap<Integer, Character> rotorsAndStartPosition = getRandomRotorInUse();
        setMachineRotorsInUse(rotorsAndStartPosition);
        int reflectorId = getReflectorToUse();
        setMachineReflector(reflectorId);
        Map<Character, Character> plugsInUse = getRandomPlugsInUse();
        setMachinePlugboard(plugsInUse);
        String reflectorStr = enigmaMachine.getReflectorsList().get(reflectorId - 1).getId();
        enigmaMachine.setToStartPosition();
        Map<Integer, Integer> notchDistanceFromWindow = getMapOfNotchDistanceFromWindow();
        CodeConfiguration startupSettings = new CodeConfiguration(rotorsAndStartPosition, notchDistanceFromWindow, reflectorStr, plugsInUse);
        //addStartSettingsToHistory(startupSettings);
        originalCodeConfig = startupSettings;
        return startupSettings;
    }

    public void setMachineSettingsAutomaticallyEx3() {
        LinkedHashMap<Integer, Character> rotorsAndStartPosition = getRandomRotorInUse();
        setMachineRotorsInUse(rotorsAndStartPosition);
        int reflectorId = getReflectorToUse();
        setMachineReflector(reflectorId);
        Map<Character, Character> plugsInUse = getRandomPlugsInUse();
        setMachinePlugboard(plugsInUse);
        String reflectorStr = enigmaMachine.getReflectorsList().get(reflectorId - 1).getId();
        enigmaMachine.setToStartPosition();
        Map<Integer, Integer> notchDistanceFromWindow = getMapOfNotchDistanceFromWindow();
        CodeConfiguration startupSettings = new CodeConfiguration(rotorsAndStartPosition, notchDistanceFromWindow, reflectorStr, plugsInUse);
        originalCodeConfig = startupSettings;
    }

    @Override
    public String encryptOrDecryptInput(String input) {
        long startTime = System.nanoTime();
        String output = enigmaMachine.encrypt(input);
        long endTime = System.nanoTime();
        enigmaMachine.setMessageCounter(enigmaMachine.getMessageCounter() + 1);
        long timeElapsed = endTime - startTime;
        InputProcess newProcess = new InputProcess(input, output, timeElapsed);
        addInputProcess(newProcess);
        return output;
    }

    @Override
    public void setMachineToStartSettings() {
        enigmaMachine.setToStartPosition();
    }

    @Override
    public MachineHistory getMachineHistoryAndStatistics() {
        return history;
    }

    @Override
    public int getMachineRotorCount() {
        return enigmaMachine.getRotorCount();
    }

    @Override
    public List<Rotor> getMachineRotorList() {
        return enigmaMachine.getRotorsList();
    }

    @Override
    public int getRotorId(int index) {
        return enigmaMachine.getRotorsList().get(index).getId();
    }

    @Override
    public boolean isRotorAlreadyExist(List<Integer> rotors, int rotorId) {
        return IntStream.range(0, rotors.size()).anyMatch(i -> rotors.get(i) == rotorId);
    }

    @Override
    public String getMachineAlphabet() {
        return enigmaMachine.getKeyboard().getAlphabet();
    }

    @Override
    public boolean charIsPartOfAlphabet(Character input) {
        return IntStream.range(0, enigmaMachine.getKeyboard().getAlphabet().length()).anyMatch(i -> enigmaMachine.getKeyboard().getAlphabet().charAt(i) == input);
    }

    @Override
    public boolean isStringContainOnlyLettersFromAlphabet(String input) {
        return IntStream.range(0, input.length()).allMatch(i -> charIsPartOfAlphabet(input.charAt(i)));
    }

    @Override
    public List<Reflector> getMachineReflectorList() {
        return enigmaMachine.getReflectorsList();
    }

    @Override
    public String getReflectorId(int index) {
        return enigmaMachine.getReflectorsList().get(index).getId();
    }

    @Override
    public boolean isPairAlreadyExist(Map<Character, Character> plugsInUse, Character input, Character output) {
        return plugsInUse.containsKey(input) || plugsInUse.containsValue(input)
                || plugsInUse.containsKey(output) || plugsInUse.containsValue(output);
    }

    @Override
    public CodeConfiguration getOriginalCodeConfig() {
        return originalCodeConfig;
    }

    @Override
    public CodeConfiguration getCurrentCodeConfig() {
        LinkedHashMap<Integer, Character> rotorsInUse = getRotorsInUseSettings();
        Map<Integer, Integer> notchDistanceFromWindow = getMapOfNotchDistanceFromWindow();
        String reflectorId = enigmaMachine.getReflector().getId();
        Map<Character, Character> plugsInUse = getPlugsInUse(enigmaMachine.getPlugboard());
        CodeConfiguration currentCode = new CodeConfiguration(rotorsInUse, notchDistanceFromWindow, reflectorId, plugsInUse);
        return currentCode;
    }

    @Override
    public Map<Integer, Integer> getMapOfNotchDistanceFromWindow() {
        Map<Integer, Integer> notchPositions = new LinkedHashMap<>();
        if (enigmaMachine.getRotorsInUse() != null) {
            for (Rotor rotor : enigmaMachine.getRotorsInUse()) {
                notchPositions.put(rotor.getId(), rotor.getNotchIntPosition());
            }
        }
        return notchPositions;
    }

    @Override
    public void checkRotorListInput(List<Integer> rotorList) {
        /*if (rotorList.size() != enigmaMachine.getRotorCount()) {
            throw new WrongNumberOfRotorsException(rotorList.size(), enigmaMachine.getRotorCount());
        }
        checkRotorIds(rotorList);*/
        checkUniqueRotorId(rotorList);
    }

    @Override
    public void checkRotorsStartPositions(String input) {
        if (!(isStringContainOnlyLettersFromAlphabet(input))) {
            throw new CharacterNotFromAlphabetException();
        }
        if (input.length() != enigmaMachine.getRotorCount()) {
            throw new InvalidStartupPositionLenException(input.length(), enigmaMachine.getRotorCount());
        }
    }

    @Override
    public void saveCurrentMachineToFile(String fileName) throws IOException {
        // Serialization
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(fileName + ".txt");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(this);

            out.close();
            file.close();

        } catch (IOException ex) {
            throw ex;
        }
    }

    @Override
    public Engine loadMachineFromFile(String fileName) throws ClassNotFoundException, IOException {
        // Deserialization
        Engine engine = null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(fileName + ".txt");
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            engine = (Engine) in.readObject();

            in.close();
            file.close();

        } catch (IOException | ClassNotFoundException ex) {
            throw ex;
        }
        return engine;
    }

    @Override
    public String processCharacter(String input) {
        return enigmaMachine.encrypt(input);
    }

    @Override
    public void updateHistory(InputProcess inputProcess) {
        addInputProcess(inputProcess);
        enigmaMachine.setMessageCounter(enigmaMachine.getMessageCounter() + 1);
    }

    @Override
    public Engine getDeepCopyOfEngine() {
        Engine engine = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = Base64.getEncoder().encodeToString(baos.toByteArray());

        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            engine = (Engine) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return engine;
    }

    @Override
    public CodeConfiguration getZeroCodeConfiguration() {
        enigmaMachine.setToZeroPosition();
        return getCurrentCodeConfig();
    }

    @Override
    public void advanceMachineCode(int taskSize) {
        enigmaMachine.advance(taskSize);
    }

    @Override
    public void setReflectorToMachine(Reflector reflector) {
        enigmaMachine.setReflector(reflector);
    }

    @Override
    public void setRotorInUse(List<Rotor> rotorInUse) {
        enigmaMachine.setRotorsInUse(rotorInUse);
    }

    @Override
    public List<Rotor> getMachineRotorInUseList() {
        return enigmaMachine.getRotorsInUse();
    }

    @Override
    public String decryptInput(String secretMessage) {
        return enigmaMachine.encrypt(secretMessage);
    }

    @Override
    public Integer getNumOfAgents() {
        return numOfAgents;
    }

    @Override
    public String getExcludeChars() {
        return excludeChars;
    }

    @Override
    public Set<String> getDictionary() {
        return dictionary;
    }

    @Override
    public boolean isDictionaryContainWordsFromInput(String text) {
        String words = removeExcludeChars(text);
        String parts[] = words.split(" ");
        return Arrays.stream(parts).allMatch(word -> dictionary.contains(word));
    }

    @Override
    public String removeExcludeChars(String text) {
        String words = null;
        for (int i = 0; i < excludeChars.length(); i++) {
            String regex = String.valueOf(excludeChars.charAt(i));
            words = text.replace(regex, "");
        }
        return words;
    }

    @Override
    public String[] removeExcludeCharsFromLast(String[] parts) {
        boolean found = false;
        String[] newParts = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            for (int j = 0; j < excludeChars.length(); j++) {
                if (parts[i].endsWith(String.valueOf(excludeChars.charAt(j)))) {
                    newParts[i] = parts[i].substring(0, parts[i].length() - 1);
                    found = true;
                }
                if (!found) {
                    newParts[i] = parts[i];
                }
            }
            found = false;
        }
        return newParts;
    }

    @Override
    public void setRotorsPositions(LinkedList<Character> rotorsPositions) {
        for (int i = 0; i < enigmaMachine.getRotorCount(); i++) {
            enigmaMachine.getRotorsInUse().get(i).setStartPosition(rotorsPositions.get(i));
        }
    }

    private void checkRotorIds(List<Integer> rotorInUse) {
        Set<Integer> newSet = new HashSet<>();
        for (int i = 0; i < rotorInUse.size(); i++) {
            int rotorId = rotorInUse.get(i);
            if (newSet.contains(rotorId)) {
                throw new IdIsNotUniqueException(rotorId);
            }
            newSet.add(rotorId);
            if ((rotorId < 1 || rotorId > enigmaMachine.getRotorsList().size())) {
                throw new InvalidRotorIdException(rotorInUse.get(i));
            }
        }
    }

    private void checkUniqueRotorId(List<Integer> rotorInUse) {
        Set<Integer> newSet = new HashSet<>();
        for (int i = 0; i < rotorInUse.size(); i++) {
            int rotorId = rotorInUse.get(i);
            if (newSet.contains(rotorId)) {
                throw new IdIsNotUniqueException(rotorId);
            }
            newSet.add(rotorId);
        }
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    private void checkSuffix(String fullFilePath) {
        if (!fullFilePath.toLowerCase().endsWith(".xml")) {
            throw new SuffixIsNotXmlException();
        }
    }

    private Keyboard getKeyboard(CTEMachine cteMachine) {
        if (cteMachine.getABC().trim().length() % 2 != 0)
            throw new AlphabetNotEvenLengthException();
        return new Keyboard(cteMachine.getABC().toUpperCase().trim());
    }

    private void checkValidRotorCount(CTEMachine cteMachine) {
        int rotorCount = cteMachine.getRotorsCount();
        int totalNumRotors = cteMachine.getCTERotors().getCTERotor().size();
        if (rotorCount > totalNumRotors) {
            throw new NotEnoughRotorsException(totalNumRotors, rotorCount);
        }
        if (rotorCount < 2) {
            throw new InvalidRotorCountException();
        }
    }

    private List<Rotor> createRotorsList(CTERotors cteRotors) {
        List<Rotor> rotorsList = cteRotors.getCTERotor().stream()
                .map(this::createRotor)
                .collect(Collectors.toList());
        Collections.sort(rotorsList);
        checkValidRotorId(rotorsList);

        return rotorsList;
    }

    private Rotor createRotor(CTERotor cteRotor) {
        LinkedList<Character> left = new LinkedList<>();
        LinkedList<Character> right = new LinkedList<>();
        IntStream.range(0, cteRotor.getCTEPositioning().size()).forEachOrdered(i -> {
            left.addLast(cteRotor.getCTEPositioning().get(i).getLeft().toUpperCase().charAt(0));
            right.addLast(cteRotor.getCTEPositioning().get(i).getRight().toUpperCase().charAt(0));
        });
        checkValidRotorMapping(left, cteRotor.getId());
        checkValidRotorMapping(right, cteRotor.getId());

        return new Rotor(cteRotor.getId(), left, right, cteRotor.getNotch());
    }

    private void checkValidRotorMapping(LinkedList<Character> list, int rotorId) {
        Set<Character> newSet = new HashSet<>();
        int bound = list.size();
        IntStream.range(0, bound).forEachOrdered(i -> {
            if (!(newSet.contains(list.get(i)))) {
                newSet.add(list.get(i));
            } else {
                throw new RotorHasDoubleMappingException(rotorId, list.get(i));
            }
        });
    }

    private void checkValidRotorId(List<Rotor> rotorsList) {
        IntStream.range(0, rotorsList.size()).forEachOrdered(i -> {
            int id = rotorsList.get(i).getId();
            if (id != i + 1) {
                if (id > 0 && id <= i) {
                    throw new IdIsNotUniqueException(id);
                } else {
                    throw new IdIsNotOrderInSequenceFromOneException();
                }
            }
        });
    }

    private void checkIfNotchIsInRange(CTERotors cteRotors, int alphabetLength) {
        int bound = cteRotors.getCTERotor().size();
        IntStream.range(0, bound).filter(i -> cteRotors.getCTERotor().get(i).getNotch() > alphabetLength || cteRotors.getCTERotor().get(i).getNotch() < 1).forEachOrdered(i -> {
            throw new NotchIsOutOfRangeException(cteRotors.getCTERotor().get(i).getId(), alphabetLength);
        });
    }

    private List<Reflector> createReflectorsList(CTEReflectors cteReflectors) {
        List<Reflector> reflectorsList = cteReflectors.getCTEReflector()
                .stream()
                .map(this::createReflector)
                .collect(Collectors.toList());
        Collections.sort(reflectorsList);
        this.checkReflectorsId(reflectorsList);
        return reflectorsList;
    }

    private Reflector createReflector(CTEReflector cteReflector) {
        Reflector reflector = new Reflector(cteReflector.getId());
        IntStream.range(0, cteReflector.getCTEReflect().size()).forEachOrdered(i -> {
            int input = cteReflector.getCTEReflect().get(i).getInput();
            int output = cteReflector.getCTEReflect().get(i).getOutput();
            if (input == output) {
                throw new CharMapToItselfException(cteReflector.getId(), input);
            }
            reflector.addPair(input - 1, output - 1);
        });
        return reflector;
    }

    private void checkReflectorsId(List<Reflector> reflectorsList) {
        Map<Integer, String> roman = new HashMap<Integer, String>() {{
            put(1, "I");
            put(2, "II");
            put(3, "III");
            put(4, "IV");
            put(5, "V");
        }};

        for (int i = 0; i < reflectorsList.size(); i++) {
            if (!(reflectorsList.get(i).getId().equals(roman.get(i + 1)))) {
                throw new ReflrctorsIdOrderIsNotSequenceOfRomanException();
            }
        }
    }

    private LinkedHashMap<Integer, Character> getRotorsInUseSettings() {
        LinkedHashMap<Integer, Character> rotorSettings = new LinkedHashMap<>();
        if (enigmaMachine.getRotorsInUse() != null) {
            for (Rotor rotor : enigmaMachine.getRotorsInUse()) {
                rotorSettings.put(rotor.getId(), rotor.getRightWiring().getFirst());
            }
        }
        return rotorSettings;
    }

    private Map<Character, Character> getPlugsInUse(Plugboard plugsInUse) {
        Map<Character, Character> plugs = new HashMap<>();
        plugsInUse.getConnectedPlugs().forEach((key, value) -> {
            if (!plugs.containsKey(key) && !plugs.containsKey(value)) {
                plugs.put(key, value);
            }
        });
        return plugs;
    }

    private int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private LinkedHashMap<Integer, Character> getRandomRotorInUse() {
        LinkedHashMap<Integer, Character> rotorsAndStartPosition = new LinkedHashMap<>();
        for (int i = 0; i < enigmaMachine.getRotorCount(); i++) {
            int startPosition = getRandomNumber(0, enigmaMachine.getKeyboard().getAlphabet().length() - 1);
            int rotorId = getValidRotorId(rotorsAndStartPosition);
            rotorsAndStartPosition.put(rotorId, enigmaMachine.getKeyboard().getAlphabet().charAt(startPosition));
        }
        return rotorsAndStartPosition;
    }

    private int getValidRotorId(LinkedHashMap<Integer, Character> rotorsAndStartPosition) {
        boolean validInput = false;
        int rotorId;
        do {
            rotorId = getRandomNumber(1, enigmaMachine.getRotorsList().size());
            if (!rotorsAndStartPosition.containsKey(rotorId)) {
                validInput = true;
            }
        } while (!validInput);
        return rotorId;
    }

    private void setMachineRotorsInUse(LinkedHashMap<Integer, Character> rotorsAndStartPosition) {
        List<Rotor> rotorsInUse = new ArrayList<>();
        Set<Integer> keys = rotorsAndStartPosition.keySet();
        int i = 0;
        for (Integer key : keys) {
            rotorsInUse.add(enigmaMachine.getRotorsList().get(key - 1));
            rotorsInUse.get(i).setStartPosition(rotorsAndStartPosition.get(key));
            i++;
        }
        enigmaMachine.setRotorsInUse(rotorsInUse);
    }

    private int getReflectorToUse() {
        int reflectorId = getRandomNumber(1, enigmaMachine.getReflectorsList().size());
        return reflectorId;
    }

    private void setMachineReflector(int reflectorId) {
        enigmaMachine.setReflector(enigmaMachine.getReflectorsList().get(reflectorId - 1));
    }

    private void setMachineReflector(String reflectorId) {
        IntStream.range(0, enigmaMachine.getReflectorsList().size())
                .filter(i -> enigmaMachine.getReflectorsList().get(i).getId().equals(reflectorId))
                .forEachOrdered(i -> enigmaMachine.setReflector(enigmaMachine.getReflectorsList().get(i)));
    }

    private Map<Character, Character> getRandomPlugsInUse() {
        Map<Character, Character> plugsInUse = new HashMap<>();
        int numOfPairs = getRandomNumber(0, enigmaMachine.getKeyboard().getAlphabet().length() / 2);
        for (int i = 0; i < numOfPairs; i++) {
            boolean validPair = false;
            do {
                int input = getRandomNumber(0, enigmaMachine.getKeyboard().getAlphabet().length() - 1);
                int output = getRandomNumber(0, enigmaMachine.getKeyboard().getAlphabet().length() - 1);
                char inputChar = enigmaMachine.getKeyboard().getAlphabet().charAt(input);
                char outputChar = enigmaMachine.getKeyboard().getAlphabet().charAt(output);
                if (input != output && !plugsInUse.containsKey(inputChar) && !plugsInUse.containsValue(inputChar)
                        && !plugsInUse.containsKey(outputChar) && !plugsInUse.containsValue(outputChar)) {
                    plugsInUse.put(inputChar, outputChar);
                    validPair = true;
                }
            } while (!validPair);
        }
        return plugsInUse;
    }

    private void setMachinePlugboard(Map<Character, Character> plugs) {
        Plugboard plugboard = new Plugboard();
        plugs.forEach((k, v) -> plugboard.addPair(k, v));
        enigmaMachine.setPlugboard(plugboard);
    }

    private boolean compareStartupSettings(CodeConfiguration settings1, CodeConfiguration settings2) {
        if (!settings1.getReflectorId().equals(settings2.getReflectorId())) {
            return false;
        }
        if (!linkedEquals(settings1.getRotorsInUse(), settings2.getRotorsInUse())) {
            return false;
        }
        if (!comparePlugsInUse(settings1.getPlugsInUse(), settings2.getPlugsInUse())) {
            return false;
        }
        return true;
    }

    private <K, V> boolean linkedEquals(LinkedHashMap<K, V> left, LinkedHashMap<K, V> right) {
        Iterator<Map.Entry<K, V>> leftItr = left.entrySet().iterator();
        Iterator<Map.Entry<K, V>> rightItr = right.entrySet().iterator();

        while (leftItr.hasNext() && rightItr.hasNext()) {
            Map.Entry<K, V> leftEntry = leftItr.next();
            Map.Entry<K, V> rightEntry = rightItr.next();

            if (!leftEntry.equals(rightEntry))
                return false;
        }
        return !(leftItr.hasNext() || rightItr.hasNext());
    }

    private boolean comparePlugsInUse(Map<Character, Character> plugs1, Map<Character, Character> plugs2) {
        if (plugs1.size() != plugs2.size()) {
            return false;
        }
        for (Map.Entry<Character, Character> entry : plugs1.entrySet()) {
            if (plugs2.get(entry.getKey()) != entry.getValue() && plugs2.get(entry.getValue()) != entry.getKey()) {
                return false;
            }
        }
        return true;
    }

    private boolean isStartupSettingsAlreadyExist(CodeConfiguration startupSettings) {
        for (Map.Entry<CodeConfiguration, List<InputProcess>> entry : history.getHistoryAndStats().entrySet()) {
            if (compareStartupSettings(entry.getKey(), startupSettings)) {
                return true;
            }
        }
        return false;
    }

    private void addStartSettingsToHistory(CodeConfiguration startupSettings) {
        if (!isStartupSettingsAlreadyExist(startupSettings)) {
            history.getHistoryAndStats().put(startupSettings, new ArrayList<>());
        }
    }

    private void addInputProcess(InputProcess process) {
        for (Map.Entry<CodeConfiguration, List<InputProcess>> entry : history.getHistoryAndStats().entrySet()) {
            if (compareStartupSettings(entry.getKey(), originalCodeConfig)) {
                entry.getValue().add(process);
            }
        }
    }

    private void createMachineDictionary(CTEDictionary cteDictionary) {
        String words = cteDictionary.getWords().trim();
        words = removeExcludeChars(words);
        String parts[] = words.split(" ");
        String newParts[] = removeExcludeCharsFromLast(parts);
        dictionary = new HashSet<>();
        Collections.addAll(dictionary, newParts);
    }

    private void checkIfValidNumOfAgents(int numOfAgents) {
        if (numOfAgents < 2 || numOfAgents > 50) {
            throw new InvalidNumOfAgentsException();
        }
    }

    public MachineDetailsDto getMachineDetails() {
        return new MachineDetailsDto(enigmaMachine.getRotorCount(), enigmaMachine.getKeyboard().getAlphabet(), enigmaMachine.getReflectorsList().size());
    }

    public String getBattlefieldName() {
        return battlefield.getBattleName();
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }
}
