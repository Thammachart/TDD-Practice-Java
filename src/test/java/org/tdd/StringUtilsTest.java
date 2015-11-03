package org.tdd;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class StringUtilsTest {
    private StringUtils _stringUtils;

    @Before
    public void setUp() {
        _stringUtils = new StringUtils();
    }

    @Test
    public void shouldBeAbleToCountNumberOfLettersInSimpleSentence() {
        String sentenceToScan = "TDD is awesome!";
        String characterToScanFor = "e";
        int expectedResult = 2;

        int result = _stringUtils.findNumberOfOccurrences(sentenceToScan,characterToScanFor);

        Assert.assertEquals(expectedResult,result);
    }

    @Test
    public void shouldBeAbleToCountNumberOfLettersInComplexSentence() {
        String sentenceToScan = "Once is unique, twice is a coincidence, three times is a pattern.";
        String characterToScanFor = "n";
        int expectedResult = 5;

        int result = _stringUtils.findNumberOfOccurrences(sentenceToScan,characterToScanFor);

        Assert.assertEquals(expectedResult,result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetAnIllegalArgumentExceptionWhenCharacterToScanForIsLargerThanOneCharacter() {
        String sentenceToScan = "This test should throw an exception";
        String characterToScanFor = "xx";

        _stringUtils.findNumberOfOccurrences(sentenceToScan,characterToScanFor);
    }
}
