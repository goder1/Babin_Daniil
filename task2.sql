SELECT post.post_id
FROM post
WHERE length(post.content) > 20 and
    post.title ~ '^[0-9]' and
    post.post_id in (
        SELECT post.post_id
        FROM post
                left join comment on post.post_id = comment.post_id
        GROUP BY post.post_id
        HAVING count(comment.comment_id) = 2
    )
ORDER BY post.post_id