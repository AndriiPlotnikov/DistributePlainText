package com.sarief.dao;

import com.sarief.dto.Page;
import com.sarief.entity.Data;

import java.util.List;

/**
 * Dao for text received
 */
public interface TextDao {

    void addAll(List<Data> dataList);

    List<Data> getAll();

    Data get(String id);

    void remove(String id);

    Page getAll(String page, String size, String timestampMin, String timestampMax);
}
