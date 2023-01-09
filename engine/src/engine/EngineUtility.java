package engine;

import component.Reflector;
import component.Rotor;
import dto.InputProcess;
import dto.MachineHistory;
import dto.CodeConfiguration;
import dto.Specification;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EngineUtility {
    void loadMachineData(String fullFilePath) throws JAXBException, FileNotFoundException;

    Specification getMachineSpecification();

    void setMachineSettingsManually(CodeConfiguration startupSettings);

    CodeConfiguration setMachineSettingsAutomatically();

    String encryptOrDecryptInput(String input);

    void setMachineToStartSettings();

    MachineHistory getMachineHistoryAndStatistics();

    int getMachineRotorCount();

    List<Rotor> getMachineRotorList();

    int getRotorId(int index);

    boolean isRotorAlreadyExist(List<Integer> rotors, int rotorId);

    String getMachineAlphabet();

    boolean charIsPartOfAlphabet(Character input);

    boolean isStringContainOnlyLettersFromAlphabet(String input);

    List<Reflector> getMachineReflectorList();

    String getReflectorId(int index);

    boolean isPairAlreadyExist(Map<Character, Character> plugsInUse, Character input, Character output);

    CodeConfiguration getOriginalCodeConfig();

    CodeConfiguration getCurrentCodeConfig();

    Map<Integer, Integer> getMapOfNotchDistanceFromWindow();

    void checkRotorListInput(List<Integer> rotorList);

    void checkRotorsStartPositions(String input);

    void saveCurrentMachineToFile(String fileName) throws IOException;

    Engine loadMachineFromFile(String fileName) throws ClassNotFoundException, IOException;

    String processCharacter(String input);

    void updateHistory(InputProcess inputProcess);

    Engine getDeepCopyOfEngine();

    CodeConfiguration getZeroCodeConfiguration();

    void advanceMachineCode(int taskSize);

    void setReflectorToMachine(Reflector reflector);

    void setRotorInUse(List<Rotor> rotorInUse);

    List<Rotor> getMachineRotorInUseList();

    String decryptInput(String secretMessage);

    Integer getNumOfAgents();

    String getExcludeChars();

    Set<String> getDictionary();

    boolean isDictionaryContainWordsFromInput(String text);

    String removeExcludeChars(String text);

    String[] removeExcludeCharsFromLast(String[] parts);

    void setRotorsPositions(LinkedList<Character> rotorsPositions);
}
