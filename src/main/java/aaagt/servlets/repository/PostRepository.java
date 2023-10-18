package aaagt.servlets.repository;

import aaagt.servlets.exception.NotFoundException;
import aaagt.servlets.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private static final AtomicLong idCounter = new AtomicLong(0L);
    private static final Map<Long, Post> storage = new ConcurrentHashMap<>();

    public List<Post> all() {
        return storage.values().stream().toList();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(storage.getOrDefault(id, null));
    }

    public Post save(Post post) {
        final long id;
        if (post.getId() == 0) {
            id = idCounter.incrementAndGet();
            post.setId(id);
        } else {
            id = post.getId();
            if (!storage.containsKey(id)) {
                throw new NotFoundException();
            }
        }
        storage.put(id, post);
        return post;
    }

    public void removeById(long id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException();
        }
        storage.remove(id);
    }
}
