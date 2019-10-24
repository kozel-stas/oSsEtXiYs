package com.oSsEtXiYs.rest.model;

public class Essay {

    private final String source;
    private final String result;
    private final EssayMethod method;

    public Essay(String source, String result, EssayMethod method) {
        this.source = source;
        this.result = result;
        this.method = method;
    }

    public String getResult() {
        return result;
    }

    public String getSource() {
        return source;
    }

    public EssayMethod getMethod() {
        return method;
    }

}
