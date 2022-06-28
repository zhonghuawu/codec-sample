package com.envisioniot.payload;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:25:33
 */
public interface ICodec<T> {

    byte[] encode(T t);

    T decode(byte[] bytes);

}
