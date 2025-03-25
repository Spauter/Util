package com.bloducspauter.text;

import com.bloducspauter.excelutil.base.service.FIleReadAndOutput;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 * @since 1.18.2
 */
public interface TextService extends FIleReadAndOutput {

    /**
     * ��ȡ�ĵ�·��,������洢��List������
     * @param path �ļ�·��
     * @param separator �ָ��������CSV�ļ��ָ���Ϊ������
     */
     List<Map<String,Object>> readToList(String path,String separator) throws Exception;

    /**
     *  ��ȡ�ĵ�·��,������洢��List������
     * @param file �ļ�
     * @param separator �ָ���
     */
     default  List<Map<String,Object>> readToList(File file, String separator) throws Exception {
         return readToList(file.getAbsolutePath(),separator);
     }

    /**
     *  ��ȡ�ĵ�·��,������洢��Lһ����λ����
     * @param path �ļ�·��
     * @param separator �ָ���
     */
     Object[][] readToArray(String path,String separator) throws Exception;

    /**
     *  ��ȡ�ĵ�·��,������洢��Lһ����λ����
     * @param file �ļ�
     * @param separator �ָ���
     */
     default  Object[][] readToArray(File file,String separator) throws Exception {
         return readToArray(file.getAbsolutePath(),separator);
     }

    /**
     *  ������ı��ļ�
     * @param list ���ݼ���
     * @param path �ļ�·��
     * @param separator �ָ���
     */
    void output(List<Map<String, Object>> list, String path,String separator) throws Exception;

    /**
     *  ������ı��ļ�
     * @param list ���ݼ���
     * @param file �ļ�
     */
     default void output(List<Map<String, Object>> list, File file,String separator) throws Exception {
         output(list,file.getAbsolutePath(),separator);
     }

    /**
     *  ������ı��ļ�
     * @param obj ��ά����
     * @param path �ļ�·��
     * @param separator �ָ���
     */
    void output(Object[][] obj, String path,String separator) throws Exception;

    /**
     *  ������ı��ļ�
     * @param obj ��ά����
     * @param file �ļ�
     */
     default void output(Object[][] obj, File file,String separator) throws Exception{
         output(obj, file.getAbsolutePath(),separator);
     }

    String[] getTitle();
}
