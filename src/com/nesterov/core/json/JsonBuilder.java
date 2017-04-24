package com.nesterov.core.json;

//Строит JSON из полученных данных
public class JsonBuilder<T,U> {
    private final StringBuilder stringBuilder=new StringBuilder();
    private String coma="";
    public JsonBuilder()
    {
        stringBuilder.append("{\"data\":[");
    }
    //Добавляет запятую к каждому предыдущему элементу
    public void append(String key,String value)
    {
        stringBuilder.append(String.format("%s\n{\"%s\":\"%s\"}",coma,key,value));
        coma=",";
    }
    public String build()
    {
        stringBuilder.append("]}");
        String build=stringBuilder.toString();
        return stringBuilder.toString();
    }

}
