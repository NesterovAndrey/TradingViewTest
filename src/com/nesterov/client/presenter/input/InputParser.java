package com.nesterov.client.presenter.input;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    private static String PATTERN="[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";

    public String[] parse(String input)
    {
        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile(PATTERN);
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {

                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                matchList.add(regexMatcher.group(2));
            } else {
                matchList.add(regexMatcher.group());
            }
        }
        String[] result=new String[matchList.size()];
        matchList.toArray(result);
        return result;
    }
}
