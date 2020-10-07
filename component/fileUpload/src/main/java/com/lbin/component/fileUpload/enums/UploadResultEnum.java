package com.lbin.component.fileUpload.enums;

import com.lbin.common.exception.interfaces.ResultInterface;
import lombok.Getter;

/**
 * 后台返回结果集枚举
 * @author
 * @date 2018/8/14
 */
@Getter
public enum UploadResultEnum implements ResultInterface {

    /**
     * 文件操作
     */
    NO_FILE_500(500, "错误：2001-IO传输失败"),//文件传输错误
    NO_FILE_Sha1(500, "错误：2002-IO校检Sha1失败"),//文件传输错误
    NO_FILE_Local(500, "错误：2003-文件不存在"),
    NO_FILE_Delete(500, "错误：2004-本地文件删除失败"),

    NO_FILE_NULL(401, "文件不能为空"),
    NO_FILE_TYPE(402, "不支持该文件类型"),

    ;

    private Integer code;

    private String message;

    UploadResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
