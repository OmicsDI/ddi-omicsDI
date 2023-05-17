package uk.ac.ebi.ddi.annotation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Helper methods to simplify <a href="http://www.doi.org/">DOI</a>s (digital object identifiers).
 */
public class DOIUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DOIUtils.class);

    private static final String NON_NUMBERS_OR_LETTERS = "[^0-9\\p{L}]*?";

    private static final String DOI = "\\b(10[.][0-9]{4,}(?:[.][0-9]+)*\\/(?:(?![\"&\\'<>])\\S)+)\\b";

    /**
     * Matches a pure DOI. Disregards case.
     */
    private static final Pattern DOI_PATTERN = Pattern.compile("^" + DOI + "$", Pattern.CASE_INSENSITIVE);

    /**
     * Before the DOI we allow everything but numbers or letters (i.e., whitespace, symbols, etc.)
     * Since the DOI might include almost every character (in principle) we stop when we find the
     * first non-matching symbol.
     */
    private static final Pattern STRICT_DOI_PATTERN = Pattern.compile(
            "^" + NON_NUMBERS_OR_LETTERS + DOI + NON_NUMBERS_OR_LETTERS + "$", Pattern.CASE_INSENSITIVE);
    private static final Pattern ONLY_DOI_PATTERN = Pattern.compile(DOI, Pattern.CASE_INSENSITIVE);

    /**
     * Pattern to clean up a string containing a doi.
     */
    private static final String CLEAN_DOI = "(doi\\s*=.*?)(doi:\\s*|http://.*?)?(10\\.\\d+\\/[^\\s\"'}]+)";
    private static final Pattern CLEAN_DOI_PATTERN = Pattern.compile(
            CLEAN_DOI, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);


    /**
     * Extracts a DOI from the given string.
     *
     * @param string
     * @return The extracted DOI.
     */
    public static List<String> extractDOIs(final String string) {
        Set<String> values = new HashSet<>();
        if (string != null) {
            Matcher matcher = ONLY_DOI_PATTERN.matcher(string);
            while (matcher.find()) {
                LOGGER.debug(matcher.group(1));
                values.add(matcher.group(1));
            }
        }
        return new ArrayList<>(values);
    }

    /**
     * Checks, if the given string contains a DOI.
     * This method checks, if <strong>somewhere</strong> in the string something
     * like a DOI can be found. If you want to check, if the string contains almost
     * only a DOI, use {@link #containsOnlyDOI(String)}.
     *
     * @param string
     * @return <code>true</code>, if the given string contains a DOI.
     */
    public static boolean containsDOI(final String string) {
        return string != null && ONLY_DOI_PATTERN.matcher(string).find();
    }

    /**
     * Checks, if the given string contains (almost only) a DOI.
     *
     * @param string
     * @return <code>true</code>, if the given string contains almost only the DOI, i.e.,
     * no surrounding text (only whitespace or symbols like ",", "'", ")", etc.)
     */
    public static boolean containsOnlyDOI(final String string) {
        return string != null && STRICT_DOI_PATTERN.matcher(string).find();
    }

    /**
     * Checks, if the given string represents a DOI.
     *
     * @param doi
     * @return <code>true</code>, if the given string is a DOI
     */
    public static boolean isDOI(final String doi) {
        return doi != null && DOI_PATTERN.matcher(doi).find();
    }

    /**
     * Cleans up a doi entry. The string s can be a single Line or
     * a whole BibTeX string.
     *
     * @param s
     * @return a modification of the String s. Changes occurences of
     * <i>doi = {http://dx.doi....</i> or <i>doi = {doi:...</i> to
     * <i>doi = {...}</i>.
     */
    public static String cleanDOI(String s) {
        String replace = null;
        String target = null;
        Matcher matcher = CLEAN_DOI_PATTERN.matcher(s);
        while (matcher.find()) {
            target = matcher.group(0);
            if (matcher.group(2) != null && matcher.group(3) != null) {
                replace = matcher.group(1) + matcher.group(3);
                break;
            }
        }
        if (replace != null && target != null) {
            s = s.replace(target, replace);
        }
        return s;
    }

    public static String cleanDOITrail(String doi) {
        if (doi.endsWith(".") || doi.endsWith("]") || doi.endsWith(",")) {
            doi = doi.substring(0, doi.length() - 1);
            cleanDOITrail(doi);
        }
        return doi;
    }


}
