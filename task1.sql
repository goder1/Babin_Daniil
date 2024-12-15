SELECT count(*)
FROM profile
         left join post on profile.profile_id = post.profile_id
WHERE post.profile_id is null;