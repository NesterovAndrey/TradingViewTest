package com.nesterov.core.json;


import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.JSONListAdapter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
//Плохой маппер для преобразования JSON в Map
public class JSONMapper {
    private ScriptEngine engine=new ScriptEngineManager().getEngineByName("javascript");

        public Map map(String json)
        {
            Map result=null;
            try {
                String string=new String(json);
                result=(Map)engine.eval("Java.asJSONCompatible(" + json+ ")");
            } catch (ScriptException e) {
                throw new RuntimeException("Cant parse JSON");
            }
            Map<Object,List> map=new HashMap();
            JSONListAdapter jsonListAdapter=(JSONListAdapter)result.get("data");
            jsonListAdapter.forEach((ob)->{
                ScriptObjectMirror object=(ScriptObjectMirror)ob;
                object.forEach((strKey,somOb)->
                {
                    if(!map.containsKey(strKey))
                    {
                        map.put(strKey,new ArrayList());
                    }
                    map.get(strKey).add(somOb);
                });
            });

            return map;
        }
}
