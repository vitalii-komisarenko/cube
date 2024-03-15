class Preprocessor {
    public static String processLineContinuation(String sourceCode) {
        return sourceCode.replaceAll("\\\\\\n", "");
    }
    public static String runPreprocessor(String sourceCode) {
        sourceCode = processLineContinuation(sourceCode);
        return sourceCode;
    }
}