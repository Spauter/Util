package com.bloducspauter.newexcel.read;

import com.bloducspauter.enums.ExcelType;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import com.bloducspauter.newexcel.wrapper.ReadWrapper;
import lombok.NonNull;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * @author Bloduc Spauter
 * @version 1.19
 */
public class WorkbookReader {
    private Workbook workbook;
    private Sheet sheet;
    ExcelTool excelTool = new ExcelTool();

    /**
     * 获取工作簿
     */
    public Workbook getWorkbook(@NonNull ReadWrapper readWrapper) throws IOException, GeneralSecurityException {
        String path = readWrapper.getPath();
        String suffix = excelTool.getSuffix(path);
        InputStream in;
        in = verify(readWrapper);
        switch (ExcelType.forSuffix(suffix)) {
            // 2003 版本以下的 Excel 文件
            case XLS:
                workbook = new HSSFWorkbook(in);
                break;
            // 2007 版本以上的 Excel 文件
            case XLSX:
            case XLSM:
                workbook = new XSSFWorkbook(in);
                break;
            // 其他文件类型
            case CSV:
                String err="This CSV file is a text file. Please use TextUtil("+readWrapper.getPath()+")";
                throw new UnsupportedFileException(err);
            default:
                throw new IllegalArgumentException("Unsupported file type");
        }
        return workbook;
    }

    /**
     * 获取工作表
     *
     * @param index 工作表索引
     * @throws IllegalArgumentException  工作表索引不存在
     * @throws IndexOutOfBoundsException 工作表索引超出范围
     */
    public Sheet getSheet(int index) {
        int max = workbook.getNumberOfSheets();
        if (index < 0) {
            throw new NullPointerException("No sheet");
        }
        if (index > max) {
            throw new IndexOutOfBoundsException("The index is out of range:" + index + "/" + max);
        }
        sheet = workbook.getSheetAt(index);
        return sheet;
    }

    /**
     * 验证密码是否正确
     *
     * @param readWrapper 读取包装类
     * @throws IOException              文件读取异常
     * @throws GeneralSecurityException 加密异常
     * @throws IllegalArgumentException 密码错误
     */
    public InputStream verify(ReadWrapper readWrapper) throws IOException, GeneralSecurityException {
        // 创建一个输入流，用于读取要解密的文件
        InputStream in = new FileInputStream(readWrapper.getPath());
        // 创建一个 POIFSFileSystem 对象，用于读取 POIFS 文件系统
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
        // 创建一个 EncryptionInfo 对象，用于获取加密信息
        EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
        // 创建一个 Decryption 对象，用于解密文件
        Decryptor decryptor = Decryptor.getInstance(encInfo);
        // 定义一个字符串，用于存储打开文件的密码
        String openPassword = readWrapper.getPassword();
        // 使用 Decryption 对象，验证密码是否正确
        boolean isPasswordCorrect = decryptor.verifyPassword(openPassword);
        if (!isPasswordCorrect) {
            poifsFileSystem.close();
            in.close();
            // 如果密码正确，则使用 Decryption 对象，获取解密后的文件流
            // 如果密码错误，则抛出一个 IllegalArgumentException 异常
            if (openPassword == null) {
                throw new IllegalArgumentException("The document is encrypted, please enter the password");
            }
            throw new IllegalArgumentException("The password is wrong");
        }
        return decryptor.getDataStream(poifsFileSystem);
    }

    /**
     * 获取最大行数
     *
     * @return 最大行数
     */
    public int getMaxRow() {
        return sheet.getLastRowNum();
    }

    /**
     *  获取最大列数
     * @param index 行索引
     * @return 最大列数
     */
    public int getMaxColumn(int index) {
        return sheet.getRow(index).getLastCellNum();
    }
}
