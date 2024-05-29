package com.bloducspauter.excel.tool;


import com.bloducspauter.origin.service.ValidationTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloducspauter.origin.Util.*;

/**
 * 表格文档检查工具
 *
 * @author Bloduc Spauter
 * @version 1.16
 */
public class ExcelTool extends ValidationTool {
    private final List<Map<String, Object>> list = new ArrayList<>();


    @Override
    protected void checkSuffix(File file) {

    }
}
