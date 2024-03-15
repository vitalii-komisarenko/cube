import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class PreprocessorCli {
    private static String getInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static void main(String []args) {
        String input = getInput();
        String output = Preprocessor.runPreprocessor(input);
        System.out.print(output);
    }
}