package shared;


import akka.japi.Pair;
import shared.AkkaMessages.modifyGraph.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static enum Type {
        // Token types cannot have underscores
        COMMAND("vertex|edge"),
        DELETE("DEL|DELETE"),
        ATTROPEN("<"),
        ATTRCLOSE(">"),
        EQUALITY("="),
        STRING("[-|?|^_|.|0-9|a-z|A-Z]+"),
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
            } else if (matcher.group(Type.DELETE.name()) != null) {
                tokens.add(new Token(Type.DELETE, matcher.group(Type.DELETE.name())));
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
    }

    public static List<ModifyGraphMsg> parse(List<Token> tokens){
        ArrayList<ModifyGraphMsg> messages = new ArrayList<>();
        Iterator<Token> iterator = tokens.iterator();
        /*
            0:initial state
            1:vertex keyword inserted
            2:edge keyword inserted
            3:name inserted for vertex
            4:name inserted for edge
            5:delete
            6:< inserted
            7:attrName inserted
            8:eq
            9:DEL/STR
            10:> inserte
         */
        int state = 0;
        String name1 = null;
        String name2 = null;
        String attrName = null;
        ArrayList<Pair<String, String>> attributes = new ArrayList<>();
        Token next;


        while(iterator.hasNext()){
            next = iterator.next();

            if (state == 0 && next.data.equals("vertex")){
                name1 = null;
                name2 = null;
                attributes = new ArrayList<>();
                state = 1;
            } else if (state == 0 && next.data.equals("edge")){
                name1 = null;
                name2 = null;
                attributes = new ArrayList<>();
                state = 2;
            } else if (state == 1 && next.type == Type.STRING){
                state = 3;
                name1 = next.data;
            } else if (state == 2 && next.type == Type.STRING){
                state = 4;
                name1 = next.data;
            } else if ((state == 3 || state ==5)&& next.data.equals("<")){
                state = 6;
            } else if ((state == 3 || state ==5)&& next.type == Type.DELETE){
                state = 0;
                if (name2==null)  messages.add(new DeleteVertexMsg(name1, null));
                else messages.add(new DeleteEdgeMsg(name1, name2, null));
            } else if (state == 4 && next.type == Type.STRING) {
                state = 5;
                name2 = next.data;
            } else if ((state == 6 || state == 9) && next.data.equals(">")){
                state = 0;
                if (name2 == null) messages.add(new UpdateVertexMsg(name1, attributes, null));
                else messages.add(new AddEdgeMsg(name1, name2, addEdgeName(attributes, name2), null));
            } else if (state == 6 && next.type == Type.STRING){
                attrName = next.data;
                state = 7;
            } else if (state == 7 && next.type == Type.EQUALITY){
                state = 8;
            } else if (state == 8 && (next.type == Type.DELETE||next.type == Type.STRING)){
                state = 9;
                attributes.add(new Pair<>(attrName, next.data));
            } else {
                System.err.println("Parsing error: " + next.toString());
            }
        }
        return messages;
    }

    private static ArrayList<Pair<String, String>> addEdgeName(ArrayList<Pair<String, String>> attributes, String destination) {
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        for (Pair<String, String> attribute : attributes) {
            result.add(new Pair<>(destination + "_" + attribute.first(), attribute.second()));
        }
        return result;
    }

    public static void main(String[] args) {
        String input = "vertex pippo <ciao=hello melma=infangata, opla=DELETE> edge pluto paperino";

        // Create tokens and print them
        ArrayList<Token> tokens = lex(input);
        for (Token token : tokens)
            System.out.println(token);
    }
}
