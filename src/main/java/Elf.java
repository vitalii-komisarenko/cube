import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Elf {
    public class Section {
        public Section(long size, boolean isZero, List<Byte> data) {
            this.size = size;
            this.isZero = isZero;
            this.data = data;
        }

        long size;
        boolean isZero;
        List<Byte> data;
    }

    Elf() {}

    void addZeroInitializedSection(String name, long size) {
        sections.put(name, new Section(size, true, null));
    }

    void addSection(String name, List<Byte> data) {
        sections.put(name, new Section(data.size(), false, data));
    }

    List<Byte> encode() {
        List<Byte> res = new ArrayList<Byte>();
        return res;
    }

    void saveToFile(String filename) {
    }

    Map<String, Section> sections = new LinkedHashMap<>();
}