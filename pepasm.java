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
                output.setLength(output.length() );
            }

            System.out.println(output.toString().trim());
        } catch (IOException e) {
        }
    }

    private void processLine(String line, StringBuilder output) {
        String[] parts = line.split("\\s+");
        String instruction  = parts[0];
        String operandcode = pepInstruction.get(instruction);

        if (operandcode != null) {
            output.append(operandcode).append(" ");
            if (parts.length > 1) {
                String operand = parseOperand(parts[1]);
                output.append(operand).append(" ");
            } else {
                if (!instruction.equals("ASLA") && !instruction.equals("ASRA") && !instruction.equals("STOP")) {
                    output.append("00 ").append(" ");
                }
            }
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
