package com.sarief.dao;

import com.sarief.dto.Page;
import com.sarief.entity.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stab implementation for dao
 * note: since task does not specify use of spring repository or DB in general, simple stab will do
 */
@Component
public class TextDaoStab implements TextDao {

    private Map<String, Data> storage = new HashMap<>();

    @Override
    public void addAll(List<Data> dataList) {
        dataList.forEach(data -> storage.put(data.getId(), data));
    }

    @Override
    public List<Data> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Data get(String id) {
        return storage.get(id);
    }

    @Override
    public void remove(String id) {
        Data data = storage.get(id);
        if (data == null) {
            throw new UnsupportedOperationException("Data with id " + id + " not found");
        }
        storage.remove(id);
    }

    @Override
    public Page getAll(String pageNumber, String size, String timestampMin, String timestampMax) {
        List<Data> all = getAll();
        List<Data> filtered = all.stream().filter(data ->
                Long.valueOf(timestampMin) < Long.valueOf(data.getTimestamp())
                && Long.valueOf(data.getTimestamp()) < Long.valueOf(timestampMax))
                .collect(Collectors.toList());

        List<Data> pageVals = selectPage(filtered, pageNumber, size);

        Page page = new Page(pageVals, pageNumber, filtered.size());

        return page;
    }

    private List<Data> selectPage(List<Data> filtered, String pageNumberString, String sizeString) {
        Integer size = Integer.valueOf(sizeString);
        Integer pageNumber = Integer.valueOf(pageNumberString);
        int offset = (pageNumber -1) * size;
        if (filtered.size() <= offset) {
            throw new UnsupportedOperationException("Page does not exist");
        }
        List<Data> selected = new ArrayList<>();
        for (int i = offset; i < offset + size && i < filtered.size(); i++) {
            selected.add(filtered.get(i));
        }
        return selected;
    }
}
