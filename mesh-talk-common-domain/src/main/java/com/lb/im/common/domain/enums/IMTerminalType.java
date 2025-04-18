package com.lb.im.common.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备类型
 */
public enum IMTerminalType {

    WEB(0, "web"),
    APP(1, "app");

    private final Integer code;
    private final String desc;

    IMTerminalType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static IMTerminalType getByCode(Integer code) {
        for (IMTerminalType deviceType : IMTerminalType.values()) {
            if (deviceType.getCode().equals(code)) {
                return deviceType;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public static List<Integer> getAllCode() {
        return Arrays.stream(values()).map(IMTerminalType::getCode).collect(Collectors.toList());
    }
}
