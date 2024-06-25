package com.bloducspauter.newexcel.read;

import com.bloducspauter.enums.ExcelType;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import com.bloducspauter.newexcel.wrapper.ReadWrapper;
import lombok.NonNull;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
    @Setter
    private Workbook workbook;
    ExcelTool excelTool = new ExcelTool();

    public Workbook getWorkbook() {
        if (workbook == null) {
            throw new NullPointerException();
        }
        return workbook;
    }

    /**
     * 获取工作簿
     */
    public Workbook getWorkbook(@NonNull ReadWrapper readWrapper) throws IOException, GeneralSecurityException {
        return getWorkbook(readWrapper.getPath(), readWrapper.getPassword());
    }

    /**
     * 获取工作簿
     *
     * @throws GeneralSecurityException 加密异常
     * @throws IOException 文件读取异常
     * @throws IllegalArgumentException 文件类型不支持
     */
    public Workbook getWorkbook(String path, String password) throws GeneralSecurityException, IOException {
        String suffix = excelTool.getSuffix(path);
        InputStream in;
        in = verify(path, password);
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
                String err = "This CSV file is a text file. Please use TextUtil(" + path + ")";
                throw new UnsupportedFileException(err);
            default:
                throw new IllegalArgumentException("Unsupported file type");
        }
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Input stream close error");
        }
        return workbook;
    }


    /**
     * 验证密码是否正确
     *
     * @param path 文件路径
     * @throws IOException              文件读取异常
     * @throws GeneralSecurityException 加密异常
     * @throws IllegalArgumentException 密码错误
     */
    public InputStream verify(String path, String password) throws IOException, GeneralSecurityException {
        InputStream in = new FileInputStream(path);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
        // 创建一个 EncryptionInfo 对象，用于获取加密信息
        EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
        // 创建一个 Decryption 对象，用于解密文件
        Decryptor decryptor = Decryptor.getInstance(encInfo);
        // 使用 Decryption 对象，验证密码是否正确
        boolean isPasswordCorrect = decryptor.verifyPassword(password);
        if (!isPasswordCorrect) {
            poifsFileSystem.close();
            in.close();
            // 如果密码正确，则使用 Decryption 对象，获取解密后的文件流
            // 如果密码错误，则抛出一个 IllegalArgumentException 异常
            if (password == null) {
                throw new IllegalArgumentException("The document is encrypted, please enter the password");
            }
            throw new IllegalArgumentException("The password is wrong,please check the password");
        }
        return decryptor.getDataStream(poifsFileSystem);
    }
}
