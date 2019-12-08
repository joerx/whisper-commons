package io.yodo.whisper.commons.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoutPageDTO {

    @JsonProperty("items")
    private List<ShoutDTO> items;

    @JsonProperty("page")
    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    public List<ShoutDTO> getItems() {
        return items;
    }

    public void setItems(List<ShoutDTO> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
