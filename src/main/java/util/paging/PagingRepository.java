package util.paging;

import Entitati.Entitati;
import Repository.Repository;

public interface PagingRepository<ID, E extends Entitati<ID>> extends Repository<ID, E> {

    Page<E> findAllOnPage(Pageable pageable);

}
