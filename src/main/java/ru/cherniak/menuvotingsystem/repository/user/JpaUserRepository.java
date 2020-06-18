package ru.cherniak.menuvotingsystem.repository.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.User;

@Repository
@Transactional(readOnly = true)
public interface JpaUserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") long id);

    User getByEmail(@Param("email") String email);

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"votes", "roles"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM User u WHERE u.id=:userId")
    User findOneWithVotes(@Param("userId") long userId);

}
