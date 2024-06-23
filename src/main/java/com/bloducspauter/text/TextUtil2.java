package com.bloducspauter.text;

import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import java.util.List;
public class TextUtil2<T> extends TextUtil{
    private TableDefinition tableDefinition;

    public TextUtil2(Class<?> entity) {
        tableDefinition= MyAnnotationConfigApplicationContext.getTableDefinition(entity);
    }

    public List<T>read(String path){
        return null;
    }
}
