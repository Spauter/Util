package com.bloducspauter.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.excelutil.base.init.FiledPropertyLoader;
import com.bloducspauter.excelutil.base.init.TableDefinition;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Bloduc Spauter
 *
 */
public class TextUtil2<T> extends TextUtil {
    private final TableDefinition tableDefinition;

    public TextUtil2(Class<?> entity) {
        tableDefinition = FiledPropertyLoader.getTableDefinition(entity);
    }

    private List<T> read(String path, String separator) throws IOException, NoSuchFieldException {
        separator = separator == null ? TXT_CSV_SEPARATOR : separator;
        List<T> list = new ArrayList<>();
        File file = new File(path);
        FileReader iis = null;
        BufferedReader bfr = null;
        iis = new FileReader(file);
        bfr = new BufferedReader(iis);
        String info;
        while ((info = bfr.readLine()) != null) {
            if (first) {
                title = info.split(separator);
                first = false;
            }
            Map<String,Object>map=checkData(info,separator);
            String jsonString = JSON.toJSONString(map);
            Object o = JSONObject.parseObject(jsonString, tableDefinition.getClassName());
            try {
                list.add((T) o);
            } catch (ClassCastException e) {
                System.out.println("Loading info failed:"+o.toString() );
            }
        }
        return list;
    }

    @Override
    Map<String, Object> checkData(String s, String separator) throws NoSuchFieldException {
        separator = separator == null ? TXT_CSV_SEPARATOR : separator;
        Map<String, Object> map = new HashMap<>();
        String[] infos = s.split(separator);
        for (int i = 0; i < title.length; i++) {
            Field field = tableDefinition.getCellNameAndField().get(title[i]);
            if (field == null) {
               throw new NoSuchFieldException("No fields found");
            }
            if (i < infos.length) {
                map.put(field.getName(), infos[i]);
            }
        }
        return map;
    }

    public List<T>readAll(String path) throws IOException, NoSuchFieldException {
        return  read(path,TXT_CSV_SEPARATOR);
    }

    public List<T>readAll(String path,String separator) throws IOException, NoSuchFieldException {
        return  read(path,separator);
    }

}
