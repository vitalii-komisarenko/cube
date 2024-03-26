class Preprocessor {
    public static String convertLineTerminationsToUnix(String sourceCode) {
        return sourceCode.replaceAll("\\\\\\r", "");
    }
    public static String processLineContinuation(String sourceCode) {
        return sourceCode.replaceAll("\\\\\\n", "");
    }
    public static String runPreprocessor(String sourceCode) {
        sourceCode = convertLineTerminationsToUnix(sourceCode);
        sourceCode = processLineContinuation(sourceCode);
        return sourceCode;
    }
}