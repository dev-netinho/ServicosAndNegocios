package com.deefy.group2.repository;

import com.deefy.group2.model.Music;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Filtros din\u00e2micos com AND (apenas crit\u00e9rios n\u00e3o nulos). Evita JPQL com {@code OR ... IS NULL},
 * que em alguns casos n\u00e3o restringe o resultado como esperado.
 */
public final class MusicSpecifications {

    private MusicSpecifications() {
    }

    public static Specification<Music> withOptionalFilters(String title, String artist, String genre) {
        return (root, query, cb) -> {
            List<Predicate> parts = new ArrayList<>();
            if (title != null) {
                parts.add(cb.like(cb.lower(root.get("title")), likePattern(title), '\\'));
            }
            if (artist != null) {
                parts.add(cb.like(cb.lower(root.get("artist")), likePattern(artist), '\\'));
            }
            if (genre != null) {
                parts.add(cb.like(cb.lower(root.get("genre")), likePattern(genre), '\\'));
            }
            return cb.and(parts.toArray(Predicate[]::new));
        };
    }

    private static String likePattern(String raw) {
        String escaped = raw.toLowerCase(Locale.ROOT).replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
