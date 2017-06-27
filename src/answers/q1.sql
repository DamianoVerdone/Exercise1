select  @curRank:=case WHEN @prevScore = t.votes THEN @curRank ELSE @curRank:= @curRank + 1 END AS rank,t.name,   
@prevScore := t.votes as votes
from  
(SELECT @curRank := 0) r,
(SELECT @prevScore := -1) p,
(select name, votes from votes order by votes desc) t;