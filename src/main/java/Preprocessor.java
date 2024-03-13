class Preprocessor {
    public static String processLineContinuation(String sourceCode) {
        return sourceCode.replaceAll("\\\n");
    }
}