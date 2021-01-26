package SyntaxParsing;

import Model.statement.*;
import java.io.File;
import java.util.*;


public class SyntaxParser {
    private final List<String> instructions;

    public SyntaxParser(String file) throws Exception {
        this.instructions = getInstructions(readFile(file));
    }

    private static String readFile(String filePath) throws Exception{
        StringBuilder fileContent = new StringBuilder();
        File myFileObj = new File(filePath);
        Scanner myReader = new Scanner(myFileObj);
        while(myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.length() > 0 && !line.matches("\\s*/.*"))
                fileContent.append(line).append(' ');
        }
        myReader.close();
        System.out.println(myFileObj.getAbsolutePath());
        return fileContent.toString();
    }

    private static List<String> getInstructions(String fileContent)
    {
        String clearContent = fileContent.trim().replaceAll(";\\s+", ";");
        clearContent = clearContent.trim().replaceAll("\\s+", " ");
        // now split the file content in instructions
        String[] parts = clearContent.split(";");
        ArrayList<String> returnedParts = new ArrayList<>();
        boolean openBracket = false;
        StringBuilder s = new StringBuilder();
        for(String part : parts)
        {
            part = part.trim().replaceAll("\\s+$", "");
            // append parts for while / fork
            if(part.charAt(0) == '(' || part.matches("^fork.+") && !openBracket){
                long count1 = part.chars().filter(ch -> ch == '(').count();
                long count2 = part.chars().filter(ch -> ch == ')').count();
                if(count1 > count2) {
                    openBracket = true;
                    s = new StringBuilder(part + ';');
                }
            }
            else if(openBracket){
                if(part.charAt(part.length()-1) == ')' &&
                   part.chars().filter(ch->ch=='(').count() < part.chars().filter(ch->ch==')').count()){
                    openBracket = false;
                    s.append(part);
                    part = s.toString();
                }
                else s.append(part).append(';');
            }
            if(!openBracket)
                returnedParts.add(part);
        }
        return returnedParts;
    }

    protected static Statement getProgramRec(List<String> instructions, int idx) throws Exception{
        if(idx < 0 || idx >= instructions.size())
            throw new Exception("Instructions index out of bounds!");
        Statement current = StatementParser.getSingleStatement(instructions.get(idx));
        if(idx + 1 == instructions.size())
            return current;
        return new CompStmt(current, getProgramRec(instructions, idx+1));
    }

    public Statement getProgram() throws Exception{
        return getProgramRec(this.instructions, 0);
    }
}
