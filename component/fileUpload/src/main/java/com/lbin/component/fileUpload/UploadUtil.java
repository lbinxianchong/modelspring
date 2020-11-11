package com.lbin.component.fileUpload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.MD5;
import com.lbin.common.exception.ResultException;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.component.fileUpload.enums.UploadResultEnum;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传处理工具
 *
 * @author
 * @date 2018/11/4
 */
public class UploadUtil {

    public static Digester sha1 = SecureUtil.sha1();
    public static MD5 md5 = SecureUtil.md5();

    //支持格式
    public static Map<String, String> ext = new HashMap<String, String>() {{
        put("mp4", "video/mpeg4");
        put("mp3", "audio/mp3");
        put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put("xls", "application/vnd.ms-excel");
        put("zip", "application/zip");
    }};


    /**
     * 创建一个Upload实体对象
     * 获取本地文件(路径)
     *
     * @return
     */
    public static Upload newFile() {
        Upload upload = new Upload();
        String uuid = IdUtil.simpleUUID();
        upload.setUuid(uuid);
        return upload;
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(路径)
     *
     * @param path
     * @return
     */
    public static Upload file(String path) {
        return file(path, null);
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(路径)
     *
     * @param path
     * @return
     */
    public static Upload file(String path, String prefix) {
        path = StrUtil.addPrefixIfNot(path, prefix);
        Upload upload = newFile();
        upload.setName(FileUtil.getName(path));
        upload.setPath(path);
        return file(upload);
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(路径)
     *
     * @param upload
     * @return
     */
    public static Upload file(Upload upload) {
        File file = FileUtil.file(upload.getPath());
        upload.setFile(file);
        return upload;
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(路径)
     *
     * @param file
     * @return
     */
    public static Upload file(File file) {
        Upload upload = newFile();
        upload.setName(FileUtil.getName(file));
        upload.setFile(file);
        return upload;
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(物理文件)
     *
     * @param file
     * @return
     */
    public static Upload getLocalFile(File file) {
        Upload upload = file(file);
        return getLocalFile(upload);
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(物理文件)
     *
     * @param path
     * @return
     */
    public static Upload getLocalFile(String path) {
        Upload upload = file(path);
        return getLocalFile(upload);
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(物理文件)
     *
     * @param path
     * @return
     */
    public static Upload getLocalFile(String path, String prefix) {
        Upload upload = file(path, prefix);
        return getLocalFile(upload);
    }

    /**
     * 创建一个Upload实体对象
     * 获取本地文件(物理文件)
     *
     * @param upload
     * @return
     */
    public static Upload getLocalFile(Upload upload) {
        if (upload.getFile() == null) {
            File file = FileUtil.file(upload.getPath());
            upload.setFile(file);
        }
        if (!FileUtil.exist(upload.getFile())) {
            throw new ResultException(UploadResultEnum.NO_FILE_Local);
        }
        return upload;
    }

    /**
     * 创建一个Upload实体对象
     * 创建本地文件(创建空文件)
     *
     * @param path
     * @return
     */
    public static Upload createFile(String path) {
        Upload upload = file(path);
        return createFile(upload);
    }

    /**
     * 创建一个Upload实体对象
     * 创建本地文件(创建空文件)
     *
     * @param path
     * @return
     */
    public static Upload createFile(String path, String prefix) {
        Upload upload = file(path, prefix);
        return createFile(upload);
    }

    /**
     * 创建一个Upload实体对象
     * 创建本地文件(创建空文件)
     *
     * @param upload
     * @return
     */
    public static Upload createFile(Upload upload) {
        File file = upload.getFile();
        if (file == null) {
            file = FileUtil.touch(upload.getPath());
        } else {
            file = FileUtil.touch(file);
        }
        upload.setFile(file);
        return upload;
    }


    /**
     * 创建一个Upload实体对象（上传对象）
     * 上传使用
     *
     * @param modulePath 文件模块路径
     */
    public static Upload getFileUpload(String name, String modulePath, String baseUrl) {

        Upload upload = newFile();

        upload = getFileUpload(upload, name, modulePath, baseUrl);

        upload = file(upload);

        return upload;
    }


    /**
     * 创建一个Upload实体对象
     */
    public static Upload getFileUpload(Upload upload, String name, String modulePath, String baseUrl) {
        String uuid = upload.getUuid();

        String fileName = genFileName(name, uuid);
        String path = modulePath + "/" + fileName;
        String url = baseUrl + "/" + fileName;

        upload.setName(name);
        upload.setFileName(fileName);

        upload.setPath(path);
        upload.setUrl(url);

        return upload;
    }

    /**
     * 创建一个Upload实体对象
     * 下载使用(url)
     */
    public static Upload getFile(String url) {
        Upload upload = newFile();
        upload.setUrl(url);
        upload.setName(FileUtil.getName(url));
        return upload;
    }

    /**
     * 创建一个Upload实体对象
     * 下载使用(file)
     * @param file
     * @return
     */
    public static Upload getFile(File file) {
        return getFile(file,file.getName(),null);
    }
    /**
     * 创建一个Upload实体对象
     * 下载使用(file)
     * @param file
     * @param name
     * @return
     */
    public static Upload getFile(File file, String name) {
        return getFile(file,name,null);
    }

    /**
     * 创建一个Upload实体对象
     * 下载使用(file)
     * @param file
     * @param name
     * @param baseUrl
     * @return
     */
    public static Upload getFile(File file, String name, String baseUrl) {
        Upload upload = newFile();
        if (file != null) {
            if (name == null) {
                name = file.getName();
            }
            upload.setFile(file);
        }else {
            upload.setUrl(baseUrl);
        }

        if (name == null) {
            name = FileUtil.getName(baseUrl);
        }

        upload.setName(name);
        return upload;
    }

    /**
     * 获取支持的格式
     */
    public static Upload getContentType(Upload upload) {
        String mime = upload.getMime();
        if (mime == null || mime.trim().length() == 0) {
            String name = upload.getName();
            String mimeType = FileUtil.getMimeType(name);
            if (mimeType == null) {
                String extName = FileUtil.extName(name);
                mimeType = ext.get(extName);
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
            }
            upload.setMime(mimeType);
        }
        return upload;
    }


    /**
     * 生成随机且唯一的文件名
     */
    public static String genFileName(String originalFilename, String simpleUUID) {
        String mainName = FileUtil.mainName(originalFilename);
        String extName = FileUtil.extName(originalFilename);
        return mainName + "-" + simpleUUID + "." + extName;
    }

    /**
     * 生成指定格式的目录名称(日期格式)
     */
    public static String genDateMkdir(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return "/" + sdf.format(new Date()) + "/";
    }

    /**
     * 保存文件及获取文件MD5值和SHA1值
     *
     * @param upload Upload
     */
    public static Upload saveFileAndDigester(InputStream inputStream, Upload upload) {
        if (FileUtil.exist(upload.getPath())) {
            File file = FileUtil.file(upload.getPath());
            upload.setFile(file);
            return upload;
        }
        File file = FileUtil.touch(upload.getPath());
        upload = saveFile(inputStream, FileUtil.getOutputStream(file), upload);
        upload.setMd5(getFileMd5(file));
        upload.setSha1(getFileSha1(file));
        upload.setFile(file);
        return upload;
    }

    /**
     * 文件传输
     *
     * @param inputStream
     * @param outputStream
     */
    public static Upload saveFile(InputStream inputStream, OutputStream outputStream, Upload upload) {
        try {
            long io = IoUtil.copyByNIO(inputStream, outputStream, IoUtil.DEFAULT_BUFFER_SIZE, null);
            if (io < 128) {
                throw new ResultException(UploadResultEnum.NO_FILE_500);
            }
            if (upload.getSize() == null) {
                upload.setSize(io);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IoUtil.close(inputStream);
        IoUtil.close(outputStream);
        return upload;
    }


    /**
     * 获取文件的SHA1值
     */
    public static String getFileSha1NoClose(InputStream inputStream) {
        return sha1.digestHex(inputStream);
    }

    /**
     * 获取文件的SHA1值
     */
    public static String getFileSha1(File file) {
        return sha1.digestHex(file);
    }


    /**
     * 获取文件的Md5值
     */
    public static String getFileMd5NoClose(InputStream inputStream) {
        return md5.digestHex(inputStream);
    }

    /**
     * 获取文件的Md5值
     */
    public static String getFileMd5(File file) {
        return md5.digestHex(file);
    }

    /**
     * addPrefix
     * path加入前缀
     *
     * @param upload
     * @return
     */
    public static Upload addPrefixPath(Upload upload, String prefix) {
        String str = upload.getPath();
        str = StrUtil.addPrefixIfNot(str, prefix);
        upload.setPath(str);
        return upload;
    }


    /**
     * removePrefix
     * path删除前缀
     *
     * @param upload
     * @return
     */
    public static Upload removePrefixPath(Upload upload, String prefix) {
        String str = upload.getPath();
        str = StrUtil.removePrefix(str, prefix);
        upload.setPath(str);
        return upload;
    }

    /**
     * addPrefix
     * path加入前缀
     *
     * @param upload
     * @return
     */
    public static Upload addPrefixUrl(Upload upload, String prefix) {
        String str = upload.getUrl();
        str = StrUtil.addPrefixIfNot(str, prefix);
        upload.setUrl(str);
        return upload;
    }


    /**
     * addPrefix
     * path删除前缀
     *
     * @param upload
     * @return
     */
    public static Upload removePrefixUrl(Upload upload, String prefix) {
        String str = upload.getUrl();
        str = StrUtil.removePrefix(str, prefix);
        upload.setUrl(str);
        return upload;
    }

    /**
     * 下载
     *
     * @param upload
     * @param request
     * @param response
     * @return
     */
    public static Upload download(Upload upload, HttpServletRequest request, HttpServletResponse response) {
        if (upload.getFile() == null) {
            upload = downloadUrl(upload, request, response);
        } else {
            upload = downloadFile(upload, response);
        }
        return upload;
    }

    /**
     * 下载Url
     *
     * @param upload
     * @param request
     * @param response
     * @return
     */
    public static Upload downloadUrl(Upload upload, HttpServletRequest request, HttpServletResponse response) {
        String url = upload.getUrl();
        if (!url.startsWith("http")) {
            String host = StrUtil.removeSuffix(request.getRequestURL(), request.getRequestURI());
            url = host + upload.getUrl();
        }
        upload.setUrl(url);
        return downloadUrl(upload, response);
    }

    /**
     * 下载Url
     *
     * @param upload
     * @param response
     * @return
     */
    public static Upload downloadUrl(Upload upload, HttpServletResponse response) {
        try {
            InputStream inputStream = URLUtil.url(upload.getUrl()).openStream();
            return downloadIO(upload, inputStream, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return upload;
    }

    /**
     * 下载File
     *
     * @param upload
     * @param response
     * @return
     */
    public static Upload downloadFile(Upload upload, HttpServletResponse response) {
        File file = upload.getFile();
        if (upload.getSize() == null) {
            upload.setSize(file.length());
        }
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
        return downloadIO(upload, inputStream, response);
    }

    /**
     * 下载文件主程序（Web）
     *
     * @param upload
     * @param inputStream
     * @param response
     * @return
     */
    public static Upload downloadIO(Upload upload, InputStream inputStream, HttpServletResponse response) {
        upload = getContentType(upload);
        if (upload.getSize() != null) {
            response.setContentLengthLong(upload.getSize());
        }
        response.setContentType(upload.getMime());
        String headerKey = "Content-Disposition";
        String filename = upload.getName();
        try {
            filename = new String(filename.getBytes(), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String headerValue = String.format("attachment; filename=\"%s\"", filename);
        response.setHeader(headerKey, headerValue);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            IoUtil.copy(inputStream, outputStream);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
        return upload;
    }

    /**
     * 删除
     *
     * @param upload
     * @return
     */
    public static boolean delete(Upload upload) {
        File file = upload.getFile();
        if (file == null) {
            file = FileUtil.file(upload.getPath());
        }
        return FileUtil.del(file);
    }

}
