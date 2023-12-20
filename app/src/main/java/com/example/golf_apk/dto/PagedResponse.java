package com.example.golf_apk.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagedResponse <T>{
    @SerializedName("_embedded")
    private Embedded<T> embedded;

    @SerializedName("_links")
    private Links links;

    @SerializedName("page")
    private PageMetadata page;

    public Embedded<T> getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded<T> embedded) {
        this.embedded = embedded;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public PageMetadata getPage() {
        return page;
    }

    public void setPage(PageMetadata page) {
        this.page = page;
    }


    public static class Embedded<T> {
        @SerializedName("warmupGameDtoList")
        private List<T> practices;

        public List<T> getPractices() {
            return practices;
        }

        public void setPractices(List<T> practices) {
            this.practices = practices;
        }
    }

    public static class Links {
        private Link first;
        private Link prev;
        private Link self;
        private Link next;
        private Link last;

        public Link getFirst() {
            return first;
        }

        public void setFirst(Link first) {
            this.first = first;
        }

        public Link getPrev() {
            return prev;
        }

        public void setPrev(Link prev) {
            this.prev = prev;
        }

        public Link getSelf() {
            return self;
        }

        public void setSelf(Link self) {
            this.self = self;
        }

        public Link getNext() {
            return next;
        }

        public void setNext(Link next) {
            this.next = next;
        }

        public Link getLast() {
            return last;
        }

        public void setLast(Link last) {
            this.last = last;
        }
    }

    public static class Link {
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        // Getter 메서드...
    }

    public static class PageMetadata {
        private int size;
        private int totalElements;
        private int totalPages;
        private int number;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
