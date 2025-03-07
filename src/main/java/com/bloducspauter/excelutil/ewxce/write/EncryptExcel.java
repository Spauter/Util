package com.bloducspauter.excelutil.ewxce.write;


import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import com.bloducspauter.excelutil.base.exceptions.WrongPasswordException;
import com.bloducspauter.excelutil.enums.ExcelType;
import com.bloducspauter.excelutil.origin.tool.ExcelTool;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.*;
import java.security.GeneralSecurityException;
/**
 * 加密文档
 * @author Bloduc Spauter
 *
 */
public class EncryptExcel {
    public static void encryptExcl(File file, String password) throws IOException, GeneralSecurityException, InvalidFormatException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found:" + file.getAbsolutePath());
        }
        if (password == null) {
            throw new WrongPasswordException("Please provide a password");
        }
        String suffix=new ExcelTool().getSuffix(file.getName());
        switch (ExcelType.forSuffix(suffix)) {
            case XLS:
                encryptFor2003Excel(file,password);
                break;
            case XLSM:
            case XLSX:
                encryptFor2007Excel(file,password);
                break;
            case CSV:
                throw new UnsupportedFileException("The CSV document is a text file");
            default:
                throw new UnsupportedFileException("Unsupported file type");
        }
    }

    private static void encryptFor2003Excel(File file,String password) throws IOException {
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook wb = new HSSFWorkbook(poifsFileSystem);
        Biff8EncryptionKey.setCurrentUserPassword(password);
        wb.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "管理员");
        wb.unwriteProtectWorkbook();
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
    }

    private static void encryptFor2007Excel(File file, String password) throws InvalidFormatException, GeneralSecurityException, IOException {
        // 设置密 码 保 护 ·
        POIFSFileSystem fs = new POIFSFileSystem();
        EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        Encryptor enc = info.getEncryptor();
        //设置密码
        enc.confirmPassword(password);
        //加密文件
        OPCPackage opc = OPCPackage.open(file, PackageAccess.READ_WRITE);
        OutputStream os = enc.getDataStream(fs);
        opc.save(os);
        opc.close();
        // 这一步特别注意，导出之前一定要先关闭加密文件流，不然导出文件会损坏而无法打开
        os.close();
        //把加密后的文件写回到流
        FileOutputStream fos = new FileOutputStream(password);
        fs.writeFilesystem(fos);
        fos.close();
    }

    public static void encryptExcl(String path,String password) throws IOException, GeneralSecurityException, InvalidFormatException {
        File file=new File(path);
        encryptExcl(file,password);
    }
}
