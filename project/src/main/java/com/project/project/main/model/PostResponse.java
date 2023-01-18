package com.project.project.main.model;

import com.project.project.forum.model.Post;

public record PostResponse (Post post, String username, byte[] photo) {
}
