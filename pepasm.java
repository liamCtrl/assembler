import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class pepasm{
    private static final HashMap<String, String> pepInstruction = new HashMap<>();

    static {
        pepInstruction.put("LDBA", "D0");
        pepInstruction.put("STBA", "F1");
        pepInstruction.put("LDWA", "C0");
        pepInstruction.put("STWA", "E1");
        pepInstruction.put("ANDA", "80");
        pepInstruction.put("ASLA", "0A");
        pepInstruction.put("ASRA", "0C");
        pepInstruction.put("STOP", "00");
        pepInstruction.put("CPBA", "B0");
        pepInstruction.put("BRNE", "1A");
        pepInstruction.put(".END", "zz");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        new pepasm().assemble(args[0]);
    }

    public void assemble(String file) {
        try {
            String fileContent = readFile(file);
            StringBuilder output = new StringBuilder();

            for (String line : fileContent.split("\n")) {
                line = line.trim();
                if (!line.isEmpty()) {
                    processLine(line, output);
                }
            }

            if (!output.isEmpty()) {
                output.setLength(output.length() - 9);
                output.append("zz");
            }

            System.out.println(output.toString().trim());
        } catch (IOException e) {
        }
    }

    private void processLine(String line, StringBuilder output) {
        String[] parts = line.split("\\s+");
        String opcode = pepInstruction.get(parts[0]);
        if (opcode != null) {
            output.append(opcode).append(" ");
            String operand = (parts.length > 1) ? parseOperand(parts[1]) : "00";
            output.append(operand).append(" ");
        }
    }

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

    private String parseOperand(String operand) {
        operand = operand.replaceAll("0x", "").replaceAll("[^0-9A-Fa-f]", "");

        try {
            if (operand.length() == 4) {
                return operand.substring(0, 2) + " " + operand.substring(2, 4);
            } else {
                return String.format("%02X", Integer.parseInt(operand, 16));
            }
        } catch (NumberFormatException e) {
            return "00";
        }
    }
}
