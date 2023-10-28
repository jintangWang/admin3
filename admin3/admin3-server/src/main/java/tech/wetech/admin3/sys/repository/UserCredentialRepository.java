package tech.wetech.admin3.sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.wetech.admin3.sys.model.UserCredential;

import java.util.Optional;

/**
 * @author cjbi
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

  @Query("from UserCredential authCredential where authCredential.identifier=:identifier and authCredential.identityType=:identityType")
  Optional<UserCredential> findCredential(String identifier, UserCredential.IdentityType identityType);

  @Transactional
  @Modifying
  @Query(nativeQuery = true,value = "update user_credential ul set ul.credential = ?1 where ul.user_id = ?2")
  void update(String credential,Long userId);

}
