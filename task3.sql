SELECT post.post_id
FROM post
left join comment on post.post_id = comment.post_id
WHERE comment.comment_id is null
UNION
SELECT temp.post_id
FROM (select post.post_id, count(comment.comment_id)
FROM post
inner join comment on post.post_id = comment.post_id
GROUP BY post.post_id
) temp
WHERE temp.count = 1
ORDER BY post_id
limit 10;