package shared;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static enum Type {
        // Token types cannot have underscores
        COMMAND("vertex|edge"),
        NULLVALUE("[n|N][u|U][l|L][l|L]"),
        ATTROPEN("<"),
        ATTRCLOSE(">"),
        EQUALITY("="),
        STRING("[-|?|.|0-9|a-z|A-Z]+"),
        WHITESPACE("[ \t\f\r\n]+");

        public final String pattern;

        private Type(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class Token {
        public Type type;
        public String data;

        public Token(Type type, String data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", type.name(), data);
        }
    }

    public static ArrayList<Token> lex(String input) {

        ArrayList<Token> tokens = new ArrayList<Token>();

        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (Type type : Type.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            if (matcher.group(Type.COMMAND.name()) != null) {
                tokens.add(new Token(Type.COMMAND, matcher.group(Type.COMMAND.name())));
                continue;
            } else if (matcher.group(Type.NULLVALUE.name()) != null) {
                tokens.add(new Token(Type.NULLVALUE, matcher.group(Type.NULLVALUE.name())));
                continue;
            } else if (matcher.group(Type.ATTROPEN.name()) != null) {
                tokens.add(new Token(Type.ATTROPEN, matcher.group(Type.ATTROPEN.name())));
                continue;
            } else if (matcher.group(Type.ATTRCLOSE.name()) != null) {
                tokens.add(new Token(Type.ATTRCLOSE, matcher.group(Type.ATTRCLOSE.name())));
                continue;
            } else if (matcher.group(Type.EQUALITY.name()) != null) {
                tokens.add(new Token(Type.EQUALITY, matcher.group(Type.EQUALITY.name())));
                continue;
            } else if (matcher.group(Type.WHITESPACE.name()) != null) {
                continue;
            } else if (matcher.group(Type.STRING.name()) != null) {
                tokens.add(new Token(Type.STRING, matcher.group(Type.STRING.name())));
                continue;
            }
        }

        return tokens;

        //Todo: public static command solver
    }

    public static void main(String[] args) {
        String input = "vertex pippo <ciao=hello melma=infangata, > edge pluto paperino";

        // Create tokens and print them
        ArrayList<Token> tokens = lex(input);
        for (Token token : tokens)
            System.out.println(token);
    }
}
