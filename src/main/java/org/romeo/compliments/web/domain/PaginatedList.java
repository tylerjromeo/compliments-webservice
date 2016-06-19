package org.romeo.compliments.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * User: tylerromeo
 * Date: 6/16/16
 * Time: 9:04 PM
 */
public class PaginatedList<T> {

    private long totalResults;
    private int page;
    private int count;
    private String next;
    private List<T> results;

    public PaginatedList(long totalResults, int page, int count, String next, List<T> results) {
        this.totalResults = totalResults;
        this.page = page;
        this.count = count;
        this.next = next;
        this.results = results;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The total number of results available from the service", required = true)
    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The number of results skipped in the result set", required = true)
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The number of results given in the response", required = true)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JsonProperty(required = false)
    @ApiModelProperty(notes = "The url of the next set of results. If not given then the current response is the last set", required = false)
    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The objects requested", required = true)
    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
