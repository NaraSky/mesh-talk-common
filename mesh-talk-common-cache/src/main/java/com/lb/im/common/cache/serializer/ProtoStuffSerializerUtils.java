package com.lb.im.common.cache.serializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ProtoStuffSerializerUtils {

    /**
     * 将对象序列化为字节数组。
     *
     * @param obj 待序列化的对象，不能为空
     * @return 序列化后的字节数组
     * @throws RuntimeException 若对象为null或序列化过程中发生异常
     */
    public static <T> byte[] serialize(T obj) {
        // 参数校验：确保对象不为空
        if (obj == null) {
            throw new RuntimeException("序列化对象(" + obj + ")!");
        }
        // 根据对象类型获取对应的Protobuf schema
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        // 分配足够大小的缓冲区用于序列化（初始大小1MB）
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        try {
            // 执行实际的序列化操作
            protostuff = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!", e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    /**
     * 将字节数组反序列化为目标类的实例。
     *
     * @param paramArrayOfByte 待反序列化的字节数组
     * @param targetClass      目标类类型
     * @return 反序列化后的对象实例
     */
    public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
        }

        T instance = null;
        // 尝试根据目标类创建实例，捕获并转换可能的异常为运行时异常
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException e1) {
            throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e1);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e2);
        }

        // 使用Protostuff根据模式将字节数组反序列化到目标实例中
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
        return instance;
    }

    /**
     * 将对象列表序列化为字节数组
     *
     * @param objList 需要序列化的对象列表，不能为空且不能为空列表
     * @return 序列化后的字节数组
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException("序列化对象列表(" + objList + ")参数异常!");
        }

        // 获取对象对应的Protostuff序列化Schema
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());

        // 预分配1MB大小的缓冲区用于序列化操作
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protostuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            // 执行列表对象的序列化操作
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("序列化对象列表(" + objList + ")发生异常!", e);
        } finally {
            buffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return protostuff;
    }


    /**
     * 将字节数组反序列化为指定类型的列表对象。
     *
     * @param paramArrayOfByte 待反序列化的字节数组，不能为空或空数组
     * @param targetClass      列表元素的目标类型
     * @return 反序列化后的列表对象
     */
    public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
        }

        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        // 反序列化字节数组为列表对象
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
        } catch (IOException e) {
            throw new RuntimeException("反序列化对象列表发生异常!", e);
        }
        return result;
    }


}
