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
    NO_FILE_500(500, "IO未知错误：2001"),//文件传输错误

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
