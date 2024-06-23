package com.bloducspauter.excel.read;

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
 *  @author Bloduc Spauter
 */
public class WorkBookReader {
    private Workbook workbook;
    private Sheet sheet;

    /**
     *  获取工作簿
     */
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
     * @throws IllegalArgumentException 工作表索引不存在
     * @throws IndexOutOfBoundsException 工作表索引超出范围
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
     *  验证密码是否正确
     * @param readWrapper 读取包装类
     * @throws IOException 文件读取异常
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
        String openPassword=readWrapper.getPassword();
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

    public int getMaxRow(){
        return sheet.getLastRowNum();
    }

    public int getMaxColumn(int index){
        return sheet.getRow(0).getLastCellNum();
    }
}
