package org.tdd;

public class StringUtils {
    public int findNumberOfOccurrences(String sentenceToScan, String characterToScanFor) {
        if(characterToScanFor.length() != 1)
            throw new IllegalArgumentException();

        char characterToCheckFor = characterToScanFor.charAt(0);
        return (int)sentenceToScan.chars().filter(currentChar -> currentChar == characterToCheckFor).count();
    }
}
