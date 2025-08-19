package com.mbonfim.literalura.services;

import java.util.List;

public interface JsonConversionInterface {
    <T> T getData(String json, Class<T> classe);
    <T> List<T> getList(String json, Class<T> classe);
}
