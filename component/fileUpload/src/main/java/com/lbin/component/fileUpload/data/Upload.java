package com.lbin.component.fileUpload.data;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
public class Upload implements Serializable {

    /** 文件名 */
    private String name;

    /** 文件路径 */
    private String path;

    /** url路径 */
    private String url;

    /** 文件类型 */
    private String mime;

    /** 文件大小 */
    private Long size;

    /** 文件md5值 */
    private String md5;

    /** 文件sha1值 */
    private String sha1;

    /** 随机UUID */
    private String uuid;

    /** 时间 */
    private Date date;

    private File file;

}
