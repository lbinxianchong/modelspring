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
import org.springframework.web.multipart.MultipartFile;

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
     * 创建本地文件
     *
     * @param path
     * @return
     */
    public static Upload createFile(String path) {
        Upload upload = new Upload();
        upload.setDate(new Date());
        File file = FileUtil.file(path);
        upload.setFile(file);
        upload.setName(FileUtil.getName(path));
        return upload;
    }

    /**
     * 创建一个Upload实体对象（上传对象）
     * 上传使用
     *
     * @param multipartFile MultipartFile对象
     * @param modulePath    文件模块路径
     */
    public static Upload getFileUpload(MultipartFile multipartFile, String modulePath, String baseUrl) {
        if (multipartFile.getSize() == 0) {
            throw new ResultException(UploadResultEnum.NO_FILE_NULL);
        }
        Upload upload = new Upload();
        upload.setMime(multipartFile.getContentType());
        upload.setSize(multipartFile.getSize());
        upload.setDate(new Date());
        upload.setName(genFileName(multipartFile.getOriginalFilename()));
        upload.setPath(modulePath + "/" + upload.getName());
        upload.setUrl(baseUrl + "/" + upload.getName());
        return upload;
    }

    /**
     * 创建一个Upload实体对象(url)
     * 下载使用
     */
    public static Upload getFile(String url) {
        Upload upload = new Upload();
        upload.setDate(new Date());
        upload.setUrl(url);
        upload.setName(FileUtil.getName(url));
        return upload;
    }

    /**
     * 创建一个Upload实体对象(file)
     * 下载使用
     */
    public static Upload getFile(File file, String name, String baseUrl) {
        Upload upload = new Upload();
        upload.setDate(new Date());
        if (file != null) {
            upload.setSize(file.length());
            if (name == null) {
                name = file.getName();
            }
            upload.setPath(file.getAbsolutePath());
        }
        if (name != null) {
            upload.setName(name);
            upload.setUrl(baseUrl + "/" + upload.getName());
        } else {
            upload.setName(FileUtil.getName(baseUrl));
            upload.setUrl(baseUrl);
        }
        return upload;
    }

    /**
     * 判断文件是否为支持的格式
     *
     * @param multipartFile MultipartFile对象
     * @param types         支持的文件类型数组
     */
    public static boolean isContentType(MultipartFile multipartFile, String[] types) {
        List<String> typeList = Arrays.asList(types);
        return typeList.contains(multipartFile.getContentType());
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
    public static String genFileName(String originalFilename) {
        String mainName = FileUtil.mainName(originalFilename);
        String simpleUUID = IdUtil.simpleUUID();
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
     * 获取目标文件对象
     *
     * @param upload 上传实体类
     */
    public static Upload getDestFile(Upload upload) {

        return upload;
    }

    /**
     * 保存文件及获取文件MD5值和SHA1值
     *
     * @param upload Upload
     */
    public static Upload saveFileAndDigester(MultipartFile multipartFile, Upload upload) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            upload = saveFileAndDigester(inputStream, upload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return upload;
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
        upload.setMd5(md5.digestHex(inputStream));
        upload.setMd5(sha1.digestHex(inputStream));
        upload.setFile(file);
        upload = saveFile(inputStream, FileUtil.getOutputStream(file), upload);
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
     * 下载Url
     *
     * @param upload
     * @param request
     * @param response
     * @return
     */
    public static Upload downloadAsset(Upload upload, HttpServletRequest request, HttpServletResponse response) {
        String host = StrUtil.removeSuffix(request.getRequestURL(), request.getRequestURI());
        String fileName = host + upload.getUrl();
        try {
            InputStream inputStream = URLUtil.url(fileName).openStream();
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
        try {
            IoUtil.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return upload;
    }

}
