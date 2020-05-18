package ru.cherniak.menuvotingsystem.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.User;

@Transactional(readOnly = true)
public interface JpaUserRepository extends JpaRepository<User, Long> {



    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") long id);

    User getByEmail(@Param("email") String email);


    @Query("SELECT u FROM User u LEFT OUTER JOIN FETCH u.votes WHERE u.id=:userId")
    User findOneWithVotes(@Param("userId") long userId);

}
