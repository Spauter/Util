package com.bloducspauter.excel.sheet;

import com.bloducspauter.enums.ExcelType;
import com.bloducspauter.origin.wrapper.ReadWrapper;
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
 * 2024/6/23
 * @author Bloduc Spauter
 */
public class WorkBookReader {
    private Workbook workbook;
    private Sheet sheet;


    public Workbook getWorkbook(@NonNull ReadWrapper readWrapper) throws IOException, GeneralSecurityException {
        String path = readWrapper.getPath();
        InputStream in;
        in = verify(readWrapper);
        if (path.endsWith(ExcelType.XLSX.suffix)) {
            workbook = new XSSFWorkbook(in);
        } else {
            workbook = new HSSFWorkbook(in);
        }
        return workbook;
    }

    /**
     * 获取工作表
     *
     * @param index 工作表索引
     * @return 工作表
     * @throws IndexOutOfBoundsException 索引越界异常
     */
    public Sheet getSheet(int index) {
        int max = workbook.getNumberOfSheets();
        if (index < 0) {
            throw new IllegalArgumentException("No sheet");
        }
        if (index > max) {
            throw new IndexOutOfBoundsException("The index is out of range:" + index + "/" + max);
        }
        return workbook.getSheetAt(index);
    }

    /**
     * 验证密码
     *
     * @param readWrapper 读取包装类
     * @return 输入流
     * @throws IOException 文件读取异常
     * @throws GeneralSecurityException 加密异常
     * @throws IllegalArgumentException 密码错误
     */
    public InputStream verify(ReadWrapper readWrapper) throws IOException, GeneralSecurityException {
        InputStream in = new FileInputStream(readWrapper.getPath());
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
        EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
        Decryptor decryptor = Decryptor.getInstance(encInfo);
        String openPassword=readWrapper.getPassword();
        boolean isPasswordCorrect = decryptor.verifyPassword(openPassword);
        if (!isPasswordCorrect) {
            if (openPassword == null) {
                throw new IllegalArgumentException("The document is encrypted, please enter the password");
            }
            throw new IllegalArgumentException("The password is wrong");
        }
        return decryptor.getDataStream(poifsFileSystem);
    }
}
