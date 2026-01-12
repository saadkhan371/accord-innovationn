package org.example.dto;

public record BookDto(Long id, String title, String isbn, AuthorDto author) {}
