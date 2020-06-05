package ru.cherniak.menuvotingsystem.util;

import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.to.VoteTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {

    private VoteUtil() {
    }

    public static VoteTo createTo(Vote voteWithRestaurant) {
        return new VoteTo(voteWithRestaurant.getId(), voteWithRestaurant.getRestaurant().getName(), voteWithRestaurant.getDate());
    }

    public static List<VoteTo> getVoteTos(Collection<Vote> votesWithRestaurant) {
        return votesWithRestaurant.stream().map(VoteUtil::createTo).collect(Collectors.toList());
    }
}
