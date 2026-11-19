package util.paging;

import Entitati.Utilizatori;

public interface FriendsPagingRepository  {

    Page<Utilizatori> findFriendsOnPage(Long id,Pageable pageable);
}
