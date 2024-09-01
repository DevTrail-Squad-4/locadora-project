package br.edu.solutis.dev.trail.locadora.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private long totalItems;
    private int totalPages;
}