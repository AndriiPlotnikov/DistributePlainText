package com.sarief.rest;

import com.sarief.dto.IdInputDto;
import com.sarief.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/text")
public class TextReceiverEndpoint {

    @Autowired
    private TextService textService;

    /**
     * Receive text
     *
     * @param text value
     */
    @PostMapping
    @Consumes(MediaType.TEXT_PLAIN)
    public void receiveText(@RequestBody String text){
        textService.parseText(text);
    }

    /**
     * send response in same file format
     *
     * @param id of element, optional
     * @param page page number
     * @param size size of page
     * @param timestampMin min
     * @param timeStampMax max
     * @return single record or page in format
     */
    @GetMapping(path = "/formatted")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendTextKeepFormat(@RequestParam String id,
                                     @RequestParam(defaultValue = "1") String page,
                                     @RequestParam(defaultValue = "10") String size,
                                     @RequestParam(name = "timestamp-min", defaultValue = "0") String timestampMin,
                                     @RequestParam(name = "timestamp-max", defaultValue = "999999999") String timeStampMax){
        if (!id.trim().isEmpty()) {
            return textService.getTextFormatted(id);
        } else {
            return textService.getTextFormatted(page, size, timestampMin, timeStampMax);
        }

    }

    /**
     * send response in json format
     *
     * @param id of element, optional
     * @param page page number
     * @param size size of page
     * @param timestampMin min
     * @param timestampMax max
     * @return single record or page in json format
     */
    @GetMapping
    @Produces(MediaType.APPLICATION_JSON)
    public Object sendText(@RequestParam(required = false) String id,
                         @RequestParam(defaultValue = "1") String page,
                         @RequestParam(defaultValue = "10") String size,
                         @RequestParam(name = "timestamp-min", defaultValue = "0") String timestampMin,
                         @RequestParam(name = "timestamp-max", defaultValue = "999999999") String timestampMax){
        if (id != null && !id.trim().isEmpty()) {
            return textService.getData(id);
        } else {
            return textService.getData(page, size, timestampMin, timestampMax);
        }
    }

    /**
     * delete element
     *
     * @param dto dto with id of element
     */
    @DeleteMapping
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeRow(@RequestBody IdInputDto dto){
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            throw new UnsupportedOperationException("id not specified");
        }
        textService.removeText(dto.getId());
    }

}
