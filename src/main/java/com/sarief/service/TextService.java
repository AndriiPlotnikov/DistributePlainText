package com.sarief.service;

import com.sarief.dao.TextDao;
import com.sarief.dto.Page;
import com.sarief.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Text service
 */
@Service
public class TextService {
    public static final String HEADER_ROW = "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP";

    @Autowired
    private TextDao textDao;

    /**
     * Parse input text
     *
     * @param text text
     */
    public void parseText(String text) {
        if (text == null) {
            throw new UnsupportedOperationException("Text not received");
        }
        String[] split = text.split("\\n");
        validate(split);
        List<Data> dataList = parseDataRows(split);
        textDao.addAll(dataList);
    }

    private List<Data> parseDataRows(String[] split) {
        List<Data> dataList = new ArrayList<>();
        for (int i = 1; i < split.length - 1; i++) {
            Data data = new Data();
            String row = split[i];
            String[] rowSplit = row.split(",");

            if (rowSplit.length != 4) {
                throw new UnsupportedOperationException("invalid row, " + String.join(",", rowSplit));
            }
            data.setId(rowSplit[0]);
            data.setName(rowSplit[1]);
            data.setDescription(rowSplit[2]);
            data.setTimestamp(rowSplit[3]);

            dataList.add(data);
        }
        return dataList;
    }

    private void validate(String[] split) {
        if (!split[0].equals("PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP")) {
            throw new UnsupportedOperationException("Not supported yet");
        }

        if (!split[split.length - 1].trim().isEmpty()) {
            throw new UnsupportedOperationException("Not Empty, is actually: " + split[split.length - 1]);
        }
    }


    /**
     * return data in simple/text format
     *
     * @param id id of data element
     * @return data in simple/text format
     */
    public String getTextFormatted(String id) {
        return toDataOutput(toDataRow(getData(id)));
    }

    private String toDataRow(Data data) {
        return data.getId() + "," + data.getName() + "," + data.getDescription() + "," + data.getTimestamp();
    }

    private String toDataOutput(String data) {
        return HEADER_ROW + "\n" + data + "\n";
    }

    public void removeText(String id) {
        textDao.remove(id);
    }

    /**
     * get Data by id
     *
     * @param id id of data
     * @return data found by id
     * @throws IllegalArgumentException if data with id does not exist
     */
    public Data getData(String id) {
        Data data = textDao.get(id);
        if (data == null) {
            throw new IllegalArgumentException("Data with id " + id + " not found");
        }
        return data;
    }

    /**
     * get data page
     *
     * @param page page number
     * @param size size of page
     * @param timestampMin min
     * @param timestampMax max
     * @return page of data
     */
    public Page getData(String page, String size, String timestampMin, String timestampMax) {
        Page dataList = textDao.getAll(page, size, timestampMin, timestampMax);
        return dataList;
    }

    /**
     * get data formatted
     *
     * @param page page number
     * @param size size of page
     * @param timestampMin min
     * @param timestampMax max
     * @return text formatted data
     */
    public String getTextFormatted(String page, String size, String timestampMin, String timestampMax) {
        Page dataPage = textDao.getAll(page, size, timestampMin, timestampMax);
        List<Data> dataList = dataPage.getData();
        String dataRows = dataList.stream().map(data -> toDataRow(data)).collect(Collectors.joining("\n"));

        return toDataOutput(dataRows);
    }

}
