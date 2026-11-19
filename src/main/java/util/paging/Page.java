package util.paging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page<E> {

    private final Long totalNumberOfElements;

    private final List<E> elementsOnPage;

    public Page(Long totalNumberOfElements, List<E> elementsOnPage) {
        if(totalNumberOfElements < 0)
            throw new IllegalArgumentException("Numarul elementelor nu poate fi negativ !");
        if(elementsOnPage == null)
            throw new IllegalArgumentException("Elementele paginii nu pot fi null !");
        this.totalNumberOfElements = totalNumberOfElements;
        this.elementsOnPage = Collections.unmodifiableList(new ArrayList<>(elementsOnPage));
    }

    public Long getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    public List<E> getElementsOnPage() {
        return elementsOnPage;
    }
}
