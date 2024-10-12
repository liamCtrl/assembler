import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
public class pepasm {
    private static final HashMap<String,String> PepInstruction= new HashMap<>();
    static{
        PepInstruction.put("LDBA", "D0");
        PepInstruction.put("STBA", "F1");
        PepInstruction.put("LDWA", "C0");
        PepInstruction.put("STWA", "E1");
        PepInstruction.put("ANDA", "80");
        PepInstruction.put("ASLA", "0A");
        PepInstruction.put("ASRA", "0C");
        PepInstruction.put("STOP", "zz");
        PepInstruction.put("CPBA", "B0");
        PepInstruction.put("BRNE", "1A");

    }

    public static void main(String[] args) {

    private String readFile(String file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    private String getOperand(String instruction) {
        return PepInstruction.get(instruction);
    }
    private String parseOperand(String operand) {
        operand = operand.replaceAll("[^0-9A-Fa-f]", "");
        if (operand.isEmpty()) {
            return "00"; //
        }

        if (operand.length() > 2) {
            return "00";
        }
        try {
            return convertToHex(operand);
        } catch (NumberFormatException e) {
            return "00";
        }
    }

    private String convertToHex(String value) {
        int intValue = Integer.parseInt(value, 16);
        return String.format("%02X", intValue);
    }


}
